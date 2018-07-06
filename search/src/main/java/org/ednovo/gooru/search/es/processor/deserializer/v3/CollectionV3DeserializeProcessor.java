
package org.ednovo.gooru.search.es.processor.deserializer.v3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.search.es.constant.IndexFields;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.ednovo.gooru.search.es.service.SearchSettingService;
import org.ednovo.gooru.search.responses.Metadata;
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
		for (Map<String, Object> hit : hits) {
			if (hit.isEmpty()) {
				return output;
			}
			Map<String, Object> source = (Map<String, Object>) hit.get(SEARCH_SOURCE);
			CollectionSearchResult collection = (CollectionSearchResult) collect(source, searchData);
			output.add(collection);
		}
		return output;
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	CollectionSearchResult collect(Map<String, Object> source, SearchData searchData) {
		CollectionSearchResult output = new CollectionSearchResult();

		output.setId((String) source.get(IndexFields.ID));

		if (source.containsKey(IndexFields.TITLE)) {
			output.setTitle((String) source.get(IndexFields.TITLE));
		}
		
		if (source.get(IndexFields.LEARNING_OBJECTIVE) != null) {
			output.setDescription((String) source.get(IndexFields.LEARNING_OBJECTIVE));
		}
		
		String contentFormat = (String) source.get(IndexFields.CONTENT_FORMAT);
		output.setFormat(contentFormat);
		
		if (source.containsKey(IndexFields.URL)) {
			String url = (String) source.get(IndexFields.URL);
			if (!url.startsWith(HTTP)) url = HTTP + COLON + searchData.getContentCdnUrl() + url;
			output.setUrl(url);
			output.setPlayerUrl(url);
		}
		if (!contentFormat.contains(EXTERNAL)) output.setPlayerUrl(SearchSettingService.getByName(DNS_ENV) + "/player/" + output.getId() + "?type=" + contentFormat);

		if (source.containsKey(IndexFields.THUMBNAIL)) {
			String thumbnail = (String) source.get(IndexFields.THUMBNAIL);
			if (!thumbnail.startsWith(HTTP)) thumbnail = HTTP + COLON + searchData.getContentCdnUrl() + thumbnail;
			output.setThumbnail(thumbnail);
		}

		// set creator
		if (source.get(IndexFields.OWNER) != null) {
			output.setCreator(setUser((Map<String, Object>) source.get(IndexFields.OWNER), searchData));
		}

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
		if (source.get(IndexFields.METADATA) != null) {
			Map<String, Object> contentMeta = (Map<String, Object>) source.get(IndexFields.METADATA);
			if (contentMeta != null) {
				//Grade
				List<String> grade = (List<String>) contentMeta.get(IndexFields.GRADE);
				if (grade != null && grade.size() > 0) {
					metadata.setGrade(String.join(COMMA, grade));
				}

				// 21st century skill
				List<Map<String, String>> twentyOneCenturySkills = (List<Map<String, String>>) contentMeta.get(IndexFields.TWENTY_ONE_CENTURY_SKILL);
				if (twentyOneCenturySkills != null && !twentyOneCenturySkills.isEmpty()) {
					metadata.setTwentyOneCenturySkills(twentyOneCenturySkills);
				}
				
				// depth of knowledge
				if (contentFormat.equalsIgnoreCase(TYPE_ASSESSMENT)) {
					List<String> depthOfKnowledge = new ArrayList<>();
					depthOfKnowledge.add("Level 0");
					if (contentMeta.get(IndexFields.DEPTH_OF_KNOWLEDGE) != null) depthOfKnowledge = (List<String>) contentMeta.get(IndexFields.DEPTH_OF_KNOWLEDGE);
					Collections.sort(depthOfKnowledge);
					metadata.setDok(depthOfKnowledge.get(depthOfKnowledge.size() - 1));
				}
			}
		}
		
		if (contentFormat.contains(EXTERNAL)) {
			Map<String, Object> licenseMap = (Map<String, Object>) source.get(IndexFields.LICENSE);
			if (licenseMap != null && !licenseMap.isEmpty()) {
				Map<String, Object> licenseAsMap = new HashMap<>();
				licenseAsMap.put(IndexFields.CODE, licenseMap.get(IndexFields.CODE));
				licenseAsMap.put(IndexFields.URL, licenseMap.get(IndexFields.URL));
				if (!licenseAsMap.isEmpty()) metadata.setLicense(licenseAsMap);
			}
		}

		boolean curated = false;
		Map<String, Object> statisticsMap = (Map<String, Object>) source.get(IndexFields.STATISTICS);
		String publishStatus = (String) source.get(IndexFields.PUBLISH_STATUS);
		if((publishStatus != null && publishStatus.equalsIgnoreCase(PublishedStatus.PUBLISHED.getStatus())) || (statisticsMap.containsKey(IndexFields.IS_FEATURED) && ((Boolean) statisticsMap.get(IndexFields.IS_FEATURED)))) curated = true;
		metadata.setCurated(curated);

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
	
}
