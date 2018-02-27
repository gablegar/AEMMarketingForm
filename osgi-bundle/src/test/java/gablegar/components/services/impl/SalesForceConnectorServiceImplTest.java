package gablegar.components.services.impl;

import com.sforce.soap.partner.DeleteResult;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.QueryResult;
import com.sforce.soap.partner.SaveResult;
import com.sforce.soap.partner.sobject.ISObject;
import com.sforce.soap.partner.sobject.SObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.support.membermodification.MemberModifier;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by glegarda on 26/02/18.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ SalesForceConnectorServiceImpl.class })
public class SalesForceConnectorServiceImplTest {

	public static final String MOCKED_OBJECT_ID = "444555666";

	SalesForceConnectorServiceImpl salesForceConnectorService;

	@Before
	public void setUp() {
		salesForceConnectorService = new SalesForceConnectorServiceImpl();
	}


	@Test
	public void noConnectionGivesError() throws Exception {
		//given
		PartnerConnection mockedConnection = mock(PartnerConnection.class);
		PowerMockito.mockStatic(PartnerConnection.class);
		PowerMockito.whenNew(PartnerConnection.class).withAnyArguments().thenReturn(mockedConnection);
		String Query = "Select Id from Campaign where Name = 'campaign name'";
		QueryResult queryResult;
		SaveResult[] saveResults;
		DeleteResult[] deleteResults;
		SObject[] mockedLead = new SObject[]{new SObject()};

		//when
		queryResult = salesForceConnectorService.executeSalesForceQuery(Query);
		saveResults = salesForceConnectorService.saveObjectInSalesForce(mockedLead);
		deleteResults = salesForceConnectorService.deleteObjectInSalesForce(MOCKED_OBJECT_ID);

		//then
		assertEquals(getErrorQueryResult().getRecords().length, queryResult.getRecords().length);
		assertEquals(getErrorSaveResult()[0].getSuccess(), saveResults[0].getSuccess());
		assertEquals(getErrorDeleteResult()[0].getSuccess(), deleteResults[0].getSuccess());
	}

	@Test
	public void queryGivesError() throws Exception {
		//given
		PartnerConnection mockedConnection = mock(PartnerConnection.class);
		MemberModifier.field(SalesForceConnectorServiceImpl.class, "connection").set(salesForceConnectorService , mockedConnection);
		String Query = "Select Id from Campaign where Name = 'campaign name'";
		QueryResult queryResult;
		QueryResult expectedQueryResult = getErrorQueryResult();

		//when

		when(mockedConnection.query(Query)).thenReturn(expectedQueryResult);


		queryResult = salesForceConnectorService.executeSalesForceQuery(Query);

		//then
		assertEquals(expectedQueryResult.getRecords().length, queryResult.getRecords().length);
	}

	@Test
	public void querySuccessful() throws Exception {
		//given
		PartnerConnection mockedConnection = mock(PartnerConnection.class);
		MemberModifier.field(SalesForceConnectorServiceImpl.class, "connection").set(salesForceConnectorService , mockedConnection);
		String Query = "Select Id from Campaign where Name = 'campaign name'";
		QueryResult queryResult;
		QueryResult expectedQueryResult = getSuccessfulQueryResult();

		//when
		when(mockedConnection.query(Query)).thenReturn(expectedQueryResult);

		queryResult = salesForceConnectorService.executeSalesForceQuery(Query);

		//then
		assertEquals(expectedQueryResult.getRecords().length, queryResult.getRecords().length);
	}

	@Test
	public void saveObjectGivesError() throws Exception {
		//given
		PartnerConnection mockedConnection = mock(PartnerConnection.class);
		MemberModifier.field(SalesForceConnectorServiceImpl.class, "connection").set(salesForceConnectorService , mockedConnection);
		SObject[] mockedLead = new SObject[]{new SObject()};
		SaveResult[] queryResult;
		//when

		when(mockedConnection.create(mockedLead)).thenReturn(getErrorSaveResult());

		queryResult = salesForceConnectorService.saveObjectInSalesForce(mockedLead);

		//then
		assertEquals(getErrorSaveResult()[0].getSuccess(), queryResult[0].getSuccess());
	}

	@Test
	public void saveObjectSuccessful() throws Exception {
		//given
		PartnerConnection mockedConnection = mock(PartnerConnection.class);
		MemberModifier.field(SalesForceConnectorServiceImpl.class, "connection").set(salesForceConnectorService , mockedConnection);
		SObject[] mockedLead = new SObject[]{new SObject()};
		SaveResult[] queryResult;

		//when
		when(mockedConnection.create(mockedLead)).thenReturn(getSuccessfulSaveResult());

		queryResult = salesForceConnectorService.saveObjectInSalesForce(mockedLead);

		//then
		assertEquals(getSuccessfulSaveResult()[0].getSuccess(), queryResult[0].getSuccess());
	}

	@Test
	public void deleteObjectGivesError() throws Exception {
		//given
		PartnerConnection mockedConnection = mock(PartnerConnection.class);
		MemberModifier.field(SalesForceConnectorServiceImpl.class, "connection").set(salesForceConnectorService , mockedConnection);
		SObject[] mockedLead = new SObject[]{new SObject()};
		DeleteResult[] queryResult;
		//when

		when(mockedConnection.delete(new String[]{MOCKED_OBJECT_ID})).thenReturn(getErrorDeleteResult());

		queryResult = salesForceConnectorService.deleteObjectInSalesForce(MOCKED_OBJECT_ID);

		//then
		assertEquals(getErrorDeleteResult()[0].getSuccess(), queryResult[0].getSuccess());
	}

	@Test
	public void deleteObjectSuccessful() throws Exception {
		//given
		PartnerConnection mockedConnection = mock(PartnerConnection.class);
		MemberModifier.field(SalesForceConnectorServiceImpl.class, "connection").set(salesForceConnectorService , mockedConnection);
		SObject[] mockedLead = new SObject[]{new SObject()};
		DeleteResult[] queryResult;
		//when

		when(mockedConnection.delete(new String[]{MOCKED_OBJECT_ID})).thenReturn(getSucessfulDeleteResult());

		queryResult = salesForceConnectorService.deleteObjectInSalesForce(MOCKED_OBJECT_ID);

		//then
		assertEquals(getSucessfulDeleteResult()[0].getSuccess(), queryResult[0].getSuccess());
	}

	private QueryResult getErrorQueryResult() {
		return  new QueryResult();
	}

	private QueryResult getSuccessfulQueryResult() {
		QueryResult succesfulQuery = new QueryResult();
		SObject salesForceObject = new SObject();
		salesForceObject.setId(MOCKED_OBJECT_ID);
		ISObject[] objects = new ISObject[1];
		objects[0] = salesForceObject;
		succesfulQuery.setRecords(objects);
		succesfulQuery.setDone(true);
		return succesfulQuery;
	}

	private SaveResult[] getErrorSaveResult() {
		SaveResult[] saveResults = new SaveResult[1];
		SaveResult error = new SaveResult();
		error.setSuccess(false);
		saveResults[0] = error;
		return saveResults;
	}

	private SaveResult[] getSuccessfulSaveResult() {
		SaveResult[] saveResults = new SaveResult[1];
		SaveResult success = new SaveResult();
		success.setSuccess(true);
		saveResults[0] = success;
		return saveResults;
	}

	private DeleteResult[] getErrorDeleteResult() {
		DeleteResult[] deleteResults = new DeleteResult[1];
		DeleteResult error = new DeleteResult();
		error.setSuccess(false);
		deleteResults[0] = error;
		return deleteResults;
	}

	private DeleteResult[] getSucessfulDeleteResult() {
		DeleteResult[] deleteResults = new DeleteResult[1];
		DeleteResult success = new DeleteResult();
		success.setSuccess(true);
		deleteResults[0] = success;
		return deleteResults;
	}
}
