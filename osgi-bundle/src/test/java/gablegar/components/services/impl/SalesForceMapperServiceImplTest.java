package gablegar.components.services.impl;

import com.adobe.acs.commons.genericlists.GenericList.Item;
import com.adobe.acs.commons.genericlists.impl.GenericListImpl;
import com.sforce.soap.partner.sobject.SObject;
import gablegar.components.constants.FormConstants;
import gablegar.components.constants.SalesForce;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by glegarda on 21/02/18.
 */
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

		//when calling the mapper will bring a lead with all fields
		resultLead = salesForceMapperService.mapFormToSalesForceLead(createMockRequestData(), createMockSalesForceMappingList());

		//then the returned object should have all set
		assertEquals(expectedFullLead()[0].toString(), resultLead[0].toString());
	}

	@Test
	public void testShouldNotMapFields(){
		//given
		//An entry with no fields

		SObject[] resultLead;

		//when calling the mapper
		resultLead = salesForceMapperService.mapFormToSalesForceLead(createEmptyMockFormData(), createEmptyMockListData());

		//then the returned object should be empty
		assertEquals(expectedRequestFormEmptyModel()[0].toString(), resultLead[0].toString());
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
