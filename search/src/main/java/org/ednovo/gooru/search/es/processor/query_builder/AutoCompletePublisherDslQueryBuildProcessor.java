package org.ednovo.gooru.search.es.processor.query_builder;

import java.util.HashMap;
import java.util.Map;

import org.ednovo.gooru.search.es.constant.IndexFields;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.processor.SearchProcessor;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.ednovo.gooru.search.responses.SearchResponse;
import org.springframework.stereotype.Component;

@Component
public class AutoCompletePublisherDslQueryBuildProcessor extends SearchProcessor<SearchData, Object> {

	protected void buildQuery(SearchData searchData) {
		String query = searchData.getOriginalQuery();
		if(query.equalsIgnoreCase("*:*") || query.equalsIgnoreCase(STAR) || query.trim().length() == 0){
			return;
		}
		
		Map<String, Object> matchQuery = constructMatchQuery(searchData);
		searchData.getQueryDsl().put(QUERY, matchQuery);
		searchData.getQueryDsl().put(FROM, searchData.isPaginated() ? searchData.getFrom() * searchData.getSize() : searchData.getFrom()).put(SIZE, searchData.getSize());
	}

	private Map<String, Object> constructMatchQuery(SearchData searchData) {
		Map<String, Object> matchQuery = new HashMap<String, Object>(1);
		Map<String, Object> keywordQuery = new HashMap<String, Object>(1);
		Map<String, Object> query = new HashMap<String, Object>(2);
		String queryAnalyzer = getSearchSetting(SEARCH + DOT + searchData.getType().toLowerCase() + DOT + QUERY + DOT + ANALYZER, STANDARD);
		query.put(QUERY, searchData.getQueryString());
		query.put(ANALYZER, queryAnalyzer);
		keywordQuery.put(IndexFields.PUBLISHER_SUGGEST, query);
		matchQuery.put(MATCH, keywordQuery);
		return matchQuery;
	}

	@Override
	public void process(SearchData searchData, SearchResponse<Object> response) {
		buildQuery(searchData);
	}

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.AutoCompletePublisherDslQueryBuild;
	}
}
