package gablegar.components.servlet;

import gablegar.components.services.MarketingFormService;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.felix.scr.annotations.Reference;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created by glegarda on 21/02/18.
 */
@Component(metatype = true)
@Service(Servlet.class)
@Properties({
		@Property(name = "sling.servlet.resourceTypes", value = "AEMMarketingForm/components/page/marketingForm"),
		@Property(name = "sling.servlet.selectors", value = "save"),
		@Property(name = "sling.servlet.extensions", value = "json"),
		@Property(name = "sling.servlet.methods", value = "POST")
})
public class MarketingFormServlet extends SlingAllMethodsServlet {

	@Reference
	MarketingFormService formService;


	private static final Logger log = LoggerFactory.getLogger(MarketingFormServlet.class);

	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
		log.info("Post to Marketing Form Component form {} /n with {}", request.getRequestURI(), request.getParameterMap().toString());
		boolean result = formService.processForm(request.getResource(), request.getParameterMap(), request.getResourceResolver());
		if (result) {
			response.setContentType("application/json");
			response.setCharacterEncoding("utf-8");
			response.setStatus(200);
		} else {
			response.setContentType("application/json");
			response.setCharacterEncoding("utf-8");
			response.setStatus(500);
		}

	}

}
