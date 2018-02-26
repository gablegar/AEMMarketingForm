package gablegar.components.services.impl;

import com.sforce.soap.partner.sobject.SObject;
import gablegar.components.constants.SalesForce;
import gablegar.components.services.SalesForceConnectorService;
import gablegar.components.services.SalesForceMapperService;
import gablegar.components.services.SalesForceService;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by glegarda on 21/02/18.
 */
@RunWith(MockitoJUnitRunner.class)
public class MarketingFormServiceImplTest {


	@Mock
	private SalesForceMapperService salesForceMapperService;
	@Mock
	private SalesForceService salesForceService;
	@Mock
	private SalesForceConnectorService salesForceConnectorService;
	@InjectMocks
	private MarketingFormServiceImpl marketingFormService;


	@Test
	public void testNoConnectionWithSalesForceProvided() {
		//given
		//A Request but no connection to Sales Force is provided or invalid

		boolean result;
		Resource resource = null;
		Map mockedFormValues = new HashMap();
		ResourceResolver mockedResourceResolver = mock(ResourceResolver.class);


		//when calling the marketing service will not be successful
		when(salesForceConnectorService.isSalesForceConnectionAvailable()).thenReturn(false);

		result = marketingFormService.processForm(resource, mockedFormValues, mockedResourceResolver);

		//then the returned values should be false
		assertEquals(false, result);
	}

	@Test
	public void testNoCampaignProvided() {
		//given
		//An empty Request with no Campaign name provided

		boolean result;
		Resource mockedResource = null;
		Map mockedFormValues = new HashMap();
		ResourceResolver resourceResolver = mock(ResourceResolver.class);


		//when calling the marketing form service should not be successful
		when(salesForceConnectorService.isSalesForceConnectionAvailable()).thenReturn(true);
		when(salesForceService.saveNewLeadOnSalesForce(salesForceMapperService.mapFormToSalesForceLead(mockedFormValues, resourceResolver), SalesForce.CAMPAIGN_NAME)).thenReturn(true);

		result = marketingFormService.processForm(mockedResource, mockedFormValues, resourceResolver);

		//then the returned result should be false
		assertEquals(false, result);
	}

	@Test
	public void testCampaignNotFoundOnMapping() {
		//given
		//A Request with no Campaign name provided

		boolean result;
		Resource mockedResource = null;
		Map mockedFormValues = getMockedMismatchingRequestFormValues();
		ResourceResolver mockedResourceResolver = mock(ResourceResolver.class);


		//when calling the marketing form service should not be successful
		when(salesForceConnectorService.isSalesForceConnectionAvailable()).thenReturn(true);
		when(salesForceService.saveNewLeadOnSalesForce(salesForceMapperService.mapFormToSalesForceLead(mockedFormValues, mockedResourceResolver), SalesForce.CAMPAIGN_NAME)).thenReturn(true);

		result = marketingFormService.processForm(mockedResource, mockedFormValues, mockedResourceResolver);

		//then the returned result should be false
		assertEquals(false, result);
	}

	@Test
	public void testCampaignProvided() {
		//given
		//A Request with Campaign name provided

		boolean result;
		Resource mockedResource = null;
		Map mockedFormValues = getMockedRequestFormValues();
		ResourceResolver mockedResourceResolver = mock(ResourceResolver.class);
		SObject[] mockedSObject = new SObject[1];

		//when calling the marketing service and the following services respond ok
		when(salesForceMapperService.mapFormToSalesForceLead(mockedFormValues, mockedResourceResolver)).thenReturn(mockedSObject);
		when(salesForceConnectorService.isSalesForceConnectionAvailable()).thenReturn(true);
		when(salesForceMapperService.mapCampaignFormNameToSalesForce(SalesForce.CAMPAIGN_NAME,mockedResourceResolver)).thenReturn(SalesForce.CAMPAIGN_NAME);
		when(salesForceService.saveNewLeadOnSalesForce(mockedSObject, SalesForce.CAMPAIGN_NAME)).thenReturn(true);

		result = marketingFormService.processForm(mockedResource, mockedFormValues, mockedResourceResolver);

		//then the result should be ok
		assertEquals(true, result);
	}

	@Test
	public void testCampaignProvidedButFollowingCallsFail() {
		//given
		//A Request with Campaign name provided

		boolean result;
		Resource mockedResource = null;
		Map mockedFormValues = getMockedRequestFormValues();
		ResourceResolver resourceResolver = mock(ResourceResolver.class);


		//when calling the marketing service and the following services respond ok
		when(salesForceConnectorService.isSalesForceConnectionAvailable()).thenReturn(true);
		when(salesForceService.saveNewLeadOnSalesForce(salesForceMapperService.mapFormToSalesForceLead(mockedFormValues, resourceResolver), SalesForce.CAMPAIGN_NAME)).thenReturn(false);

		result = marketingFormService.processForm(mockedResource, mockedFormValues, resourceResolver);

		//then the result should be ok
		assertEquals(false, result);
	}

	private Map getMockedRequestFormValues() {
		Map<String, Object> mockedRequestData = new HashMap<>();
		mockedRequestData.put(SalesForce.CAMPAIGN_NAME, new String[]{SalesForce.CAMPAIGN_NAME});
		return mockedRequestData;
	}

	private Map getMockedMismatchingRequestFormValues() {
		Map<String, Object> mockedRequestData = new HashMap<>();
		mockedRequestData.put(SalesForce.CAMPAIGN_MEMBER, new String[]{SalesForce.CAMPAIGN_MEMBER});
		return mockedRequestData;
	}


}

