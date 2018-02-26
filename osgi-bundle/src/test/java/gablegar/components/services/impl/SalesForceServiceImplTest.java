package gablegar.components.services.impl;

import com.sforce.soap.partner.QueryResult;
import com.sforce.soap.partner.SaveResult;
import com.sforce.soap.partner.sobject.ISObject;
import com.sforce.soap.partner.sobject.SObject;
import gablegar.components.constants.SalesForce;
import gablegar.components.services.SalesForceConnectorService;
import gablegar.components.services.SalesForceMapperService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by glegarda on 26/02/18.
 */
@RunWith(MockitoJUnitRunner.class)
public class SalesForceServiceImplTest {

	public static final String MOCKED_LEAD_ID = "444555666";
	public static final String MOCKED_CAMPAIGN_ID = "111222333";
	@Mock
	SalesForceConnectorService salesForceConnectorService;

	@Mock
	SalesForceMapperService salesForceMapperService;

	@InjectMocks
	SalesForceServiceImpl salesForceService;

	@Before
	public void setUp() {
	}


	@Test
	public void noCampaignIdProvided() {
		//given
		//no Campaign Id provided
		boolean result;
		SObject[] mockedLead = new SObject[1];

		//when
		result = salesForceService.saveNewLeadOnSalesForce(mockedLead, null);

		//then
		assertEquals(false, result);
	}

	@Test
	public void searchingCampaignInSalesForceFails() {
		//given
		//Lead data and campaign data ok
		boolean result;
		SObject[] mockedLead = new SObject[1];
		QueryResult mockedQueryResult = getErrorQueryResult();
		String mockedCampaignName = SalesForce.CAMPAIGN_NAME;

		//when searching Campaign Id in Sales Force fails
		when(salesForceConnectorService.executeSalesForceQuery("Select Id from Campaign where Name = '" + mockedCampaignName + "'")).thenReturn(mockedQueryResult);
		result = salesForceService.saveNewLeadOnSalesForce(mockedLead, mockedCampaignName);

		//then the process should return false state
		assertEquals(false, result);
	}


	@Test
	public void savingLeadInSalesForceFails() {
		//given
		//Lead data and campaign data ok
		boolean result;
		SObject[] mockedLead = new SObject[1];
		SaveResult[] mockedSaveResults = getErrorMockedResult();
		QueryResult mockedQueryResult = getSuccessQueryResult();

		//when saving something in salesforce fails
		when(salesForceConnectorService.saveObjectInSalesForce(mockedLead)).thenReturn(mockedSaveResults);
		when(salesForceConnectorService.executeSalesForceQuery("Select Id from Campaign where Name = " + "'" + SalesForce.CAMPAIGN_NAME + "'")).thenReturn(mockedQueryResult);
		result = salesForceService.saveNewLeadOnSalesForce(mockedLead, SalesForce.CAMPAIGN_NAME);

		//then the process should return false state
		assertEquals(false, result);
	}


	@Test
	public void savingLeadInSalesForceSuccessful() {
		//given
		//Lead data and campaign data ok
		boolean result;
		SObject[] mockedLead = new SObject[1];
		SObject[] mockedMember = new SObject[1];
		SaveResult[] mockedSaveResults = getSuccessMockedResult();
		QueryResult mockedQueryResult = getSuccessQueryResult();

		//when saving all the process is ok
		when(salesForceConnectorService.saveObjectInSalesForce(mockedLead)).thenReturn(mockedSaveResults);
		when(salesForceConnectorService.executeSalesForceQuery("Select Id from Campaign where Name = " + "'" + SalesForce.CAMPAIGN_NAME + "'")).thenReturn(mockedQueryResult);
		when(salesForceMapperService.mapFormToSalesForceCampaignMember(MOCKED_LEAD_ID, MOCKED_CAMPAIGN_ID)).thenReturn(mockedMember);
		when(salesForceConnectorService.saveObjectInSalesForce(mockedMember)).thenReturn(mockedSaveResults);
		result = salesForceService.saveNewLeadOnSalesForce(mockedLead, SalesForce.CAMPAIGN_NAME);

		//then the result should be success
		assertEquals(true, result);
	}


	private SaveResult[] getErrorMockedResult() {
		SaveResult[] saveResults = new SaveResult[1];
		SaveResult error = new SaveResult();
		error.setSuccess(false);
		saveResults[0] = error;
		return saveResults;
	}

	private SaveResult[] getSuccessMockedResult() {
		SaveResult[] saveResults = new SaveResult[1];
		SaveResult success = new SaveResult();
		success.setSuccess(true);
		success.setId(MOCKED_LEAD_ID);
		saveResults[0] = success;
		return saveResults;
	}

	private QueryResult getErrorQueryResult() {
		QueryResult errorQueryResult = new QueryResult();
		return errorQueryResult;
	}

	private QueryResult getSuccessQueryResult() {
		QueryResult successfulQueryResult = new QueryResult();
		ISObject[] succesfulRecords = new ISObject[1];
		SObject successful = new SObject();
		successful.setId(MOCKED_CAMPAIGN_ID);
		succesfulRecords[0] = successful;
		successfulQueryResult.setRecords(succesfulRecords);
		return successfulQueryResult;
	}
}
