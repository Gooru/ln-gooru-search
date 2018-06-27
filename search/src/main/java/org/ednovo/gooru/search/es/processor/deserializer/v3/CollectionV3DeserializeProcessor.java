
package org.ednovo.gooru.search.es.processor.deserializer.v3;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.search.es.constant.IndexFields;
import org.ednovo.gooru.search.es.model.License;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.ednovo.gooru.search.responses.v3.CollectionSearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author SearchTeam
 * 
 */
@Component
public class CollectionV3DeserializeProcessor extends DeserializeV3Processor<List<CollectionSearchResult>, CollectionSearchResult> {

	protected static final Logger logger = LoggerFactory.getLogger(CollectionV3DeserializeProcessor.class);
	public static final String NO_QUESTION = "noQuestion";
	public static final String ONLY_QUESTION = "onlyQuestion";
	public static final String SOME_QUESTION = "someQuestion";
	public static final String SHARING_PUBLIC = "public";
	public static final String SHARING_PRIVATE = "private";

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.SCollectionV3Deserializer;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	List<CollectionSearchResult> deserialize(Map<String, Object> model, SearchData searchData, List<CollectionSearchResult> output) {
		Map<String, Object> hitsMap = (Map<String, Object>) model.get(SEARCH_HITS);
		List<Map<String, Object>> hits = (List<Map<String, Object>>) (hitsMap).get(SEARCH_HITS);
		output = new ArrayList<CollectionSearchResult>();
		List<String> resourceContentIds = new ArrayList<String>();
		for (Map<String, Object> hit : hits) {
			if (hit.isEmpty()) {
				return output;
			}
			Map<String, Object> source = (Map<String, Object>) hit.get(SEARCH_SOURCE);
			CollectionSearchResult scollection = (CollectionSearchResult) collect(source, searchData, null);
			output.add(scollection);
			resourceContentIds.add(scollection.getId());
		}
		searchData.setResourceGooruOIds(resourceContentIds);
		return output;
	}

	@SuppressWarnings("unchecked")
	@Override
	CollectionSearchResult collect(Map<String, Object> model, SearchData searchData, CollectionSearchResult searchResult) {
		CollectionSearchResult output = new CollectionSearchResult();
		output.setContentFormat((String) model.get(IndexFields.CONTENT_FORMAT));
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

				if (metadata.get(IndexFields.AUDIENCE) != null) {
					List<String> audience = metadata.get(IndexFields.AUDIENCE);
					output.setAudience(audience);
				}
				if (metadata.get(IndexFields.DEPTH_OF_KNOWLEDGE) != null) {
					List<String> depthOfknowlede = metadata.get(IndexFields.DEPTH_OF_KNOWLEDGE);
					output.setDepthOfknowledge(depthOfknowlede);
				}
			}
		}
		
		// set creator
		if(model.get(IndexFields.CREATOR) != null){
			output.setCreator(setUser((Map<String, Object>) model.get(IndexFields.CREATOR)));
		}

		// set owner
		if(model.get(IndexFields.OWNER) != null){
			output.setOwner(setUser((Map<String, Object>) model.get(IndexFields.OWNER)));
		}

		// set original creator 
		if(model.get(IndexFields.ORIGINAL_CREATOR) != null){
			output.setOriginalCreator(setUser((Map<String, Object>) model.get(IndexFields.ORIGINAL_CREATOR)));
		}

		if (model.get(IndexFields.MODIFIER_ID) != null) {
			output.setLastModifiedBy((String) model.get(IndexFields.MODIFIER_ID));
		}
		if (model.get(IndexFields.LEARNING_OBJECTIVE) != null) {
			output.setLearningObjective((String) model.get(IndexFields.LEARNING_OBJECTIVE));
		}

		Map<String, Object> licenseMap = (Map<String, Object>) model.get(IndexFields.LICENSE);
		if (licenseMap != null) {
			License license = new License();
			license.setName((String) licenseMap.get(SEARCH_NAME));
			license.setUrl((String) licenseMap.get(SEARCH_URL));
			license.setCode((String) licenseMap.get(SEARCH_CODE));
			license.setIcon((String) licenseMap.get(SEARCH_ICON));
			license.setDefinition((String) licenseMap.get(SEARCH_DEFINITION));
			output.setLicense(license);
		}

		Map<String, Object> statisticsMap = (Map<String, Object>) model.get(IndexFields.STATISTICS);
		if (statisticsMap.get(IndexFields.COLLECTION_REMIX_COUNT) != null) {
			output.setCollectionRemixCount((Integer) statisticsMap.get(IndexFields.COLLECTION_REMIX_COUNT));
		}
		if (statisticsMap.get(IndexFields.QUESTION_COUNT) != null) {
			output.setQuestionCount((Integer) statisticsMap.get(IndexFields.QUESTION_COUNT));
		}
		if (statisticsMap.get(IndexFields.RESOURCE_COUNT) != null) {
			output.setResourceCount((Integer) statisticsMap.get(IndexFields.RESOURCE_COUNT));
		}
		output.setRemixedInCourseCount(statisticsMap.get(IndexFields.REMIXED_IN_COURSE_COUNT) != null ? ((Number) statisticsMap.get(IndexFields.REMIXED_IN_COURSE_COUNT)).longValue() : 0L);
		output.setUsedByStudentCount(statisticsMap.get(IndexFields.USED_BY_STUDENT_COUNT) != null ? ((Number) statisticsMap.get(IndexFields.USED_BY_STUDENT_COUNT)).longValue() : 0L);
        
		output.setEfficacy((statisticsMap.get(IndexFields.EFFICACY) != null) ? ((Number) statisticsMap.get(IndexFields.EFFICACY)).doubleValue() : 0.5);
		output.setEngagement((statisticsMap.get(IndexFields.ENGAGEMENT) != null) ? ((Number) statisticsMap.get(IndexFields.ENGAGEMENT)).doubleValue() : 0.5);
		output.setRelevance((statisticsMap.get(IndexFields.RELEVANCE) != null) ? ((Number) statisticsMap.get(IndexFields.RELEVANCE)).doubleValue() : 0.5);

		output.setId((String) model.get(IndexFields.ID));

		output.setPublishStatus((String) model.get(IndexFields.PUBLISH_STATUS));

		if (model.containsKey(IndexFields.TITLE)) {
			output.setTitle((String) model.get(IndexFields.TITLE));
		}

		List<String> collaboratorIds = (List<String>) model.get(IndexFields.COLLABORATORS_IDS);
		if (collaboratorIds != null && collaboratorIds.size() > 0) {
			output.setCollaboratorIds(collaboratorIds);
		}
		output.setThumbnail((String) model.get(IndexFields.THUMBNAIL));

		output.setLastModified((String) model.get(IndexFields.UPDATED_AT));
		if (model.get(IndexFields.CREATED_AT) != null) {
			output.setCreatedAt((String) model.get(IndexFields.CREATED_AT));
		}
		if (statisticsMap.get(IndexFields.VIEWS_COUNT) != null) {
			output.setViewCount(statisticsMap.get(IndexFields.VIEWS_COUNT) != null ? ((Number) statisticsMap.get(IndexFields.VIEWS_COUNT)).longValue() : 0L);
		}
		if (statisticsMap.get(IndexFields.COLLABORATOR_COUNT) != null) {
			output.setCollaboratorCount((Integer) statisticsMap.get(IndexFields.COLLABORATOR_COUNT));
		}
		output.setResultUId(java.util.UUID.randomUUID().toString());

		Map<String, Object> courseMap = (Map<String, Object>) model.get(IndexFields.COURSE);
		if (courseMap != null && !courseMap.isEmpty()) {
			output.setCourse(courseMap);
		}
		
		Map<String, Object> taxonomyMap = (Map<String, Object>) model.get(IndexFields.TAXONOMY);
		if (taxonomyMap != null) {
			Map<String, Object> taxonomySetAsMap = (Map<String, Object>) taxonomyMap.get(IndexFields.TAXONOMY_SET);
			if (searchData.isCrosswalk()) {
				if (searchData.getTaxFilterType() != null && TAX_FILTERS.matcher(searchData.getTaxFilterType()).matches()) {
					setCrosswalkData(searchData, output, taxonomyMap);
				} else if (searchData.getUserTaxonomyPreference() != null) {
					long start = System.currentTimeMillis();
					taxonomySetAsMap = transformTaxonomy(taxonomyMap, searchData);
					logger.debug("Latency of Taxonomy Transformation : {} ms", (System.currentTimeMillis() - start));
				}
			}
			if (!taxonomySetAsMap.containsKey(IndexFields.TAXONOMY_SET)) cleanUpTaxonomyCurriculumObject(taxonomySetAsMap);
			output.setTaxonomy(taxonomySetAsMap);			
		}

		return output;
	}
	
}
