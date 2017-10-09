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
import org.ednovo.gooru.search.es.processor.SearchProcessor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.CaseFormat;

public abstract class SuggestDeserializeProcessor<O, S> extends SearchProcessor<SearchData, O> implements Constants {

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
		JSONObject standardPrefs = input.getUserTaxonomyPreference();
		List<String> leafInternalCodes = (List<String>) taxonomyMap.get(IndexFields.LEAF_INTERNAL_CODES);
		if (leafInternalCodes != null) {
			List<Map<String, Object>> crosswalkResponse = searchCrosswalk(input, leafInternalCodes);
			Map<String, Object> curriculumAsMap = (Map<String, Object>) taxonomySetAsMap.get(IndexFields.CURRICULUM);
			List<Map<String, String>> curriculumInfoAsList = (List<Map<String, String>>) curriculumAsMap.get(IndexFields.CURRICULUM_INFO);
			if (curriculumInfoAsList != null && !curriculumInfoAsList.isEmpty()) {
				curriculumInfoAsList.forEach(codeAsMap -> {
					if (standardPrefs == null) {
						convertKeysToSnakeCase(finalConvertedMap, codeAsMap);
					} else {
						List<Map<String, String>> crosswalkCodes = null;
						crosswalkCodes = deserializeCrosswalkResponse(crosswalkResponse, codeAsMap.get(IndexFields.ID), crosswalkCodes);
						transformToPreferredCode(finalConvertedMap, standardPrefs, codeAsMap, crosswalkCodes);
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

	private void transformToPreferredCode(Map<String, Object> finalConvertedMap, JSONObject standardPrefs, Map<String, String> codeAsMap, List<Map<String, String>> crosswalkCodes) {
		String internalCode = codeAsMap.get(IndexFields.ID);
		final String subject = internalCode.substring((internalCode.indexOf(DOT) + 1), internalCode.indexOf(HYPHEN));
		String framework = null;
		Boolean isTransformed = false;
		try {
			if (standardPrefs != null && standardPrefs.has(subject)) {
				framework = standardPrefs.getString(subject);
				if (framework != null && !internalCode.startsWith(framework + DOT) && crosswalkCodes != null) {
					for (Map<String, String> crosswalk : crosswalkCodes) {
						if (!crosswalk.get(FRAMEWORK_CODE).equalsIgnoreCase(framework)) {
							continue;
						}
						crosswalk.put(PARENT_TITLE, codeAsMap.get(PARENT_TITLE));
						convertKeysToSnakeCase(finalConvertedMap, crosswalk);
						isTransformed = true;
					}
				}
			}
			if (!isTransformed) {
				convertKeysToSnakeCase(finalConvertedMap, codeAsMap);
			}
		} catch (JSONException e) {
			logger.error("JsonException during taxonomy tranformation!", e.getMessage());
		}
	}

	private void convertKeysToSnakeCase(Map<String, Object> finalConvertedMap, Map<String, String> codeAsMap) {
		Map<String, String> convertedMap = new HashMap<>();
		codeAsMap.forEach((k, v) -> {
			if (!k.equalsIgnoreCase(IndexFields.ID))
				convertedMap.put(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, k), v);
		});
		finalConvertedMap.put(codeAsMap.get(IndexFields.ID), convertedMap);
	}

	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> searchCrosswalk(SearchData input, List<String> leafInternalCodes) {
		SearchData crosswalkRequest = new SearchData();
		crosswalkRequest.setPretty(input.getPretty());
		crosswalkRequest.setIndexType(EsIndex.CROSSWALK);
		crosswalkRequest.putFilter(AMPERSAND + CARET_SYMBOL + IndexFields.CROSSWALK_CODES + DOT + IndexFields.ID, (StringUtils.join(leafInternalCodes,",")));
		crosswalkRequest.setQueryString(STAR);
		List<Map<String, Object>> searchResponse = (List<Map<String, Object>>) SearchHandler.getSearcher(SearchHandlerType.CROSSWALK.name()).search(crosswalkRequest).getSearchResults();
		return searchResponse;
	}

	protected Map<String, Object> transformMetadata(Map<String, List<String>> metadata) {
		Map<String, Object> txMetadata = new HashMap<>();
		metadata.forEach((k, v) -> {
			if (!v.isEmpty()) {
				String key = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, k);
				if (!k.equalsIgnoreCase(IndexFields.GRADE)) {
					txMetadata.put(key, String.join(SEARCH_COMMA_SEPARATOR, ((List<String>) v)));
				} else {
					txMetadata.put(key, v);
				}
			}
		});
		return txMetadata;
	}

}
