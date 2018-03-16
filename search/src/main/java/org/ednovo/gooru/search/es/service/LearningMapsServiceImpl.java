package org.ednovo.gooru.search.es.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.ednovo.gooru.search.es.constant.Constants;
import org.ednovo.gooru.search.es.constant.EsIndex;
import org.ednovo.gooru.search.es.constant.IndexFields;
import org.ednovo.gooru.search.es.handler.SearchHandler;
import org.ednovo.gooru.search.es.handler.SearchHandlerType;
import org.ednovo.gooru.search.es.model.MapWrapper;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.ednovo.gooru.search.es.model.Taxonomy;
import org.ednovo.gooru.search.model.GutPrerequisites;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
/**
 * @author Renuka
 * 
 */
@Service
public class LearningMapsServiceImpl implements LearningMapsService, Constants {

	private static final Logger logger = LoggerFactory.getLogger(LearningMapsServiceImpl.class);
	
	@Override
	public SearchResponse<Object> searchPedagogy(SearchData searchData) {
		SearchResponse<Object> searchResponse = new SearchResponse<Object>();
		Map<String, Object> searchResult = new HashMap<>();
		Map<String, Object> contentResultAsMap = new HashMap<>();
		String[] codes = null;
		String key = null;
		String fwCode = null;
		if (searchData.getParameters() != null && searchData.getParameters().getValues().size() != 0) {
			MapWrapper<Object> parameters = searchData.getParameters();
			if (parameters != null) {
				fwCode = (parameters.getString(FLT_FWCODE));
				if (parameters.containsKey(FLT_STANDARD_DISPLAY)) {
					codes = (parameters.getString(FLT_STANDARD_DISPLAY)).split(COMMA);
					key = IndexFields.CODE;
				} else if (parameters.containsKey(FLT_STANDARD)) {
					codes = (parameters.getString(FLT_STANDARD)).split(COMMA);
					key = IndexFields.ID;
				} else if (parameters.containsKey(FLT_TAXONOMY_GUT_CODE)) {
					codes = (parameters.getString(FLT_TAXONOMY_GUT_CODE)).split(COMMA);
					key = TAXONOMY_GUT_CODE;
				}
			}
		}
		if (key != null && codes != null) {
			generateRequestedCodeInfo(searchData, key, codes, fwCode, searchResult);
			generateStandardFilter(searchData, key, codes, fwCode);
		}

		search(searchData, TYPE_RESOURCE, contentResultAsMap);
		search(searchData, TYPE_QUESTION, contentResultAsMap);
		search(searchData, TYPE_SCOLLECTION, contentResultAsMap);
		search(searchData, TYPE_ASSESSMENT, contentResultAsMap);
		search(searchData, TYPE_RUBRIC, contentResultAsMap);

		//To be disabled with aggregated tags are in working condition
		setFilterWithExtractedKeywordsAndSubjects(searchData, key, codes, fwCode);

		search(searchData, TYPE_COURSE, contentResultAsMap);
		search(searchData, TYPE_UNIT, contentResultAsMap);
		search(searchData, TYPE_LESSON, contentResultAsMap);
		
		searchResult.put(Constants.CONTENTS, contentResultAsMap);
		searchResponse.setSearchResults(searchResult);
		return searchResponse;
	}

	private void extractSubjects(List<String> stds, Set<String> subjects, Boolean isGut) {
		if (stds != null) {
			stds.forEach(std -> {
				if (std.contains(HYPHEN)) {
					if (isGut) {
						subjects.add(std.substring(0, std.indexOf(HYPHEN)));
					} else {
						subjects.add(std.substring(0, std.indexOf(HYPHEN)));
						subjects.add(std.substring((std.indexOf(DOT) + 1), std.indexOf(HYPHEN)));
					}
				}
			});
		}
	}
	
	@Override
	public Map<String, Object> search(SearchData searchData, String type, Map<String, Object> resultAsMap) {
		long uStart = System.currentTimeMillis();
		SearchData inputSearchData = new SearchData();
		inputSearchData.setPretty(searchData.getPretty());
		inputSearchData.setQueryString(searchData.getDefaultQuery());
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
		if (RQC_MATCH.matcher(type).matches()) {
			inputSearchData.putFilter(AMPERSAND + CARET_SYMBOL + IndexFields.CONTENT_FORMAT, (type.equalsIgnoreCase(TYPE_SCOLLECTION) ? TYPE_COLLECTION : type));
			if (type.equalsIgnoreCase(TYPE_QUESTION)) inputSearchData.setType(TYPE_RESOURCE);
		} else if (type.equalsIgnoreCase(TYPE_ASSESSMENT)) {
			inputSearchData.putFilter(AMPERSAND + CARET_SYMBOL + IndexFields.CONTENT_FORMAT, type);
			inputSearchData.setType(TYPE_SCOLLECTION);
		}
		//To be enabled when aggregated tags are in working condition
/*		if (CUL_MATCH.matcher(type).matches()) {
			if (searchData.getParameters().containsKey(FLT_STANDARD)) {
				searchData.getParameters().put(FLT_RELATED_LEAF_INTERNAL_CODES, searchData.getParameters().getString(FLT_STANDARD));
				searchData.getParameters().remove(FLT_STANDARD);
			}
		}*/
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
	private void generateStandardFilter(SearchData searchData, String key, String[] codes, String fwCode) {
		if (searchData.getParameters() != null && searchData.getParameters().getValues().size() != 0) {
			MapWrapper<Object> parameters = searchData.getParameters();
			if (parameters != null && ((parameters.containsKey(FLT_STANDARD_DISPLAY) || (parameters.containsKey(FLT_STANDARD))) && parameters.containsKey(FLT_FWCODE))) {
				SearchData crosswalkRequest = new SearchData();
				crosswalkRequest.setPretty(searchData.getPretty());
				crosswalkRequest.setIndexType(EsIndex.CROSSWALK);
				crosswalkRequest.putFilter(AMPERSAND + CARET_SYMBOL + IndexFields.CROSSWALK_CODES + DOT + key, (StringUtils.join(codes, COMMA)));
				if (fwCode != null) crosswalkRequest.putFilter(AMPERSAND + CARET_SYMBOL + IndexFields.CROSSWALK_CODES + DOT + IndexFields.FRAMEWORK_CODE, fwCode);
				crosswalkRequest.setQueryString(STAR);
				List<Map<String, Object>> crosswalkResponses = (List<Map<String, Object>>) SearchHandler.getSearcher(SearchHandlerType.CROSSWALK.name()).search(crosswalkRequest).getSearchResults();
				Set<String> filterCodes = new HashSet<>();
				if (crosswalkResponses != null && !crosswalkResponses.isEmpty()) {
					for (Map<String, Object> response : crosswalkResponses) {
						Map<String, Object> source = (Map<String, Object>) response.get(SEARCH_SOURCE);
						//filterCodes.add(((String) source.get(IndexFields.GUT_CODE)).toLowerCase());
						List<Map<String, String>> crosswalkCodes = (List<Map<String, String>>) source.get(IndexFields.CROSSWALK_CODES);
						for (Map<String, String> code : crosswalkCodes) {
							filterCodes.add(((String) code.get(IndexFields.ID)).toLowerCase());
						}
					}
				}
				if (!filterCodes.isEmpty()) {
					searchData.getParameters().remove(FLT_STANDARD);
					searchData.getParameters().remove(FLT_STANDARD_DISPLAY);
					searchData.getParameters().remove(FLT_FWCODE);
					searchData.getParameters().put(FLT_STANDARD, StringUtils.join(codes, COMMA));
					//searchData.getParameters().put(FLT_RELATED_GUT_CODES, StringUtils.join(filterCodes, COMMA));
				} else {
					searchData.getParameters().put(FLT_STANDARD, StringUtils.join(codes, COMMA));
				}
			} else if (searchData.getParameters().containsKey(FLT_TAXONOMY_GUT_CODE)) {
				searchData.getParameters().put(FLT_RELATED_GUT_CODES, StringUtils.join(codes, COMMA));
				searchData.getParameters().remove(FLT_TAXONOMY_GUT_CODE);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void generateRequestedCodeInfo(SearchData searchData, String key,  String[] codes, String requestedFwCode, Map<String, Object> searchResult) {
		SearchData taxonomyRequest = new SearchData();
		taxonomyRequest.setPretty(searchData.getPretty());
		taxonomyRequest.setIndexType(EsIndex.TAXONOMY);
		taxonomyRequest.putFilter(AMPERSAND + CARET_SYMBOL + key, (StringUtils.join(codes, COMMA)));
		if (requestedFwCode != null) taxonomyRequest.putFilter(AMPERSAND + CARET_SYMBOL + IndexFields.FRAMEWORK_CODE, requestedFwCode);
		taxonomyRequest.setQueryString(STAR);
		List<Taxonomy> taxonomyResponses = (List<Taxonomy>) SearchHandler.getSearcher(SearchHandlerType.TAXONOMY.name()).search(taxonomyRequest).getSearchResults();
		if (taxonomyResponses != null && !taxonomyResponses.isEmpty()) {
			Taxonomy response = taxonomyResponses.get(0);
			List<GutPrerequisites> gutPrerequisites = response.getGutPrerequisites();
			if (!key.equalsIgnoreCase(TAXONOMY_GUT_CODE)) {
				Set<Map<String, String>> progressions = new HashSet<>();
				if (gutPrerequisites != null && gutPrerequisites.size() > 0) {
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
								crosswalkCodes.forEach(code -> {
									String cwFw = (String) code.get(IndexFields.FRAMEWORK_CODE);
									if (cwFw.equalsIgnoreCase(requestedFwCode)) {
										Map<String, String> cwCode = new HashMap<>();
										cwCode.put(IndexFields.CODE, (String) code.get(IndexFields.CODE));
										cwCode.put(IndexFields.TITLE, (String) code.get(IndexFields.TITLE));
										progressions.add(cwCode);
									}
								});
							});
						}
					});
				}
				searchResult.put(PREREQUISITES, progressions);
			} else if (key.equalsIgnoreCase(TAXONOMY_GUT_CODE)) searchResult.put(PREREQUISITES, gutPrerequisites);
			searchResult.put(SIGNATURE_CONTENTS, response.getSignatureContents());
			searchResult.put(IndexFields.GUT_CODE, response.getGutCode());
			searchResult.put(IndexFields.CODE, response.getDisplayCode());
			searchResult.put(IndexFields.CODE_TYPE, response.getCodeType());
			searchResult.put(IndexFields.TITLE, response.getTitle());
		}
	}

	@SuppressWarnings("unchecked")
	private void setFilterWithExtractedKeywordsAndSubjects(SearchData searchData, String key, String[] codes, String fwCode) {
		if (searchData.getParameters().containsKey(FLT_STANDARD) || searchData.getParameters().containsKey(FLT_STANDARD_DISPLAY) || searchData.getParameters().containsKey(FLT_RELATED_GUT_CODES)) {
			SearchData input = new SearchData();
			input.setPretty(searchData.getPretty());
			input.setParameters(new MapWrapper<Object>());
			input.setQueryString(STAR);
			input.putFilter(key, StringUtils.join(codes, COMMA));
			if (fwCode != null) input.putFilter(AMPERSAND + CARET_SYMBOL + IndexFields.FRAMEWORK_CODE, fwCode);
			List<Taxonomy> searchResponse = (List<Taxonomy>) SearchHandler.getSearcher(SearchHandlerType.TAXONOMY.name()).search(input).getSearchResults();
			if (searchResponse != null && !searchResponse.isEmpty()) {
				StringBuilder queryBuilder = new StringBuilder();
				for (Taxonomy response : searchResponse) {
					List<String> keywords = response.getKeywords();
					for (String keyword : keywords) {
						if (queryBuilder.length() > 0)
							queryBuilder.append(" OR ");
						queryBuilder.append(keyword);
					}
				}
				if (queryBuilder.length() > 0) {
					searchData.setDefaultQuery(queryBuilder.toString());
					List<String> stds = null;
					List<String> gutStds = null;
					if (searchData.getParameters().getString(FLT_STANDARD) != null) stds = Arrays.asList(searchData.getParameters().getString(FLT_STANDARD).split(COMMA));
					if (searchData.getParameters().getString(FLT_RELATED_GUT_CODES) != null) gutStds = Arrays.asList(searchData.getParameters().getString(FLT_RELATED_GUT_CODES).split(COMMA));
					Set<String> subjects = new HashSet<>();
					extractSubjects(stds, subjects, false);
					extractSubjects(gutStds, subjects, true);
					if (!subjects.isEmpty()) searchData.getParameters().put(FLT_SUBJECT, StringUtils.join(subjects, COMMA));
					searchData.getParameters().remove(FLT_STANDARD);
					searchData.getParameters().remove(FLT_RELATED_GUT_CODES);
				}
			}
		}
	}

}
