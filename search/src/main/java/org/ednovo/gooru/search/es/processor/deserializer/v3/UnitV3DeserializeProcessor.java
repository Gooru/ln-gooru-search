package org.ednovo.gooru.search.es.processor.deserializer.v3;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.search.es.constant.IndexFields;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.ednovo.gooru.search.responses.v3.UnitSearchResult;
import org.springframework.stereotype.Component;
/**
 * @author Renuka
 * 
 */
@Component
public class UnitV3DeserializeProcessor extends DeserializeV3Processor<List<UnitSearchResult>, UnitSearchResult> {

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.UnitV3DeserializeProcessor;
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
		unitResult.setCreatedAt((String) model.get(IndexFields.CREATED_AT));
        unitResult.setLastModifiedBy((String) model.get(IndexFields.MODIFIER_ID));
        unitResult.setContentFormat((String) model.get(IndexFields.CONTENT_FORMAT));

        // set counts
        if(model.get(IndexFields.STATISTICS) != null){
        	Map<String, Object> statistics = (Map<String, Object>) model.get(IndexFields.STATISTICS);
            unitResult.setLessonCount(statistics.get("lessonCount") != null ? (Integer) statistics.get("lessonCount") : 0);
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
			unitResult.setCreator(setUser((Map<String, Object>) model.get(IndexFields.CREATOR), input));
		}

		// set owner
		if(model.get(IndexFields.OWNER) != null){
			unitResult.setOwner(setUser((Map<String, Object>) model.get(IndexFields.OWNER), input));
		}

		// set original creator 
		if(model.get(IndexFields.ORIGINAL_CREATOR) != null){
			unitResult.setOriginalCreator(setUser((Map<String, Object>) model.get(IndexFields.ORIGINAL_CREATOR), input));
		}

		// set taxonomy
		Map<String, Object> taxonomyMap = (Map<String, Object>) model.get(IndexFields.TAXONOMY);
		if (taxonomyMap != null) {
			Map<String, Object> taxonomySetAsMap = (Map<String, Object>) taxonomyMap.get(IndexFields.TAXONOMY_SET);
			if (input.isCrosswalk() && input.getUserTaxonomyPreference() != null) {
				long start = System.currentTimeMillis();
				taxonomySetAsMap = transformTaxonomy(taxonomyMap, input);
				logger.debug("Latency of Taxonomy Transformation : {} ms", (System.currentTimeMillis() - start));
			}
			if (!taxonomySetAsMap.containsKey(IndexFields.TAXONOMY_SET)) cleanUpTaxonomyCurriculumObject(taxonomySetAsMap);
			unitResult.setTaxonomy(taxonomySetAsMap);
		}
 		return unitResult;
	}

}