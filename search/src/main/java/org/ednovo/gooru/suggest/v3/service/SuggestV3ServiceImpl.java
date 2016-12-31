package org.ednovo.gooru.suggest.v3.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.search.es.model.SuggestResponse;
import org.ednovo.gooru.suggest.v3.data.provider.model.ResourceDataProviderCriteria;
import org.ednovo.gooru.suggest.v3.data.provider.model.SuggestDataProviderType;
import org.ednovo.gooru.suggest.v3.data.provider.service.ContentDataProviderService;
import org.ednovo.gooru.suggest.v3.handler.SuggestHandler;
import org.ednovo.gooru.suggest.v3.model.ResourceContextData;
import org.ednovo.gooru.suggest.v3.model.SuggestData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SuggestV3ServiceImpl<D> implements SuggestV3Service {
	
	@Autowired
	private ContentDataProviderService resourceDataProviderService;
	
	@Override
	public List<SuggestResponse<Object>> suggest(SuggestData suggestData) throws Exception {
		
		ResourceDataProviderCriteria resourceDataProviderCriteria = new ResourceDataProviderCriteria();
		String resourceId = suggestData.getSuggestV3Context().getId() != null ? suggestData.getSuggestV3Context().getId() : null;
		resourceDataProviderCriteria.setResourceId(resourceId);
				
		Map<SuggestDataProviderType, Object> dataProviderInput = new HashMap<SuggestDataProviderType, Object>();
		
		List<SuggestResponse<Object>> suggestResList = new ArrayList<SuggestResponse<Object>>();
		
		List<SuggestHandler<Object>> suggestHandlerList = SuggestHandler.getSuggester(suggestData.getType().toUpperCase());
		for (SuggestHandler<Object> suggestHandler : suggestHandlerList) {
			List<SuggestDataProviderType> dataProviders = suggestHandler.suggestDataProviderTypes();
			for (SuggestDataProviderType dataProvider : dataProviders) {
				if (dataProvider.name().equals(SuggestDataProviderType.RESOURCE.name())) {
					ResourceContextData resourceData = resourceDataProviderService.getResourceContextData(resourceDataProviderCriteria);
					if (resourceData != null) {
						dataProviderInput.put(SuggestDataProviderType.RESOURCE, resourceData);
					}
				}
			}
			suggestResList.add(suggestHandler.suggest(suggestData, dataProviderInput));
		}
		return suggestResList;
	}

}
