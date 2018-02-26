package gablegar.components.services;

import com.adobe.acs.commons.genericlists.GenericList.Item;
import com.sforce.soap.partner.sobject.SObject;
import org.apache.sling.api.resource.ResourceResolver;

import java.util.List;
import java.util.Map;

/**
 * Created by glegarda on 22/02/18.
 */
public interface SalesForceMapperService {

	SObject[] mapFormToSalesForceLead(Map valuesFromForm, ResourceResolver resourceResolver);

	SObject[] mapFormToSalesForceCampaignMember(String LeadId, String campaignID);

	String mapCampaignFormNameToSalesForce(String formCampaignName, ResourceResolver resourceResolver);

}

