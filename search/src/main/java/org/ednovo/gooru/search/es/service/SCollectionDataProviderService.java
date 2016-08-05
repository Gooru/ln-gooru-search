package org.ednovo.gooru.search.es.service;

import java.util.List;

import org.ednovo.gooru.search.model.CollectionContextData;
import org.ednovo.gooru.search.model.CollectionDataProviderCriteria;

public interface SCollectionDataProviderService {
	public List<CollectionContextData> getCollectionList(CollectionDataProviderCriteria collectionDataProviderCriteria);

}

