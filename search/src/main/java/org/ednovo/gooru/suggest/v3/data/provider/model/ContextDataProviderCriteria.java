package org.ednovo.gooru.suggest.v3.data.provider.model;


public class ContextDataProviderCriteria {

	private String userId;
	
	private String suggestDataType;
	
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public void setSuggestDataType(String suggestDataType) {
		this.suggestDataType = suggestDataType;
	}

	public String getSuggestDataType() {
		return suggestDataType;
	}
	
}
