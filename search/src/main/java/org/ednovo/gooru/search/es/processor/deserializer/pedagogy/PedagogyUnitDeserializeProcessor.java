package org.ednovo.gooru.search.es.processor.deserializer.pedagogy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.search.es.constant.IndexFields;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.ednovo.gooru.search.responses.PedagogyUnitSearchResult;
import org.springframework.stereotype.Component;
/**
 * @author Renuka
 * 
 */
@Component
public class PedagogyUnitDeserializeProcessor extends PedagogyDeserializeProcessor<List<PedagogyUnitSearchResult>, PedagogyUnitSearchResult> {

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.PedagogyUnitDeserializer;
	}

	@SuppressWarnings("unchecked")
	@Override
	List<PedagogyUnitSearchResult> deserialize(Map<String, Object> model, SearchData input, List<PedagogyUnitSearchResult> output) {
		Map<String, Object> hitsMap = (Map<String, Object>) model.get(SEARCH_HITS);
		List<Map<String, Object>> hits = (List<Map<String, Object>>) (hitsMap).get(SEARCH_HITS);
		output = new ArrayList<PedagogyUnitSearchResult>();
		for (Map<String, Object> hit : hits) {
			Map<String, Object> fields = (Map<String, Object>) hit.get(SEARCH_SOURCE);
			output.add(collect(fields, input, null));
			
		}
		return output;
	}

	@SuppressWarnings("unchecked")
	@Override
	PedagogyUnitSearchResult collect(Map<String, Object> model, SearchData input, PedagogyUnitSearchResult unitResult) {
		if(unitResult == null){
			unitResult = new PedagogyUnitSearchResult();
		}
		unitResult.setId((String) model.get(IndexFields.ID));
		unitResult.setTitle((String) model.get(IndexFields.TITLE));
		unitResult.setPublishStatus((String) model.get(IndexFields.PUBLISH_STATUS));
        unitResult.setFormat((String) model.get(IndexFields.CONTENT_FORMAT));

        // set counts
        if(model.get(IndexFields.STATISTICS) != null){
        	Map<String, Object> statistics = (Map<String, Object>) model.get(IndexFields.STATISTICS);
            unitResult.setLessonCount(statistics.get("lessonCount") != null ? (Integer) statistics.get("lessonCount") : 0);
        	unitResult.setCollectionCount(statistics.get("collectionCount") != null ? (Integer) statistics.get("collectionCount") : 0);
        	unitResult.setAssessmentCount(statistics.get("assessmentCount") != null ? (Integer) statistics.get("assessmentCount") : 0);
        	unitResult.setExternalAssessmentCount(statistics.get("externalAssessmentCount") != null ? (Integer) statistics.get("externalAssessmentCount") : 0);
        	unitResult.setIsFeatured(statistics.get("isFeatured") != null ? (Boolean) statistics.get("isFeatured") : false);
/*        	unitResult.setEfficacy((statistics.get(IndexFields.EFFICACY) != null) ? ((Number) statistics.get(IndexFields.EFFICACY)).doubleValue() : 0.5);
        	unitResult.setEngagement((statistics.get(IndexFields.ENGAGEMENT) != null) ? ((Number) statistics.get(IndexFields.ENGAGEMENT)).doubleValue() : 0.5);
        	unitResult.setRelevance((statistics.get(IndexFields.RELEVANCE) != null) ? ((Number) statistics.get(IndexFields.RELEVANCE)).doubleValue() : 0.5);
		*/ //TODO to be reverted
        	unitResult.setEfficacy(randFloat(0.4F, 0.6F));
        	unitResult.setEngagement(randFloat(0.4F, 0.6F));
        	unitResult.setRelevance(randFloat(0.4F, 0.6F));
			
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
			unitResult.setOriginalCreator(setUser((Map<String, Object>) model.get(IndexFields.ORIGINAL_CREATOR)));
		}

		// set taxonomy
		Map<String, Object> taxonomyMap = (Map<String, Object>) model.get(IndexFields.TAXONOMY);
		if (taxonomyMap != null) {
			long start = System.currentTimeMillis();
			setTaxonomy(taxonomyMap, input, unitResult);
			logger.debug("Latency of Taxonomy Transformation : {} ms", (System.currentTimeMillis() - start));
		}
 		return unitResult;
	}

}