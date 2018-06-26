/**
 *
 */
package org.ednovo.gooru.search.es.processor.deserializer.v3;

import java.util.ArrayList;
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
import org.ednovo.gooru.search.es.model.Code;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.UserV2;
import org.ednovo.gooru.search.es.processor.SearchProcessor;
import org.ednovo.gooru.search.responses.SearchResponse;
import org.ednovo.gooru.search.responses.SearchResult;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * @author SearchTeam
 *
 */

public abstract class DeserializeV3Processor<O, S> extends SearchProcessor<SearchData, O> implements Constants {

	abstract O deserialize(Map<String, Object> model, SearchData input, O output);

	abstract S collect(Map<String, Object> model, SearchData input, S output);

	@SuppressWarnings("unchecked")
	@Override
	public final void process(SearchData searchData, SearchResponse<O> response) {
		try {
			Map<String, Object> responseAsMap = (Map<String, Object>) SERIAILIZER.readValue(searchData.getSearchResultText(),
					new TypeReference<Map<String, Object>>() {});
			O searchResult = deserialize(responseAsMap, searchData, null);
			if(!searchData.isAggregationRequest()) response.setSearchResults(searchResult);			
			if (responseAsMap.get(SEARCH_HITS) != null) {
				Map<String, Object> hit = (Map<String, Object>) responseAsMap.get(SEARCH_HITS);
				if (((List<Map<String, Object>>) (hit).get(SEARCH_HITS)) != null) {
					List<Map<String, Object>> hits = (List<Map<String, Object>>) (hit).get(SEARCH_HITS);
					Map<String, Object> stats = new HashMap<String, Object>(3);
					stats.put("totalHitCount", ((Integer) hit.get(SEARCH_TOTAL)).longValue());
					if (!searchData.isAggregationRequest()) {
						stats.put("pageSize", searchData.getSize());
						stats.put("pageNumber", searchData.getPageNum());
						stats.put("resultCount", hits.size());
					}
					response.setStats(stats);

					Map<String, Object> query = new HashMap<String, Object>(4);
					query.put("userQueryString", searchData.getUserQueryString());
					query.put("rewrittenQueryString", searchData.getQueryString());
					if (searchData.getSpellCheckQueryString() != null && !searchData.getSpellCheckQueryString().isEmpty()) {
						query.put("current", searchData.getSpellCheckQueryString());
						query.put("rewriteType", SPELLCHECKER);
					}
					response.setQuery(query);
				}
			} 
		   	if (searchData.isAggregationRequest() && responseAsMap.get("aggregations") != null) {
				List<Map<String, Object>> bucketList = (List<Map<String, Object>>) ((Map<String, Object>) ((Map<String, Object>) responseAsMap.get("aggregations")).get("agg_key")).get("buckets");
				if(bucketList.size() > 0) {
					response.setAggregations(bucketList);
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
		List<Map<String, String>> txCurriculumInfoAsList = new ArrayList<>();
		JSONObject standardPrefs = input.getUserTaxonomyPreference();
		List<String> leafInternalCodes = (List<String>) taxonomyMap.get(IndexFields.LEAF_INTERNAL_CODES);
		if (leafInternalCodes != null) {
			List<Map<String, Object>> crosswalkResponse = searchCrosswalk(input, leafInternalCodes);
			Map<String, Object> curriculumAsMap = (Map<String, Object>) taxonomySetAsMap.get(IndexFields.CURRICULUM);
			List<Map<String, String>> curriculumInfoAsList = (List<Map<String, String>>) curriculumAsMap.get(IndexFields.CURRICULUM_INFO);
			if (curriculumInfoAsList != null) {
				curriculumInfoAsList.forEach(code -> {
					String id = code.get(IndexFields.ID);
					List<Map<String, String>> crosswalkCodes = null;
					crosswalkCodes = deserializeCrosswalkResponse(crosswalkResponse, id, crosswalkCodes);
					transformToPreferredCode(txCurriculumInfoAsList, standardPrefs, code, crosswalkCodes);
				});
				curriculumAsMap.put(IndexFields.CURRICULUM_INFO, txCurriculumInfoAsList);
				taxonomySetAsMap.put(IndexFields.CURRICULUM, curriculumAsMap);
			}
		}
		return taxonomySetAsMap;
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
	
	private void transformToPreferredCode(List<Map<String, String>> txCurriculumInfoAsList, JSONObject standardPrefs, Map<String, String> codeAsMap, List<Map<String, String>> crosswalkCodes) {
		String internalCode = codeAsMap.get(IndexFields.ID);
		final String subject = getSubjectFromCodeId(internalCode);
		String framework = null;
		try {
			if (standardPrefs != null && subject != null && standardPrefs.has(subject)) {
				framework = standardPrefs.getString(subject);
				if (framework != null) {
					if (!internalCode.startsWith(framework + DOT) && crosswalkCodes != null) {
						for (Map<String, String> crosswalk : crosswalkCodes) {
							if (!crosswalk.get(IndexFields.FRAMEWORK_CODE).equalsIgnoreCase(framework)) {
								continue;
							}
							crosswalk.put(LEAF_INTERNAL_CODE, internalCode);
							crosswalk.put(IndexFields.PARENT_TITLE, codeAsMap.get(IndexFields.PARENT_TITLE));
							txCurriculumInfoAsList.add(crosswalk);
						}
					} else if (internalCode.startsWith(framework + DOT)) {
						txCurriculumInfoAsList.add(codeAsMap);
					}
				}
			}
		} catch (JSONException e) {
			logger.error("JsonException during taxonomy tranformation!", e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	protected List<Map<String, Object>> searchCrosswalk(SearchData input, List<String> leafInternalCodes) {
		List<Map<String, Object>> searchResponse = null;
		SearchData crosswalkRequest = new SearchData();
		crosswalkRequest.setPretty(input.getPretty());
		crosswalkRequest.setIndexType(EsIndex.CROSSWALK);
		crosswalkRequest.putFilter(AMPERSAND + CARET_SYMBOL + IndexFields.CROSSWALK_CODES + DOT + IndexFields.ID, (StringUtils.join(leafInternalCodes,",")));
		crosswalkRequest.setQueryString(STAR);
		try {
			searchResponse = (List<Map<String, Object>>) SearchHandler.getSearcher(SearchHandlerType.CROSSWALK.name()).search(crosswalkRequest).getSearchResults();
		} catch (Exception e) { 
			logger.error("No matching crosswalk for codes : {} : Exception : {}", leafInternalCodes, e.getMessage());
		}
		return searchResponse;
	}

	private String getSubjectFromCodeId(String codeId) {
		return codeId.contains(HYPHEN) ? codeId.substring((codeId.indexOf(DOT) + 1), codeId.indexOf(HYPHEN)) : null;
	}

	@SuppressWarnings("unchecked")
	protected void setCrosswalkData(SearchData input, SearchResult resource, Map<String, Object> taxonomyMap) {
		String fltStandard = null;
		String fltStandardDisplay = null;
		if(input.getFilters().containsKey(AMPERSAND_EQ_INTERNAL_CODE)) fltStandard = input.getFilters().get(AMPERSAND_EQ_INTERNAL_CODE).toString();
		if(input.getFilters().containsKey(AMPERSAND_EQ_DISPLAY_CODE)) fltStandardDisplay = input.getFilters().get(AMPERSAND_EQ_DISPLAY_CODE).toString();
		Boolean isCrosswalked = false;
		List<String> leafInternalCodes = (List<String>) taxonomyMap.get(IndexFields.LEAF_INTERNAL_CODES);
		List<String> leafDisplayCodes = (List<String>) taxonomyMap.get(IndexFields.LEAF_DISPLAY_CODES);
		List<Map<String, Object>> equivalentCompetencies = new ArrayList<>();
		fetchCrosswalks(input, leafInternalCodes, equivalentCompetencies);
	
		if (!(leafInternalCodes != null && leafInternalCodes.size() > 0 && fltStandard != null && leafInternalCodes.contains(fltStandard.toUpperCase()))
				&& !(leafDisplayCodes != null && leafDisplayCodes.size() > 0 && fltStandardDisplay != null && leafDisplayCodes.contains(fltStandardDisplay.toUpperCase()))) {
			isCrosswalked = true;
		}
		resource.setIsCrosswalked(isCrosswalked);
		if (equivalentCompetencies.size() > 0) resource.setTaxonomyEquivalentCompetencies(equivalentCompetencies);
	}

	protected void fetchCrosswalks(SearchData input, List<String> leafInternalCodes, List<Map<String, Object>> equivalentCompetencies) {
		List<Map<String, Object>> crosswalkResponses = searchCrosswalk(input, leafInternalCodes);
		if (crosswalkResponses != null && !crosswalkResponses.isEmpty()) {
			leafInternalCodes.forEach(leafInternalCode -> {
				Map<String, Object> crosswalksAsMap = new HashMap<>();
				crosswalksAsMap.put(IndexFields.ID, leafInternalCode);
				List<Map<String, String>> crosswalkCodes = null;
				crosswalkCodes = deserializeCrosswalkResponse(crosswalkResponses, leafInternalCode, crosswalkCodes);
				crosswalksAsMap.put(IndexFields.CROSSWALK_CODES, crosswalkCodes);
				if (crosswalkCodes != null && crosswalkCodes.size() > 0) equivalentCompetencies.add(crosswalksAsMap);
			});
		}
	}
	
	@SuppressWarnings("unchecked")
	protected String getTaxonomyMetadataLabel(List<Code> taxonomyMetadatas) {
		if (taxonomyMetadatas != null) {
			String label = "";
			for (int i = 0; i < taxonomyMetadatas.size(); i++) {
				if (label.length() > 0) {
					label += ",";
				}
				Map<String, String> taxonomyMetadata = (Map<String, String>) taxonomyMetadatas.get(i);
				label += taxonomyMetadata.get(IndexFields.LABEL);
			}
			return label;
		}
		return null;
	}

	protected UserV2 setUser(Map<String, Object> userData){
		UserV2 user = null;
		if (userData != null && !userData.isEmpty()) {
			user = new UserV2();
			user.setFirstname((String) userData.get(IndexFields.FIRST_NAME));
			user.setLastname((String) userData.get(IndexFields.LAST_NAME));
			user.setUsernameDisplay((String) userData.get(IndexFields.USERNAME));
			user.setId((String) userData.get(IndexFields.USER_ID));
			user.setProfileImage((String) userData.get(IndexFields.PROFILE_IMAGE));
		}
		return user;
	}
}
