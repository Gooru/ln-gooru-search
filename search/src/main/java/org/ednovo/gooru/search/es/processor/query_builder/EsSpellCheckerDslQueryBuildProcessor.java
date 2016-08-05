package org.ednovo.gooru.search.es.processor.query_builder;

import java.util.HashMap;
import java.util.Map;

import org.ednovo.gooru.search.es.filter.Query;
import org.ednovo.gooru.search.es.model.MapWrapper;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.ednovo.gooru.search.es.processor.SearchProcessor;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.springframework.stereotype.Component;

@Component
public class EsSpellCheckerDslQueryBuildProcessor extends SearchProcessor<SearchData, Object> {

  protected void buildQuery(SearchData searchData) {

    try {

      String searchDataType = searchData.getIndexType().getName().toUpperCase();

      String query = searchData.getQueryString();
      if (query == null || query.equalsIgnoreCase("*:*") || query.equalsIgnoreCase("*") || query.trim().length() == 0) {
        return;
      }

      String queryField = "";

      MapWrapper<Object> searchDataMap = new MapWrapper<Object>();
      searchData.setParameters(searchDataMap);

      queryField = getSetting("S_" + searchDataType + "_QUERY_SPELLCHECKER_FIELD");

      String[] queryFields = null;
      if (queryField != null && !queryField.isEmpty()) {
        queryFields = queryField.split(",");
      }
      if (queryFields != null) {
        Map<String, Object> queryStringQuery = constructQueryStringQuery(searchData, queryFields);
        searchData.getQueryDsl().put(QUERY, queryStringQuery);

        Map<String, Object> suggestStringQuery = constructSuggestStringQuery(searchData, queryFields);
        searchData.getQueryDsl().put(SUGGEST, suggestStringQuery);
      }
    } catch (Exception e) {
      LOG.error("SpellChecker query builder encountered an exception: ", e);
    }
  }

  private Map<String, Object> constructQueryStringQuery(SearchData searchData, String[] queryFields) {
    Map<String, Object> queryString = new HashMap<String, Object>(1);

    Query queryStringQuery = new Query(searchData.getQueryString(), queryFields, null);
    queryString.put(QUERY_STRING, queryStringQuery);
    return queryString;
  }

  private Map<String, Object> constructSuggestStringQuery(SearchData searchData, String[] queryFields) {
    Map<String, Object> suggestTermQuery = new HashMap<String, Object>(1);
    suggestTermQuery.put(TEXT, searchData.getQueryString());

    suggestTermQuery.put(CHECK_SUGGEST, getTermSuggestForField(queryFields));
    return suggestTermQuery;
  }


  private Map<String, Object> getTermSuggestForField(String[] queryFields) {

    Map<String, Object> queryString = new HashMap<String, Object>(1);
    Map<String, Object> termQuery = new HashMap<String, Object>(1);
    //we are currently looking up to 1 field only "customSpellCheckerSuggest"
    queryString.put(FIELD, queryFields[0]);
    termQuery.put(TERM, queryString);

    return termQuery;
  }

  @Override
  public void process(SearchData searchData, SearchResponse<Object> response) {
    buildQuery(searchData);
  }

  @Override
  protected SearchProcessorType getType() {
    return SearchProcessorType.EsSpellCheckerDslQueryBuild;
  }
}
