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
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.processor.SearchProcessor;
import org.ednovo.gooru.search.responses.Metadata;
import org.ednovo.gooru.search.responses.SearchResponse;
import org.ednovo.gooru.search.responses.v3.UserV3;
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

	abstract S collect(Map<String, Object> model, SearchData input);

	@SuppressWarnings("unchecked")
	@Override
	public final void process(SearchData searchData, SearchResponse<O> response) {
		try {
			Map<String, Object> responseAsMap = (Map<String, Object>) SERIAILIZER.readValue(searchData.getSearchResultText(),
					new TypeReference<Map<String, Object>>() {});
			O searchResult = deserialize(responseAsMap, searchData, null);
			if(!searchData.isAggregationRequest()) response.setResults(searchResult);			
			if (responseAsMap.get(SEARCH_HITS) != null) {
				Map<String, Object> hit = (Map<String, Object>) responseAsMap.get(SEARCH_HITS);
				if (((List<Map<String, Object>>) (hit).get(SEARCH_HITS)) != null) {
					List<Map<String, Object>> hits = (List<Map<String, Object>>) (hit).get(SEARCH_HITS);
					Map<String, Object> stats = new HashMap<String, Object>(3);
					stats.put("total", ((Integer) hit.get(SEARCH_TOTAL)).longValue());
					if (!searchData.isAggregationRequest()) {
						stats.put("max", searchData.getSize());
						stats.put("offset", searchData.getFrom());
						stats.put("count", hits.size());
					}
					response.setStats(stats);
					response.setQuery(searchData.getUserQueryString());
				}
			} 
		   	if (searchData.isAggregationRequest() && responseAsMap.get("aggregations") != null) {
				List<Map<String, Object>> bucketList = (List<Map<String, Object>>) ((Map<String, Object>) ((Map<String, Object>) responseAsMap.get("aggregations")).get("agg_key")).get("buckets");
				if(bucketList.size() > 0) {
					response.setAggregations(bucketList);
				}
			}

		} catch (Exception e) {
			logger.error("Search Error", e);
			throw new SearchException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	protected void transformTaxonomy(Map<String, Object> taxonomyMap, SearchData searchData, Metadata metadata) {
		Map<String, Object> taxonomySetAsMap = (Map<String, Object>) taxonomyMap.get(IndexFields.TAXONOMY_SET);
		if (taxonomySetAsMap == null) {
			return;
		}
		List<Map<String, String>> finalConvertedMap = new ArrayList<>();
		JSONObject standardPrefs = searchData.getUserTaxonomyPreference();
		List<String> leafInternalCodes = (List<String>) taxonomyMap.get(IndexFields.LEAF_INTERNAL_CODES);
		if (leafInternalCodes != null) {
			List<Map<String, Object>> crosswalkResponse = searchCrosswalk(searchData, leafInternalCodes);
			Map<String, Object> curriculumAsMap = (Map<String, Object>) taxonomySetAsMap.get(IndexFields.CURRICULUM);
			List<Map<String, String>> curriculumInfoAsList = (List<Map<String, String>>) curriculumAsMap.get(IndexFields.CURRICULUM_INFO);
			if (curriculumInfoAsList != null) {
				curriculumInfoAsList.forEach(code -> {
					String id = code.get(IndexFields.ID);
					List<Map<String, String>> crosswalkCodes = null;
					crosswalkCodes = deserializeCrosswalkResponse(crosswalkResponse, id, crosswalkCodes);
					transformToPreferredCode(finalConvertedMap, standardPrefs, code, crosswalkCodes);
				});
			}
		}
		if (!finalConvertedMap.isEmpty()) metadata.setStandards(finalConvertedMap);	
		if (taxonomySetAsMap.get(IndexFields.SUBJECT) != null && !((List<String>) taxonomySetAsMap.get(IndexFields.SUBJECT)).isEmpty()) metadata.setSubject((List<String>) taxonomySetAsMap.get(IndexFields.SUBJECT));		
		if (taxonomySetAsMap.get(IndexFields.COURSE) != null && !((List<String>) taxonomySetAsMap.get(IndexFields.COURSE)).isEmpty()) metadata.setCourse((List<String>) taxonomySetAsMap.get(IndexFields.COURSE));		
		if (taxonomySetAsMap.get(IndexFields.DOMAIN) != null && !((List<String>) taxonomySetAsMap.get(IndexFields.DOMAIN)).isEmpty()) metadata.setDomain((List<String>) taxonomySetAsMap.get(IndexFields.DOMAIN));		
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

	private void transformToPreferredCode(List<Map<String, String>> finalConvertedMap, JSONObject standardPrefs, Map<String, String> codeAsMap, List<Map<String, String>> crosswalkCodes) {
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
							crosswalk.remove(IndexFields.ID);
							if(crosswalk.containsKey(IndexFields.PARENT_TITLE)) crosswalk.remove(IndexFields.PARENT_TITLE);
							finalConvertedMap.add(crosswalk);
						}
					} else if (internalCode.startsWith(framework + DOT)) {
						codeAsMap.remove(IndexFields.ID);
						if(codeAsMap.containsKey(IndexFields.PARENT_TITLE)) codeAsMap.remove(IndexFields.PARENT_TITLE);
						finalConvertedMap.add(codeAsMap);
					}
				}
			}
		} catch (JSONException e) {
			logger.error("JsonException during taxonomy tranformation!", e.getMessage());
		}
	}
	
	private String getSubjectFromCodeId(String codeId) {
		return codeId.contains(HYPHEN) ? codeId.substring((codeId.indexOf(DOT) + 1), codeId.indexOf(HYPHEN)) : null;
	}

	protected UserV3 setUser(Map<String, Object> userData, SearchData input){
		UserV3 user = null;
		if (userData != null && !userData.isEmpty()) {
			user = new UserV3();
			user.setFirstName((String) userData.get(IndexFields.FIRST_NAME));
			user.setLastName((String) userData.get(IndexFields.LAST_NAME));
			if (RESOURCE_MATCH.matcher(input.getType()).matches()) user.setUsername((String) userData.get(IndexFields.USERNAME));
			if (userData.get(IndexFields.PROFILE_IMAGE) != null) user.setProfileImageUrl(HTTP + COLON + input.getUserCdnUrl() + (String) userData.get(IndexFields.PROFILE_IMAGE));
		}
		return user;
	}
	
}
