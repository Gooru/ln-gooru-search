package org.ednovo.gooru.search.es.processor.deserializer.v3;

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
import org.ednovo.gooru.search.es.constant.IndexFields;
import org.ednovo.gooru.search.es.model.License;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.ednovo.gooru.search.es.service.SearchSettingService;
import org.ednovo.gooru.search.responses.v3.CourseSearchResult;
import org.springframework.stereotype.Component;

@Component
public class CourseV3DeserializeProcessor extends DeserializeV3Processor<List<CourseSearchResult>, CourseSearchResult> {

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.CourseV3DeserializeProcessor;
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
		courseResult.setCreatedAt((String) model.get(IndexFields.CREATED_AT));
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
		courseResult.setContentFormat((String) model.get(IndexFields.CONTENT_FORMAT));

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
		
		// set metadata 
		List<String> audience = null;
		if (model.get(IndexFields.METADATA) != null) {
			Map<String, List<String>> metadata = (Map<String, List<String>>) model.get(IndexFields.METADATA);
			if (metadata != null && metadata.get(IndexFields.AUDIENCE) != null) {
				audience = metadata.get(IndexFields.AUDIENCE);
			}
		}
		courseResult.setAudience(audience);
		
		// set taxonomy
		Map<String, Object> taxonomyMap = (Map<String, Object>) model.get(IndexFields.TAXONOMY);
		if (taxonomyMap != null) {
			Map<String, Object> taxonomySetAsMap = (Map<String, Object>) taxonomyMap.get(IndexFields.TAXONOMY_SET);
			if (input.isCrosswalk()) {
				if (input.getTaxFilterType() != null && TAX_FILTERS.matcher(input.getTaxFilterType()).matches()) {
					setCrosswalkData(input, courseResult, taxonomyMap);
				} else if (input.getUserTaxonomyPreference() != null) {
					long start = System.currentTimeMillis();
					taxonomySetAsMap = transformTaxonomy(taxonomyMap, input);
					logger.debug("Latency of Taxonomy Transformation : {} ms", (System.currentTimeMillis() - start));
				}
			}
			if (!taxonomySetAsMap.containsKey(IndexFields.TAXONOMY_SET)) cleanUpTaxonomyCurriculumObject(taxonomySetAsMap);
			courseResult.setTaxonomy(taxonomySetAsMap);
		}

		return courseResult;
	}

	@SuppressWarnings("unchecked")
	private List<CourseSearchResult> generateFeaturedCourseResponse(List<Map<String, Object>> hits, SearchData input, List<CourseSearchResult> courseResult) {
		Map<String, Object> featured = new HashMap<>();
		featured = aggregateByTenant(hits, input, featured);
		if (!featured.isEmpty()) {
			List<Map<String, Object>> userTenant = (List<Map<String, Object>>) featured.get(input.getUserTenantId());
			Map<String, Object> tenantFeaturedSorted = aggregateBySubjectAndSortByCourse(userTenant);
			featured.remove(input.getUserTenantId());
			if (featured != null && !featured.isEmpty()) {
				List<String> globalTenantIds = SearchSettingService.getListByName(ALL_DISCOVERABLE_TENANT_IDS);
				Map<String, Object> subTenantFcs = new HashMap<>();
				featured.forEach((k, v) -> {
					if (!globalTenantIds.contains(k)) {
						subTenantFcs.put(k, v);
					}
				});
				if (!subTenantFcs.isEmpty()) {
					for (String subTenantId : subTenantFcs.keySet()) {
						tenantFeaturedSorted = sequenceAndMergeTenantCourses(subTenantFcs, tenantFeaturedSorted, subTenantId);
						featured.remove(subTenantId);
					}
				}
			}
			if (featured != null && !featured.isEmpty()) {
				for (String key : featured.keySet()) {
					tenantFeaturedSorted = sequenceAndMergeTenantCourses(featured, tenantFeaturedSorted, key);
				}
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
	private Map<String, Object> sequenceAndMergeTenantCourses(Map<String, Object> featured, Map<String, Object> tenantFeaturedSorted, String tenantId) {
		List<Map<String, Object>> tenant = (List<Map<String, Object>>) featured.get(tenantId);
		Map<String, Object> tenantFcAggAndSortedByC = aggregateBySubjectAndSortByCourse(tenant);
		if (tenantFeaturedSorted.isEmpty()) {
			tenantFeaturedSorted = tenantFcAggAndSortedByC;
		} else {
			tenantFeaturedSorted = mergeNextToTenantCourses(tenantFeaturedSorted, tenantFcAggAndSortedByC);
		}
		return tenantFeaturedSorted;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> mergeNextToTenantCourses(Map<String, Object> tenantFeaturedSorted, Map<String, Object> otherTenantFcSorted) {
		Set<String> extraSubjectsOfOtherTenant = new HashSet<>();
		otherTenantFcSorted.keySet().forEach(key -> {
			if (tenantFeaturedSorted.containsKey(key)) {
				List<Map<String, Object>> tenantFeaturedList = (List<Map<String, Object>>) (tenantFeaturedSorted.get(key));
				int numOfCourses = tenantFeaturedList.size();
				List<Map<String, Object>> otherTenantFCSortedList = (List<Map<String, Object>>) (otherTenantFcSorted.get(key));
				int numOfOpenCourses = otherTenantFCSortedList.size();
				for (int index = 0; index < numOfOpenCourses; index++) {
					Map<String, Object> otherTenantFCMap = otherTenantFCSortedList.get(index);
					otherTenantFCMap.put(IndexFields.SEQUENCE, numOfCourses + 1);
					otherTenantFCMap.put(IndexFields.SUBJECT_SEQUENCE, tenantFeaturedList.get(index).get(IndexFields.SUBJECT_SEQUENCE));
					tenantFeaturedList.add(otherTenantFCMap);
					numOfCourses++;
				}
				tenantFeaturedSorted.put(key, tenantFeaturedList);
			} else {
				extraSubjectsOfOtherTenant.add(key);
			}
		});
		extraSubjectsOfOtherTenant.forEach(key -> {
			int tenantFeaturedListSize = tenantFeaturedSorted.size();
			List<Map<String, Object>> otherTenantFCSortedList = (List<Map<String, Object>>) (otherTenantFcSorted.get(key));
			List<Map<String, Object>> finalList = new ArrayList<>(otherTenantFCSortedList.size());
			int subjectSequence = tenantFeaturedListSize + 1;
			for (Map<String, Object> openTenantFCMap : otherTenantFCSortedList) {
				openTenantFCMap.put(IndexFields.SUBJECT_SEQUENCE, subjectSequence);
				finalList.add(openTenantFCMap);
			}
			tenantFeaturedListSize++;
			tenantFeaturedSorted.put(key, finalList);
		});
		return tenantFeaturedSorted;
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
	private static Map<String, Object> aggregateBySubjectAndSortByCourse(List<Map<String, Object>> tenantFeaturedList) {
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
