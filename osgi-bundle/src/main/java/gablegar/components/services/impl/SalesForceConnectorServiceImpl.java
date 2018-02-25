package gablegar.components.services.impl;

import com.sforce.soap.partner.*;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;
import gablegar.components.services.SalesForceConnectorService;
import org.apache.felix.scr.annotations.*;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static gablegar.components.constants.SalesForce.ENDPOINT;
import static gablegar.components.constants.SalesForce.SALES_FORCE_SECRET;
import static gablegar.components.constants.SalesForce.SALES_FORCE_USER;

/**
 * Created by glegarda on 22/02/18.
 */
@Component(metatype = true)
@Service(SalesForceConnectorService.class)
//Properties values are set by environment using xml
//gablegar.components.services.impl.SalesForceConnectorServiceImpl.xml
@Properties({
		@Property(name = ENDPOINT, description = "Sales Force Endpoint", value = ""),
		@Property(name = SALES_FORCE_USER, description = "Sales Force User", value = ""),
		@Property(name = SALES_FORCE_SECRET, description = "Sales Force Secret can be password + token", value = "")})
public class SalesForceConnectorServiceImpl implements SalesForceConnectorService {

	private static final Logger log = LoggerFactory.getLogger(SalesForceConnectorService.class);

	private String endpoint;
	private String username;
	private String password;
	private PartnerConnection connection;

	@Activate
	@Modified
	protected void createConnection(ComponentContext context) {
		retrieveConfigFromOSGI(context);
		if(configValuesNotEmpty()) {
			ConnectorConfig config = new ConnectorConfig();
			setSalesForceConnectionConfig(config);
			try {
				connection = Connector.newConnection(config);
				log.info("MarketingFormServiceImpl activated with {} {}", getEndpoint(), getUsername());
				if(connection != null) {
					logConnectionSuccessful(config);
				}

			} catch (ConnectionException exception) {
				log.error("Error while connecting to Sales Force: {}", exception);
			}
		} else {
			log.error("Please set the SalesForce properties in the OSGI Service");
		}
	}


	public QueryResult executeQuery(String query) {
		try {
			return connection.query(query);
		} catch (Exception exception) {
			log.error("Error executing query: {}", exception);
			return new QueryResult();
		}
	}

	public SaveResult[] createObject(SObject[] object) {
		try {
			return connection.create(object);
		} catch (Exception exception) {
			log.error("Error creating object in SalesForce: {}, {}",object, exception);
			SaveResult[] saveResults = new SaveResult[1];
			SaveResult error = new SaveResult();
			error.setSuccess(false);
			saveResults[0] = error;
			return saveResults;
		}
	}

	public DeleteResult[] deleteObject(String id) {
		try {
			return connection.delete(new String[]{id});
		} catch (ConnectionException exception) {
			log.error("Error deleting object in SalesForce id: {}, exception :{}",id, exception);
			DeleteResult[] deleteResults = new DeleteResult[1];
			DeleteResult error = new DeleteResult();
			error.setSuccess(false);
			deleteResults[0] = error;
			return deleteResults;
		}
	}


	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	private void setSalesForceConnectionConfig(ConnectorConfig config) {
		config.setUsername(getUsername());
		config.setPassword(getPassword());
		config.setAuthEndpoint(getEndpoint());
		config.setServiceEndpoint(getEndpoint());
		config.setTraceMessage(true);
	}

	private void logConnectionSuccessful(ConnectorConfig config) {
		//this method is not called often
		// it is called when the OSGI service is activated
		// or if the properties change
		log.info("Connection to SalesForce Successful");
		// display some current settings
		log.debug("Authorization EndPoint: {}", config.getAuthEndpoint());
		log.debug("Service EndPoint: {}", config.getServiceEndpoint());
		log.debug("Username: {}", config.getUsername());
		log.debug("SessionId: {}", config.getSessionId());
	}

	private void retrieveConfigFromOSGI(ComponentContext context) {
		setEndpoint(PropertiesUtil.toString(context.getProperties().get(ENDPOINT), null));
		setUsername(PropertiesUtil.toString(context.getProperties().get(SALES_FORCE_USER), null));
		setPassword(PropertiesUtil.toString(context.getProperties().get(SALES_FORCE_SECRET), null));
	}

	private boolean configValuesNotEmpty() {
		return getUsername()!= null && !getUsername().isEmpty()
				&& getEndpoint() != null && !getEndpoint().isEmpty()
				&& getPassword()!= null && !getPassword().isEmpty();
	}

}