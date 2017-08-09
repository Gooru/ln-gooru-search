package org.ednovo.gooru.suggest.v3.data.provider.service;

import org.ednovo.gooru.suggest.v3.data.provider.model.ResourceDataProviderCriteria;
import org.ednovo.gooru.suggest.v3.model.ResourceContextData;
import org.json.JSONException;

public interface ContentDataProviderService {
			
	public ResourceContextData getResourceContextData(ResourceDataProviderCriteria resourceDataProviderCriteria) throws JSONException;
	
}
