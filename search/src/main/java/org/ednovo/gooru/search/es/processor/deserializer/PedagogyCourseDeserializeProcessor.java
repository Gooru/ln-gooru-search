package org.ednovo.gooru.search.es.processor.deserializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.search.domain.service.PedagogyCourseSearchResult;
import org.ednovo.gooru.search.es.constant.IndexFields;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.springframework.stereotype.Component;
/**
 * @author Renuka
 * 
 */
@Component
public class PedagogyCourseDeserializeProcessor extends PedagogyDeserializeProcessor<List<PedagogyCourseSearchResult>, PedagogyCourseSearchResult> {

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.PedagogyCourseDeserializer;
	}

	@SuppressWarnings("unchecked")
	@Override
	List<PedagogyCourseSearchResult> deserialize(Map<String, Object> model, SearchData input, List<PedagogyCourseSearchResult> output) {
		Map<String, Object> hitsMap = (Map<String, Object>) model.get(SEARCH_HITS);
		List<Map<String, Object>> hits = (List<Map<String, Object>>) (hitsMap).get(SEARCH_HITS);
		output = new ArrayList<PedagogyCourseSearchResult>();
		for (Map<String, Object> hit : hits) {
			Map<String, Object> fields = (Map<String, Object>) hit.get(SEARCH_SOURCE);
			output.add(collect(fields, input, null));
		}
		return output;
	}

	@SuppressWarnings("unchecked")
	@Override
	PedagogyCourseSearchResult collect(Map<String, Object> model, SearchData input, PedagogyCourseSearchResult courseResult) {
		if (courseResult == null) {
			courseResult = new PedagogyCourseSearchResult();
		}
		courseResult.setId((String) model.get(IndexFields.ID));
		courseResult.setTitle((String) model.get(IndexFields.TITLE));
		courseResult.setDescription((String) model.get(IndexFields.DESCRIPTION));
		courseResult.setThumbnail((String) model.get(IndexFields.THUMBNAIL));
		courseResult.setPublishStatus((String) model.get(IndexFields.PUBLISH_STATUS));
		courseResult.setFormat((String) model.get(IndexFields.CONTENT_FORMAT));

		// set counts
		if (model.get(IndexFields.STATISTICS) != null) {
			Map<String, Object> statistics = (Map<String, Object>) model.get(IndexFields.STATISTICS);
			courseResult.setUnitCount(statistics.get(IndexFields.UNIT_COUNT) != null ? (Integer) statistics.get(IndexFields.UNIT_COUNT) : 0);
			courseResult.setCourseRemixCount(statistics.get(IndexFields.COURSE_REMIXCOUNT) != null ? (Integer) statistics.get(IndexFields.COURSE_REMIXCOUNT) : 0);
			courseResult.setCollaboratorCount(statistics.get(IndexFields.COLLABORATOR_COUNT) != null ? (Integer) statistics.get(IndexFields.COLLABORATOR_COUNT) : 0);
			courseResult.setLessonCount(statistics.get(IndexFields.LESSON_COUNT) != null ? ((Number) statistics.get(IndexFields.LESSON_COUNT)).longValue() : 0);
			courseResult.setCollectionCount(statistics.get(IndexFields.COLLECTION_COUNT) != null ? ((Number) statistics.get(IndexFields.COLLECTION_COUNT)).longValue() : 0L);
			courseResult.setAssessmentCount(statistics.get(IndexFields.ASSESSMENT_COUNT) != null ? ((Number) statistics.get(IndexFields.ASSESSMENT_COUNT)).longValue() : 0L);
			courseResult.setExternalAssessmentCount(statistics.get(IndexFields.EXTERNAL_ASSESSMENT_COUNT) != null ? ((Number) statistics.get(IndexFields.EXTERNAL_ASSESSMENT_COUNT)).longValue() : 0L);
			courseResult.setIsFeatured(statistics.get(IndexFields.IS_FEATURED) != null ? (Boolean) statistics.get(IndexFields.IS_FEATURED) : false);
			courseResult.setRemixedInClassCount(statistics.get(IndexFields.REMIXED_IN_CLASS_COUNT) != null ? ((Number) statistics.get(IndexFields.REMIXED_IN_CLASS_COUNT)).longValue() : 0L);
			courseResult.setUsedByStudentCount(statistics.get(IndexFields.USED_BY_STUDENT_COUNT) != null ? ((Number) statistics.get(IndexFields.USED_BY_STUDENT_COUNT)).longValue() : 0L);
			courseResult.setEfficacy((statistics.get(IndexFields.EFFICACY) != null) ? ((Number) statistics.get(IndexFields.EFFICACY)).doubleValue() : 0.5);
			courseResult.setEngagement((statistics.get(IndexFields.ENGAGEMENT) != null) ? ((Number) statistics.get(IndexFields.ENGAGEMENT)).doubleValue() : 0.5);
			courseResult.setRelevance((statistics.get(IndexFields.RELEVANCE) != null) ? ((Number) statistics.get(IndexFields.RELEVANCE)).doubleValue() : 0.5);
		
			long viewsCount = 0L;
			if (statistics.get(IndexFields.VIEWS_COUNT) != null) {
				viewsCount = ((Number) statistics.get(IndexFields.VIEWS_COUNT)).longValue();
				courseResult.setViewCount(viewsCount);
			}
		}

		// set creator
		if (model.get(IndexFields.CREATOR) != null) {
			courseResult.setCreator(setUser((Map<String, Object>) model.get(IndexFields.CREATOR)));
		}

		// set owner
		if (model.get(IndexFields.OWNER) != null) {
			courseResult.setOwner(setUser((Map<String, Object>) model.get(IndexFields.OWNER)));
		}

		// set original creator
		if (model.get(IndexFields.ORIGINAL_CREATOR) != null) {
			courseResult.setOriginalCreator(setUser((Map<String, Object>) model.get(IndexFields.ORIGINAL_CREATOR)));
		}

		// set taxonomy
		Map<String, Object> taxonomyMap = (Map<String, Object>) model.get(IndexFields.TAXONOMY);
		if (taxonomyMap != null) {
			long start = System.currentTimeMillis();
			setTaxonomy(taxonomyMap, input, courseResult);
			logger.debug("Latency of Taxonomy Transformation : {} ms", (System.currentTimeMillis() - start));
		}

		return courseResult;
	}

}
