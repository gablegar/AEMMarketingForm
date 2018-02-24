package gablegar.components.services.impl;

import com.adobe.acs.commons.genericlists.GenericList;
import com.sforce.soap.partner.sobject.SObject;
import gablegar.components.services.SalesForceMapperService;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.request.RequestParameterMap;

/**
 * Created by glegarda on 22/02/18.
 */
@Component(metatype = true)
@Service(SalesForceMapperService.class)
public class SalesForceMapperServiceImpl implements SalesForceMapperService {

	public SObject[] mapFormToSalesForceLead(final RequestParameterMap valuesFromForm, GenericList salesForceValueMap) {
		SObject[] leads = new SObject[1];
		final SObject lead = new SObject();
		lead.setType("Lead");
		for(GenericList.Item item : salesForceValueMap.getItems()) {
			lead.setField(item.getValue(), valuesFromForm.getValue(item.getTitle()).getString());
		}
		leads[0] = lead;
		return leads;
	}

	public SObject[] mapFormToSalesForceCampaignMember(String leadId, String campaignId) {
		SObject campaignMember = new SObject();
		campaignMember.setType("CampaignMember");
		campaignMember.setField("campaignId", campaignId);
		campaignMember.setField("leadId", leadId);
		return  new SObject[]{campaignMember};
	}
}

