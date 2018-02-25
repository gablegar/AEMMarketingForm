package gablegar.components.constants;

/**
 * Created by glegarda on 25/02/18.
 */
public class FormConstants {

	public static final String PHONE_NUMBER = "phoneNumber";
	public static final String BUSINESS = "business";
	public static final String CAMPAIGN_MISSING_CONFIGURE_PATH =
											"Sales Force campaign missing, please configure ACS generic list" +
											" on /etc/acs-commons/lists/salesForceCampaignNames";

	private FormConstants(){
		throw new AssertionError();
	}
}
