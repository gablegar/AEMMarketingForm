# AEMMarketingForm
This is an example of a system of marketing forms ready to use in AEM, 
this examples uses JAVA, Sling, JCR, OSGI, OSGI services.

The idea is to create dynamic forms that can be easily authored to reflect
changes in the Sales Force Structure

It integrates a marketing form to SalesForce using WSC API
therefore for using WSC salesForce dependencies will need to be deployed


Mappings to the Sales Force Database field names can be found in:

1) Sales Force database field names may be different from those in the form 
and can easily change(created, deleted) so I use this authorable list to map them
/Users/glegarda/AEMMarketingForm/content/src/main/content/jcr_root/etc/acs-commons/lists/salesforce-field-Mapping

2) Sales Force campaigns can vary in time so I use this list to map the name 
in the dialog, like this everyone can create new options without coding
/Users/glegarda/AEMMarketingForm/content/src/main/content/jcr_root/etc/acs-commons/lists/salesForceCampaignNames



Integrate Sales Force dependencies 

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