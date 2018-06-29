package org.ednovo.gooru.search.es.processor.deserializer.v3;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.search.es.constant.IndexFields;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.ednovo.gooru.search.responses.v3.LessonSearchResult;
import org.springframework.stereotype.Component;
/**
 * @author Renuka
 * 
 */
@Component
public class LessonV3DeserializeProcessor extends DeserializeV3Processor<List<LessonSearchResult>, LessonSearchResult> {

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.LessonV3DeserializeProcessor;
	}

	@SuppressWarnings("unchecked")
	@Override
	List<LessonSearchResult> deserialize(Map<String, Object> model, SearchData input, List<LessonSearchResult> output) {
		Map<String, Object> hitsMap = (Map<String, Object>) model.get(SEARCH_HITS);
		List<Map<String, Object>> hits = (List<Map<String, Object>>) (hitsMap).get(SEARCH_HITS);
		output = new ArrayList<LessonSearchResult>();
		for (Map<String, Object> hit : hits) {
			Map<String, Object> fields = (Map<String, Object>) hit.get(SEARCH_SOURCE);
			output.add(collect(fields, input, null));
			
		}
		return output;
	}

	@SuppressWarnings("unchecked")
	@Override
	LessonSearchResult collect(Map<String, Object> model, SearchData input, LessonSearchResult lessonResult) {
		if(lessonResult == null){
			lessonResult = new LessonSearchResult();
		}
		lessonResult.setId((String) model.get(IndexFields.ID));
		lessonResult.setTitle((String) model.get(IndexFields.TITLE));
		lessonResult.setPublishStatus((String) model.get(IndexFields.PUBLISH_STATUS));
		lessonResult.setLastModified((String) model.get(IndexFields.UPDATED_AT));
		lessonResult.setCreatedAt((String) model.get(IndexFields.CREATED_AT));
        lessonResult.setLastModifiedBy((String) model.get(IndexFields.MODIFIER_ID));
        lessonResult.setContentFormat((String) model.get(IndexFields.CONTENT_FORMAT));

        // set counts
        if(model.get(IndexFields.STATISTICS) != null){
        	Map<String, Object> statistics = (Map<String, Object>) model.get(IndexFields.STATISTICS);
        	lessonResult.setCollectionCount(statistics.get("collectionCount") != null ? (Integer) statistics.get("collectionCount") : 0);
        	lessonResult.setAssessmentCount(statistics.get("assessmentCount") != null ? (Integer) statistics.get("assessmentCount") : 0);
        	lessonResult.setExternalAssessmentCount(statistics.get("externalAssessmentCount") != null ? (Integer) statistics.get("externalAssessmentCount") : 0);
        	lessonResult.setIsFeatured(statistics.get("isFeatured") != null ? (Boolean) statistics.get("isFeatured") : false);
        	lessonResult.setEfficacy((statistics.get(IndexFields.EFFICACY) != null) ? ((Number) statistics.get(IndexFields.EFFICACY)).doubleValue() : 0.5);
        	lessonResult.setEngagement((statistics.get(IndexFields.ENGAGEMENT) != null) ? ((Number) statistics.get(IndexFields.ENGAGEMENT)).doubleValue() : 0.5);
        	lessonResult.setRelevance((statistics.get(IndexFields.RELEVANCE) != null) ? ((Number) statistics.get(IndexFields.RELEVANCE)).doubleValue() : 0.5);
    	
			long viewsCount = 0L;
			if (statistics.get(IndexFields.VIEWS_COUNT) != null) {
				viewsCount = ((Number) statistics.get(IndexFields.VIEWS_COUNT)).longValue();
				lessonResult.setViewCount(viewsCount);
			}
		}

		// set unit
		if (model.get(IndexFields.UNIT) != null) {
			lessonResult.setUnit((Map<String, Object>) model.get(IndexFields.UNIT));
		}

		// set course
		if (model.get(IndexFields.COURSE) != null) {
			lessonResult.setCourse((Map<String, Object>) model.get(IndexFields.COURSE));
		}

		// set creator
		if(model.get(IndexFields.CREATOR) != null){
			lessonResult.setCreator(setUser((Map<String, Object>) model.get(IndexFields.CREATOR), input));
		}

		// set owner
		if(model.get(IndexFields.OWNER) != null){
			lessonResult.setOwner(setUser((Map<String, Object>) model.get(IndexFields.OWNER), input));
		}

		// set original creator 
		if(model.get(IndexFields.ORIGINAL_CREATOR) != null){
			lessonResult.setOriginalCreator(setUser((Map<String, Object>) model.get(IndexFields.ORIGINAL_CREATOR), input));
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
			lessonResult.setTaxonomy(taxonomySetAsMap);
		}
		
 		return lessonResult;
	}

}
