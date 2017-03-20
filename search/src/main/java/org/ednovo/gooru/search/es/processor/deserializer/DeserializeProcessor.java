/**
 *
 */
package org.ednovo.gooru.search.es.processor.deserializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.ednovo.gooru.search.es.constant.Constants;
import org.ednovo.gooru.search.es.constant.EsIndex;
import org.ednovo.gooru.search.es.constant.IndexFields;
import org.ednovo.gooru.search.es.exception.SearchException;
import org.ednovo.gooru.search.es.handler.SearchHandler;
import org.ednovo.gooru.search.es.handler.SearchHandlerType;
import org.ednovo.gooru.search.es.model.Code;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.ednovo.gooru.search.es.processor.SearchProcessor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * @author SearchTeam
 *
 */

public abstract class DeserializeProcessor<O, S> extends SearchProcessor<SearchData, O> implements Constants {

	abstract O deserialize(Map<String, Object> model, SearchData input, O output);

	abstract S collect(Map<String, Object> model, SearchData input, S output);

	@SuppressWarnings("unchecked")
	@Override
	public final void process(SearchData searchData, SearchResponse<O> response) {
		try {
			Map<String, Object> responseAsMap = (Map<String, Object>) SERIAILIZER.readValue(searchData.getSearchResultText(),
					new TypeReference<Map<String, Object>>() {
					});
			O searchResult = deserialize(responseAsMap, searchData, null);
			response.setSearchResults(searchResult);			
		   if(responseAsMap.get(SEARCH_HITS) !=null) {
			   Map<String, Object> hit = (Map<String, Object>) responseAsMap.get(SEARCH_HITS);
			   if(((List<Map<String, Object>>) (hit).get(SEARCH_HITS) )!= null) {
					Map<String,Object> stats = new HashMap<String,Object>(3);
					 stats.put("totalHitCount",((Integer) hit.get(SEARCH_TOTAL)).longValue());
					 stats.put("pageSize",searchData.getSize());
					 stats.put("pageNumber", searchData.getPageNum());
					 
					 Map<String,Object> query = new HashMap <String,Object>(4);
					 query.put("userQueryString", searchData.getUserQueryString());
					 query.put("rewrittenQueryString", searchData.getQueryString());
					 if(searchData.getSpellCheckQueryString()!= null && !searchData.getSpellCheckQueryString().isEmpty()) {
						  query.put("current",searchData.getSpellCheckQueryString());
						  query.put("rewriteType",SPELLCHECKER);
					 }	 
					 response.setQuery(query);
					 response.setStats(stats);
		
					List<Map<String, Object>> hits = (List<Map<String, Object>>) (hit).get(SEARCH_HITS);
					response.setTotalHitCount(((Integer) hit.get(SEARCH_TOTAL)).longValue());
					response.setResultCount(hits.size());
					//response.setSpellCheckQueryString(searchData.getSpellCheckQueryString()); 
				}

			}
			/*if(searchData.getFacet() !=  null && searchData.getFacet().trim().length() > 0 && responseAsMap.get(FACETS) !=null){
				response.setFacets(responseAsMap.get(FACETS));
			}*/

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
		if (standardPrefs != null) {
			List<String> leafInternalCodes = (List<String>)taxonomyMap.get(IndexFields.LEAF_INTERNAL_CODES);
			List<Map<String, Object>> crosswalkResponse = searchCrosswalk(input, leafInternalCodes);
			
			Map<String, Object> curriculumAsMap = (Map<String, Object>) taxonomySetAsMap.get(IndexFields.CURRICULUM);
			List<Map<String, String>> curriculumInfoAsList = (List<Map<String, String>>) curriculumAsMap.get(IndexFields.CURRICULUM_INFO);
			if (curriculumInfoAsList != null) {
				curriculumInfoAsList.forEach(code -> {
					Map<String, String> codeAsMap = code;
					String id = codeAsMap.get(IndexFields.ID);

					Map<String, Map<String, String>> crosswalkResult = null;
					crosswalkResult = deserializeCrosswalkResponse(crosswalkResponse, id, crosswalkResult);
					transformToPreferredCode(txCurriculumInfoAsList, standardPrefs, codeAsMap, crosswalkResult);
				});
				curriculumAsMap.put(IndexFields.CURRICULUM_INFO, txCurriculumInfoAsList);
				taxonomySetAsMap.put(IndexFields.CURRICULUM, curriculumAsMap);
			}
		}
		return taxonomySetAsMap;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Map<String, String>> deserializeCrosswalkResponse(List<Map<String, Object>> crosswalkResponse, String id, Map<String, Map<String, String>> crosswalkResult) {
		if (crosswalkResponse != null && !crosswalkResponse.isEmpty()) {
			for (Map<String, Object> response : crosswalkResponse) {
				Map<String, Object> source = (Map<String, Object>) response.get(SEARCH_SOURCE);
				String crosswalkId = (String) source.get(IndexFields.ID);
				if (!crosswalkId.equalsIgnoreCase(id)) {
					continue;
				} else {
					crosswalkResult = (Map<String, Map<String, String>>) source.get(IndexFields.EQUIVALENT_COMPETENCIES);
				}
			}
		}
		return crosswalkResult;
	}

	private void transformToPreferredCode(List<Map<String, String>> txCurriculumInfoAsList, JSONObject standardPrefs, Map<String, String> codeAsMap, Map<String, Map<String, String>> crosswalkResult) {
		String internalCode = codeAsMap.get(IndexFields.ID);
		final String subject = getSubjectFromCodeId(internalCode);
		if (standardPrefs.has(subject)) {
			String framework = null;
			try {
				framework = standardPrefs.getString(subject);
				if (framework != null) {
					if (!internalCode.startsWith(framework + DOT) && crosswalkResult != null) {
						Map<String, String> txCodes = crosswalkResult.get(framework);
						if (txCodes != null) {
							txCodes.put("leafInternalCode", internalCode);
							txCurriculumInfoAsList.add(txCodes);
						}
					} else if (internalCode.startsWith(framework + DOT)) {
						codeAsMap.put("leafInternalCode", internalCode);
						txCurriculumInfoAsList.add(codeAsMap);
					}
				}
			} catch (JSONException e) {
				logger.error("JsonException during taxonomy tranformation!", e.getMessage());
			}
		}
	}

	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> searchCrosswalk(SearchData input, List<String> leafInternalCodes) {
		SearchData crosswalkRequest = new SearchData();
		crosswalkRequest.setPretty(input.getPretty());
		crosswalkRequest.setIndexType(EsIndex.CROSSWALK);
		crosswalkRequest.putFilter(AMPERSAND + CARET_SYMBOL + IndexFields.ID, (StringUtils.join(leafInternalCodes,",")));
		crosswalkRequest.setQueryString(STAR);
		List<Map<String, Object>> searchResponse = (List<Map<String, Object>>) SearchHandler.getSearcher(SearchHandlerType.CROSSWALK.name()).search(crosswalkRequest).getSearchResults();
		return searchResponse;
	}

	private String getSubjectFromCodeId(String codeId) {
		return codeId.substring((codeId.indexOf(DOT) + 1), codeId.indexOf(HYPHEN));
	}

	protected Long getAvgTimeSpent(Object avgTimeSpentObj) {
		Long avgTimeSpent = 0L;
		try {
			Integer avgTSInt = (Integer) avgTimeSpentObj;
			avgTimeSpent = avgTSInt.longValue();
		} catch (Exception e) {
			if (e instanceof ClassCastException) {
				avgTimeSpent = (Long) avgTimeSpentObj;
			} else {
				logger.error("Error in setting avg timespnet " + e);
			}
		}

		return avgTimeSpent;
	}
	
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

}
