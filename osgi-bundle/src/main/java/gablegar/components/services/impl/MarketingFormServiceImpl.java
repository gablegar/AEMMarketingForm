package gablegar.components.services.impl;

import com.adobe.acs.commons.genericlists.GenericList;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import gablegar.components.services.SalesForceMapperService;
import gablegar.components.services.SalesForceService;
import org.apache.felix.scr.annotations.Reference;
import org.apache.sling.api.resource.ResourceResolver;
import gablegar.components.services.MarketingFormService;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static gablegar.components.constants.FormConstants.CAMPAIGN_MISSING_CONFIGURE_PATH;
import static gablegar.components.constants.SalesForce.CAMPAIGN_NAME;
import static gablegar.components.constants.SalesForce.PATH_LIST_SALESFORCE_FIELD_MAPPING;

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

	public boolean processForm(Resource resource, Map formValues, ResourceResolver resourceResolver) {
		boolean processingResult = false;
		String campaignNameOnSalesForce = getCampaignName(formValues);
		PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
		Page fieldMappingPage = pageManager.getPage(PATH_LIST_SALESFORCE_FIELD_MAPPING);
		GenericList listOfMappingsSalesForce = fieldMappingPage.adaptTo(GenericList.class);

		if(campaignNameOnSalesForce != null && !campaignNameOnSalesForce.isEmpty()) {
			log.info("Campaign name on SalesForce: {} ", campaignNameOnSalesForce);
			processingResult = salesForceService.saveNewLeadOnSalesForce(salesForceMapperService.mapFormToSalesForceLead(formValues, listOfMappingsSalesForce.getItems()), campaignNameOnSalesForce);
			log.info("Post to Marketing Form Service done");
		} else {
			log.error(CAMPAIGN_MISSING_CONFIGURE_PATH);
		}
		return processingResult;
	}

	private String getCampaignName(Map formValues) {
		return formValues.get(CAMPAIGN_NAME)==null?null:((String[])formValues.get(CAMPAIGN_NAME))[0];
	}

	protected void activate(ComponentContext context) {
		log.info("MarketingFormServiceImpl activated");
	}
}

