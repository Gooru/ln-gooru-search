package org.ednovo.gooru.search.es.processor.deserializer.v3;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.search.es.constant.IndexFields;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.ednovo.gooru.search.es.service.SearchSettingService;
import org.ednovo.gooru.search.responses.Metadata;
import org.ednovo.gooru.search.responses.v3.LessonSearchResult;
import org.springframework.stereotype.Component;
/**
 * @author Renuka
 * 
 */
@Component
public class LessonV3DeserializeProcessor extends DeserializeV3Processor<List<LessonSearchResult>, LessonSearchResult> {

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.LessonV3DeserializeProcessor;
	}

	@SuppressWarnings("unchecked")
	@Override
	List<LessonSearchResult> deserialize(Map<String, Object> model, SearchData input, List<LessonSearchResult> output) {
		Map<String, Object> hitsMap = (Map<String, Object>) model.get(SEARCH_HITS);
		List<Map<String, Object>> hits = (List<Map<String, Object>>) (hitsMap).get(SEARCH_HITS);
		output = new ArrayList<LessonSearchResult>();
		for (Map<String, Object> hit : hits) {
			Map<String, Object> fields = (Map<String, Object>) hit.get(SEARCH_SOURCE);
			output.add(collect(fields, input));
			
		}
		return output;
	}

	@SuppressWarnings("unchecked")
	@Override
	LessonSearchResult collect(Map<String, Object> source, SearchData searchData) {
		LessonSearchResult output = new LessonSearchResult();
		output.setId((String) source.get(IndexFields.ID));
		output.setTitle((String) source.get(IndexFields.TITLE));
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

		Map<String, String> course = (Map<String, String>) source.get(IndexFields.COURSE);
		Map<String, String> unit = (Map<String, String>) source.get(IndexFields.UNIT);
		output.setPlayerUrl(SearchSettingService.getByName(DNS_ENV) + "/content/courses/play/" + course.get(IndexFields.ID) + "?lessonId=" + output.getId() + "&unitId=" + unit.get(IndexFields.ID));

		output.setPlayerUrl(SearchSettingService.getByName(DNS_ENV) + "/content/lessons/play/" + output.getId());

		// set creator
		if(source.get(IndexFields.CREATOR) != null){
			output.setCreator(setUser((Map<String, Object>) source.get(IndexFields.CREATOR), searchData));
		}

		Metadata metadata = new Metadata();
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

}
