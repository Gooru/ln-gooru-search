package org.ednovo.gooru.search.es.processor.deserializer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
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
		long start = System.currentTimeMillis();
		if (input.isFeaturedCourseSearch()) {
			generateFeaturedCourseResponse(hits, input, output);
			logger.debug("Proccessing time for featured courses of both tenants : {} ms", System.currentTimeMillis() - start);
		} else {
			for (Map<String, Object> hit : hits) {
				Map<String, Object> fields = (Map<String, Object>) hit.get(SEARCH_SOURCE);
				output.add(collect(fields, input, null));
			}
			logger.debug("Proccessing time for featured courses of user tenant : {} ms", System.currentTimeMillis() - start);
		}
		return output;
	}

	@SuppressWarnings("unchecked")
	@Override
	CourseSearchResult collect(Map<String, Object> model, SearchData input, CourseSearchResult courseResult) {
		if (courseResult == null) {
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
		if (model.get(IndexFields.STATISTICS) != null) {
			Map<String, Object> statistics = (Map<String, Object>) model.get(IndexFields.STATISTICS);
			courseResult.setUnitCount(statistics.get(IndexFields.UNIT_COUNT) != null ? (Integer) statistics.get(IndexFields.UNIT_COUNT) : 0);
			courseResult.setCourseRemixCount(statistics.get(IndexFields.COURSE_REMIXCOUNT) != null ? (Integer) statistics.get(IndexFields.COURSE_REMIXCOUNT) : 0);
			courseResult.setCollaboratorCount(statistics.get(IndexFields.COLLABORATOR_COUNT) != null ? (Integer) statistics.get(IndexFields.COLLABORATOR_COUNT) : 0);
			courseResult.setLessonCount(statistics.get(IndexFields.LESSON_COUNT) != null ? ((Number) statistics.get(IndexFields.LESSON_COUNT)).longValue() : 0);
			courseResult.setContainingCollectionCount(
					statistics.get(IndexFields.CONTAINING_COLLECTIONS_COUNT) != null ? ((Number) statistics.get(IndexFields.CONTAINING_COLLECTIONS_COUNT)).longValue() : 0L);
			courseResult.setCollectionCount(statistics.get(IndexFields.COLLECTION_COUNT) != null ? ((Number) statistics.get(IndexFields.COLLECTION_COUNT)).longValue() : 0L);
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
		if (model.get(IndexFields.LICENSE) != null) {
			Map<String, Object> licenseData = (Map<String, Object>) model.get(IndexFields.LICENSE);
			License license = new License();
			license.setCode((String) licenseData.get(IndexFields.LICENSE_CODE));
			license.setDefinition((String) licenseData.get(IndexFields.LICENSE_DEFINITION));
			license.setIcon((String) licenseData.get(IndexFields.LICENSE_ICON));
			license.setName((String) licenseData.get(IndexFields.LICENSE_NAME));
			license.setUrl((String) licenseData.get(IndexFields.LICENSE_URL));
			courseResult.setLicense(license);
		} else {
			courseResult.setLicense(new License());
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
		if (model.get(IndexFields.TAXONOMY) != null) {
			Map<String, Object> tax = (Map<String, Object>) model.get(IndexFields.TAXONOMY);
			courseResult.setTaxonomy((Map<String, Object>) tax.get(IndexFields.TAXONOMY_SET));
		}

		return courseResult;
	}

	private UserV2 setUser(Map<String, Object> userData) {
		UserV2 user = new UserV2();
		user.setFirstname((String) userData.get(IndexFields.FIRST_NAME));
		user.setLastname((String) userData.get(IndexFields.LAST_NAME));
		user.setUsernameDisplay((String) userData.get(IndexFields.USERNAME));
		user.setId((String) userData.get(IndexFields.USER_ID));
		user.setProfileImage((String) userData.get(IndexFields.PROFILE_IMAGE));
		return user;
	}

	@SuppressWarnings("unchecked")
	private List<CourseSearchResult> generateFeaturedCourseResponse(List<Map<String, Object>> hits, SearchData input, List<CourseSearchResult> courseResult) {
		Map<String, Object> featured = new HashMap<>();
		featured = aggregateByTenant(hits, input, featured);
		if (!featured.isEmpty()) {
			List<Map<String, Object>> userTenant = (List<Map<String, Object>>) featured.get(input.getUserTenantId());
			Map<String, Object> tenantFeaturedSorted = aggregateBySubjectAndSort(userTenant);

			for (String key : featured.keySet()) {
				if (key.equalsIgnoreCase(input.getUserTenantId())) {
					continue;
				}
				List<Map<String, Object>> openTenant = (List<Map<String, Object>>) featured.get(key);
				Map<String, Object> gooruFeaturedSorted = aggregateBySubjectAndSort(openTenant);
				mergeDiscoverableTenantCourses(tenantFeaturedSorted, gooruFeaturedSorted);
			}

			tenantFeaturedSorted.entrySet().forEach(mergedFeaturedMap -> {
				((List<Map<String, Object>>) mergedFeaturedMap.getValue()).forEach(hit -> {
					courseResult.add(collect(hit, input, null));
				});
			});
		}
		return courseResult;
	}

	@SuppressWarnings("unchecked")
	private void mergeDiscoverableTenantCourses(Map<String, Object> tenantFeaturedSorted, Map<String, Object> openTenantFeaturedSorted) {
		Set<String> extraSubjectsOfOpenTenant = new HashSet<>();
		openTenantFeaturedSorted.keySet().forEach(key -> {
			if (tenantFeaturedSorted.containsKey(key)) {
				List<Map<String, Object>> tenantFeaturedList = (List<Map<String, Object>>) (tenantFeaturedSorted.get(key));
				int numOfCourses = tenantFeaturedList.size();
				List<Map<String, Object>> openTenantFeaturedSortedList = (List<Map<String, Object>>) (openTenantFeaturedSorted.get(key));
				int numOfOpenCourses = openTenantFeaturedSortedList.size();
				for (int index = 0; index < numOfOpenCourses; index++) {
					Map<String, Object> openTenantFeaturedCourseMap = openTenantFeaturedSortedList.get(index);
					openTenantFeaturedCourseMap.put(IndexFields.SEQUENCE, numOfCourses + 1);
					openTenantFeaturedCourseMap.put(IndexFields.SUBJECT_SEQUENCE, tenantFeaturedList.get(index).get(IndexFields.SUBJECT_SEQUENCE));
					tenantFeaturedList.add(openTenantFeaturedCourseMap);
					numOfCourses++;
				}
				tenantFeaturedSorted.put(key, tenantFeaturedList);
			} else {
				extraSubjectsOfOpenTenant.add(key);
			}
		});

		extraSubjectsOfOpenTenant.forEach(key -> {
			int tenantFeaturedListSize = tenantFeaturedSorted.size();
			List<Map<String, Object>> openTenantFeaturedSortedList = (List<Map<String, Object>>) (openTenantFeaturedSorted.get(key));
			List<Map<String, Object>> finalList = new ArrayList<>(openTenantFeaturedSortedList.size());
			for (Map<String, Object> openTenantFeaturedCourseMap : openTenantFeaturedSortedList) {
				openTenantFeaturedCourseMap.put(IndexFields.SUBJECT_SEQUENCE, tenantFeaturedListSize + 1);
				tenantFeaturedListSize++;
				finalList.add(openTenantFeaturedCourseMap);
			}
			tenantFeaturedSorted.put(key, finalList);
		});
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> aggregateByTenant(List<Map<String, Object>> hits, SearchData input, Map<String, Object> featuredByTenantAsMap) {
		hits.forEach(hit -> {
			Map<String, Object> fields = (Map<String, Object>) hit.get(SEARCH_SOURCE);
			Map<String, String> tenant = (Map<String, String>) fields.get(IndexFields.TENANT);
			if (fields.containsKey(IndexFields.SUBJECT_BUCKET)) {
				String tenantId = tenant.get(IndexFields.TENANT_ID);
				if (featuredByTenantAsMap.containsKey(tenantId)) {
					List<Map<String, Object>> courses = (List<Map<String, Object>>) featuredByTenantAsMap.get(tenantId);
					courses.add(fields);
					featuredByTenantAsMap.put(tenantId, courses);
				} else {
					List<Map<String, Object>> courses = new ArrayList<>(1);
					courses.add(fields);
					featuredByTenantAsMap.put(tenantId, courses);
				}
			}
		});
		return featuredByTenantAsMap;
	}

	@SuppressWarnings("unchecked")
	private static Map<String, Object> aggregateBySubjectAndSort(List<Map<String, Object>> tenantFeaturedList) {
		Map<String, Object> featuredSubjectBucketSortedList = new HashMap<>();
		if (tenantFeaturedList != null && tenantFeaturedList.size() > 0) {
			Map<String, Object> featuredBySubjectBucketAsMap = new HashMap<>();
			for (Map<String, Object> featuredTenantMap : tenantFeaturedList) {
				String subjectBucketString = (String) featuredTenantMap.get(IndexFields.SUBJECT_BUCKET);
				String subjectBucket = subjectBucketString.substring(StringUtils.lastOrdinalIndexOf(subjectBucketString, DOT, 2) + 1, subjectBucketString.length());
				if (featuredBySubjectBucketAsMap.containsKey(subjectBucket)) {
					List<Map<String, Object>> subjectBucketAsList = (List<Map<String, Object>>) featuredBySubjectBucketAsMap.get(subjectBucket);
					subjectBucketAsList.add(featuredTenantMap);
					featuredBySubjectBucketAsMap.put(subjectBucket, subjectBucketAsList);
				} else {
					List<Map<String, Object>> subjectBucketAsList = new ArrayList<>();
					subjectBucketAsList.add(featuredTenantMap);
					featuredBySubjectBucketAsMap.put(subjectBucket, subjectBucketAsList);
				}
			}
			for (Entry<String, Object> featuredSubjectBucket : featuredBySubjectBucketAsMap.entrySet()) {
				List<Map<String, Object>> featuredSubjectBucketToSort = (List<Map<String, Object>>) featuredSubjectBucket.getValue();
				Collections.sort(featuredSubjectBucketToSort, new Comparator<Map<String, Object>>() {
					@Override
					public int compare(Map<String, Object> o1, Map<String, Object> o2) {
						return Integer.valueOf(o1.get(IndexFields.SEQUENCE).toString()).compareTo(Integer.valueOf(o2.get(IndexFields.SEQUENCE).toString()));
					}
				});
				featuredSubjectBucketSortedList.put(featuredSubjectBucket.getKey(), featuredSubjectBucketToSort);
			}
		}
		return featuredSubjectBucketSortedList;
	}

}
