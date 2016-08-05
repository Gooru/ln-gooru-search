package org.ednovo.gooru.search.domain.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class SearchResults<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6430955873153488215L;
	private List<T> searchResults;
	private long totalHitCount;
	private long searchCount;
	private String userInput;
	private String searchType;
	private String category;

	Map<String, Object> searchInfo;

	private String queryUId;

	public SearchResults() {

	}

	public List<T> getSearchResults() {
		return searchResults;
	}

	public void setSearchResults(List<T> searchResults) {
		this.searchResults = searchResults;
	}

	public String getUserInput() {
		return userInput;
	}

	public void setUserInput(String userInput) {
		this.userInput = userInput;
	}

	public long getTotalHitCount() {
		return totalHitCount;
	}

	public void setTotalHitCount(long totalHitCount) {
		this.totalHitCount = totalHitCount;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCategory() {
		return category;
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

	/**
	 * @return the searchCount
	 */
	public long getSearchCount() {
		return searchCount;
	}

	/**
	 * @param searchCount
	 *            the searchCount to set
	 */
	public void setSearchCount(long searchCount) {
		this.searchCount = searchCount;
	}

}
