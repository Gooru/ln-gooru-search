package org.ednovo.gooru.search.es.processor.deserializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.search.domain.service.UnitSearchResult;
import org.ednovo.gooru.search.es.constant.IndexFields;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.UserV2;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.springframework.stereotype.Component;
/**
 * @author Renuka
 * 
 */
@Component
public class UnitDeserializeProcessor extends DeserializeProcessor<List<UnitSearchResult>, UnitSearchResult> {

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.UnitDeserializeProcessor;
	}

	@SuppressWarnings("unchecked")
	@Override
	List<UnitSearchResult> deserialize(Map<String, Object> model, SearchData input, List<UnitSearchResult> output) {
		Map<String, Object> hitsMap = (Map<String, Object>) model.get(SEARCH_HITS);
		List<Map<String, Object>> hits = (List<Map<String, Object>>) (hitsMap).get(SEARCH_HITS);
		output = new ArrayList<UnitSearchResult>();
		for (Map<String, Object> hit : hits) {
			Map<String, Object> fields = (Map<String, Object>) hit.get(SEARCH_SOURCE);
			output.add(collect(fields, input, null));
			
		}
		return output;
	}

	@SuppressWarnings("unchecked")
	@Override
	UnitSearchResult collect(Map<String, Object> model, SearchData input, UnitSearchResult unitResult) {
		if(unitResult == null){
			unitResult = new UnitSearchResult();
		}
		unitResult.setId((String) model.get(IndexFields.ID));
		unitResult.setTitle((String) model.get(IndexFields.TITLE));
		unitResult.setPublishStatus((String) model.get(IndexFields.PUBLISH_STATUS));
		unitResult.setLastModified((String) model.get(IndexFields.UPDATED_AT));
		unitResult.setAddDate((String) model.get(IndexFields.CREATED_AT));
        unitResult.setLastModifiedBy((String) model.get(IndexFields.MODIFIER_ID));
        unitResult.setFormat((String) model.get(IndexFields.CONTENT_FORMAT));

        // set counts
        if(model.get(IndexFields.STATISTICS) != null){
        	Map<String, Object> statistics = (Map<String, Object>) model.get(IndexFields.STATISTICS);
            unitResult.setLessonCount(statistics.get("lessonCount") != null ? (Integer) statistics.get("lessonCount") : 0);
            unitResult.setContainingCollectionCount(statistics.get("containingCollectionsCount") != null ? (Integer) statistics.get("containingCollectionsCount") : 0);
        	unitResult.setCollectionCount(statistics.get("collectionCount") != null ? (Integer) statistics.get("collectionCount") : 0);
        	unitResult.setAssessmentCount(statistics.get("assessmentCount") != null ? (Integer) statistics.get("assessmentCount") : 0);
        	unitResult.setExternalAssessmentCount(statistics.get("externalAssessmentCount") != null ? (Integer) statistics.get("externalAssessmentCount") : 0);
        	unitResult.setIsFeatured(statistics.get("isFeatured") != null ? (Boolean) statistics.get("isFeatured") : false);
        	unitResult.setEfficacy((statistics.get(IndexFields.EFFICACY) != null) ? ((Number) statistics.get(IndexFields.EFFICACY)).doubleValue() : 0.5);
        	unitResult.setEngagement((statistics.get(IndexFields.ENGAGEMENT) != null) ? ((Number) statistics.get(IndexFields.ENGAGEMENT)).doubleValue() : 0.5);
        	unitResult.setRelevance((statistics.get(IndexFields.RELEVANCE) != null) ? ((Number) statistics.get(IndexFields.RELEVANCE)).doubleValue() : 0.5);
    	
			long viewsCount = 0L;
			if (statistics.get(IndexFields.VIEWS_COUNT) != null) {
				viewsCount = ((Number) statistics.get(IndexFields.VIEWS_COUNT)).longValue();
				unitResult.setViewCount(viewsCount);
			}
		}

		// set course
		if (model.get(IndexFields.COURSE) != null) {
			unitResult.setCourse((Map<String, Object>) model.get(IndexFields.COURSE));
		}
		
		// set creator
		if(model.get(IndexFields.CREATOR) != null){
			unitResult.setCreator(setUser((Map<String, Object>) model.get(IndexFields.CREATOR)));
		}

		// set owner
		if(model.get(IndexFields.OWNER) != null){
			unitResult.setOwner(setUser((Map<String, Object>) model.get(IndexFields.OWNER)));
		}

		// set original creator 
		if(model.get(IndexFields.ORIGINAL_CREATOR) != null){
			unitResult.setOrginalCreator(setUser((Map<String, Object>) model.get(IndexFields.ORIGINAL_CREATOR)));
		}

		// set taxonomy
		Map<String, Object> taxonomyMap = (Map<String, Object>) model.get(IndexFields.TAXONOMY);
		if (taxonomyMap != null) {
			Map<String, Object> taxonomySetAsMap = (Map<String, Object>) taxonomyMap.get(IndexFields.TAXONOMY_SET);
			if (input.isCrosswalk()) {
				if (input.getTaxFilterType() != null && TAX_FILTERS.matcher(input.getTaxFilterType()).matches()) {
					setCrosswalkData(input, unitResult, taxonomyMap);
				} else if (input.getUserTaxonomyPreference() != null) {
					long start = System.currentTimeMillis();
					taxonomySetAsMap = transformTaxonomy(taxonomyMap, input);
					logger.debug("Latency of Taxonomy Transformation : {} ms", (System.currentTimeMillis() - start));
				}
			}
			unitResult.setTaxonomy(taxonomySetAsMap);
		}
 		return unitResult;
	}
	
	private UserV2 setUser(Map<String, Object> userData){
		UserV2 user = new UserV2();
		user.setFirstname((String) userData.get(IndexFields.FIRST_NAME));
		user.setLastname((String) userData.get(IndexFields.LAST_NAME));
		user.setUsernameDisplay((String) userData.get(IndexFields.USERNAME));
		user.setId((String) userData.get(IndexFields.USER_ID));
		user.setProfileImage((String) userData.get(IndexFields.PROFILE_IMAGE));
		return user;
	}

}