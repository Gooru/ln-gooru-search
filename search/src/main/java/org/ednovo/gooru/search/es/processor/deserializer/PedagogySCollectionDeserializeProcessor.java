
package org.ednovo.gooru.search.es.processor.deserializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.search.domain.service.PedagogyCollectionSearchResult;
import org.ednovo.gooru.search.es.constant.IndexFields;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.UserV2;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author SearchTeam
 * 
 */
@Component
public class PedagogySCollectionDeserializeProcessor extends PedagogyDeserializeProcessor<List<PedagogyCollectionSearchResult>, PedagogyCollectionSearchResult> {

	protected static final Logger logger = LoggerFactory.getLogger(PedagogySCollectionDeserializeProcessor.class);
	public static final String NO_QUESTION = "noQuestion";
	public static final String ONLY_QUESTION = "onlyQuestion";
	public static final String SOME_QUESTION = "someQuestion";
	public static final String SHARING_PUBLIC = "public";
	public static final String SHARING_PRIVATE = "private";

	@SuppressWarnings("unchecked")
	@Override
	List<PedagogyCollectionSearchResult> deserialize(Map<String, Object> model, SearchData searchData, List<PedagogyCollectionSearchResult> output) {
		Map<String, Object> hitsMap = (Map<String, Object>) model.get(SEARCH_HITS);
		List<Map<String, Object>> hits = (List<Map<String, Object>>) (hitsMap).get(SEARCH_HITS);
		output = new ArrayList<PedagogyCollectionSearchResult>();
		List<String> resourceContentIds = new ArrayList<String>();
		for (Map<String, Object> hit : hits) {
			if (hit.isEmpty()) {
				return output;
			}
			Map<String, Object> fields = (Map<String, Object>) hit.get(SEARCH_SOURCE);
			PedagogyCollectionSearchResult scollection = (PedagogyCollectionSearchResult) collect(fields, searchData, null);
			output.add(scollection);
			resourceContentIds.add(scollection.getId());
		}
		searchData.setResourceGooruOIds(resourceContentIds);
		return output;
	}

	@SuppressWarnings("unchecked")
	@Override
	PedagogyCollectionSearchResult collect(Map<String, Object> model, SearchData searchData, PedagogyCollectionSearchResult searchResult) {
		PedagogyCollectionSearchResult output = new PedagogyCollectionSearchResult();
		if (model.get(IndexFields.THUMBNAIL) != null) {
			output.setThumbnail((String) model.get(IndexFields.THUMBNAIL));
		}

		if (model.get(IndexFields.METADATA) != null) {
			Map<String, List<String>> metadata = (Map<String, List<String>>) model.get(IndexFields.METADATA);

			if (metadata != null) {
				List<String> grades = metadata.get(IndexFields.GRADE);
				StringBuffer grade = new StringBuffer();
				if (grades != null && grades.size() > 0) {
					for (String g : grades) {
						grade.append(g);
					}
				}
				output.setGrade(grade.toString());
			}
		}

		Map<String, Object> originalCreator = (Map<String, Object>) model.get(IndexFields.ORIGINAL_CREATOR);
		Map<String, Object> creator = (Map<String, Object>) model.get(IndexFields.CREATOR);
		
		if (originalCreator == null && creator != null) {
			originalCreator = creator;
		}
		UserV2 creatorObject = new UserV2();
		if (originalCreator != null) {
			creatorObject = setUser(originalCreator);
		}
		output.setCreator(creatorObject);
		
		Map<String, Object> owner = (Map<String, Object>) model.get(IndexFields.OWNER);
		UserV2 ownerObject = new UserV2();
		if (owner == null && creatorObject != null) {
			ownerObject = creatorObject;
		}
		if (owner != null) {
			ownerObject = setUser(owner);
		}
		output.setUser(ownerObject);
		
		output.setLearningObjective((String) model.get(IndexFields.LEARNING_OBJECTIVE));
		
		Map<String, Object> statisticsMap = (Map<String, Object>) model.get(IndexFields.STATISTICS);
		if (statisticsMap.get(IndexFields.COLLECTION_REMIX_COUNT) != null) {
			output.setCollectionRemixCount((Integer) statisticsMap.get(IndexFields.COLLECTION_REMIX_COUNT));
		}
		if (statisticsMap.get(IndexFields.QUESTION_COUNT) != null) {
			Integer questionCount = (Integer) statisticsMap.get(IndexFields.QUESTION_COUNT);
			output.setQuestionCount(questionCount != null ? questionCount : 0);
		}
		if (statisticsMap.get(IndexFields.RESOURCE_COUNT) != null) {
			Integer resourceCount = (Integer) statisticsMap.get(IndexFields.RESOURCE_COUNT);
			output.setResourceCount(resourceCount != null ? resourceCount : 0);
		}
		output.setRemixedInCourseCount(statisticsMap.get(IndexFields.REMIXED_IN_COURSE_COUNT) != null ? ((Number) statisticsMap.get(IndexFields.REMIXED_IN_COURSE_COUNT)).longValue() : 0L);
		output.setUsedByStudentCount(statisticsMap.get(IndexFields.USED_BY_STUDENT_COUNT) != null ? ((Number) statisticsMap.get(IndexFields.USED_BY_STUDENT_COUNT)).longValue() : 0L);
        
		Integer itemCount = (Integer) statisticsMap.get(IndexFields.CONTENT_COUNT);
		output.setCollectionItemCount(itemCount != null ? itemCount : 0);

		String type = (String) model.get(IndexFields.CONTENT_FORMAT);
		output.setFormat(type);

		output.setId((String) model.get(IndexFields.ID));
		
		output.setPublishStatus((String) model.get(IndexFields.PUBLISH_STATUS));
		output.setIsFeatured(statisticsMap.get(IndexFields.IS_FEATURED) != null ? (Boolean) statisticsMap.get(IndexFields.IS_FEATURED) : false);

		if (model.containsKey(IndexFields.TITLE)) {
			output.setTitle((String) model.get(IndexFields.TITLE));
		}
		
		if (statisticsMap.get(IndexFields.VIEWS_COUNT) != null) {
			output.setViewCount((Integer) statisticsMap.get(IndexFields.VIEWS_COUNT));
		}
		if (statisticsMap.get(IndexFields.COLLABORATOR_COUNT) != null) {
			output.setCollaboratorCount((Integer) statisticsMap.get(IndexFields.COLLABORATOR_COUNT));
		}
		output.setEfficacy((statisticsMap.get(IndexFields.EFFICACY) != null) ? ((Number) statisticsMap.get(IndexFields.EFFICACY)).doubleValue() : 0.5);
		output.setEngagement((statisticsMap.get(IndexFields.ENGAGEMENT) != null) ? ((Number) statisticsMap.get(IndexFields.ENGAGEMENT)).doubleValue() : 0.5);
		output.setRelevance((statisticsMap.get(IndexFields.RELEVANCE) != null) ? ((Number) statisticsMap.get(IndexFields.RELEVANCE)).doubleValue() : 0.5);
	
		Map<String, Object> taxonomyMap = (Map<String, Object>) model.get(IndexFields.TAXONOMY);
		if (taxonomyMap != null) {
			long start = System.currentTimeMillis();
			setTaxonomy(taxonomyMap, searchData, output);
			logger.debug("Latency of Taxonomy Transformation : {} ms", (System.currentTimeMillis() - start));
		}
		return output;
	}

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.PedagogySCollectionDeserializer;
	}
	
}
