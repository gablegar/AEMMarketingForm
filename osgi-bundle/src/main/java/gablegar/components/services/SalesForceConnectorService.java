package gablegar.components.services;

import com.sforce.soap.partner.DeleteResult;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.QueryResult;
import com.sforce.soap.partner.SaveResult;
import com.sforce.soap.partner.sobject.SObject;

/**
 * Created by glegarda on 22/02/18.
 */
public interface SalesForceConnectorService {

	QueryResult executeSalesForceQuery(String query);
	SaveResult[] saveObjectInSalesForce(SObject[] object);
	DeleteResult[] deleteObjectInSalesForce(String id);
	boolean isSalesForceConnectionAvailable();
}

