package org.ednovo.gooru.suggest.v3.model;

import java.io.Serializable;

public class SuggestResponse<R> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String suggestedType;
	
	private long executionTime;

	private R suggestResults;
	
	public void setSuggestedType(String suggestedType) {
		this.suggestedType = suggestedType;
	}

	public String getSuggestedType() {
		return suggestedType;
	}

	public void setExecutionTime(long executionTime) {
		this.executionTime = executionTime;
	}

	public long getExecutionTime() {
		return executionTime;
	}

	public void setSuggestResults(R suggestResults) {
		this.suggestResults = suggestResults;
	}

	public R getSuggestResults() {
		return suggestResults;
	}
	
}
