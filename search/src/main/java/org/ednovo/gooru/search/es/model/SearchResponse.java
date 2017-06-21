/**
 * 
 */
package org.ednovo.gooru.search.es.model;

import java.io.Serializable;
import java.util.Map;

/**
 * @author SearchTeam
 * 
 */
public class SearchResponse<R> implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 3657537671508783010L;

  private R searchResults;

  private long totalHitCount;

  private long searchCount;

  private int resultCount;

  private long executionTime;

  private Map<String, Object> query;

  private Map<String, Object> stats;

  /*
   * private String category;
   * 
   * private String currentSubject;
   * 
   * private Object facets;
   * 
   * private List<String> suggestText;
   * 
   * private List<String> relatedSubject;
   * 
   * private List<Map<String, Object>> relatedSubjectMap;
   * 
   * private String spellCheckQueryString;
   * 
   * private String searchType;
   * 
   * private Map<String, Object> searchInfo;
   * 
   * private String userInput;
   * 
   * // original query string put in by user private String userQueryString;
   * 
   * private String queryUId;
   */

  public R getSearchResults() {
    return searchResults;
  }

  public void setSearchResults(R searchResults) {
    this.searchResults = searchResults;
  }

  public long getTotalHitCount() {
    return totalHitCount;
  }

  public void setTotalHitCount(long totalHitCount) {
    this.totalHitCount = totalHitCount;
  }

  public long getSearchCount() {
    return searchCount;
  }

  public void setSearchCount(long searchCount) {
    this.searchCount = searchCount;
  }

  public int getResultCount() {
    return resultCount;
  }

  public void setResultCount(int resultCount) {
    this.resultCount = resultCount;
  }

  public long getExecutionTime() {
    return executionTime;
  }

  public void setExecutionTime(long executionTime) {
    this.executionTime = executionTime;
  }

  public Map<String, Object> getQuery() {
    return query;
  }

  public Map<String, Object> getStats() {
    return stats;
  }

  public void setQuery(Map<String, Object> query) {
    this.query = query;
  }

  public void setStats(Map<String, Object> stats) {
    this.stats = stats;
  }

/*  public String getUserInput() {
    return userInput;
  }

  public void setUserInput(String userInput) {
    this.userInput = userInput;
  }

  public String getSearchType() {
    return searchType;
  }

  public void setSearchType(String searchType) {
    this.searchType = searchType;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public Map<String, Object> getSearchInfo() {
    return searchInfo;
  }

  public void setSearchInfo(Map<String, Object> searchInfo) {
    this.searchInfo = searchInfo;
  }

  public String getQueryUId() {
    return queryUId;
  }

  public void setQueryUId(String queryUId) {
    this.queryUId = queryUId;
  }

  public List<String> getSuggestText() {
    return suggestText;
  }

  public void setSuggestText(List<String> suggestText) {
    this.suggestText = suggestText;
  }

  public Object getFacets() {
    return facets;
  }

  public void setFacets(Object facets) {
    this.facets = facets;
  }

  public void setCurrentSubject(String currentSubject) {
    this.currentSubject = currentSubject;
  }

  public String getCurrentSubject() {
    return currentSubject;
  }

  public void setRelatedSubject(List<String> relatedSubject) {
    this.relatedSubject = relatedSubject;
  }

  public List<String> getRelatedSubject() {
    return relatedSubject;
  }

  public void setRelatedSubjectMap(List<Map<String, Object>> relatedSubjectMap) {
    this.relatedSubjectMap = relatedSubjectMap;
  }

  public List<Map<String, Object>> getRelatedSubjectMap() {
    return relatedSubjectMap;
  }

  public String getUserQueryString() {
    return userQueryString;
  }

  public void setUserQueryString(String userQueryString) {
    this.userQueryString = userQueryString;
  }

  public String getSpellCheckQueryString() {
    return spellCheckQueryString;
  }

  public void setSpellCheckQueryString(String spellCheckQueryString) {
    this.spellCheckQueryString = spellCheckQueryString;
  }*/

}
