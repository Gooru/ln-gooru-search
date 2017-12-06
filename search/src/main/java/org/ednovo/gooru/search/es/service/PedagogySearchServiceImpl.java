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
		if (searchData.getParameters() != null && searchData.getParameters().getValues().size() != 0) {
			MapWrapper<Object> parameters = searchData.getParameters();
			if (parameters != null && (parameters.containsKey(FLT_STANDARD_DISPLAY) || (parameters.containsKey(FLT_STANDARD))) && parameters.containsKey("flt.fwCode")) {
				if(parameters.containsKey(FLT_STANDARD_DISPLAY)) {
					codes = (parameters.getString(FLT_STANDARD_DISPLAY)).split(COMMA);
					key = IndexFields.CODE;
				}  else if (parameters.containsKey(FLT_STANDARD)) {
					codes = (parameters.getString(FLT_STANDARD)).split(COMMA);
					key = IndexFields.ID;
				}
			}
		}
		if (key != null && codes != null) {
			generateSearchedCodeData(searchData, key, codes, searchResult);
			generateStandardFilter(searchData, key, codes);
		}

		search(searchData, TYPE_RESOURCE, SearchHandlerType.PEDAGOGY_RESOURCE.name(), contentResultAsMap);
		search(searchData, TYPE_QUESTION, SearchHandlerType.PEDAGOGY_RESOURCE.name(), contentResultAsMap);
		search(searchData, TYPE_COLLECTION, SearchHandlerType.PEDAGOGY_SCOLLECTION.name(), contentResultAsMap);
		search(searchData, TYPE_ASSESSMENT, SearchHandlerType.PEDAGOGY_SCOLLECTION.name(), contentResultAsMap);
		
		setStandardKeywordToQuery(searchData);
		if (!searchData.getDefaultQuery().equalsIgnoreCase(STAR)) searchData.getParameters().remove(FLT_STANDARD);

		search(searchData, TYPE_COURSE, SearchHandlerType.PEDAGOGY_COURSE.name(), contentResultAsMap);
		search(searchData, TYPE_UNIT, SearchHandlerType.PEDAGOGY_UNIT.name(), contentResultAsMap);
		search(searchData, TYPE_LESSON, SearchHandlerType.PEDAGOGY_LESSON.name(), contentResultAsMap);
		
		searchResult.put(Constants.CONTENTS, contentResultAsMap);
		searchResponse.setSearchResults(searchResult);
		return searchResponse;
	}
	
	@Override
	public Map<String, Object> search(SearchData searchData, String type, String searchHandlerType, Map<String, Object> resultAsMap) {
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
		if (type.equalsIgnoreCase(TYPE_RESOURCE)) {
			inputSearchData.putFilter("&^contentFormat", TYPE_RESOURCE);
		} else if (type.equalsIgnoreCase(TYPE_QUESTION)) {
			inputSearchData.putFilter("&^contentFormat", TYPE_QUESTION);
		} else if (type.equalsIgnoreCase(TYPE_ASSESSMENT)) {
			inputSearchData.putFilter("&^contentFormat", "assessment,assessment-external");
		} else if (type.equalsIgnoreCase(TYPE_COLLECTION)) {
			inputSearchData.putFilter("&^contentFormat", TYPE_COLLECTION);
		}
		if (CUL_MATCH.matcher(type).matches() && !searchData.getDefaultQuery().equalsIgnoreCase(STAR))
			inputSearchData.setParameters(new MapWrapper<>());
		SearchResponse<Object> searchResponse = (SearchResponse<Object>) SearchHandler.getSearcher(searchHandlerType).search(inputSearchData);
		Map<String, Object> searchMap = new HashMap<>();
		searchMap.put(TOTAL_HIT_COUNT, searchResponse.getStats().get(TOTAL_HIT_COUNT));
		searchMap.put(RESULT_COUNT, searchResponse.getStats().get(RESULT_COUNT));
		searchMap.put(SEARCH_RESULTS, searchResponse.getSearchResults());
		resultAsMap.put(type, searchMap);
		logger.info("Elapsed time to complete unit search process :" + (System.currentTimeMillis() - uStart) + " ms");
		return resultAsMap;

	}

	@SuppressWarnings("unchecked")
	private void generateStandardFilter(SearchData searchData, String key, String[] codes) {
		if (searchData.getParameters() != null && searchData.getParameters().getValues().size() != 0) {
			MapWrapper<Object> parameters = searchData.getParameters();
			if (parameters != null && (parameters.containsKey(FLT_STANDARD_DISPLAY) || (parameters.containsKey(FLT_STANDARD))) && parameters.containsKey("flt.fwCode")) {
				String fwCode = (parameters.getString("flt.fwCode"));
				SearchData crosswalkRequest = new SearchData();
				crosswalkRequest.setPretty(searchData.getPretty());
				crosswalkRequest.setIndexType(EsIndex.CROSSWALK);
				crosswalkRequest.putFilter(AMPERSAND + CARET_SYMBOL + IndexFields.CROSSWALK_CODES + DOT + key, (StringUtils.join(codes, COMMA)));
				crosswalkRequest.putFilter(AMPERSAND + CARET_SYMBOL + IndexFields.CROSSWALK_CODES + DOT + IndexFields.FRAMEWORK_CODE, fwCode);
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
					parameters.remove("flt.fwCode");
					searchData.getParameters().put(FLT_STANDARD, StringUtils.join(filterCodes, COMMA));
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void generateSearchedCodeData(SearchData searchData, String key,  String[] codes, Map<String, Object> searchResult) {
		SearchData taxonomyRequest = new SearchData();
		taxonomyRequest.setPretty(searchData.getPretty());
		taxonomyRequest.setIndexType(EsIndex.TAXONOMY);
		if (key.equals(IndexFields.CODE)) key = IndexFields.DISPLAY_CODE;
		taxonomyRequest.putFilter(AMPERSAND + CARET_SYMBOL + key, (StringUtils.join(codes, COMMA)));
		taxonomyRequest.setQueryString(STAR);
		List<Taxonomy> taxonomyResponses = (List<Taxonomy>) SearchHandler.getSearcher(SearchHandlerType.TAXONOMY.name()).search(taxonomyRequest).getSearchResults();
		if (taxonomyResponses != null && !taxonomyResponses.isEmpty()) {
			Taxonomy response = taxonomyResponses.get(0);
			searchResult.put(IndexFields.GUT_PREREQUISITES, response.getGutPrerequisites());
		}
	}
	

	@SuppressWarnings("unchecked")
	private void setStandardKeywordToQuery(SearchData searchData) {
		if (searchData.getParameters().containsKey(FLT_STANDARD)) {
			SearchData input = new SearchData();
			input.setPretty(searchData.getPretty());
			input.setParameters(new MapWrapper<Object>());
			input.setQueryString(STAR);
			input.putFilter(IndexFields.ID, searchData.getParameters().getString(FLT_STANDARD).toUpperCase());
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
