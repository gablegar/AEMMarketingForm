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

	QueryResult executeQuery(String query);
	SaveResult[] createObject(SObject[] object);
	DeleteResult[] deleteObject(String id);
}

