package org.ednovo.gooru.search.es.processor.deserializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.ednovo.gooru.search.es.constant.IndexFields;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.ednovo.gooru.search.responses.SimpleSuggestResponse;
import org.springframework.stereotype.Component;

@Component
public class CollectionSuggestDeserializeProcessor extends SuggestDeserializeProcessor<List<SimpleSuggestResponse>, SimpleSuggestResponse>{

	@SuppressWarnings("unchecked")
	@Override
	List<SimpleSuggestResponse> deserialize(Map<String, Object> model, SearchData searchData, List<SimpleSuggestResponse> output) {
		Map<String, Object> hitsMap = (Map<String, Object>) model.get(SEARCH_HITS);
		List<Map<String, Object>> hits = (List<Map<String, Object>>) (hitsMap).get(SEARCH_HITS);
		output = new ArrayList<SimpleSuggestResponse>();
		List<String> collectionIds = new ArrayList<String>();
		for (Map<String, Object> hit : hits) {
			if (hit.isEmpty()) {
				return output;
			}
			Map<String, Object> fields = (Map<String, Object>) hit.get(SEARCH_SOURCE);
			SimpleSuggestResponse collection = new SimpleSuggestResponse();
			collection = collect(fields, searchData, collection);
			output.add(collection);
			collectionIds.add(collection.getId());
		}
		searchData.setResourceGooruOIds(collectionIds);
		return output;
	}

	@SuppressWarnings("unchecked")
	@Override
	SimpleSuggestResponse collect(Map<String, Object> dataMap, SearchData input, SimpleSuggestResponse output) {

		output.setId((String) dataMap.get(IndexFields.ID));
		output.setFormat((String) dataMap.get(IndexFields.CONTENT_FORMAT));
		output.setSubformat((String) dataMap.get(IndexFields.CONTENT_SUB_FORMAT));
		output.setTitle(StringUtils.defaultString((String) dataMap.get(IndexFields.TITLE), ""));
		output.setThumbnail((String) dataMap.get(IndexFields.THUMBNAIL));
		
		Map<String, Object> taxonomyMap = (Map<String, Object>) dataMap.get(IndexFields.TAXONOMY);
		if (taxonomyMap != null) {
			long start = System.currentTimeMillis();
			Map<String, Object> taxonomy = transformTaxonomy(taxonomyMap, input);
			logger.debug("Latency of Taxonomy Transformation : {} ms", (System.currentTimeMillis() - start));
			if(!taxonomy.isEmpty()) output.setTaxonomy(taxonomy);
		}
		
		if (dataMap.get(IndexFields.METADATA) != null) {
			Map<String, List<String>> metadata = (Map<String, List<String>>) dataMap.get(IndexFields.METADATA);
			if (metadata != null) {
				Map<String, Object> txMtadata = transformMetadata(metadata);
				output.setMetadata(txMtadata);
			}
		}
		return output;
	}

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.CollectionSuggestDeserializeProcessor;
	}

}
