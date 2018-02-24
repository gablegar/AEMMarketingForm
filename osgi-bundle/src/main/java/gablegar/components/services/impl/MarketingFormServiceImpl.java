package gablegar.components.services.impl;

import com.adobe.acs.commons.genericlists.GenericList;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.sforce.soap.partner.sobject.SObject;
import gablegar.components.services.SalesForceMapperService;
import gablegar.components.services.SalesForceService;
import org.apache.felix.scr.annotations.Reference;
import org.apache.sling.api.request.RequestParameterMap;
import org.apache.sling.api.resource.ResourceResolver;
import gablegar.components.services.MarketingFormService;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by glegarda on 21/02/18.
 */
@Component(metatype = true)
@Service(MarketingFormService.class)
public class MarketingFormServiceImpl implements MarketingFormService {

	@Reference
	SalesForceMapperService salesForceMapperService;
	@Reference
	SalesForceService salesForceService;

	private static final Logger log = LoggerFactory.getLogger(MarketingFormServiceImpl.class);

	public boolean processForm(Resource resource, RequestParameterMap formValues, ResourceResolver resourceResolver) {
		boolean processingResult = false;
		PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
		Page salesForceCampaignsNamePage = pageManager.getPage("/etc/acs-commons/lists/salesForceCampaignNames");
		GenericList listOfSalesForceCampaigns = salesForceCampaignsNamePage.adaptTo(GenericList.class);
		String campaignNameOnSalesForce = listOfSalesForceCampaigns.lookupTitle("RegistrationNewClients");

		Page fieldMappingPage = pageManager.getPage("/etc/acs-commons/lists/salesforce-field-Mapping");
		GenericList listOfMappingsSalesForce = fieldMappingPage.adaptTo(GenericList.class);

		if(campaignNameOnSalesForce != null && !campaignNameOnSalesForce.isEmpty()) {
			log.info("Campaign name on SalesForce: {} ", campaignNameOnSalesForce);
			processingResult =salesForceService.saveNewLeadOnSalesForce(salesForceMapperService.mapFormToSalesForceLead(formValues, listOfMappingsSalesForce), campaignNameOnSalesForce);
			log.info("Post to Marketing Form Service done");
		} else {
			log.error("Sales Force campaign missing, please configure ACS generic list on /etc/acs-commons/lists/salesForceCampaignNames");
		}
		return processingResult;
	}

	protected void activate(ComponentContext context) {
		log.info("MarketingFormServiceImpl activated");
	}
}

