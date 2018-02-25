package gablegar.components.services;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import java.util.Map;

/**
 * Created by glegarda on 21/02/18.
 */
public interface MarketingFormService {

	boolean processForm(Resource resource, Map formValues, ResourceResolver resourceResolver);
}

