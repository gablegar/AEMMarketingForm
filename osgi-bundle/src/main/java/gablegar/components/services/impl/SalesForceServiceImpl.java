package gablegar.components.services.impl;

import com.sforce.soap.partner.Error;
import com.sforce.soap.partner.QueryResult;
import com.sforce.soap.partner.SaveResult;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.ConnectionException;
import gablegar.components.services.SalesForceConnectorService;
import gablegar.components.services.SalesForceMapperService;
import gablegar.components.services.SalesForceService;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by glegarda on 24/02/18.
 */
@Component(metatype = true)
@Service(SalesForceService.class)
public class SalesForceServiceImpl implements SalesForceService {

	@Reference
	private SalesForceConnectorService salesForceConnectorService;

	@Reference
	private SalesForceMapperService salesForceMapperService;

	private static final Logger log = LoggerFactory.getLogger(SalesForceService.class);

	public boolean saveNewLeadOnSalesForce(SObject[] lead, String campaignName) {
		boolean success = false;
		String campaignId = retrieveSalesForceCampaignId(campaignName);
		if(campaignId != null) {
			SaveResult saveLeadResults = salesForceConnectorService.saveObjectInSalesForce(lead)[0]; //this is because we save only one lead
			if(saveLeadResults.isSuccess()) {
				success = saveLeadOnCampaign(saveLeadResults, campaignId);
			}
		} else {
			log.error("Could not save lead as campaign ID is invalid");
		}
		return success;
	}

	private String retrieveSalesForceCampaignId(String campaignName) {
		String CampaignId = null;
		if(campaignName != null && !campaignName.isEmpty()) {
			QueryResult qr = salesForceConnectorService.executeSalesForceQuery("Select Id from Campaign where Name = '" + campaignName + "'");
			if (qr.getRecords().length > 0) {
				CampaignId = qr.getRecords()[0].getId();
			} else {
				log.error("Please set a correct value for Campaign Name");
			}
		} else {
			log.warn("Please configure Sales Force Campaign name in " +
					"/etc/acs-commons/lists/salesforce-field-Mapping.html as it is on salesforce");
		}
		return CampaignId;
	}

	private boolean saveLeadOnCampaign(SaveResult leadResults, String campaignId) {
		try {
			SObject[] campaignMember = salesForceMapperService.mapFormToSalesForceCampaignMember(leadResults.getId(), campaignId);
			SaveResult[] resultAddingLeadToCampaign = salesForceConnectorService.saveObjectInSalesForce(campaignMember);
			//in this case we are sure that SaveResult has only one element as we only saved one lead
			if (resultAddingLeadToCampaign[0].isSuccess()) {
				log.info("Lead {} created and added to campaign {} ",
						leadResults.getId(), resultAddingLeadToCampaign[0].getId());
				return true;
			} else {
				deleteLeadIfCampaignError(leadResults);
			}
		} catch (Exception exception) {
			log.error("Error while adding Lead to campaign: {}", exception);
		}
		return false;
	}

	private void deleteLeadIfCampaignError(SaveResult result) throws ConnectionException {
		salesForceConnectorService.deleteObjectInSalesForce(result.getId());
		logSalesForceErrors(result);
	}

	private void logSalesForceErrors(SaveResult result) {
		for(Error error: result.getErrors()) {
			log.error("Error while creating to Lead: {}", error);
		}
	}
}
