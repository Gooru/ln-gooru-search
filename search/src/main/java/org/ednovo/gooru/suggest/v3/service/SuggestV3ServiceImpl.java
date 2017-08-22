package org.ednovo.gooru.suggest.v3.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.search.es.model.SuggestResponse;
import org.ednovo.gooru.suggest.v3.data.provider.model.CollectionDataProviderCriteria;
import org.ednovo.gooru.suggest.v3.data.provider.model.LessonDataProviderCriteria;
import org.ednovo.gooru.suggest.v3.data.provider.model.ResourceDataProviderCriteria;
import org.ednovo.gooru.suggest.v3.data.provider.model.SuggestDataProviderType;
import org.ednovo.gooru.suggest.v3.data.provider.model.TaxonomyDataProviderCriteria;
import org.ednovo.gooru.suggest.v3.data.provider.service.ContainerDataProviderService;
import org.ednovo.gooru.suggest.v3.data.provider.service.ContentDataProviderService;
import org.ednovo.gooru.suggest.v3.data.provider.service.LessonDataProviderService;
import org.ednovo.gooru.suggest.v3.data.provider.service.TaxonomyDataProviderService;
import org.ednovo.gooru.suggest.v3.handler.SuggestHandler;
import org.ednovo.gooru.suggest.v3.model.CollectionContextData;
import org.ednovo.gooru.suggest.v3.model.LessonContextData;
import org.ednovo.gooru.suggest.v3.model.ResourceContextData;
import org.ednovo.gooru.suggest.v3.model.SuggestData;
import org.ednovo.gooru.suggest.v3.model.TaxonomyContextData;
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
	
	@Autowired
	private LessonDataProviderService lessonDataProviderService;
	
	@Autowired
	private TaxonomyDataProviderService taxonomyDataProviderService;
	
	@Override
	public List<SuggestResponse<Object>> suggest(SuggestData suggestData) throws Exception {
		
		ResourceDataProviderCriteria resourceDataProviderCriteria = new ResourceDataProviderCriteria();
		resourceDataProviderCriteria.setResourceId(suggestData.getSuggestContextData().getResourceId());
				
		CollectionDataProviderCriteria collectionDataProviderCriteria = new CollectionDataProviderCriteria();
		collectionDataProviderCriteria.setCollectionId(suggestData.getSuggestContextData().getCollectionId());
		
		LessonDataProviderCriteria lessonDataProviderCriteria = new LessonDataProviderCriteria();
		lessonDataProviderCriteria.setLessonId(suggestData.getSuggestContextData().getLessonId());
		
		TaxonomyDataProviderCriteria taxonomyDataProviderCriteria = new TaxonomyDataProviderCriteria();
		taxonomyDataProviderCriteria.setCodeIds(suggestData.getSuggestContextData().getCodes());
		taxonomyDataProviderCriteria.setIsInternalCode(suggestData.getInputTypeInternalCode());

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
				if (dataProvider.toString().equals(SuggestDataProviderType.LESSON.toString())) {
					LessonContextData lessonData = lessonDataProviderService.getLessonData(lessonDataProviderCriteria);
					if (lessonData != null) {
						dataProviderInput.put(SuggestDataProviderType.LESSON, lessonData);
					}
				}
				if (dataProvider.toString().equals(SuggestDataProviderType.TAXONOMY.toString())) {
					TaxonomyContextData taxonomyData = taxonomyDataProviderService.getTaxonomyData(taxonomyDataProviderCriteria);
					if (taxonomyData != null) {
						dataProviderInput.put(SuggestDataProviderType.TAXONOMY, taxonomyData);
					}
				}
			}
			suggestResList.add(suggestHandler.suggest(suggestData, dataProviderInput));
		}
		return suggestResList;
	}

}
