/**
 * 
 */
package org.ednovo.gooru.search.es.processor.query_builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.search.es.constant.SearchType;
import org.ednovo.gooru.search.es.filter.Filter;
import org.ednovo.gooru.search.es.filter.FunctionScore;
import org.ednovo.gooru.search.es.filter.Query;
import org.ednovo.gooru.search.es.filter.QueryString;
import org.ednovo.gooru.search.es.model.MapWrapper;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.ednovo.gooru.search.es.processor.SearchProcessor;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.ednovo.gooru.search.es.processor.util.FilterBuilderUtils;
import org.springframework.stereotype.Component;

/**
 * @author SearchTeam
 * 
 */
@Component
public class EsDslQueryBuildProcessor extends SearchProcessor<SearchData, Object> {
	

	protected void buildQuery(SearchData searchData) {
		String searchDataType = searchData.getType();
		if (searchDataType.equalsIgnoreCase(MULTI_RESOURCE)) {
			searchDataType = searchData.getIndexType().getName().toUpperCase();
		}
		String typeLower = searchDataType.toLowerCase();
		//Map<String, Object> queryString = new HashMap<String, Object>(1);
		searchData.getQueryDsl().put(FROM, searchData.isPaginated() ? searchData.getFrom() * searchData.getSize() : searchData.getFrom()).put(SIZE, searchData.getSize());
		String fields = "";

		MapWrapper<Object> searchParameters = searchData.getParameters();
		if (searchDataType.equalsIgnoreCase(TYPE_SCOLLECTION)
				&& (searchParameters != null && searchParameters.containsKey(INCLUDE_COLLECTION_ITEM) && searchParameters.getBoolean(INCLUDE_COLLECTION_ITEM))) {
			fields = getSetting("S_" + searchDataType + "_ITEM_FIELDS");
		} else if (searchDataType.equalsIgnoreCase(TYPE_SCOLLECTION) && (searchParameters != null && searchParameters.containsKey(INCLUDE_CIMIN) && searchParameters.getBoolean(INCLUDE_CIMIN))) {
			fields = getSetting("S_" + searchDataType + "_ITEM_SPECIFIC_FIELDS");
		} else {
			fields = getSearchSetting("search." + typeLower + ".fields");
		}

		if (fields != null && fields.trim().length() > 0) {
			searchData.getQueryDsl().put("_source", fields.split(","));
		}
		String queryField = "";
		if (!searchData.isFacetSubjectSearch()) {
			if (searchDataType.equalsIgnoreCase(SEARCH_TAXONOMY) && (searchData.getParameters() != null && searchData.getParameters().containsKey("searchBy")
					&& searchData.getParameters().getString("searchBy").equalsIgnoreCase("standard"))) {
				queryField = getSetting("S_STANDARD_QUERY_FIELDS");
			} else {
				queryField = getSearchSetting("search." + typeLower + ".query.fields");
			}
		}

		String[] queryFields = null;
		if (queryField != null && !queryField.isEmpty()) {
			queryFields = queryField.split(",");
			for (int i = 0; i < queryFields.length; i++) {
				String boost = getSearchSetting("search." + typeLower + ".field." + queryFields[i].toLowerCase() + ".boost");
				if (boost != null) {
					queryFields[i] = queryFields[i] + "^" + boost;
				}
			}
		}

		String analyzer = getSearchSetting("search." + typeLower + ".query.analyzer");
		if (emailValidate(searchData.getQueryString().toLowerCase()) || uuidValidate(searchData.getQueryString().toLowerCase())) {
			if (getSearchSetting("search." + typeLower + ".query.email.uuid.analyzer") != null) {
				analyzer = getSearchSetting("search." + typeLower + ".query.email.uuid.analyzer");
			}
		}

		Query mainQuery = new Query(searchData.getQueryString(), queryFields, true, searchData.getDefaultOperator(), searchData.isAllowLeadingWildcard(), analyzer,
				getCassandraSettingAsFloat("search." + typeLower + ".query.user_query.boost"));
		QueryString queryString = new QueryString(mainQuery);
		//queryString.put("query_string", mainQuery);
		String lang = getSearchSetting("search." + searchData.getType().toLowerCase() + ".query.nativescore.lang");
		String score = getSearchSetting("search." + searchData.getType().toLowerCase() + ".query.score");
		// FIXME:temporary fix because of IllegalArgumentException issue with calculating score in lucene when use match all docs.
		if (searchData.getQueryString().equalsIgnoreCase("*") && !searchData.getType().equalsIgnoreCase(TYPE_USER)) {
			score = "1.0";
		}

		Map<String, Object> customQuery = new HashMap<String, Object>(1);
		if (score != null && searchDataType.equalsIgnoreCase(SearchType.SIMPLE_COLLECTION.getType())) {
			Map<String, Object> query = new HashMap<String, Object>(3);
			Map<String, Object> scriptScore = new HashMap<String, Object>(1);
			Map<String, Object> script = new HashMap<String, Object>(2);
			customQuery.put("function_score", query);
			query.put("query", queryString);
			if (searchDataType.equalsIgnoreCase(SearchType.SIMPLE_COLLECTION.getType()) && lang != null && !(lang.equalsIgnoreCase("native"))) {
				query.put("script_score", scriptScore);
				scriptScore.put("script", script);
				script.put("source", score);
				script.put("lang", getSearchSetting("search." + searchData.getType().toLowerCase() + ".query.script.lang"));
			}
			searchData.getQueryDsl().put("query", customQuery);
		} else {
			searchData.getQueryDsl().put("query", queryString);
		}
		if (!searchData.isQueriesEmpty()) {
			List<Object> shouldQueries = searchData.getQueries();
			shouldQueries.add(searchData.getQueryDsl().getObject(QUERY));
			Map<String, Object> boolQuery = new HashMap<String, Object>(1);
			Map<String, Object> globalQuery = new HashMap<String, Object>(1);
			boolQuery.put("should", shouldQueries);
			globalQuery.put("bool", boolQuery);
			searchData.getQueryDsl().put("query", globalQuery);
		}

		if (searchData.getCustomFilters() != null && searchData.getCustomFilters().size() > 0) {
			List<Object> filters = new ArrayList<Object>(1);
			for (String customFilter : searchData.getCustomFilters()) {
				String params[] = customFilter.split("\\:");
				String[] keys = params[0].split("\\^");
				filters.add(new Filter(FilterBuilderUtils.buildFilter(params[1], keys[0]), keys[1]));
			}
			if (filters.size() > 0) {
				FunctionScore customFiltersScore = new FunctionScore(searchData.getQueryDsl().getObject(QUERY), filters);
				searchData.getQueryDsl().put("query", customFiltersScore);
			}
		}
		if (searchDataType.equalsIgnoreCase(SearchType.RESOURCE.getType())) {
			Map<String, Object> rescoreQuery = new HashMap<String, Object>(2);
			Map<String, Object> queryObj = new HashMap<String, Object>(2);
			Map<String, Object> customScoreQuery = new HashMap<String, Object>(1);
			Map<String, Object> query = new HashMap<String, Object>(1);
			Map<String, Object> scriptScore = new HashMap<String, Object>(1);
			Map<String, Object> script = new HashMap<String, Object>(2);
			customScoreQuery.put("function_score", query);
			query.put("script_score", scriptScore);
			scriptScore.put("script", script);
			script.put("source", getSearchSetting(RESCORE_SCRIPT, DEFAULT_RESCORE_SCRIPT));
			script.put("lang", getSearchSetting(RESCORE_SCRIPT_LANG, DEFAULT_RESCORE_LANG));
			rescoreQuery.put("rescore_query", customScoreQuery);
			rescoreQuery.put("score_mode", getSearchSetting(RESCORE_MODE, DEFAULT_RESCORE_MODE));
			queryObj.put("query", rescoreQuery);
			queryObj.put("window_size", getCassandraSettingAsInt(RESCORE_SCRIPT_LIMIT, DEFAULT_RESCORE_WINDOW_SIZE));
			searchData.getQueryDsl().put("rescore", queryObj);
		}
		if (searchData.getFilters().containsKey(AGG_FIELDNAME) && searchData.getFilters().get(AGG_FIELDNAME) != null) {
			String aggField = searchData.getFilters().get(AGG_FIELDNAME).toString();
			searchData.getFilters().remove(AGG_FIELDNAME);
			Map<String, Object> aggMap = new HashMap<>(1);
			Map<String, Object> aggTermMap = new HashMap<>(1);
			Map<String, Object> aggTermFieldMap = new HashMap<>(2);
			aggTermFieldMap.put(FIELD, aggField);
			if (searchData.getParameters().containsKey("aggSize")) {
				int aggSize = searchData.getParameters().getInteger("aggSize");
				aggTermFieldMap.put(SIZE, aggSize);
			}
			aggTermMap.put(TERMS, aggTermFieldMap);
			aggMap.put("agg_key", aggTermMap);
			searchData.getQueryDsl().put("aggs", aggMap);
			searchData.setSize(0);
			searchData.setAggregationRequest(true);
			searchData.getQueryDsl().remove("rescore");
			searchData.getFilters().put("&?^queryString", queryString);
		}
		searchData.getQueryDsl().put(FROM, searchData.isPaginated() ? searchData.getFrom() * searchData.getSize() : searchData.getFrom()).put(SIZE, searchData.getSize());
	}

	@Override
	public void process(SearchData searchData,
			SearchResponse<Object> response) {
		buildQuery(searchData);
		if (searchData.getFilters() != null && searchData.getFilters().size() > 0) {
			Object boolQuery = FilterBuilderUtils.buildFilters(searchData.getFilters());
			if (boolQuery != null) {
				if(searchData.isAggregationRequest()) {
					searchData.getQueryDsl().put("query", boolQuery); 
				} else {
					searchData.getQueryDsl().put("post_filter", boolQuery);
				}
			}
		}
	}

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.EsDslQueryBuild;
	}

}
