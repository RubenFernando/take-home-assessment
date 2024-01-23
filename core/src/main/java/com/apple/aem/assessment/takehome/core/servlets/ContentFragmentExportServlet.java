package com.apple.aem.assessment.takehome.core.servlets;

import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_EXTENSIONS;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_METHODS;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_PATHS;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletResponse;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.apple.aem.assessment.takehome.core.services.ContentFragmentExportService;
import com.apple.aem.assessment.takehome.core.utils.Constants;
import com.google.gson.JsonObject;

@Component(service = Servlet.class, property = {
		"service.description=" + "This servlet will export the content fragment data to Json.",
		SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_GET, SLING_SERVLET_PATHS + "=/bin/getGlobalNavigation",
		SLING_SERVLET_EXTENSIONS + "=json" })
public class ContentFragmentExportServlet extends SlingSafeMethodsServlet {

	/**
	 *
	 */
	private static final long serialVersionUID = -6677554076769778247L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ContentFragmentExportServlet.class);

	@Reference
	private ContentFragmentExportService exportService;

	@Override
	protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws IOException {
		LOGGER.trace("Entering doGet method in ContentFragmentExportServlet");
		String cfPath = request.getRequestPathInfo().getSuffix();
		try {
			JsonObject root = exportService.exportCfToJson(request, cfPath);
			String json = root.toString();
			response.setContentType(Constants.RESP_CONTENT_TYPE);
			response.getWriter().write(json);
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (IOException e) {
			LOGGER.error("Exception occurred in doGet (ContentFragmentExportServlet): {}", e.getMessage());
		}
	}

}
