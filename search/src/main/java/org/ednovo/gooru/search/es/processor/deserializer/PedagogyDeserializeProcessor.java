/**
 *
 */
package org.ednovo.gooru.search.es.processor.deserializer;

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
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.ednovo.gooru.search.es.model.UserV2;
import org.ednovo.gooru.search.es.processor.SearchProcessor;
import org.json.JSONObject;
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
					response.setQuery(query);
					response.setStats(stats);
				}
			}
		} catch (Exception e) {
			LOG.error("Search Error", e);
			throw new SearchException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	protected Map<String, Object> transformTaxonomy(Map<String, Object> taxonomyMap, SearchData input) {
		Map<String, Object> taxonomySetAsMap = (Map<String, Object>) taxonomyMap.get(IndexFields.TAXONOMY_SET);
		if (taxonomySetAsMap == null) {
			return taxonomyMap;
		}
		Map<String, Object> finalConvertedMap = new HashMap<>();
		Map<String, Object> finalCrosswalkMap = new HashMap<>();
		JSONObject standardPrefs = input.getUserTaxonomyPreference();
		List<String> leafInternalCodes = (List<String>) taxonomyMap.get(IndexFields.LEAF_INTERNAL_CODES);
		if (leafInternalCodes != null) {
			Map<String, Object> curriculumAsMap = (Map<String, Object>) taxonomySetAsMap.get(IndexFields.CURRICULUM);
			List<Map<String, String>> curriculumInfoAsList = (List<Map<String, String>>) curriculumAsMap.get(IndexFields.CURRICULUM_INFO);
			if (curriculumInfoAsList != null && !curriculumInfoAsList.isEmpty()) {
				curriculumInfoAsList.forEach(codeAsMap -> {
					if (standardPrefs == null || !input.isCrosswalk()) {
						convertKeysToSnakeCase(finalConvertedMap, codeAsMap);
					} else {
						List<Map<String, String>> crosswalkCodes = null;
						List<Map<String, Object>> crosswalkResponse = searchCrosswalk(input, leafInternalCodes);
						crosswalkCodes = deserializeCrosswalkResponse(crosswalkResponse, codeAsMap.get(IndexFields.ID), crosswalkCodes);
						convertKeysToSnakeCase(finalConvertedMap, codeAsMap);
						for (Map<String, String> crosswalk : crosswalkCodes) {
							crosswalk.put(PARENT_TITLE, codeAsMap.get(PARENT_TITLE));
							convertKeysToSnakeCase(finalCrosswalkMap, crosswalk);
						}
					}
				});
			}
		}
		return finalConvertedMap;
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
		crosswalkRequest.putFilter(AMPERSAND + CARET_SYMBOL + IndexFields.CROSSWALK_CODES + DOT + IndexFields.ID, (StringUtils.join(leafInternalCodes, ",")));
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
