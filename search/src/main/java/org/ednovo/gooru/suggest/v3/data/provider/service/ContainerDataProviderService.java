package org.ednovo.gooru.suggest.v3.data.provider.service;

import org.ednovo.gooru.suggest.v3.data.provider.model.CollectionDataProviderCriteria;
import org.ednovo.gooru.suggest.v3.model.CollectionContextData;

public interface ContainerDataProviderService {
	public CollectionContextData getCollectionData(CollectionDataProviderCriteria collectionDataProviderCriteria);

}

