/**
 * 
 */
package org.ednovo.gooru.search.responses;

import java.io.Serializable;
import java.util.List;
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

	private Long totalHitCount;

	private Integer resultCount;

	private Long executionTime; 
	
	private Map<String, Object> searchQuery;
	
	private Map<String, Object> stats;

	private List<Map<String, Object>> aggregations;

	private R results;

	private String query;

	public R getSearchResults() {
		return searchResults;
	}

	public void setSearchResults(R searchResults) {
		this.searchResults = searchResults;
	}

	public List<Map<String, Object>> getAggregations() {
		return aggregations;
	}

	public void setAggregations(List<Map<String, Object>> aggregations) {
		this.aggregations = aggregations;
	}

	public R getResults() {
		return results;
	}

	public void setResults(R results) {
		this.results = results;
	}

	public Long getTotalHitCount() {
		return totalHitCount;
	}

	public void setTotalHitCount(Long totalHitCount) {
		this.totalHitCount = totalHitCount;
	}

	public Integer getResultCount() {
		return resultCount;
	}

	public void setResultCount(Integer resultCount) {
		this.resultCount = resultCount;
	}

	public Long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Long executionTime) {
        this.executionTime = executionTime;
    }

	public Map<String, Object> getSearchQuery() {
		return searchQuery;
	}

	public void setSearchQuery(Map<String, Object> searchQuery) {
		this.searchQuery = searchQuery;
	}

	public Map<String, Object> getStats() {
		return stats;
	}
	
	public void setStats(Map<String, Object> stats) {
		this.stats = stats;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

}
