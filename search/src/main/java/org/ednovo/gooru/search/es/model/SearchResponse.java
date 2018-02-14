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

  private int resultCount;

  private long executionTime;

  private Map<String, Object> query;

  private Map<String, Object> stats;

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

}
