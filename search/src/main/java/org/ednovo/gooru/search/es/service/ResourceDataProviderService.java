package org.ednovo.gooru.search.es.service;

import org.ednovo.gooru.search.model.ResourceContextData;
import org.ednovo.gooru.search.model.ResourceDataProviderCriteria;

public interface ResourceDataProviderService {
	
	public ResourceContextData getResourceContextData(String resourceGooruOid);
		
	public ResourceContextData getResourceContextData(ResourceDataProviderCriteria resourceDataProviderCriteria);
	
}
