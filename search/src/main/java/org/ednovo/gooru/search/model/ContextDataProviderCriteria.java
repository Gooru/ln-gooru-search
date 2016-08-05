package org.ednovo.gooru.search.model;


public class ContextDataProviderCriteria {

	private String userUid;
	
	private String suggestDataType;
	
	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	public String getUserUid() {
		return userUid;
	}

	public void setSuggestDataType(String suggestDataType) {
		this.suggestDataType = suggestDataType;
	}

	public String getSuggestDataType() {
		return suggestDataType;
	}
	
}
