/**
 * 
 */
package org.ednovo.gooru.search.responses.v3;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author SearchTeam
 * 
 */
public class SearchV3Response<R> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3657537671508783010L;

	private R searchResults;
	
	private Map<String, Object> query;

	private Map<String, Object> stats;

	private List<Map<String, Object>> aggregations;

	public R getSearchResults() {
		return searchResults;
	}

	public void setSearchResults(R searchResults) {
		this.searchResults = searchResults;
	}

	public Map<String, Object> getQuery() {
		return query;
	}

	public void setQuery(Map<String, Object> query) {
		this.query = query;
	}

	public Map<String, Object> getStats() {
		return stats;
	}

	public void setStats(Map<String, Object> stats) {
		this.stats = stats;
	}

	public List<Map<String, Object>> getAggregations() {
		return aggregations;
	}

	public void setAggregations(List<Map<String, Object>> aggregations) {
		this.aggregations = aggregations;
	}

}
