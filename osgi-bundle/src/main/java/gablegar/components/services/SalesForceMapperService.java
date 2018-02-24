package gablegar.components.services;

import com.adobe.acs.commons.genericlists.GenericList;
import com.sforce.soap.partner.sobject.SObject;
import org.apache.sling.api.request.RequestParameterMap;

/**
 * Created by glegarda on 22/02/18.
 */
public interface SalesForceMapperService {

	SObject[] mapFormToSalesForceLead(RequestParameterMap valuesFromForm, GenericList salesForceValueMap);

	SObject[] mapFormToSalesForceCampaignMember(String campaignID, String LeadId);

}

