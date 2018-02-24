package gablegar.components.services.impl;

import com.sforce.soap.partner.*;
import com.sforce.soap.partner.Error;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;
import gablegar.components.services.SalesForceMapperService;
import gablegar.components.services.SalesForceService;
import org.apache.felix.scr.annotations.*;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by glegarda on 22/02/18.
 */
@Component(metatype = true)
@Service(SalesForceService.class)
public class SalesForceServiceImpl implements SalesForceService {

	@Reference
	SalesForceMapperService salesForceMapperService;

	private static final Logger log = LoggerFactory.getLogger(SalesForceService.class);

	private String endpoint;
	private String username;
	private String password;
	static PartnerConnection connection;

	@Activate
	@Modified
	protected void createConnection(ComponentContext context) {
		setEndpoint(PropertiesUtil.toString(context.getProperties().get("endpoint"), null));
		setUsername(PropertiesUtil.toString(context.getProperties().get("salesforce-user"), null));
		setPassword(PropertiesUtil.toString(context.getProperties().get("salesforce-secret"), null));

		ConnectorConfig config = new ConnectorConfig();
		if(getUsername()!= null && getEndpoint() != null && getPassword()!= null) {
			setSalesForceConnectionConfig(config);
			try {

				connection = Connector.newConnection(config);
				log.info("MarketingFormServiceImpl activated with {} {}", getEndpoint(), getUsername());
				if(connection != null) {
					logConnectionSucessful(config);
				}

			} catch (ConnectionException exception) {
				log.error("Error while connecting to Sales Force: {}", exception);
			}
		} else {
			log.error("Please set the SalesForce properties in the OSGI Service");
		}
	}

	public boolean saveNewLeadOnSalesForce(SObject[] lead, String campaignID) {
		boolean success = false;
		if( connection != null ) {
			campaignID = retrieveSalesForceCampaignId(campaignID);
			if(campaignID != null) {
				SaveResult saveLeadResults = saveLead(lead)[0]; //this is because we save only one lead
				success = saveLeadOnCampaign(saveLeadResults, campaignID);
			}
		}
		return success;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	private String retrieveSalesForceCampaignId(String campaignName) {
		try {
			if(campaignName != null && !campaignName.isEmpty()) {
				QueryResult qr = connection.query("Select Id from Campaign where Name = " + "'" + campaignName + "'");
				campaignName = qr.getRecords()[0].getId();
			} else {
				log.warn("Please configure Sales Force Campaign name in " +
						"/etc/acs-commons/lists/salesforce-field-Mapping.html as it is on salesforce");
			}

		} catch (Exception exception) {
			log.error("Error searching for SalesForce campaign, please set a valid value: {}", exception);
		}
		return campaignName;
	}

	private SaveResult[] saveLead(SObject[] lead) {
		try {
			// create the records in SalesForce.com
			return connection.create(lead);
		} catch (Exception exception) {
			log.error("Error while creating Lead: {}", exception);
			SaveResult[] saveResults = new SaveResult[1];
			SaveResult error = new SaveResult();
			error.setSuccess(false);
			saveResults[0] = error;
			return saveResults;
		}
	}

	private boolean saveLeadOnCampaign(SaveResult leadResults, String campaignId) {
		try {
				if (leadResults.isSuccess()) {
					log.info("Lead created with ID " + leadResults.getId());
					SObject[] campaignMember = salesForceMapperService.mapFormToSalesForceCampaignMember(leadResults.getId(), campaignId);
					SaveResult[] resultAddingLeadToCampaign = connection.create(campaignMember);
					//in this case we are sure that SaveResult has only one element as we only saved one lead
					if (resultAddingLeadToCampaign[0].isSuccess()) {
						log.info("Lead {} created and added to campaign {} ", leadResults.getId(), resultAddingLeadToCampaign[0].getId());
						return true;
					} else {
						deleteLeadIfCampaignError(leadResults);
					}
				} else {
					deleteLeadIfCampaignError(leadResults);
				}
		} catch (Exception exception) {
			log.error("Error while adding Lead to campaign: {}", exception);
		}
		return false;
	}

	private void deleteLeadIfCampaignError(SaveResult result) throws ConnectionException {
		connection.delete(new String[]{result.getId()});
		logSalesForceErrors(result);
	}

	private void setSalesForceConnectionConfig(ConnectorConfig config) {
		config.setUsername(getUsername());
		config.setPassword(getPassword());
		config.setAuthEndpoint(getEndpoint());
		config.setServiceEndpoint(getEndpoint());
		config.setTraceMessage(true);
	}

	private void logConnectionSucessful(ConnectorConfig config) {
		//this method is not called often
		// it is called when the OSGI service is activated
		// or if the properties change
		log.info("Connection to SalesForce Successful");
		// display some current settings
		log.debug("Authorization EndPoint: " + config.getAuthEndpoint());
		log.debug("Service EndPoint: " + config.getServiceEndpoint());
		log.debug("Username: " + config.getUsername());
		log.debug("SessionId: " + config.getSessionId());
	}


	private void logSalesForceErrors(SaveResult result) {
		for(Error error: result.getErrors()) {
			log.error("Error while creating to Lead: {}", error);
		}
	}

}