package gablegar.components.services.impl;

import gablegar.components.services.SalesForceConnectorService;
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
import static gablegar.components.constants.FormConstants.CONNECTION_SALES_FORCE_MISSING;
import static gablegar.components.constants.SalesForce.CAMPAIGN_NAME;

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
	@Reference
	SalesForceConnectorService salesForceConnectorService;

	private static final Logger LOG = LoggerFactory.getLogger(MarketingFormServiceImpl.class);

	public boolean processForm(Resource resource, Map formValues, ResourceResolver resourceResolver) {
		boolean processingResult = false;
		if (salesForceConnectorService.isSalesForceConnectionAvailable()) {
			String campaignNameOnSalesForce = getCampaignName(formValues);

			if (campaignNameOnSalesForce != null && !campaignNameOnSalesForce.isEmpty()) {
				LOG.debug("Campaign name on SalesForce: {} ", campaignNameOnSalesForce);
				processingResult = salesForceService.saveNewLeadOnSalesForce(salesForceMapperService.mapFormToSalesForceLead(formValues, resourceResolver), salesForceMapperService.mapCampaignFormNameToSalesForce(campaignNameOnSalesForce,resourceResolver));
				LOG.info("Post to Marketing Form Service done");
			} else {
				LOG.error(CAMPAIGN_MISSING_CONFIGURE_PATH);
			}
		} else {
			LOG.error(CONNECTION_SALES_FORCE_MISSING);
		}
		return processingResult;
	}

	private String getCampaignName(Map formValues) {
		return formValues.get(CAMPAIGN_NAME)==null?null:((String[])formValues.get(CAMPAIGN_NAME))[0];
	}

	protected void activate() {
		LOG.info("MarketingFormServiceImpl activated");
	}
}

