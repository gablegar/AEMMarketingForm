package gablegar.components.services.impl;

import com.adobe.acs.commons.genericlists.GenericList;
import com.adobe.acs.commons.genericlists.GenericList.Item;
import com.adobe.acs.commons.genericlists.impl.GenericListImpl;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.sforce.soap.partner.sobject.SObject;
import gablegar.components.constants.FormConstants;
import gablegar.components.constants.SalesForce;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gablegar.components.constants.SalesForce.PATH_LIST_SALES_FORCE_CAMPAIGN_FIELD_MAPPING;
import static gablegar.components.constants.SalesForce.PATH_LIST_SALES_FORCE_FIELD_MAPPING;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by glegarda on 21/02/18.
 */
@RunWith(MockitoJUnitRunner.class)
public class SalesForceMapperServiceImplTest {

	SalesForceMapperServiceImpl salesForceMapperService;

	@Before
	public void setUp() {
		salesForceMapperService = new SalesForceMapperServiceImpl();
	}

	@Test
	public void testShouldMapAllFields(){
		//given
		//A lead

		SObject[] resultLead;
		ResourceResolver resourceResolver = mock(ResourceResolver.class);
		PageManager mockedPageManager = mock(PageManager.class);
		Page mockedPage = mock(Page.class);
		GenericList mockedGenericList = mock(GenericList.class);
		List<Item> mockedCampaignList = createMockSalesForceMappingList();

		//when calling the mapper will bring a lead with all fields
		when(resourceResolver.adaptTo(PageManager.class)).thenReturn(mockedPageManager);
		when(mockedPageManager.getPage(PATH_LIST_SALES_FORCE_FIELD_MAPPING)).thenReturn(mockedPage);
		when(mockedPage.adaptTo(GenericList.class)).thenReturn(mockedGenericList);
		when(mockedGenericList.getItems()).thenReturn(mockedCampaignList);

		resultLead = salesForceMapperService.mapFormToSalesForceLead(createMockRequestData(), resourceResolver);

		//then the returned object should have all set
		assertEquals(expectedFullLead()[0].toString(), resultLead[0].toString());
	}

	@Test
	public void testShouldNotMapFields(){
		//given
		//An entry with no fields

		SObject[] resultLead;
		ResourceResolver resourceResolver = mock(ResourceResolver.class);
		PageManager mockedPageManager = mock(PageManager.class);
		Page mockedPage = mock(Page.class);
		GenericList mockedGenericList = mock(GenericList.class);
		List<Item> mockedCampaignList = createEmptyMockListData();

		//when calling the mapper
		when(resourceResolver.adaptTo(PageManager.class)).thenReturn(mockedPageManager);
		when(mockedPageManager.getPage(PATH_LIST_SALES_FORCE_FIELD_MAPPING)).thenReturn(mockedPage);
		when(mockedPage.adaptTo(GenericList.class)).thenReturn(mockedGenericList);
		when(mockedGenericList.getItems()).thenReturn(mockedCampaignList);
		resultLead = salesForceMapperService.mapFormToSalesForceLead(createEmptyMockFormData(), resourceResolver);

		//then the returned object should be empty
		assertEquals(expectedRequestFormEmptyModel()[0].toString(), resultLead[0].toString());
	}

	@Test
	public void testShouldMapCampaignField(){
		//given
		//A campaign Name

		String resultName;
		ResourceResolver resourceResolver = mock(ResourceResolver.class);
		PageManager mockedPageManager = mock(PageManager.class);
		Page mockedPage = mock(Page.class);
		GenericList mockedGenericList = mock(GenericList.class);
		List<Item> mockedCampaignList = getMockedCampaignList();

		//when calling the mapper will bring a lead with all fields
		when(resourceResolver.adaptTo(PageManager.class)).thenReturn(mockedPageManager);
		when(mockedPageManager.getPage(PATH_LIST_SALES_FORCE_CAMPAIGN_FIELD_MAPPING)).thenReturn(mockedPage);
		when(mockedPage.adaptTo(GenericList.class)).thenReturn(mockedGenericList);
		when(mockedGenericList.getItems()).thenReturn(mockedCampaignList);

		resultName = salesForceMapperService.mapCampaignFormNameToSalesForce(SalesForce.CAMPAIGN_NAME, resourceResolver);

		//then the returned object should have all set
		assertEquals(SalesForce.CAMPAIGN_NAME, resultName);
	}

	@Test
	public void testShouldNotMapCampaignField(){
		//given
		//An empty campaign name

		String resultCampaign;
		ResourceResolver resourceResolver = mock(ResourceResolver.class);
		PageManager mockedPageManager = mock(PageManager.class);
		Page mockedPage = mock(Page.class);
		GenericList mockedGenericList = mock(GenericList.class);
		List<Item> mockedCampaignList = createMockSalesForceMappingList();

		//
		//when calling the mapper
		when(resourceResolver.adaptTo(PageManager.class)).thenReturn(mockedPageManager);
		when(mockedPageManager.getPage(PATH_LIST_SALES_FORCE_CAMPAIGN_FIELD_MAPPING)).thenReturn(mockedPage);
		when(mockedPage.adaptTo(GenericList.class)).thenReturn(mockedGenericList);
		when(mockedGenericList.getItems()).thenReturn(mockedCampaignList);
		resultCampaign = salesForceMapperService.mapCampaignFormNameToSalesForce(SalesForce.CAMPAIGN_NAME, resourceResolver);

		//then the returned object should be empty
		assertEquals("", resultCampaign);
	}


	@Test
	public void testShouldNotMapCampaignFields(){
		//given
		//An Empty leadID and empty CampaignID

		SObject[] resultMember;
		Map<String, String> mockedInputData = createEmptyMockCampaignMemberData();
		//when calling the mapper
		resultMember = salesForceMapperService.mapFormToSalesForceCampaignMember(mockedInputData.get(SalesForce.LEAD_ID), mockedInputData.get(SalesForce.CAMPAIGN_ID));

		//then the returned object should be empty
		assertEquals(expectedCampaignMemberEmptyModel().toString(), resultMember[0].toString());
	}

	@Test
	public void testShouldMapAllCampaignMemberFields(){
		//given
		//A leadId and CampaignID

		SObject[] resultMember;
		Map<String, String> mockedInputData = createMockCampaignMemberData();
		//when calling the mapper
		resultMember = salesForceMapperService.mapFormToSalesForceCampaignMember(mockedInputData.get(SalesForce.LEAD_ID), mockedInputData.get(SalesForce.CAMPAIGN_ID));

		//then the returned object should have all set
		assertEquals(expectedFullCampaignMember().toString(), resultMember[0].toString());
	}

	private Map<String, Object> createMockRequestData() {
		Map<String, Object> mockedRequestData = new HashMap<>();
		mockedRequestData.put(SalesForce.FIRST_NAME, new String[]{"Lucas"});
		mockedRequestData.put(SalesForce.LAST_NAME, new String[]{"Mathews"});
		mockedRequestData.put(FormConstants.BUSINESS, new String[]{"My Business"});
		mockedRequestData.put(FormConstants.PHONE_NUMBER, new String[]{"444333222"});
		mockedRequestData.put(SalesForce.EMAIL, new String[]{"lucas.mathews@test.com"});
		return mockedRequestData;
	}

	private Map<String, String> createMockCampaignMemberData() {
		Map<String, String> mockedCampaignData = new HashMap<>();
		mockedCampaignData.put(SalesForce.CAMPAIGN_ID, "111222333");
		mockedCampaignData.put(SalesForce.LEAD_ID, "444555666");
		return mockedCampaignData;
	}

	private Map createEmptyMockFormData() {
		Map mockedEmptyData = new HashMap();
		return mockedEmptyData;
	}

	private List<Item> createMockSalesForceMappingList() {
		List<Item> mockedDataList = new ArrayList<>();
		Item firstNameMapping = new GenericListImpl.ItemImpl(SalesForce.FIRST_NAME , SalesForce.FIRST_NAME, null);
		Item lastNameMapping = new GenericListImpl.ItemImpl(SalesForce.LAST_NAME , SalesForce.LAST_NAME, null);
		Item companyNameMapping = new GenericListImpl.ItemImpl(FormConstants.BUSINESS , SalesForce.COMPANY, null);
		Item phoneNameMapping = new GenericListImpl.ItemImpl(FormConstants.PHONE_NUMBER , SalesForce.PHONE, null);
		Item emailNameMapping = new GenericListImpl.ItemImpl(SalesForce.EMAIL , SalesForce.EMAIL, null);
		mockedDataList.add(firstNameMapping);
		mockedDataList.add(lastNameMapping);
		mockedDataList.add(companyNameMapping);
		mockedDataList.add(phoneNameMapping);
		mockedDataList.add(emailNameMapping);
		return mockedDataList;
	}

	private List<Item> getMockedCampaignList() {
		List<Item> mockedDataList = new ArrayList<>();
		Item campaignNameMapping = new GenericListImpl.ItemImpl(SalesForce.CAMPAIGN_NAME , SalesForce.CAMPAIGN_NAME, null);
		mockedDataList.add(campaignNameMapping);
		return mockedDataList;
	}

	private List<Item> createEmptyMockListData() {
		List<Item> mockedDataList = new ArrayList<>();
		return mockedDataList;
	}

	private Map<String, String> createEmptyMockCampaignMemberData() {
		Map<String, String> mockedCampaignData = new HashMap<>();
		mockedCampaignData.put(SalesForce.CAMPAIGN_ID, null);
		mockedCampaignData.put(SalesForce.LEAD_ID, null);
		return mockedCampaignData;
	}

	private SObject[] expectedFullLead() {
		SObject[] expectedSalesForceObject = new SObject[1];
		SObject lead = new SObject();

		lead.setType(SalesForce.LEAD);
		lead.setField(SalesForce.FIRST_NAME, "Lucas");
		lead.setField(SalesForce.LAST_NAME, "Mathews");
		lead.setField(SalesForce.COMPANY, "My Business");
		lead.setField(SalesForce.PHONE, "444333222");
		lead.setField(SalesForce.EMAIL, "lucas.mathews@test.com");
		expectedSalesForceObject[0] = lead;
		return expectedSalesForceObject;
	}

	private SObject[] expectedRequestFormEmptyModel() {
		SObject[] expectedSalesForceObject = new SObject[1];
		SObject lead = new SObject();
		lead.setType(SalesForce.LEAD);
		expectedSalesForceObject[0] = lead;
		return expectedSalesForceObject;
	}

	private SObject expectedCampaignMemberEmptyModel() {
		SObject expectedSalesForceObject = new SObject();
		expectedSalesForceObject.setType(SalesForce.CAMPAIGN_MEMBER);
		expectedSalesForceObject.setField(SalesForce.CAMPAIGN_ID, null);
		expectedSalesForceObject.setField(SalesForce.LEAD_ID, null);
		return expectedSalesForceObject;
	}

	private SObject expectedFullCampaignMember() {
		SObject expectedSalesForceObject = new SObject();
		expectedSalesForceObject.setType(SalesForce.CAMPAIGN_MEMBER);
		expectedSalesForceObject.setField(SalesForce.CAMPAIGN_ID, "111222333");
		expectedSalesForceObject.setField(SalesForce.LEAD_ID, "444555666");
		return expectedSalesForceObject;
	}
}
