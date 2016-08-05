package org.ednovo.gooru.search.model;

import java.util.List;

public class CollectionDataProviderCriteria extends ContextDataProviderCriteria{

	private List<String> collectionIds;

	public void setCollectionIds(List<String> collectionIds) {
		this.collectionIds = collectionIds;
	}

	public List<String> getCollectionIds() {
		return collectionIds;
	}
	
}
