package gablegar.components.services;

import com.adobe.acs.commons.genericlists.GenericList.Item;
import com.sforce.soap.partner.sobject.SObject;

import java.util.List;
import java.util.Map;

/**
 * Created by glegarda on 22/02/18.
 */
public interface SalesForceMapperService {

	SObject[] mapFormToSalesForceLead(Map valuesFromForm, List<Item> salesForceValueMap);

	SObject[] mapFormToSalesForceCampaignMember(String LeadId, String campaignID);

}

