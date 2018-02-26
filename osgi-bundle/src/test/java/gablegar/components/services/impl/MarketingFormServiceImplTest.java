package gablegar.components.services.impl;

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
		//A Request

		boolean result;
		Resource resource = null;
		Map formValues = new HashMap();
		ResourceResolver resourceResolver = mock(ResourceResolver.class);
		when(salesForceConnectorService.isSalesForceConnectionAvailable()).thenReturn(false);

		//when calling the mapper will bring a lead with all fields

		result = marketingFormService.processForm(resource, formValues, resourceResolver);

		//then the returned object should have all set
		assertEquals(false, result);
	}

	@Test
	public void testNoCampaignProvided() {
		//given
		//A Request

		boolean result;
		Resource resource = null;
		Map formValues = new HashMap();
		ResourceResolver resourceResolver = mock(ResourceResolver.class);


		//when calling the mapper will bring a lead with all fields
		when(salesForceConnectorService.isSalesForceConnectionAvailable()).thenReturn(true);

		when(salesForceService.saveNewLeadOnSalesForce(salesForceMapperService.mapFormToSalesForceLead(formValues, resourceResolver), SalesForce.CAMPAIGN_NAME)).thenReturn(true);

		result = marketingFormService.processForm(resource, formValues, resourceResolver);

		//then the returned object should have all set
		assertEquals(false, result);
	}

	@Test
	public void testCampaignNotFoundOnMapping() {
		//given
		//A Request

		boolean result;
		Resource resource = null;
		Map formValues = getMockedMismatchingRequestFormValues();
		ResourceResolver resourceResolver = mock(ResourceResolver.class);


		//when calling the mapper will bring a lead with all fields
		when(salesForceConnectorService.isSalesForceConnectionAvailable()).thenReturn(true);
		when(salesForceService.saveNewLeadOnSalesForce(salesForceMapperService.mapFormToSalesForceLead(formValues, resourceResolver), SalesForce.CAMPAIGN_NAME)).thenReturn(true);

		result = marketingFormService.processForm(resource, formValues, resourceResolver);

		//then the returned object should have all set
		assertEquals(false, result);
	}

	@Test
	public void testCampaignProvided() {
		//given
		//A Request

		boolean result;
		Resource resource = null;
		Map formValues = getMockedRequestFormValues();
		ResourceResolver resourceResolver = mock(ResourceResolver.class);


		//when calling the mapper will bring a lead with all fields
		when(salesForceConnectorService.isSalesForceConnectionAvailable()).thenReturn(true);

		when(salesForceService.saveNewLeadOnSalesForce(salesForceMapperService.mapFormToSalesForceLead(formValues, resourceResolver), SalesForce.CAMPAIGN_NAME)).thenReturn(true);

		result = marketingFormService.processForm(resource, formValues, resourceResolver);

		//then the returned object should have all set
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

