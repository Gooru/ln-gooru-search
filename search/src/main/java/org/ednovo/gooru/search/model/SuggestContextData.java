package org.ednovo.gooru.search.model;

import java.util.List;


public class SuggestContextData<D> {

	private D suggestContextData;
	
	private List<D> suggestContextDataList;

	public void setSuggestContextData(D suggestContextData) {
		this.suggestContextData = suggestContextData;
	}

	public D getSuggestContextData() {
		return suggestContextData;
	}

	public void setSuggestContextDataList(List<D> suggestContextDataList) {
		this.suggestContextDataList = suggestContextDataList;
	}

	public List<D> getSuggestContextDataList() {
		return suggestContextDataList;
	}
	
	
}
