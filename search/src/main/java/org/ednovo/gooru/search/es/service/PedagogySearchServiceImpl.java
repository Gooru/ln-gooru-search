package org.ednovo.gooru.search.es.service;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
/**
 * @author Renuka
 * 
 */
@Service
public class PedagogySearchServiceImpl implements PedagogySearchService, Constants {

	private static final Logger logger = LoggerFactory.getLogger(PedagogySearchServiceImpl.class);
	
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
				if ((parameters.containsKey(FLT_STANDARD_DISPLAY) || (parameters.containsKey(FLT_STANDARD))) && parameters.containsKey(FLT_FWCODE)) {
					fwCode = (parameters.getString(FLT_FWCODE));
					if (parameters.containsKey(FLT_STANDARD_DISPLAY)) {
						codes = (parameters.getString(FLT_STANDARD_DISPLAY)).split(COMMA);
						key = IndexFields.CODE;
					} else if (parameters.containsKey(FLT_STANDARD)) {
						codes = (parameters.getString(FLT_STANDARD)).split(COMMA);
						key = IndexFields.ID;
					}
				} else if (parameters.containsKey(FLT_FWCODE)) {
					parameters.remove(FLT_FWCODE);
				}
			}
		}
		if (key != null && codes != null) {
			generateSearchedCodeData(searchData, key, codes, fwCode, searchResult);
			generateStandardFilter(searchData, key, codes, fwCode);
		}

		search(searchData, TYPE_RESOURCE, contentResultAsMap);
		search(searchData, TYPE_QUESTION, contentResultAsMap);
		search(searchData, TYPE_SCOLLECTION, contentResultAsMap);
		search(searchData, TYPE_ASSESSMENT, contentResultAsMap);
		
		setStandardKeywordToQuery(searchData, key, codes, fwCode);
		if (!searchData.getDefaultQuery().equalsIgnoreCase(STAR)) searchData.getParameters().remove(FLT_STANDARD);

		search(searchData, TYPE_COURSE, contentResultAsMap);
		search(searchData, TYPE_UNIT, contentResultAsMap);
		search(searchData, TYPE_LESSON, contentResultAsMap);
		
		searchResult.put(Constants.CONTENTS, contentResultAsMap);
		searchResponse.setSearchResults(searchResult);
		return searchResponse;
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
		inputSearchData.setStandardsSearch(searchData.isStandardsSearch());
		inputSearchData.setParameters(searchData.getParameters());
		inputSearchData.setUserTenantId(searchData.getUserTenantId());
		inputSearchData.setUserTenantRootId(searchData.getUserTenantRootId());
		inputSearchData.setUserPermits(searchData.getUserPermits());
		inputSearchData.setType(type);
		if (RQC_MATCH.matcher(type).matches()) {
			inputSearchData.putFilter(AMPERSAND + CARET_SYMBOL + IndexFields.CONTENT_FORMAT, type.equalsIgnoreCase(TYPE_SCOLLECTION) ? TYPE_COLLECTION : type);
			if (type.equalsIgnoreCase(TYPE_QUESTION)) inputSearchData.setType(TYPE_RESOURCE);
		} else if (type.equalsIgnoreCase(TYPE_ASSESSMENT)) {
			inputSearchData.putFilter(AMPERSAND + CARET_SYMBOL + IndexFields.CONTENT_FORMAT, "assessment,assessment-external");
			inputSearchData.setType(TYPE_SCOLLECTION);
		}
		if (CUL_MATCH.matcher(type).matches() && !searchData.getDefaultQuery().equalsIgnoreCase(STAR))
			inputSearchData.setParameters(new MapWrapper<>());
		SearchResponse<Object> searchResponse = (SearchResponse<Object>) SearchHandler.getSearcher((PEDAGOGY_UNDERSCORE + inputSearchData.getType()).toUpperCase()).search(inputSearchData);
		Map<String, Object> searchMap = new HashMap<>();
		searchMap.put(TOTAL_HIT_COUNT, searchResponse.getStats().get(TOTAL_HIT_COUNT));
		searchMap.put(RESULT_COUNT, searchResponse.getStats().get(RESULT_COUNT));
		searchMap.put(SEARCH_RESULTS, searchResponse.getSearchResults());
		resultAsMap.put(type, searchMap);
		logger.info("Elapsed time to complete search process :" + (System.currentTimeMillis() - uStart) + " ms");
		return resultAsMap;
	}

	@SuppressWarnings("unchecked")
	private void generateStandardFilter(SearchData searchData, String key, String[] codes, String fwCode) {
		if (searchData.getParameters() != null && searchData.getParameters().getValues().size() != 0) {
			MapWrapper<Object> parameters = searchData.getParameters();
			if (parameters != null && (parameters.containsKey(FLT_STANDARD_DISPLAY) || (parameters.containsKey(FLT_STANDARD))) && parameters.containsKey(FLT_FWCODE)) {
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
						List<Map<String, String>> crosswalkCodes = (List<Map<String, String>>) source.get(IndexFields.CROSSWALK_CODES);
						for (Map<String, String> code : crosswalkCodes) {
							filterCodes.add(((String) code.get(IndexFields.ID)).toLowerCase());
						}
					}
				}
				if (!filterCodes.isEmpty()) {
					parameters.remove(FLT_STANDARD);
					parameters.remove(FLT_STANDARD_DISPLAY);
					parameters.remove(FLT_FWCODE);
					searchData.getParameters().put(FLT_STANDARD, StringUtils.join(filterCodes, COMMA));
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void generateSearchedCodeData(SearchData searchData, String key,  String[] codes, String fwCode, Map<String, Object> searchResult) {
		SearchData taxonomyRequest = new SearchData();
		taxonomyRequest.setPretty(searchData.getPretty());
		taxonomyRequest.setIndexType(EsIndex.TAXONOMY);
		if (key.equals(IndexFields.CODE)) key = IndexFields.DISPLAY_CODE;
		taxonomyRequest.putFilter(AMPERSAND + CARET_SYMBOL + key, (StringUtils.join(codes, COMMA)));
		if (fwCode != null) taxonomyRequest.putFilter(AMPERSAND + CARET_SYMBOL + IndexFields.FRAMEWORK_CODE, fwCode);
		taxonomyRequest.setQueryString(STAR);
		List<Taxonomy> taxonomyResponses = (List<Taxonomy>) SearchHandler.getSearcher(SearchHandlerType.TAXONOMY.name()).search(taxonomyRequest).getSearchResults();
		if (taxonomyResponses != null && !taxonomyResponses.isEmpty()) {
			Taxonomy response = taxonomyResponses.get(0);
			searchResult.put(PREREQUISITES, response.getGutPrerequisites());
		}
	}

	@SuppressWarnings("unchecked")
	private void setStandardKeywordToQuery(SearchData searchData, String key, String[] codes, String fwCode) {
		if (searchData.getParameters().containsKey(FLT_STANDARD) || searchData.getParameters().containsKey(FLT_STANDARD_DISPLAY)) {
			SearchData input = new SearchData();
			input.setPretty(searchData.getPretty());
			input.setParameters(new MapWrapper<Object>());
			input.setQueryString(STAR);
			if (key.equals(IndexFields.CODE)) key = IndexFields.DISPLAY_CODE;
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
				if (queryBuilder.length() > 0)
					searchData.setDefaultQuery(queryBuilder.toString());
			}
		}
	}

}
