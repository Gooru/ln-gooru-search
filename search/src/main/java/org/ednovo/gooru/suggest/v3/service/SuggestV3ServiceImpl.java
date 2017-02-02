package org.ednovo.gooru.suggest.v3.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.search.es.model.SuggestResponse;
import org.ednovo.gooru.suggest.v3.data.provider.model.CollectionDataProviderCriteria;
import org.ednovo.gooru.suggest.v3.data.provider.model.ResourceDataProviderCriteria;
import org.ednovo.gooru.suggest.v3.data.provider.model.SuggestDataProviderType;
import org.ednovo.gooru.suggest.v3.data.provider.service.ContainerDataProviderService;
import org.ednovo.gooru.suggest.v3.data.provider.service.ContentDataProviderService;
import org.ednovo.gooru.suggest.v3.handler.SuggestHandler;
import org.ednovo.gooru.suggest.v3.model.CollectionContextData;
import org.ednovo.gooru.suggest.v3.model.ResourceContextData;
import org.ednovo.gooru.suggest.v3.model.SuggestData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SuggestV3ServiceImpl<D> implements SuggestV3Service {
	
	protected static final Logger LOG = LoggerFactory.getLogger(SuggestV3ServiceImpl.class);

	@Autowired
	private ContentDataProviderService contentDataProviderService;
	
	@Autowired
	private ContainerDataProviderService containerDataProviderService;
	
	@Override
	public List<SuggestResponse<Object>> suggest(SuggestData suggestData) throws Exception {
		
		ResourceDataProviderCriteria resourceDataProviderCriteria = new ResourceDataProviderCriteria();
		resourceDataProviderCriteria.setResourceId(suggestData.getSuggestV3Context().getId());
				
		CollectionDataProviderCriteria collectionDataProviderCriteria = new CollectionDataProviderCriteria();
		collectionDataProviderCriteria.setCollectionId(suggestData.getSuggestV3Context().getContainerId());
		
		Map<SuggestDataProviderType, Object> dataProviderInput = new HashMap<SuggestDataProviderType, Object>();
		
		List<SuggestResponse<Object>> suggestResList = new ArrayList<SuggestResponse<Object>>();
		
		List<SuggestHandler<Object>> suggestHandlerList = SuggestHandler.getSuggester(suggestData.getType().toUpperCase());
		for (SuggestHandler<Object> suggestHandler : suggestHandlerList) {
			List<SuggestDataProviderType> dataProviders = suggestHandler.suggestDataProviderTypes();
			for (SuggestDataProviderType dataProvider : dataProviders) {
				if (dataProvider.toString().equals(SuggestDataProviderType.RESOURCE.toString())) {
					ResourceContextData resourceData = contentDataProviderService.getResourceContextData(resourceDataProviderCriteria);
					if (resourceData != null) {
						dataProviderInput.put(SuggestDataProviderType.RESOURCE, resourceData);
					}
				}
				if (dataProvider.toString().equals(SuggestDataProviderType.COLLECTION.toString())) {
					CollectionContextData collectionData = containerDataProviderService.getCollectionData(collectionDataProviderCriteria);
					if (collectionData != null) {
						dataProviderInput.put(SuggestDataProviderType.COLLECTION, collectionData);
					}
				}
			}
			suggestResList.add(suggestHandler.suggest(suggestData, dataProviderInput));
		}
		return suggestResList;
	}

}
