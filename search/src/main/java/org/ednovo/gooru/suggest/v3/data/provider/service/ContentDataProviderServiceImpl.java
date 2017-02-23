package org.ednovo.gooru.suggest.v3.data.provider.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.ednovo.gooru.search.es.constant.Constants;
import org.ednovo.gooru.search.es.repository.ContentRepository;
import org.ednovo.gooru.search.es.repository.TaxonomyRepository;
import org.ednovo.gooru.suggest.v3.data.provider.model.ResourceDataProviderCriteria;
import org.ednovo.gooru.suggest.v3.model.ResourceContextData;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContentDataProviderServiceImpl implements ContentDataProviderService, Constants {

	private static final Logger LOG = LoggerFactory.getLogger(ContentDataProviderServiceImpl.class);

	@Autowired
	private ContentRepository contentRepository;

	@Autowired
	private TaxonomyRepository taxonomyRepository;

	@SuppressWarnings("unchecked")
	@Override
	public ResourceContextData getResourceContextData(ResourceDataProviderCriteria resourceDataProviderCriteria) throws JSONException {
		ResourceContextData resourceContextDo = null;
		if (resourceDataProviderCriteria.getResourceId() != null) {
			try {
				Map<String, Object> resourceData = contentRepository.getContent(resourceDataProviderCriteria.getResourceId());
				if (resourceData == null) {
					return null;
				}
				resourceContextDo = new ResourceContextData();
				resourceContextDo.setId((String) resourceData.get("id"));
				resourceContextDo.setTitle((String) resourceData.get("title"));
				resourceContextDo.setDescription((String) resourceData.get("description"));
				resourceContextDo.setContentFormat(((String) resourceData.get("contentFormat") != null) ? (String) resourceData.get("contentFormat") : null);
				resourceContextDo.setContentSubFormat((String) resourceData.get("contentSubFormat"));
				resourceContextDo.setCreatorId((String) resourceData.get("creatorId"));
				resourceContextDo.setKeywords(((String) resourceData.get("keywords") != null) ? (String) resourceData.get("keywords") : null);
				Map<String, Object> conceptNodeJson = getTaxonomy((resourceData.get("taxonomy") != null) ? (JSONObject) resourceData.get("taxonomy") : null);
				resourceContextDo.setStandards((ArrayList<String>) conceptNodeJson.get("leafSLInternalCodes"));
				resourceContextDo.setConceptNodeNeighbours((ArrayList<String>) conceptNodeJson.get("conceptNodeNeighbours"));
			} catch(Exception e) {
				LOG.error("Unable to fetch from DB : Resource ID may be a copied resource", e.getMessage());
			}
		}
		return resourceContextDo;
	}

	@SuppressWarnings("unchecked")
	Map<String, Object> getTaxonomy(JSONObject taxonomyObject) {
		List<String> subjectArray = new ArrayList<>();
		List<String> courseArray = new ArrayList<>();
		List<String> domainArray = new ArrayList<>();
		List<String> standardArray = new ArrayList<>();
		List<String> learningTargetArray = new ArrayList<>();
		List<String> leafSLInternalCodes = new ArrayList<>();
		List<String> conceptNodeNeighbours = new ArrayList<>();
		Map<String, Object> conceptNodeJson = new HashMap<>();

		if (taxonomyObject != null && (taxonomyObject.length() > 0)) {
			Iterator<String> keys = taxonomyObject.keys();
			while (keys.hasNext()) {
				String code = keys.next().toString();
				String[] codes = code.split("-");
				String subjectCode = null;
				String courseCode = null;
				String domainCode = null;

				if (codes.length > 0) {

					if (codes.length == 1) {
						subjectCode = code;
					} else if (codes.length > 1) {
						subjectCode = code.substring(0, StringUtils.ordinalIndexOf(code, "-", 1));
					}
					if (codes.length == 2) {
						courseCode = code;
					} else if (codes.length > 2) {
						courseCode = code.substring(0, StringUtils.ordinalIndexOf(code, "-", 2));
					}
					if (codes.length == 3) {
						domainCode = code;
					} else if (codes.length > 3) {
						domainCode = code.substring(0, StringUtils.ordinalIndexOf(code, "-", 3));
					}
					if (codes.length == 4) {
						standardArray.add(code);
						leafSLInternalCodes.add(code);
					}
					if (codes.length == 5) {
						learningTargetArray.add(code);
						leafSLInternalCodes.add(code);
					}
				}
				subjectArray.add(subjectCode != null ? subjectCode.toLowerCase() : null);
				courseArray.add(courseCode != null ? courseCode.toLowerCase() : null);
				domainArray.add(domainCode != null ? domainCode.toLowerCase() : null);
			}
			for (String leafSLInternalCode : leafSLInternalCodes) {
				String parentCode = taxonomyRepository.getParentTaxonomyCode(leafSLInternalCode);
				List<String> conceptNodes = taxonomyRepository.getConceptNeighbours(leafSLInternalCode, parentCode);
				if (conceptNodes != null && conceptNodes.size() > 0)
					conceptNodeNeighbours.addAll(conceptNodes);
			}
			conceptNodeJson.put("standards", standardArray);
			conceptNodeJson.put("subjects", subjectArray);
			conceptNodeJson.put("courses", courseArray);
			conceptNodeJson.put("domains", domainArray);
			conceptNodeJson.put("leafSLInternalCodes", leafSLInternalCodes.stream().map(String::toLowerCase).collect(Collectors.toList()));
			conceptNodeJson.put("conceptNodeNeighbours", conceptNodeNeighbours.stream().map(String::toLowerCase).collect(Collectors.toList()));
		}
		return conceptNodeJson;
	}

}
