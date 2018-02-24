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

	public SalesForceServiceImpl(){
	}

	@Activate
	@Modified
	protected void createConnection(ComponentContext context) {
		String endpoint = PropertiesUtil.toString(context.getProperties().get("endpoint"), null);
		String user = PropertiesUtil.toString(context.getProperties().get("salesforce-user"), null);
		String password = PropertiesUtil.toString(context.getProperties().get("salesforce-secret"), null);
		setUsername(user);
		setPassword(password);
		setEndpoint(endpoint);

		ConnectorConfig config = new ConnectorConfig();
		if(getUsername()!= null && getEndpoint() != null && getPassword()!= null) {
			config.setUsername(getUsername());
			config.setPassword(getPassword());
			config.setAuthEndpoint(getEndpoint());
			config.setServiceEndpoint(getEndpoint());
			config.setTraceMessage(true);
			try {

				connection = Connector.newConnection(config);
				log.info("MarketingFormServiceImpl activated with {} {}", endpoint, user);
				if(connection != null) {
					log.info("Connection to SalesForce Successful");
					// display some current settings
					log.debug("Authorization EndPoint: " + config.getAuthEndpoint());
					log.debug("Service EndPoint: " + config.getServiceEndpoint());
					log.debug("Username: " + config.getUsername());
					log.debug("SessionId: " + config.getSessionId());
				}

			} catch (ConnectionException exception) {
				log.error("Error while connecting to Sales Force: {}", exception);
			}
		} else {
			log.error("Please set the SalesForce properties in the OSGI Service");
		}
	}


	public boolean saveNewLeadOnSalesForce(SObject[] lead, String campaignID) {
		boolean saved = false;
		if( connection != null ) {
			try {
				if(campaignID != null && !campaignID.isEmpty()) {
					QueryResult qr = connection.query("Select Id from Campaign where Name = " + "'" + campaignID + "'");
					campaignID = qr.getRecords()[0].getId();
				} else {
					log.warn("Please configure Sales Force Campaign name in " +
							"/etc/acs-commons/lists/salesforce-field-Mapping.html as it is on salesforce");
				}

			} catch (Exception exception) {
				log.error("Error searching for SalesForce campaign, please set a valid value: {}", exception);
			}

			try {
				if(campaignID != null) {

					// create the records in SalesForce.com
					SaveResult[] saveResults = connection.create(lead);
					for (SaveResult result : saveResults) {
						if (result.isSuccess()) {
							log.info("Lead created with ID " + result.getId());
							SaveResult[] resultAddingLeadToCampaign = connection.create(salesForceMapperService.mapFormToSalesForceCampaignMember(result.getId(), campaignID));
							for (SaveResult resultCampaign : resultAddingLeadToCampaign) {
								if (resultCampaign.isSuccess()) {
									log.info("Lead created and added to campaign " + resultCampaign.getId());
									saved = true;
								} else {
									connection.delete(new String[]{result.getId()});
									logSalesForceErrors(result);
								}
							}
						} else {
							connection.delete(new String[]{result.getId()});
							logSalesForceErrors(result);
						}
					}
				}

			} catch (Exception exception) {
				log.error("Error while creating Lead: {}", exception);
			}
		}
		return saved;
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

	private void logSalesForceErrors(SaveResult result) {
		for(Error error: result.getErrors()) {
			log.error("Error while creating to Lead: {}", error);
		}
	}
}