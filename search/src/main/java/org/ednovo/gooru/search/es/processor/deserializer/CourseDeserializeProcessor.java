package org.ednovo.gooru.search.es.processor.deserializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.search.domain.service.CourseSearchResult;
import org.ednovo.gooru.search.es.constant.IndexFields;
import org.ednovo.gooru.search.es.model.License;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.UserV2;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.springframework.stereotype.Component;

@Component
public class CourseDeserializeProcessor extends DeserializeProcessor<List<CourseSearchResult>, CourseSearchResult> {

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.CourseDeserializeProcessor;
	}

	@SuppressWarnings("unchecked")
	@Override
	List<CourseSearchResult> deserialize(Map<String, Object> model, SearchData input, List<CourseSearchResult> output) {
		Map<String, Object> hitsMap = (Map<String, Object>) model.get(SEARCH_HITS);
		List<Map<String, Object>> hits = (List<Map<String, Object>>) (hitsMap).get(SEARCH_HITS);
		output = new ArrayList<CourseSearchResult>();
		for (Map<String, Object> hit : hits) {
			Map<String, Object> fields = (Map<String, Object>) hit.get(SEARCH_SOURCE);
			output.add(collect(fields, input, null));
			
		}
		return output;
	}

	@SuppressWarnings("unchecked")
	@Override
	CourseSearchResult collect(Map<String, Object> model, SearchData input, CourseSearchResult courseResult) {
		if(courseResult == null){
			courseResult = new CourseSearchResult();
		}
		courseResult.setId((String) model.get(IndexFields.ID));
		courseResult.setTitle((String) model.get(IndexFields.TITLE));
		courseResult.setDescription((String) model.get(IndexFields.DESCRIPTION));
		courseResult.setThumbnail((String) model.get(IndexFields.THUMBNAIL));
		courseResult.setPublishStatus((String) model.get(IndexFields.PUBLISH_STATUS));
		courseResult.setLastModified((String) model.get(IndexFields.UPDATED_AT));
		courseResult.setAddDate((String) model.get(IndexFields.CREATED_AT));
        courseResult.setLastModifiedBy((String) model.get(IndexFields.MODIFIER_ID));
		if (model.containsKey(IndexFields.SEQUENCE)) {
			courseResult.setSequence((Integer) model.get(IndexFields.SEQUENCE));
		}
		if (model.containsKey(IndexFields.SUBJECT_BUCKET)) {
			courseResult.setSubjectBucket((String) model.get(IndexFields.SUBJECT_BUCKET));
		}
		if (model.containsKey(IndexFields.SUBJECT_SEQUENCE)) {
			courseResult.setSubjectSequence((Integer) model.get(IndexFields.SUBJECT_SEQUENCE));
		}
		courseResult.setFormat((String) model.get(IndexFields.CONTENT_FORMAT));

		
        // set counts
        if(model.get(IndexFields.STATISTICS) != null){
        	Map<String, Object> statistics = (Map<String, Object>) model.get(IndexFields.STATISTICS);
            courseResult.setUnitCount(statistics.get(IndexFields.UNIT_COUNT) != null ? (Integer) statistics.get(IndexFields.UNIT_COUNT) : 0);
            courseResult.setCourseRemixCount(statistics.get(IndexFields.COURSE_REMIXCOUNT) != null ? (Integer) statistics.get(IndexFields.COURSE_REMIXCOUNT) : 0); 
            courseResult.setCollaboratorCount(statistics.get(IndexFields.COLLABORATOR_COUNT) != null ? (Integer) statistics.get(IndexFields.COLLABORATOR_COUNT) : 0);
            courseResult.setLessonCount(statistics.get(IndexFields.LESSON_COUNT) != null ? ((Number) statistics.get(IndexFields.LESSON_COUNT)).longValue() : 0);
            courseResult.setContainingCollectionCount(statistics.get(IndexFields.CONTAINING_COLLECTIONS_COUNT) != null ? ((Number) statistics.get(IndexFields.CONTAINING_COLLECTIONS_COUNT)).longValue() : 0L);
            courseResult.setCollectionCount(statistics.get("collectionCount") != null ? ((Number) statistics.get(IndexFields.COLLECTION_COUNT)).longValue() : 0L);
            courseResult.setAssessmentCount(statistics.get(IndexFields.ASSESSMENT_COUNT) != null ? ((Number) statistics.get(IndexFields.ASSESSMENT_COUNT)).longValue() : 0L);
            courseResult.setExternalAssessmentCount(statistics.get(IndexFields.EXTERNAL_ASSESSMENT_COUNT) != null ? ((Number) statistics.get(IndexFields.EXTERNAL_ASSESSMENT_COUNT)).longValue() : 0L);
            courseResult.setIsFeatured(statistics.get(IndexFields.IS_FEATURED) != null ? (Boolean) statistics.get(IndexFields.IS_FEATURED) : false);
            courseResult.setRemixedInClassCount(statistics.get(IndexFields.REMIXED_IN_CLASS_COUNT) != null ? ((Number) statistics.get(IndexFields.REMIXED_IN_CLASS_COUNT)).longValue() : 0L);
            courseResult.setUsedByStudentCount(statistics.get(IndexFields.USED_BY_STUDENT_COUNT) != null ? ((Number) statistics.get(IndexFields.USED_BY_STUDENT_COUNT)).longValue() : 0L);
            
			long viewsCount = 0L;
			if (statistics.get(IndexFields.VIEWS_COUNT) != null) {
				viewsCount = ((Number) statistics.get(IndexFields.VIEWS_COUNT)).longValue();
				courseResult.setViewCount(viewsCount);
			}
        }
        
		// set unitIds
		if (model.get(IndexFields.UNIT_IDS) != null) {
			courseResult.setUnitIds((List<String>) model.get(IndexFields.UNIT_IDS));
		}
		
		// set lessonIds
		if (model.get(IndexFields.LESSON_IDS) != null) {
			courseResult.setLessonIds((List<String>) model.get(IndexFields.LESSON_IDS));
		}

		// set collectionIds
		if (model.get(IndexFields.COLLECTION_IDS) != null) {
			courseResult.setCollectionIds((List<String>) model.get(IndexFields.COLLECTION_IDS));
		}
		
        // set license 
        if(model.get(IndexFields.LICENSE) != null){
        	Map<String, Object> licenseData = (Map<String, Object>) model.get(IndexFields.LICENSE);
        	License license = new License();
        	license.setCode((String) licenseData.get(IndexFields.LICENSE_CODE));
        	license.setDefinition((String) licenseData.get(IndexFields.LICENSE_DEFINITION));
        	license.setIcon((String) licenseData.get(IndexFields.LICENSE_ICON));
        	license.setName((String) licenseData.get(IndexFields.LICENSE_NAME));
        	license.setUrl((String) licenseData.get(IndexFields.LICENSE_URL));
        	courseResult.setLicense(license);
        }
        else {
        	courseResult.setLicense(new License());
        }
        
		// set creator
		if(model.get(IndexFields.CREATOR) != null){
			courseResult.setCreator(setUser((Map<String, Object>) model.get(IndexFields.CREATOR)));
		}

		// set owner
		if(model.get(IndexFields.OWNER) != null){
			courseResult.setOwner(setUser((Map<String, Object>) model.get(IndexFields.OWNER)));
		}

		// set original creator 
		if(model.get(IndexFields.ORIGINAL_CREATOR) != null){
			courseResult.setOriginalCreator(setUser((Map<String, Object>) model.get(IndexFields.ORIGINAL_CREATOR)));
		}

		// set taxonomy
		if(model.get(IndexFields.TAXONOMY) != null){
			Map<String, Object> tax = (Map<String, Object>) model.get(IndexFields.TAXONOMY); 
			courseResult.setTaxonomy((Map<String, Object>) tax.get(IndexFields.TAXONOMY_SET));
		}
		
 		return courseResult;
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
