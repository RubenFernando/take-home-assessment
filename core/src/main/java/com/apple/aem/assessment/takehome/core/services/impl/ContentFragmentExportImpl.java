package com.apple.aem.assessment.takehome.core.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.osgi.service.component.annotations.Component;

import com.adobe.cq.dam.cfm.ContentElement;
import com.adobe.cq.dam.cfm.ContentFragment;
import com.adobe.cq.dam.cfm.ElementTemplate;
import com.adobe.cq.dam.cfm.FragmentTemplate;
import com.apple.aem.assessment.takehome.core.services.ContentFragmentExportService;
import com.apple.aem.assessment.takehome.core.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Component(immediate = true, service = ContentFragmentExportService.class)
public class ContentFragmentExportImpl implements ContentFragmentExportService {

	@Override
	public JsonObject exportCfToJson(SlingHttpServletRequest request, String assetPath) {
		JsonObject root = new JsonObject();
		Resource cfResource = request.getResourceResolver().getResource(assetPath);
		if (Objects.nonNull(cfResource)) {
			ContentFragment contentFragment = cfResource.adaptTo(ContentFragment.class);
			if (Objects.nonNull(contentFragment)) {
				FragmentTemplate fragmentTemplate = contentFragment.getTemplate();
				Iterator<ElementTemplate> elementIter = fragmentTemplate.getElements();
				root.addProperty(Constants.ID, assetPath);
				JsonArray jsonArray = new JsonArray();

				while (elementIter.hasNext()) {
					ElementTemplate elementTemplate = elementIter.next();
					String elementName = elementTemplate.getName();
					String dataType = elementTemplate.getDataType().getSemanticType();
					if (StringUtils.isBlank(dataType)) {
						dataType = elementTemplate.getDataType().getValueType();
					}
					JsonObject jsonObject = new JsonObject();
					jsonObject.addProperty(Constants.KEY, elementName);
					jsonObject.addProperty(Constants.DATA_TYPE, dataType);
					jsonObject.addProperty(Constants.TYPE, dataType);
					jsonObject.addProperty(Constants.TITLE, elementTemplate.getTitle());
					boolean isMultiValue = elementTemplate.getDataType().isMultiValue();
					jsonObject.addProperty(Constants.MULTI_VALUE, isMultiValue);
					ContentElement contentElement = contentFragment.getElement(elementName);
					String value = contentElement.getContent();
					jsonObject.addProperty(Constants.VALUE, value);
					List<String> valueList = new ArrayList<>();
					valueList.add(value);
					if (isMultiValue) {
						valueList = setValueList(jsonObject, contentElement, valueList);
					}
					jsonArray.add(jsonObject);
					getReferences(request, dataType, jsonObject, valueList);
				}
				root.add(Constants.PROPERTIES, jsonArray);
			}
		}
		return root;
	}

	private List<String> setValueList(JsonObject jsonObject, ContentElement contentElement, List<String> valueList) {
		String[] valueArr = contentElement.getValue().getValue(String[].class);
		if (Objects.nonNull(valueArr) && valueArr.length > 1) {
			JsonArray array = new Gson().toJsonTree(valueArr).getAsJsonArray();
			jsonObject.add(Constants.VALUE, array);
			valueList = Arrays.asList(valueArr);
		}
		return valueList;
	}

	private void getReferences(SlingHttpServletRequest request, String dataType, JsonObject jsonObject,
			List<String> valueList) {
		if (StringUtils.isNotBlank(dataType) && dataType.equals(Constants.CONTENT_FRAGMENT)) {
			JsonArray referenceArray = new JsonArray();
			for (String assetPath : valueList) {
				referenceArray.add(exportCfToJson(request, assetPath));
			}
			jsonObject.add(Constants.REFERENCES, referenceArray);
		}
	}

}
