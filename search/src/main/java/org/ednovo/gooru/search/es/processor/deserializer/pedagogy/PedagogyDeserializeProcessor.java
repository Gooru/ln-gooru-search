/**
 *
 */
package org.ednovo.gooru.search.es.processor.deserializer.pedagogy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.ednovo.gooru.search.es.constant.Constants;
import org.ednovo.gooru.search.es.constant.EsIndex;
import org.ednovo.gooru.search.es.constant.IndexFields;
import org.ednovo.gooru.search.es.exception.SearchException;
import org.ednovo.gooru.search.es.handler.SearchHandler;
import org.ednovo.gooru.search.es.handler.SearchHandlerType;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.UserV2;
import org.ednovo.gooru.search.es.processor.SearchProcessor;
import org.ednovo.gooru.search.responses.PedagogySearchResult;
import org.ednovo.gooru.search.responses.SearchResponse;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.CaseFormat;

/**
 * @author SearchTeam
 *
 */

public abstract class PedagogyDeserializeProcessor<O, S> extends SearchProcessor<SearchData, O> implements Constants {

	abstract O deserialize(Map<String, Object> model, SearchData input, O output);

	abstract S collect(Map<String, Object> model, SearchData input, S output);

	@SuppressWarnings("unchecked")
	@Override
	public final void process(SearchData searchData, SearchResponse<O> response) {
		try {
			Map<String, Object> responseAsMap = (Map<String, Object>) SERIAILIZER.readValue(searchData.getSearchResultText(), new TypeReference<Map<String, Object>>() {
			});
			O searchResult = deserialize(responseAsMap, searchData, null);
			response.setSearchResults(searchResult);
			if (responseAsMap.get(SEARCH_HITS) != null) {
				Map<String, Object> hit = (Map<String, Object>) responseAsMap.get(SEARCH_HITS);
				if (((List<Map<String, Object>>) (hit).get(SEARCH_HITS)) != null) {
					Map<String, Object> stats = new HashMap<String, Object>(3);
					stats.put("totalHitCount", ((Integer) hit.get(SEARCH_TOTAL)).longValue());
					stats.put("pageSize", searchData.getSize());
					stats.put("pageNumber", searchData.getPageNum());
					List<Map<String, Object>> hits = (List<Map<String, Object>>) (hit).get(SEARCH_HITS);
					stats.put("resultCount", hits.size());

					Map<String, Object> query = new HashMap<String, Object>(4);
					query.put("userQueryString", searchData.getUserQueryString());
					query.put("rewrittenQueryString", searchData.getQueryString());
					response.setSearchQuery(query);
					response.setStats(stats);
				}
			}
		} catch (Exception e) {
			logger.error("Search Error", e);
			throw new SearchException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	protected void setTaxonomy(Map<String, Object> taxonomyMap, SearchData input, PedagogySearchResult output) {
		Map<String, Object> taxonomySetAsMap = (Map<String, Object>) taxonomyMap.get(IndexFields.TAXONOMY_SET);
		Map<String, Object> finalConvertedMap = new HashMap<>();
		Map<String, Object> finalCrosswalkMap = new HashMap<>();
		List<String> leafInternalCodes = (List<String>) taxonomyMap.get(IndexFields.LEAF_INTERNAL_CODES);
		if (taxonomySetAsMap != null && leafInternalCodes != null) {
			Map<String, Object> curriculumAsMap = (Map<String, Object>) taxonomySetAsMap.get(IndexFields.CURRICULUM);
			List<Map<String, String>> curriculumInfoAsList = (List<Map<String, String>>) curriculumAsMap.get(IndexFields.CURRICULUM_INFO);
			if (curriculumInfoAsList != null && !curriculumInfoAsList.isEmpty()) {
				List<Map<String, Object>> crosswalkResponse = searchCrosswalk(input, leafInternalCodes);
				curriculumInfoAsList.forEach(codeAsMap -> {
					finalConvertedMap.put(codeAsMap.get(IndexFields.ID), codeAsMap);
					List<Map<String, String>> crosswalkCodes = null;
					crosswalkCodes = deserializeCrosswalkResponse(crosswalkResponse, codeAsMap.get(IndexFields.ID), crosswalkCodes);
					if (crosswalkCodes != null) {
						for (Map<String, String> crosswalk : crosswalkCodes) {
							if (!((String) crosswalk.get(IndexFields.ID)).equalsIgnoreCase((String) codeAsMap.get(IndexFields.ID))) {
								crosswalk.put(IndexFields.PARENT_TITLE, codeAsMap.get(IndexFields.PARENT_TITLE));
								crosswalk.put(CROSSWALK_ID, crosswalk.get(IndexFields.ID));
								crosswalk.put(IndexFields.ID, codeAsMap.get(IndexFields.ID));
								finalCrosswalkMap.put(crosswalk.get(IndexFields.ID), crosswalk);
							}
						}
						String fltStandard = null;
						String fltStandardDisplay = null;
						String fltRelatedGutCodes = null;
						if(input.getFilters().containsKey(AMPERSAND_EQ_INTERNAL_CODE)) fltStandard = input.getFilters().get(AMPERSAND_EQ_INTERNAL_CODE).toString();
						if(input.getFilters().containsKey(AMPERSAND_EQ_DISPLAY_CODE)) fltStandardDisplay = input.getFilters().get(AMPERSAND_EQ_DISPLAY_CODE).toString();
						if(input.getFilters().containsKey(AMPERSAND_RELATED_GUT_CODES)) fltRelatedGutCodes = input.getFilters().get(AMPERSAND_RELATED_GUT_CODES).toString();
						Boolean isCrosswalked = false;
						List<String> leafDisplayCodes = (List<String>) taxonomyMap.get(IndexFields.LEAF_DISPLAY_CODES);
						if (!(leafInternalCodes != null && leafInternalCodes.size() > 0 && fltStandard != null && leafInternalCodes.contains(fltStandard.toUpperCase()))
								&& !(leafDisplayCodes != null && leafDisplayCodes.size() > 0 && fltStandardDisplay != null && leafDisplayCodes.contains(fltStandardDisplay.toUpperCase())) && fltRelatedGutCodes == null) {
							isCrosswalked = true;
						}
						output.setIsCrosswalked(isCrosswalked);
					}
				});
			}
		}
		if(!finalConvertedMap.isEmpty()) output.setTaxonomy(finalConvertedMap);
		//if(!finalCrosswalkMap.isEmpty()) output.setTaxonomyEquivalentCompetencies(finalCrosswalkMap);
	}

	@SuppressWarnings("unchecked")
	protected List<Map<String, String>> deserializeCrosswalkResponse(List<Map<String, Object>> crosswalkResponses, String id, List<Map<String, String>> crosswalkResult) {
		if (crosswalkResponses != null && !crosswalkResponses.isEmpty()) {
			for (Map<String, Object> response : crosswalkResponses) {
				Map<String, Object> source = (Map<String, Object>) response.get(SEARCH_SOURCE);
				List<Map<String, String>> crosswalkCodes = (List<Map<String, String>>) source.get(IndexFields.CROSSWALK_CODES);
				for (Map<String, String> code : crosswalkCodes) {
					String crosswalkId = (String) code.get(IndexFields.ID);
					if (!crosswalkId.equalsIgnoreCase(id)) {
						continue;
					} else {
						return crosswalkCodes;
					}
				}
			}
		}
		return crosswalkResult;
	}

	protected void convertKeysToSnakeCase(Map<String, Object> finalConvertedMap, Map<String, String> codeAsMap) {
		Map<String, String> convertedMap = new HashMap<>();
		codeAsMap.forEach((k, v) -> {
			if (!k.equalsIgnoreCase(IndexFields.ID))
				convertedMap.put(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, k), v);
		});
		finalConvertedMap.put(codeAsMap.get(IndexFields.ID), convertedMap);
	}

	@SuppressWarnings("unchecked")
	protected List<Map<String, Object>> searchCrosswalk(SearchData input, List<String> leafInternalCodes) {
		SearchData crosswalkRequest = new SearchData();
		crosswalkRequest.setPretty(input.getPretty());
		crosswalkRequest.setIndexType(EsIndex.CROSSWALK);
		crosswalkRequest.putFilter(AMPERSAND + CARET_SYMBOL + IndexFields.CROSSWALK_CODES + DOT + IndexFields.ID, (StringUtils.join(leafInternalCodes, COMMA)));
		crosswalkRequest.setQueryString(STAR);
		List<Map<String, Object>> searchResponse = (List<Map<String, Object>>) SearchHandler.getSearcher(SearchHandlerType.CROSSWALK.name()).search(crosswalkRequest).getSearchResults();
		return searchResponse;
	}

	protected UserV2 setUser(Map<String, Object> userData) {
		UserV2 user = new UserV2();
		user.setFirstname((String) userData.get(IndexFields.FIRST_NAME));
		user.setLastname((String) userData.get(IndexFields.LAST_NAME));
		user.setUsernameDisplay((String) userData.get(IndexFields.USERNAME));
		user.setId((String) userData.get(IndexFields.USER_ID));
		return user;
	}

}
