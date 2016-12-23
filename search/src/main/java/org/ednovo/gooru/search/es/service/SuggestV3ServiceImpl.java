package org.ednovo.gooru.search.es.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.search.es.handler.SuggestDataProviderType;
import org.ednovo.gooru.search.es.handler.SuggestV2Handler;
import org.ednovo.gooru.search.es.model.SuggestData;
import org.ednovo.gooru.search.es.model.SuggestResponse;
import org.ednovo.gooru.search.model.ClasspageDataProviderCriteria;
import org.ednovo.gooru.search.model.CollectionContextData;
import org.ednovo.gooru.search.model.CollectionDataProviderCriteria;
import org.ednovo.gooru.search.model.ResourceContextData;
import org.ednovo.gooru.search.model.ResourceDataProviderCriteria;
import org.ednovo.gooru.search.model.ResourceUsageData;
import org.ednovo.gooru.search.model.UserActivityDataProviderCriteria;
import org.ednovo.gooru.search.model.UserDataProviderCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SuggestV3ServiceImpl<D> implements SuggestV3Service {
	
	@Autowired
	private SCollectionDataProviderService scollectionDataProviderService;
	
	@Autowired
	private ResourceDataProviderService resourceDataProviderService;
	
	@Autowired
	private UserPreferenceDataProviderService userPreferenceDataProviderService;

	@Autowired
	private UserProficiencyDataProviderService userProficiencyDataProviderService;
	
	@Autowired
	private ActivityStreamDataProviderService userActivityStreamDataProviderService;
	
	@Autowired
	private ClasspagePerformanceDataProviderService userPerformanceResourceDataProviderService;
	
	@Override
	public List<SuggestResponse<Object>> suggest(SuggestData suggestData) throws Exception {
		
		CollectionDataProviderCriteria collectionDataProviderCriteria = new CollectionDataProviderCriteria();
		List<String> collectionIds = new ArrayList<String>();
		collectionIds.add(suggestData.getSuggestV3Context().getId());
		collectionDataProviderCriteria.setCollectionIds(collectionIds);
		
		ResourceDataProviderCriteria resourceDataProviderCriteria = new ResourceDataProviderCriteria();
		String resourceId = suggestData.getSuggestV3Context().getId() != null ? suggestData.getSuggestV3Context().getId() : null;
		resourceDataProviderCriteria.setResourceId(resourceId);
		String resourceCollectionId = suggestData.getSuggestContext().getParentGooruOid() != null ? suggestData.getSuggestContext().getParentGooruOid() : null;
		resourceDataProviderCriteria.setParentId(resourceCollectionId);
		
		UserDataProviderCriteria userDataProviderCriteria = new UserDataProviderCriteria();
		userDataProviderCriteria.setSuggestDataType(suggestData.getType());
		userDataProviderCriteria.setUserUid(suggestData.getUser().getGooruUId());
		
		UserActivityDataProviderCriteria userActivityDataProviderCriteria = new UserActivityDataProviderCriteria();
		userActivityDataProviderCriteria.setUserUid(suggestData.getUser().getGooruUId());
		if(suggestData.getSuggestV3Context().getContext() != null && suggestData.getSuggestV3Context().getContext().equalsIgnoreCase("collection-edit")){
			userActivityDataProviderCriteria.setEventName("item.delete");
		}
		
		ClasspageDataProviderCriteria classpageDataProviderCriteria = new ClasspageDataProviderCriteria();
		if(suggestData.getSuggestContext().getEvent().equalsIgnoreCase("student-suggestion")) {
			classpageDataProviderCriteria.setClassId(suggestData.getSuggestContext().getClassId());
			classpageDataProviderCriteria.setCollectionIds(collectionIds);
			classpageDataProviderCriteria.setAssignmentId(suggestData.getSuggestV3Context().getId());
			classpageDataProviderCriteria.setMemberId(suggestData.getSuggestContext().getStudentId());
		}
		
		if(suggestData.getSuggestV3Context().getUserId() != null){
			userDataProviderCriteria.setUserUid(suggestData.getSuggestV3Context().getUserId());
			userActivityDataProviderCriteria.setUserUid(suggestData.getSuggestV3Context().getUserId());
		}
				
		Map<SuggestDataProviderType, Object> dataProviderInput = new HashMap<SuggestDataProviderType, Object>();
		
		List<SuggestResponse<Object>> suggestResList = new ArrayList<SuggestResponse<Object>>();
		
		List<SuggestV2Handler<Object>> suggestHandlerList = SuggestV2Handler.getSuggester(suggestData.getType().toUpperCase());
		for (SuggestV2Handler<Object> suggestHandler : suggestHandlerList) {
			List<SuggestDataProviderType> dataProviders = suggestHandler.suggestDataProviderTypes();
			for (SuggestDataProviderType dataProvider : dataProviders) {
				if (dataProvider.name().equals(SuggestDataProviderType.SCOLLECTION.name())) {
					List<CollectionContextData> collectionData = scollectionDataProviderService.getCollectionList(collectionDataProviderCriteria);
					if (collectionData.size() > 0) {
						dataProviderInput.put(SuggestDataProviderType.SCOLLECTION, collectionData.get(0));
					}
				}
				if (dataProvider.name().equals(SuggestDataProviderType.USER_RESOURCE_PERFORMANCE.name())) {
					Map<String, ResourceUsageData> userResourcePerformanceData = userPerformanceResourceDataProviderService.getUserResourcePerformanceDataMap(classpageDataProviderCriteria);
					if (userResourcePerformanceData != null) {
						dataProviderInput.put(SuggestDataProviderType.USER_RESOURCE_PERFORMANCE, userResourcePerformanceData);
					}
				}
				if (suggestData.getSuggestContext().getEvent() != null && !suggestData.getSuggestContext().getEvent().equalsIgnoreCase("collection-edit") && dataProvider.name().equals(SuggestDataProviderType.RESOURCE.name())) {
					ResourceContextData resourceData = resourceDataProviderService.getResourceContextData(resourceDataProviderCriteria);
					if (resourceData != null) {
						dataProviderInput.put(SuggestDataProviderType.RESOURCE, resourceData);
					}
				}
				if (dataProvider.name().equals(SuggestDataProviderType.USER_PREFERENCE.name())) {
					dataProviderInput.put(SuggestDataProviderType.USER_PREFERENCE, userPreferenceDataProviderService.getUserPreferenceData(userDataProviderCriteria));
				}
				if (dataProvider.name().equals(SuggestDataProviderType.USER_PROFICIENCY.name())) {
					dataProviderInput.put(SuggestDataProviderType.USER_PROFICIENCY, userProficiencyDataProviderService.getUserProficiencyData(userDataProviderCriteria));
				} 

				if (dataProvider.name().equals(SuggestDataProviderType.USER_ACTIVITY.name())) {
					dataProviderInput.put(SuggestDataProviderType.USER_ACTIVITY, userActivityStreamDataProviderService.getActivityStreamData(userActivityDataProviderCriteria,suggestData));
				}
			}
			suggestResList.add(suggestHandler.suggest(suggestData, dataProviderInput));
		}
		return suggestResList;
	}

}
