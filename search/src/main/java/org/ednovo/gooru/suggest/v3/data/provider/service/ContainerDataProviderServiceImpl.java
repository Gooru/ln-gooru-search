package org.ednovo.gooru.suggest.v3.data.provider.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.ednovo.gooru.search.es.constant.Constants;
import org.ednovo.gooru.search.es.repository.CollectionRepository;
import org.ednovo.gooru.search.es.repository.ContentRepository;
import org.ednovo.gooru.search.es.repository.CourseRepository;
import org.ednovo.gooru.search.es.repository.TaxonomyRepository;
import org.ednovo.gooru.suggest.v3.data.provider.model.CollectionDataProviderCriteria;
import org.ednovo.gooru.suggest.v3.model.CollectionContextData;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ContainerDataProviderServiceImpl implements ContainerDataProviderService, Constants{

	protected static final Logger LOG = LoggerFactory.getLogger(ContainerDataProviderServiceImpl.class);

	@Autowired
	private CollectionRepository collectionRepository;
	
	@Autowired
	private ContentRepository contentRepository;
	
	@Autowired
	private CourseRepository courseRepository;
	
	@Autowired
	private TaxonomyRepository taxonomyRepository;
	
	@SuppressWarnings("unchecked")
	@Override
	public CollectionContextData getCollectionData(CollectionDataProviderCriteria collectionDataProviderCriteria) {
		CollectionContextData collectionContextDo = new CollectionContextData();
		try {
			String collectionId = collectionDataProviderCriteria.getCollectionId();
			if (collectionId != null) {
				Map<String, Object> collectionSrc = collectionRepository.getCollectionData(collectionId);
				collectionContextDo.setId(collectionId);
				if (collectionSrc != null) {
					if (collectionSrc.get("title") != null)
						collectionContextDo.setTitle(collectionSrc.get("title").toString());
					if (collectionSrc.get("description") != null)
						collectionContextDo.setLearningObjective(collectionSrc.get("description").toString());
					Map<String, Object> conceptNodeJson = getTaxonomy((collectionSrc.get("taxonomy") != null) ? (JSONObject) collectionSrc.get("taxonomy") : null);
					if (conceptNodeJson != null && !conceptNodeJson.isEmpty()) {
						collectionContextDo.setTaxonomyDomains((ArrayList<String>) conceptNodeJson.get("domains"));
						collectionContextDo.setStandards((ArrayList<String>) conceptNodeJson.get("standards"));
						collectionContextDo.setTaxonomyLeafSLInternalCodes((ArrayList<String>) conceptNodeJson.get("leafSLInternalCodes"));
						collectionContextDo.setTaxonomyConceptNodeNeighbours((ArrayList<String>) conceptNodeJson.get("conceptNodeNeighbours"));
					}
					if (collectionSrc.get("course_id") != null) {
						String courseId = collectionSrc.get("course_id").toString();
						collectionContextDo.setCourseId(courseId);
						Map<String, Object> courseMetaMap = courseRepository.getCourseData(courseId);
						if (courseMetaMap != null && courseMetaMap.get("title") != null) {
							collectionContextDo.setCourseTitle(courseMetaMap.get("title").toString());
						}
					}
					List<Map<String, Object>> itemList = contentRepository.getItems(collectionId);
					Set<String> itemIds = new HashSet<>();
					itemList.forEach(item -> {
						itemIds.add(item.get("id").toString());
					});
					collectionContextDo.setItemIds(itemIds);
					collectionContextDo.setItemCount(itemList.size());
				}
			}
		} catch (Exception e) {
			LOG.error("Unable to fetch from DB : {} :: {}", collectionDataProviderCriteria.getCollectionId(), e.getMessage());
		}
		return collectionContextDo;
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
				if (parentCode != null) {
					List<String> conceptNodes = taxonomyRepository.getConceptNeighbours(leafSLInternalCode, parentCode);
					if (conceptNodes != null && conceptNodes.size() > 0)
						conceptNodeNeighbours.addAll(conceptNodes);
				}
			}
			conceptNodeJson.put("standards", standardArray);
			conceptNodeJson.put("subjects", subjectArray);
			conceptNodeJson.put("courses", courseArray);
			conceptNodeJson.put("domains", domainArray);
			conceptNodeJson.put("leafSLInternalCodes", leafSLInternalCodes);
			conceptNodeJson.put("conceptNodeNeighbours", conceptNodeNeighbours.stream().map(String::toLowerCase).collect(Collectors.toList()));
		}
		return conceptNodeJson;
	}

}

