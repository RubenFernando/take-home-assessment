package com.apple.aem.assessment.takehome.core.services;

import org.apache.sling.api.SlingHttpServletRequest;

import com.google.gson.JsonObject;

public interface ContentFragmentExportService {

	JsonObject exportCfToJson(SlingHttpServletRequest request, String assetPath);

}
