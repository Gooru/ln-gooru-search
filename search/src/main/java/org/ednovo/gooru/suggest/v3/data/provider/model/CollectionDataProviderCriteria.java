package org.ednovo.gooru.suggest.v3.data.provider.model;

import java.util.List;

public class CollectionDataProviderCriteria extends ContextDataProviderCriteria{

	private String collectionId;
	
	private List<String> collectionIds;

	public String getCollectionId() {
		return collectionId;
	}

	public void setCollectionId(String collectionId) {
		this.collectionId = collectionId;
	}

	public void setCollectionIds(List<String> collectionIds) {
		this.collectionIds = collectionIds;
	}

	public List<String> getCollectionIds() {
		return collectionIds;
	}
	
}
