package org.ednovo.gooru.search.es.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.ednovo.gooru.search.es.constant.Constants;
import org.ednovo.gooru.search.es.constant.EsIndex;
import org.ednovo.gooru.search.es.constant.IndexFields;
import org.ednovo.gooru.search.es.exception.NotFoundException;
import org.ednovo.gooru.search.es.handler.SearchHandler;
import org.ednovo.gooru.search.es.handler.SearchHandlerType;
import org.ednovo.gooru.search.es.model.Gut;
import org.ednovo.gooru.search.es.model.MapWrapper;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.ednovo.gooru.search.es.repository.LearningMapStatsRepository;
import org.ednovo.gooru.search.model.GutPrerequisites;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * @author Renuka
 * 
 */
@Service
public class LearningMapsServiceImpl implements LearningMapsService, Constants {

	private static final Logger logger = LoggerFactory.getLogger(LearningMapsServiceImpl.class);
	
	@Autowired
	private LearningMapStatsRepository learningMapStatsRepository;
	
	@Override
	public Map<String, Object> search(SearchData searchData, String type, Map<String, Object> resultAsMap) {
		long uStart = System.currentTimeMillis();
		SearchData inputSearchData = new SearchData();
		inputSearchData.setPretty(searchData.getPretty());
		inputSearchData.setQueryString(searchData.getQueryString());
		inputSearchData.setFrom(searchData.getFrom());
		inputSearchData.setSize(searchData.getSize());
		inputSearchData.putFilters(searchData.getFilters());
		inputSearchData.setCrosswalk(searchData.isCrosswalk());
		inputSearchData.setTaxFilterType(searchData.getTaxFilterType());
		inputSearchData.setParameters(searchData.getParameters());
		inputSearchData.setUserTenantId(searchData.getUserTenantId());
		inputSearchData.setUserTenantRootId(searchData.getUserTenantRootId());
		inputSearchData.setUserPermits(searchData.getUserPermits());
		inputSearchData.setType(type);
		inputSearchData.setAdmin(searchData.isAdmin());
		if (RQC_MATCH.matcher(type).matches()) {
			inputSearchData.putFilter(AMPERSAND + CARET_SYMBOL + IndexFields.CONTENT_FORMAT, (type.equalsIgnoreCase(TYPE_SCOLLECTION) ? TYPE_COLLECTION : type));
			if (type.equalsIgnoreCase(TYPE_QUESTION)) inputSearchData.setType(TYPE_RESOURCE);
		} else if (type.equalsIgnoreCase(TYPE_ASSESSMENT)) {
			inputSearchData.putFilter(AMPERSAND + CARET_SYMBOL + IndexFields.CONTENT_FORMAT, type);
			inputSearchData.setType(TYPE_SCOLLECTION);
		}
		
		//To be enabled when aggregated tags are in working condition
		if (CUL_MATCH.matcher(type).matches()) {
			if (searchData.getParameters().containsKey(FLT_STANDARD)) {
				searchData.getParameters().put(FLT_RELATED_LEAF_INTERNAL_CODES, searchData.getParameters().getString(FLT_STANDARD));
				searchData.getParameters().remove(FLT_STANDARD);
			}
		}
		SearchResponse<Object> searchResponse = (SearchResponse<Object>) SearchHandler.getSearcher((PEDAGOGY_UNDERSCORE + inputSearchData.getType()).toUpperCase()).search(inputSearchData);
		Map<String, Object> searchMap = new HashMap<>();
		searchMap.put(TOTAL_HIT_COUNT, searchResponse.getStats().get(TOTAL_HIT_COUNT));
		searchMap.put(RESULT_COUNT, searchResponse.getStats().get(RESULT_COUNT));
		searchMap.put(SEARCH_RESULTS, searchResponse.getSearchResults());
		if (type.equalsIgnoreCase(TYPE_SCOLLECTION)) {
			resultAsMap.put(TYPE_COLLECTION, searchMap);
		} else {
			resultAsMap.put(type, searchMap);
		}
		logger.info("Elapsed time to complete {} search process : {} ms", type , (System.currentTimeMillis() - uStart));
		return resultAsMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void generateStandardFilter(SearchData searchData, String key, String taxCodes, String fwCode) {
		if (searchData.getParameters() != null && searchData.getParameters().getValues().size() != 0) {
			MapWrapper<Object> parameters = searchData.getParameters();
			if (parameters != null && ((parameters.containsKey(FLT_STANDARD_DISPLAY) || (parameters.containsKey(FLT_STANDARD))) && parameters.containsKey(FLT_FWCODE))) {
				SearchData crosswalkRequest = new SearchData();
				crosswalkRequest.setPretty(searchData.getPretty());
				crosswalkRequest.setIndexType(EsIndex.CROSSWALK);
				crosswalkRequest.putFilter(AMPERSAND + CARET_SYMBOL + IndexFields.CROSSWALK_CODES + DOT + key, taxCodes);
				if (fwCode != null) crosswalkRequest.putFilter(AMPERSAND + CARET_SYMBOL + IndexFields.CROSSWALK_CODES + DOT + IndexFields.FRAMEWORK_CODE, fwCode);
				crosswalkRequest.setQueryString(STAR);
				List<Map<String, Object>> crosswalkResponses = (List<Map<String, Object>>) SearchHandler.getSearcher(SearchHandlerType.CROSSWALK.name()).search(crosswalkRequest).getSearchResults();
				Set<String> filterCodes = new HashSet<>();
				if (crosswalkResponses != null && !crosswalkResponses.isEmpty()) {
					for (Map<String, Object> response : crosswalkResponses) {
						Map<String, Object> source = (Map<String, Object>) response.get(SEARCH_SOURCE);
// To be enabled when GUT is fully functional
						//filterCodes.add(((String) source.get(IndexFields.GUT_CODE)).toLowerCase());
						List<Map<String, String>> crosswalkCodes = (List<Map<String, String>>) source.get(IndexFields.CROSSWALK_CODES);
						for (Map<String, String> code : crosswalkCodes) {
							filterCodes.add(((String) code.get(IndexFields.ID)).toLowerCase());
						}
					}
				}
				searchData.getParameters().remove(FLT_STANDARD);
				searchData.getParameters().remove(FLT_FWCODE);
				searchData.getParameters().put(FLT_STANDARD, taxCodes);
				if (parameters.containsKey(FLT_STANDARD_DISPLAY)) searchData.getParameters().put(FLT_STANDARD, StringUtils.join(filterCodes, COMMA));
				searchData.getParameters().remove(FLT_STANDARD_DISPLAY);
// To be enabled when GUT is fully functional
				// searchData.getParameters().put(FLT_RELATED_GUT_CODES, StringUtils.join(filterCodes, COMMA));
			} else if (searchData.getParameters().containsKey(FLT_TAXONOMY_GUT_CODE)) {
				searchData.getParameters().put(FLT_RELATED_GUT_CODES, taxCodes);
				searchData.getParameters().remove(FLT_TAXONOMY_GUT_CODE);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void generateRequestedCodeInfo(SearchData searchData, String key, String taxCodes, String requestedFwCode, Map<String, Object> searchResult) {
		String[] codes = taxCodes.split(COMMA);
		String gutCode = null;
		SearchData taxonomyRequest = new SearchData();
		taxonomyRequest.setPretty(searchData.getPretty());
		taxonomyRequest.setIndexType(EsIndex.GUT);
		if (key.equalsIgnoreCase(TAXONOMY_GUT_CODE)) { 
			taxonomyRequest.putFilter(AMPERSAND + CARET_SYMBOL + IndexFields.ID, (StringUtils.join(codes, COMMA)));
			gutCode = codes[0];
		} else taxonomyRequest.putFilter(AMPERSAND + CARET_SYMBOL + IndexFields.CROSSWALK_CODES + DOT + key, (StringUtils.join(codes, COMMA)));
		if (requestedFwCode != null) taxonomyRequest.putFilter(AMPERSAND + CARET_SYMBOL + IndexFields.CROSSWALK_CODES + DOT + IndexFields.FRAMEWORK_CODE, requestedFwCode);
		taxonomyRequest.setQueryString(STAR);
		List<Gut> taxonomyResponses = (List<Gut>) SearchHandler.getSearcher(SearchHandlerType.GUT.name()).search(taxonomyRequest).getSearchResults();
		Gut response = new Gut();
		if (taxonomyResponses != null && !taxonomyResponses.isEmpty()) {
			response = taxonomyResponses.get(0);
			if (key.equalsIgnoreCase(TAXONOMY_GUT_CODE)) {
				List<GutPrerequisites> prerequisites = new ArrayList<>();
				prerequisites = (List<GutPrerequisites>) response.getGutPrerequisites();
				searchResult.put(IndexFields.PREREQUISITES, prerequisites);
			} else if (requestedFwCode != null) {
				List<GutPrerequisites> gutPrerequisites = new ArrayList<GutPrerequisites>();
				gutPrerequisites.addAll(response.getGutPrerequisites());
				// crosswalk prerequisites for requested fw code
				if (gutPrerequisites.size() > 0) {
					Set<Map<String, String>> progressions = new HashSet<>();
					crosswalkPrerequisites(searchData, requestedFwCode, gutPrerequisites, progressions);
					searchResult.put(IndexFields.PREREQUISITES, progressions);
				}
			}
		} else {
			throw new NotFoundException("LM API: Requested Code not found : " + taxCodes);
		}
		
		searchResult.put(SIGNATURE_CONTENTS, response.getSignatureContents());
		searchResult.put(IndexFields.GUT_CODE, (response.getId()) != null ? response.getId() : gutCode);
		searchResult.put(IndexFields.CODE, response.getDisplayCode());
		searchResult.put(IndexFields.CODE_TYPE, response.getCodeType());
		searchResult.put(IndexFields.TITLE, response.getTitle());
		searchResult.put(IndexFields.SUBJECT, response.getSubjectLabel());
		searchResult.put(IndexFields.COURSE, response.getCourseLabel());
		searchResult.put(IndexFields.DOMAIN, response.getDomainLabel());
	}

	@SuppressWarnings("unchecked")
	private void crosswalkPrerequisites(SearchData searchData, String requestedFwCode, List<GutPrerequisites> gutPrerequisites, Set<Map<String, String>> progressions) {
		gutPrerequisites.forEach(a -> {
			GutPrerequisites gutPrerequisite = (GutPrerequisites) a;
			SearchData crosswalkRequest = new SearchData();
			crosswalkRequest.setPretty(searchData.getPretty());
			crosswalkRequest.setIndexType(EsIndex.CROSSWALK);
			crosswalkRequest.putFilter(AMPERSAND + CARET_SYMBOL + IndexFields.ID, gutPrerequisite.getId());
			crosswalkRequest.setQueryString(STAR);
			List<Map<String, Object>> crosswalkResponses = (List<Map<String, Object>>) SearchHandler.getSearcher(SearchHandlerType.CROSSWALK.name()).search(crosswalkRequest)
					.getSearchResults();
			if (crosswalkResponses != null && !crosswalkResponses.isEmpty()) {
				crosswalkResponses.forEach(cwResponse -> {
					Map<String, Object> source = (Map<String, Object>) cwResponse.get(SEARCH_SOURCE);
					List<Map<String, String>> crosswalkCodes = (List<Map<String, String>>) source.get(IndexFields.CROSSWALK_CODES);
					crosswalkCodes.forEach(cw -> {
						String cwFw = (String) cw.get(IndexFields.FRAMEWORK_CODE);
						if (cwFw.equalsIgnoreCase(requestedFwCode)) {
							Map<String, String> cwCode = new HashMap<>();
							cwCode.put(IndexFields.CODE, (String) cw.get(IndexFields.CODE));
							cwCode.put(IndexFields.TITLE, (String) cw.get(IndexFields.TITLE));
							progressions.add(cwCode);
						}
					});
				});
			}
		});
	}
	
	@Override
	public void generateRequestedCodesInfo(SearchData searchData, String key,  String gutCodes, String requestedFwCode, Map<String, Object> searchResult) {
		String[] codes = gutCodes.split(COMMA);
		List<Map<String, Object>> gutdata = new ArrayList<>(codes.length);
		for (String code : codes) {
			Map<String, Object> result = new HashMap<>();
			generateRequestedCodeInfo(searchData, key, code, requestedFwCode, searchResult);
			result.put(SIGNATURE_CONTENTS, searchResult.get(SIGNATURE_CONTENTS));searchResult.remove(SIGNATURE_CONTENTS);
			result.put(IndexFields.GUT_CODE, searchResult.get(IndexFields.GUT_CODE));searchResult.remove(IndexFields.GUT_CODE);
			result.put(IndexFields.CODE, searchResult.get(IndexFields.CODE));searchResult.remove(IndexFields.CODE);
			result.put(IndexFields.CODE_TYPE, searchResult.get(IndexFields.CODE_TYPE));searchResult.remove(IndexFields.CODE_TYPE);
			result.put(IndexFields.TITLE, searchResult.get(IndexFields.TITLE));searchResult.remove(IndexFields.TITLE);
			result.put(IndexFields.PREREQUISITES, searchResult.get(IndexFields.PREREQUISITES));searchResult.remove(IndexFields.PREREQUISITES);
			result.put(IndexFields.SUBJECT, searchResult.get(IndexFields.SUBJECT));searchResult.remove(IndexFields.SUBJECT);
			result.put(IndexFields.COURSE, searchResult.get(IndexFields.COURSE));searchResult.remove(IndexFields.COURSE);
			result.put(IndexFields.DOMAIN, searchResult.get(IndexFields.DOMAIN));searchResult.remove(IndexFields.DOMAIN);
			gutdata.add(result);
		}
		searchResult.put(IndexFields.GUT_DATA, gutdata);
	}

	@Override
	public void getLearningMapStats(SearchData searchData, Map<String, Object> searchResult, String subjectClassification, String subjectCode, String courseCode, String domainCode, String gutIds, String codeType) {
		List<Map<String, Object>> contentStatsAsList = null;
		if (StringUtils.isNotBlank(gutIds)) contentStatsAsList = getLearningMapStatsRepository().getStatsByIds(gutIds, codeType, searchData.getFrom(), searchData.getSize());
		else contentStatsAsList = getLearningMapStatsRepository().getStats(subjectClassification, subjectCode, courseCode, domainCode, codeType, searchData.getFrom(), searchData.getSize());
		List<Map<String, Object>> stats = new ArrayList<>();
		if (contentStatsAsList != null) {
			for(Map<String, Object> contentStat : contentStatsAsList) {
				Map<String, Object> statsAsMap = new HashMap<>();
				generateRequestedCodeInfo(searchData, TAXONOMY_GUT_CODE, contentStat.get(IndexFields.ID).toString(), null, statsAsMap);
				statsAsMap.put(IndexFields.ID, contentStat.get(IndexFields.ID).toString()); contentStat.remove(IndexFields.ID);
				statsAsMap.put("subjectCode", contentStat.get("subjectCode").toString()); contentStat.remove("subjectCode");
				statsAsMap.put("courseCode", contentStat.get("courseCode").toString()); contentStat.remove("courseCode");
				statsAsMap.put("domainCode", contentStat.get("domainCode").toString()); contentStat.remove("domainCode");
				statsAsMap.put("type", contentStat.get(IndexFields.CODE_TYPE).toString()); contentStat.remove(IndexFields.CODE_TYPE);
				statsAsMap.put("parentId", contentStat.get("parentId") == null ? null : contentStat.get("parentId").toString()); contentStat.remove("parentId");
				statsAsMap.put("sequenceId", (Integer) contentStat.get("sequenceId")); contentStat.remove("sequenceId");
				statsAsMap.remove(SIGNATURE_CONTENTS);
				statsAsMap.remove(IndexFields.GUT_CODE);
				statsAsMap.remove(IndexFields.SUBJECT);
				statsAsMap.remove(IndexFields.COURSE);
				statsAsMap.remove(IndexFields.DOMAIN);
				statsAsMap.put(CONTENT_COUNTS, contentStat);
				stats.add(statsAsMap);
			}
		}
		Long totalHitCount = 0L;
		if(gutIds != null) totalHitCount = getLearningMapStatsRepository().getTotalCountByIds(gutIds, codeType);
		else totalHitCount = getLearningMapStatsRepository().getTotalCount(subjectClassification, subjectCode, courseCode, domainCode, codeType);
		searchResult.put(STATS, stats);
		searchResult.put(TOTAL_HIT_COUNT, totalHitCount);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getLearningMapsFromStaticTable(String gutId, SearchData searchData) {
		Map<String, Object> lm = getLearningMapStatsRepository().getLearningMapsById(gutId);
		if (lm != null) {
			for (String key : lm.keySet()) {
				if (searchData.getSize() == 0 || lm.get(key) == null) {
					Map<String, Object> searchMap = new HashMap<>();
					Integer totalHitCount = 0;
					if (lm.get(key) != null) {
						Map<String, Object> contentMap = (Map<String, Object>) lm.get(key);
						totalHitCount = (Integer) contentMap.get(TOTAL_HIT_COUNT);
					}
					searchMap.put(TOTAL_HIT_COUNT, totalHitCount);
					searchMap.put(RESULT_COUNT, 0);
					searchMap.put(SEARCH_RESULTS, new ArrayList<>());
					lm.put(key, searchMap);
				}
			}
		}
		return lm;
	}
	
	private LearningMapStatsRepository getLearningMapStatsRepository() {
		return learningMapStatsRepository;
	}

}
