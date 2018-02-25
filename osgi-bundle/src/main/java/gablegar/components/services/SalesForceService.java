package gablegar.components.services;

import com.sforce.soap.partner.sobject.SObject;

/**
 * Created by glegarda on 24/02/18.
 */
public interface SalesForceService {
	boolean saveNewLeadOnSalesForce(SObject[] lead, String campaignID);
}
