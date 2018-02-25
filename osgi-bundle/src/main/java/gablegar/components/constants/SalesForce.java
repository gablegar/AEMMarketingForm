package gablegar.components.constants;

/**
 * Created by glegarda on 24/02/18.
 */
public class SalesForce {

	public static final String PATH_LIST_SALESFORCE_FIELD_MAPPING = "/etc/acs-commons/lists/salesforce-field-Mapping";
	public static final String CAMPAIGN_NAME = "campaignName";
	public static final String LEAD = "Lead";
	public static final String CAMPAIGN_MEMBER = "CampaignMember";
	public static final String CAMPAIGN_ID = "campaignId";
	public static final String LEAD_ID = "leadId";
	public static final String FIRST_NAME = "firstName";
	public static final String LAST_NAME = "lastName";
	public static final String COMPANY = "Company";
	public static final String PHONE = "phone";
	public static final String EMAIL = "email";
	public static final String ENDPOINT = "endpoint";
	public static final String SALES_FORCE_USER = "salesforce-user";
	public static final String SALES_FORCE_SECRET = "salesforce-secret";

	private SalesForce(){
		throw new AssertionError();
	}
}
