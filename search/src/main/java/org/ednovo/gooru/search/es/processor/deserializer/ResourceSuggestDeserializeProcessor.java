package org.ednovo.gooru.search.es.processor.deserializer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.ednovo.gooru.search.es.constant.IndexFields;
import org.ednovo.gooru.search.es.model.ContentFormat;
import org.ednovo.gooru.search.es.model.SuggestResult;
import org.ednovo.gooru.search.es.model.MapWrapper;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.springframework.stereotype.Component;

@Component
public class ResourceSuggestDeserializeProcessor extends SuggestDeserializeProcessor<List<SuggestResult>, SuggestResult>{

	@SuppressWarnings("unchecked")
	@Override
	List<SuggestResult> deserialize(Map<String, Object> model, SearchData searchData, List<SuggestResult> output) {
		output = new ArrayList<SuggestResult>();
		Set<String> contentUrls = new HashSet<String>();
		Set<String> contentTitles = new HashSet<String>();
		if (model != null && model.get(SEARCH_HITS) != null) {
			List<Map<String, Object>> hits = (List<Map<String, Object>>) ((Map<String, Object>) model.get(SEARCH_HITS)).get(SEARCH_HITS);
			List<String> resourceIds = new ArrayList<String>();

			for (Map<String, Object> searchHit : hits) {
				if (searchHit.get(IndexFields._SOURCE) == null) {
					// FIXME, Can this happen?
					continue;
				}
				Map<String, Object> fields = (Map<String, Object>) searchHit.get(IndexFields._SOURCE);
				// Check for Duplicates
				String url = (String) fields.get(IndexFields.URL);
				String title = (String) fields.get(IndexFields.TITLE);
				if (((String) fields.get(IndexFields.CONTENT_FORMAT)) != null && fields.get(IndexFields.CONTENT_FORMAT).equals(ContentFormat.QUESTION.getContentFormat())) {
					Map<String, Object> questionMap = (Map<String, Object>) fields.get(IndexFields.QUESTION);
					if (questionMap != null) {
						title = (String) (questionMap.get(IndexFields.QUESTION));
					}
				}
				MapWrapper<Object> parameters = searchData.getParameters();
				if (parameters != null && parameters.getBoolean(ALLOW_DUPLICATES) != null && !parameters.getBoolean(ALLOW_DUPLICATES)) {
					if (StringUtils.isNotBlank(url)) {
						if (StringUtils.contains(url, "http://www.youtube.com")) {
							url = url.split("&")[0];
						}
						if (contentUrls.contains(url)) {
							continue;
						}
					}
					if (StringUtils.isBlank(title) || contentTitles.contains(title.toLowerCase())) {
						continue;
					}
					contentUrls.add(url);
					contentTitles.add(title.toLowerCase());
				}
				SuggestResult resource = new SuggestResult();
				resource = collect(fields, searchData, resource);
				resourceIds.add(resource.getId());
				output.add(resource);
			}
			searchData.setResourceGooruOIds(resourceIds);
		}
		return output;
	}

	@SuppressWarnings("unchecked")
	@Override
	SuggestResult collect(Map<String, Object> dataMap, SearchData input, SuggestResult resource) {
		resource.setId((String) dataMap.get(IndexFields.ID));
		resource.setFormat((String) dataMap.get(IndexFields.CONTENT_FORMAT));
		resource.setSubformat((String) dataMap.get(IndexFields.CONTENT_SUB_FORMAT));
		resource.setTitle(StringUtils.defaultString((String) dataMap.get(IndexFields.TITLE), ""));
		resource.setThumbnail((String) dataMap.get(IndexFields.THUMBNAIL));
		
		Map<String, Object> taxonomyMap = (Map<String, Object>) dataMap.get(IndexFields.TAXONOMY);
		if (taxonomyMap != null) {
			long start = System.currentTimeMillis();
			Map<String, Object> taxonomy = transformTaxonomy(taxonomyMap, input);
			logger.debug("Latency of Taxonomy Transformation : {} ms", (System.currentTimeMillis() - start));
			if(!taxonomy.isEmpty()) resource.setTaxonomy(taxonomy);
		}
		
		if (dataMap.get(IndexFields.METADATA) != null) {
			Map<String, List<String>> metadata = (Map<String, List<String>>) dataMap.get(IndexFields.METADATA);
			if (metadata != null) {
				Map<String, Object> txMtadata = transformMetadata(metadata);
				resource.setMetadata(txMtadata);
			}
		}
		return resource;
	}

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.ResourceSuggestDeserializer;
	}

}
