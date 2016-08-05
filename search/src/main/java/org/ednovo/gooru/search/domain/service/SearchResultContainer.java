package org.ednovo.gooru.search.domain.service;


import java.util.List;



public class SearchResultContainer {

	private List<SearchResult> searchResults;
	private int totalHitCount;
	private String userInput;
	private String searchType;
		
	public SearchResultContainer() {
			
	}
	
	public List<SearchResult> getSearchResults() {
			return searchResults;
	}
	public void setSearchResults(List<SearchResult> searchResults) {
			this.searchResults = searchResults;
	}
	public String getUserInput() {
			return userInput;
	}
	public void setUserInput(String userInput) {
			this.userInput = userInput;
	}

	public int getTotalHitCount() {
			return totalHitCount;
	}
	public void setTotalHitCount(int totalHitCount) {
			this.totalHitCount = totalHitCount;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	
}
