package org.ednovo.gooru.search.es.processor.deserializer.v3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.ednovo.gooru.search.es.constant.IndexFields;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.ednovo.gooru.search.es.service.SearchSettingService;
import org.ednovo.gooru.search.responses.Metadata;
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
				output.add(collect(fields, input));
			}
			logger.debug("Proccessing time for featured courses of user tenant : {} ms", System.currentTimeMillis() - start);
		}
		return output;
	}

	@SuppressWarnings("unchecked")
	@Override
	CourseSearchResult collect(Map<String, Object> source, SearchData searchData) {
		
		CourseSearchResult output = new CourseSearchResult();
		output.setId((String) source.get(IndexFields.ID));
		output.setTitle((String) source.get(IndexFields.TITLE));
		output.setDescription((String) source.get(IndexFields.DESCRIPTION));
		
		if (source.containsKey(IndexFields.THUMBNAIL)) {
			String thumbnail = (String) source.get(IndexFields.THUMBNAIL);
			if (thumbnail != null && !thumbnail.startsWith(HTTP)) thumbnail = HTTP + COLON + searchData.getContentCdnUrl() + thumbnail;
			output.setThumbnail(thumbnail);
		}		
		
		output.setPlayerUrl(SearchSettingService.getByName(DNS_ENV) + "/content/courses/play/" + output.getId());

		Date date = null;
		try {
			date = SIMPLE_DATE_FORMAT.parse((String) source.get(IndexFields.UPDATED_AT) + EMPTY_STRING);
		} catch (Exception e) {
			logger.error("modifiedAt field error: {}", e);
		}
		output.setModifiedAt(date);

		Date createdDate = null;
		try {
			createdDate = SIMPLE_DATE_FORMAT.parse((String) source.get(IndexFields.CREATED_AT) + EMPTY_STRING);
		} catch (Exception e) {
			logger.error("createdAt field error: {}", e);
		}
		output.setCreatedAt(createdDate);
			
		Metadata metadata = new Metadata();
/*		if (model.containsKey(IndexFields.SEQUENCE)) {
			courseResult.setSequence((Integer) model.get(IndexFields.SEQUENCE));
		}
		if (model.containsKey(IndexFields.SUBJECT_BUCKET)) {
			courseResult.setSubjectBucket((String) model.get(IndexFields.SUBJECT_BUCKET));
		}
		if (model.containsKey(IndexFields.SUBJECT_SEQUENCE)) {
			courseResult.setSubjectSequence((Integer) model.get(IndexFields.SUBJECT_SEQUENCE));
		}*/

		// set creator
		if (source.get(IndexFields.OWNER) != null) {
			output.setCreator(setUser((Map<String, Object>) source.get(IndexFields.OWNER), searchData));
		}
		
		boolean curated = false;
		Map<String, Object> statisticsMap = (Map<String, Object>) source.get(IndexFields.STATISTICS);
		String publishStatus = (String) source.get(IndexFields.PUBLISH_STATUS);
		if((publishStatus != null && publishStatus.equalsIgnoreCase(PublishedStatus.PUBLISHED.getStatus())) || (statisticsMap.containsKey(IndexFields.IS_FEATURED) && ((Boolean) statisticsMap.get(IndexFields.IS_FEATURED)))) curated = true;
		metadata.setCurated(curated);
		
		// set taxonomy
		Map<String, Object> taxonomyMap = (Map<String, Object>) source.get(IndexFields.TAXONOMY);
		if (taxonomyMap != null) {
			if (searchData.isCrosswalk() && searchData.getUserTaxonomyPreference() != null) {
				long start = System.currentTimeMillis();
				transformTaxonomy(taxonomyMap, searchData, metadata);
				logger.debug("Latency of Taxonomy Transformation : {} ms", (System.currentTimeMillis() - start));
			}
		}

		output.setMetadata(metadata);
		return output;
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
					courseResult.add(collect(hit, input));
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
