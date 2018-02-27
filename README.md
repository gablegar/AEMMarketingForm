# AEMMarketingForm
This is an example of fully generic marketing forms integrated with Sales Force in AEM, 
This examples uses JAVA 8, Sling, JCR, OSGI, OSGI services, Sales Force WSC.
In FrontEnd uses javascript,angular,css as AEM client libs.
There are a full suite of tests of the java services using mockito and powermock

To install in AEM author please use:
mvn clean install -PautoInstallPackage


Summary:

The idea is to create dynamic forms that can be easily authored to reflect
changes in the Sales Force Structure, to use new fields and new campaigns
all of this is authorable in AEM dialogs.

It integrates a marketing form to SalesForce using WSC API
therefore for using WSC salesForce dependencies will need to be deployed

Mappings to the Sales Force Database field names can be found in:

1) Sales Force database field names may be different from those in the form 
and can easily change(created, deleted) so I use this authorable list to map them.
the form fields will iterate from this list so the form is generic.
/AEMMarketingForm/content/src/main/content/jcr_root/etc/acs-commons/lists/salesforce-field-Mapping

2) New Sales Force campaigns can be created in time so I use this list to map the name 
in the dialog, like this everyone can create new options without coding
/AEMMarketingForm/content/src/main/content/jcr_root/etc/acs-commons/lists/salesForceCampaignNames


AEM OSGI configuration

Connection is needed with sales force
in order to configure the connection values:
 
1) enter to <AEM author instance>/system/console/configMgr

2) configure endpoint password and user gablegar.components.services.impl.MarketingFormServiceImpl


AEM author configuration

Example content is provided, however  new forms with custom fields can be created to test
(notice that the fields and campaigns should exist in Sales Force instance), for this:

1) create a new list on /AEMMarketingForm/content/src/main/content/jcr_root/etc/acs-commons/lists/
2) configure the desired fields on the new list for the new form 
3) create a new campaign on 
/AEMMarketingForm/content/src/main/content/jcr_root/etc/acs-commons/lists/salesForceCampaignNames
4) create a new page using marketing form template, if no components show, 
in design mode add AEM Marketing Form components group
5) add a header, footer and registrationForm components to the page
6) configure, list of fields to be used created on first step, configure campaign, success, error message
header and footer
7) configure any new fields on Sales Force



Integrate Sales Force dependencies (this is already in pom)

WSC project needs to be built 
https://github.com/forcedotcom/wsc

There's three options then
1) Generate bundle and install in OSGI using:
/AEMMarketingForm/osgi-bundle/resources/manifest-salesforce-partner.txt
/AEMMarketingForm/osgi-bundle/resources/manifest-salesforce-wsc.txt

for example :
jar cvfm force-partner-api-42.0.0-bundle.jar manifest-salesforce-partner.txt force-partner-api-42.0.0.jar

2) Export the salesforce packages in pom like:
<Export-Package>com.sforce.*</Export-Package>

3) Export only the SalesForce packages used in compilation on pom like:(I used this one as it is the most clever) 
<Embed-Dependency>*;scope=compile</Embed-Dependency>
<Embed-Dependency>*;scope=compile</Embed-Dependency>
<Embed-Transitive>true</Embed-Transitive>
<Import-Package>!com.google.appengine.api.urlfetch.*,!org.apache.log.*,*</Import-Package>