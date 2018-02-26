package gablegar.components.services.impl;

import com.adobe.acs.commons.genericlists.GenericList;
import com.adobe.acs.commons.genericlists.GenericList.Item;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.sforce.soap.partner.sobject.SObject;
import gablegar.components.services.SalesForceMapperService;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.ResourceResolver;

import java.util.List;
import java.util.Map;

import static gablegar.components.constants.SalesForce.*;

/**
 * Created by glegarda on 22/02/18.
 */
@Component(metatype = true)
@Service(SalesForceMapperService.class)
public class SalesForceMapperServiceImpl implements SalesForceMapperService {

	public SObject[] mapFormToSalesForceLead(final Map valuesFromForm, ResourceResolver resourceResolver) {
		List<Item> salesForceValueMap = getGenericMappingSalesForceList(resourceResolver);
		SObject[] leads = new SObject[1];
		final SObject lead = new SObject();
		lead.setType(LEAD);
		for(Item item : salesForceValueMap) {
			String title = item.getTitle();
			lead.setField(item.getValue(), valuesFromForm.get(title) == null ? null:((String[])valuesFromForm.get(title))[0]);
		}
		leads[0] = lead;
		return leads;
	}

	public SObject[] mapFormToSalesForceCampaignMember(String leadId, String campaignId) {
		SObject campaignMember = new SObject();
		campaignMember.setType(CAMPAIGN_MEMBER);
		campaignMember.setField(CAMPAIGN_ID, campaignId);
		campaignMember.setField(LEAD_ID, leadId);
		return  new SObject[]{campaignMember};
	}

	public String mapCampaignFormNameToSalesForce(String formCampaignName, ResourceResolver resourceResolver) {
		List<Item> salesForceValueMap =getGenericMappingForCampaign(resourceResolver);
		for(Item item : salesForceValueMap) {
			if(item.getValue().equals(formCampaignName)) {
				return item.getTitle();
			}
		}
		return "";
	}

	private List<Item> getGenericMappingForCampaign(ResourceResolver resourceResolver) {
		PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
		Page fieldMappingPage = pageManager.getPage(PATH_LIST_SALES_FORCE_CAMPAIGN_FIELD_MAPPING);
		return fieldMappingPage.adaptTo(GenericList.class).getItems();
	}

	private List<Item> getGenericMappingSalesForceList(ResourceResolver resourceResolver) {
		PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
		Page fieldMappingPage = pageManager.getPage(PATH_LIST_SALES_FORCE_FIELD_MAPPING);
		return fieldMappingPage.adaptTo(GenericList.class).getItems();
	}
}

