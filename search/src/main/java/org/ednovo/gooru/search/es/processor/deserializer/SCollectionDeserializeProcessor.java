
package org.ednovo.gooru.search.es.processor.deserializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.search.es.constant.IndexFields;
import org.ednovo.gooru.search.es.model.License;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.ednovo.gooru.search.responses.CollectionSearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author SearchTeam
 * 
 */
@Component
public class SCollectionDeserializeProcessor extends DeserializeProcessor<List<CollectionSearchResult>, CollectionSearchResult> {

	protected static final Logger logger = LoggerFactory.getLogger(SCollectionDeserializeProcessor.class);
	public static final String NO_QUESTION = "noQuestion";
	public static final String ONLY_QUESTION = "onlyQuestion";
	public static final String SOME_QUESTION = "someQuestion";
	public static final String SHARING_PUBLIC = "public";
	public static final String SHARING_PRIVATE = "private";

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
		Integer questionCount = 0;
		output.setCollectionType((String) model.get(IndexFields.CONTENT_FORMAT));
		output.setNarrationLink(null);
		output.setNotes(null);
		output.setKeyPoints(null);
		output.setLanguage(null);
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

		Map<String, Object> originalCreator = (Map<String, Object>) model.get(IndexFields.ORIGINAL_CREATOR);
		Map<String, Object> owner = (Map<String, Object>) model.get(IndexFields.OWNER);
		Map<String, Object> creator = (Map<String, Object>) model.get(IndexFields.CREATOR);
		if (originalCreator == null && creator != null) {
			originalCreator = creator;
		}
		if (originalCreator != null) {
			output.setCreatorId((String) originalCreator.get(IndexFields.USER_ID));
			output.setCreatorFirstname((String) originalCreator.get(IndexFields.FIRST_NAME));
			output.setCreatorLastname((String) originalCreator.get(IndexFields.LAST_NAME));
			output.setCreatornameDisplay((String) originalCreator.getOrDefault(IndexFields.USERNAME, null));
			output.setCreatorProfileImage((String) originalCreator.get(IndexFields.PROFILE_IMAGE));
		}
		if (owner == null && creator != null) {
			owner = creator;
		}
		if (owner != null) {
			output.setUsernameDisplay((String) owner.getOrDefault(IndexFields.USERNAME, null));
			output.setUserFirstName((String) owner.get(IndexFields.FIRST_NAME));
			output.setUserLastName((String) owner.get(IndexFields.LAST_NAME));
			output.setGooruUId((String) owner.get(IndexFields.USER_ID));
			output.setUserProfileImage((String) owner.get(IndexFields.PROFILE_IMAGE));
			if (owner.get(IndexFields.PROFILE_VISIBILITY) != null) {
				output.setProfileUserVisibility((Boolean) owner.get(IndexFields.PROFILE_VISIBILITY));
			}
		}

		if (model.get(IndexFields.MODIFIER_ID) != null) {
			output.setLastModifiedBy((String) model.get(IndexFields.MODIFIER_ID));
		}
		if (model.get(IndexFields.LEARNING_OBJECTIVE) != null) {
			output.setDescription((String) model.get(IndexFields.LEARNING_OBJECTIVE));
			output.setGoals((String) model.get(IndexFields.LEARNING_OBJECTIVE));
		}

		Map<String, Object> ratings = null;
		if (model.containsKey(SEARCH_RATINGS) && ((Map<String, Object>) model.get(SEARCH_RATINGS)).size() > 0) {
			ratings = (Map<String, Object>) model.get(SEARCH_RATINGS);
			if (ratings.containsKey(SEARCH_COUNT)) {
				ratings.put("count", ratings.get(SEARCH_COUNT));
			} else {
				ratings.put("count", 0);
			}
			if (ratings.containsKey(SEARCH_AVERAGE)) {
				ratings.put("average", ratings.get(SEARCH_AVERAGE));
			} else {
				ratings.put("average", 0);
			}
		} else {
			ratings = new HashMap<String, Object>();
			ratings.put("count", 0);
			ratings.put("average", 0);
		}
		output.setRatings(ratings);

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

		// String instructionalMethod =(String) model.get(SEARCH_INSTRUCTIONAL_METHOD);
		output.setInstructionalMethod(null);
		// String learningAndInovation =(String) model.get(SEARCH_LEARNING_AND_INOVATION);
		output.setLearningAndInovation(null);
		output.setLanguageObjective((String) model.get(IndexFields.LEARNING_OBJECTIVE));
		// Tags
		if (model.get(SEARCH_CONTENT_TAGS) != null) {
			output.setTags((List<Map<String, Object>>) model.get(SEARCH_CONTENT_TAGS));
		} else {
			List<Map<String, Object>> tagList = new ArrayList<Map<String, Object>>();
			output.setTags(tagList);
		}

		// Skills
		if (model.get(SEARCH_SKILL) != null) {
			output.setSkills((List<Map<String, Object>>) model.get(SEARCH_SKILL));
		} else {
			List<Map<String, Object>> skills = new ArrayList<Map<String, Object>>();
			output.setSkills(skills);
		}

		Map<String, Object> statisticsMap = (Map<String, Object>) model.get(IndexFields.STATISTICS);
		if (statisticsMap.get(IndexFields.COLLECTION_REMIX_COUNT) != null) {
			output.setScollectionRemixCount((Integer) statisticsMap.get(IndexFields.COLLECTION_REMIX_COUNT));
		}
		if (statisticsMap.get(IndexFields.QUESTION_COUNT) != null) {
			questionCount = (Integer) statisticsMap.get(IndexFields.QUESTION_COUNT);
			output.setQuestionCount(String.valueOf(questionCount != null ? questionCount : 0));
		}
		if (statisticsMap.get(IndexFields.RESOURCE_COUNT) != null) {
			Integer resourceCount = (Integer) statisticsMap.get(IndexFields.RESOURCE_COUNT);
			output.setResourceCount(String.valueOf(resourceCount != null ? resourceCount : 0));
		}
		output.setRemixedInCourseCount(statisticsMap.get(IndexFields.REMIXED_IN_COURSE_COUNT) != null ? ((Number) statisticsMap.get(IndexFields.REMIXED_IN_COURSE_COUNT)).longValue() : 0L);
		output.setUsedByStudentCount(statisticsMap.get(IndexFields.USED_BY_STUDENT_COUNT) != null ? ((Number) statisticsMap.get(IndexFields.USED_BY_STUDENT_COUNT)).longValue() : 0L);
        
		Integer itemCount = (Integer) statisticsMap.get(IndexFields.CONTENT_COUNT);
		output.setCollectionItemCount(itemCount != null ? itemCount : 0);
		output.setNumberOfResources(itemCount);
		output.setEfficacy((statisticsMap.get(IndexFields.EFFICACY) != null) ? ((Number) statisticsMap.get(IndexFields.EFFICACY)).doubleValue() : 0.5);
		output.setEngagement((statisticsMap.get(IndexFields.ENGAGEMENT) != null) ? ((Number) statisticsMap.get(IndexFields.ENGAGEMENT)).doubleValue() : 0.5);
		output.setRelevance((statisticsMap.get(IndexFields.RELEVANCE) != null) ? ((Number) statisticsMap.get(IndexFields.RELEVANCE)).doubleValue() : 0.5);
		
		String type = (String) model.get(IndexFields.CONTENT_FORMAT);
		output.setType(type);
		if (type != null) {
			if (type.equalsIgnoreCase(ContentFormat.ASSESSMENT.name())) {
				output.setCategory(ONLY_QUESTION);
			} else if (questionCount == 0) {
				output.setCategory(NO_QUESTION);
			} else {
				output.setCategory(SOME_QUESTION);
			}
		} else {
			output.setCategory(null);
		}

		output.setId((String) model.get(IndexFields.ID));
		String publishedStatus = (String) model.get(IndexFields.PUBLISH_STATUS);

		if (publishedStatus != null && publishedStatus.equalsIgnoreCase(PublishedStatus.PUBLISHED.getStatus())) {
			output.setSharing(SHARING_PUBLIC);
		} else {
			output.setSharing(SHARING_PRIVATE);
		}

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
			output.setAddDate((String) model.get(IndexFields.CREATED_AT));
		}
		if (statisticsMap.get(IndexFields.VIEWS_COUNT) != null) {
			output.setViewCount((Integer) statisticsMap.get(IndexFields.VIEWS_COUNT));
		}
		if (statisticsMap.get(IndexFields.COLLABORATOR_COUNT) != null) {
			output.setCollaboratorCount((Integer) statisticsMap.get(IndexFields.COLLABORATOR_COUNT));
		}
		output.setResultUId(java.util.UUID.randomUUID().toString());

		if (model.get(IndexFields.VISIBLE_ON_PROFILE) != null) {
			output.setProfileUserVisibility((boolean) model.get(IndexFields.VISIBLE_ON_PROFILE));
		}
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
			output.setTaxonomySet(taxonomySetAsMap);			
		}
		if (model.containsKey(IndexFields.URL)) {
			String url = (String) model.get(IndexFields.URL);
			if (!url.startsWith(HTTP)) url = HTTP + COLON + searchData.getContentCdnUrl() + url;
			output.setUrl(url);
		}
		output.setTaskCount(statisticsMap.get(IndexFields.OA_TASK_COUNT) != null ? ((Number) statisticsMap.get(IndexFields.OA_TASK_COUNT)).longValue() : null);

		return output;
	}

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.SCollectionDeserializer;
	}
	
}
