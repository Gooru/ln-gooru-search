/**
 * 
 */
package org.ednovo.gooru.search.es.processor.query_builder;

import java.util.HashMap;
import java.util.Map;

import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.ednovo.gooru.search.es.processor.util.FilterBuilderUtils;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @author SearchTeam
 * 
 */
@Component
public class ResourceEsDslQueryBuildProcessor extends EsDslQueryBuildProcessor {

	private void buildMultiQuery(SearchData searchData, StringBuilder multiQueryBuilder) {
		Object boolQuery = FilterBuilderUtils.buildFilters(searchData.getFilters());
		Map<String, Object> boolAsMap = new HashMap<String, Object>(1);
		boolAsMap.put("bool", boolQuery);
		if (boolQuery != null) {
			searchData.getQueryDsl().put("filter", boolAsMap);
		}
		if (multiQueryBuilder.length() > 0) {
			multiQueryBuilder.append("\n{}\n");
		} else {
			multiQueryBuilder.append("{}\n");
		}
		try {
			multiQueryBuilder.append(SERIAILIZER.writeValueAsString(searchData.getQueryDsl().getValues()));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void process(SearchData searchData,
			SearchResponse<Object> response) {
		/*if (searchData.getQueryType() != null && searchData.getQueryType().equalsIgnoreCase(MULTI_CATEGORY)) {
			buildQuery(searchData);
			StringBuilder multiQueryBuilder = new StringBuilder();
			String[] CATEGORY_VALUES = null;
			if(searchData.isBsSearch()){
				CATEGORY_VALUES = BS_CATEGORY_LIST;
			} else {
				CATEGORY_VALUES = CATEGORY_LIST;
			}
			for (String category : CATEGORY_VALUES) {
				searchData.putFilter("&^category", category.toLowerCase());
				buildMultiQuery(searchData, multiQueryBuilder);
			}
			multiQueryBuilder.append("\n{}");
			searchData.setMultiQueryDsl(multiQueryBuilder.toString());
		}  if (searchData.getQueryType() != null && searchData.getQueryType().equalsIgnoreCase(MULTI_RESOURCE_FORMAT)) {
			buildQuery(searchData);
			StringBuilder multiQueryBuilder = new StringBuilder();
			for (String resourceFormat : RESOURCE_FORMAT_LIST) {
				searchData.putFilter("&^resourceFormat", resourceFormat.toLowerCase());
				buildMultiQuery(searchData, multiQueryBuilder);
			}
			multiQueryBuilder.append("\n{}");
			searchData.setMultiQueryDsl(multiQueryBuilder.toString());
		}else if (searchData.getQueryType() != null && searchData.getQueryType().equalsIgnoreCase(MULTI_QUERY)) {

			String queryValues = searchData.getParameters().getString("queryValues");
			String searchBy = searchData.getParameters().getString("searchBy");
			if (queryValues == null) {
				return;
			}
			String[] multiQueryValues = queryValues.toLowerCase().split("~~");

			StringBuilder multiQueryBuilder = new StringBuilder();
			for (String query : multiQueryValues) {
				if (searchData.getQueryType().equals(MULTI_QUERY)) {
					if (searchBy != null && searchBy.equalsIgnoreCase("concept")) {
						searchData.setQueryString("*");
						searchData.putFilter("&^segmentConcepts", query);
					} else {
						searchData.setQueryString(query);
					}
				}
				buildQuery(searchData);
				buildMultiQuery(searchData, multiQueryBuilder);
			}
			multiQueryBuilder.append("\n{}");
			searchData.setMultiQueryDsl(multiQueryBuilder.toString());
		} else {*/
			super.process(searchData, response);
//		}
	}

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.ResourceEsDslQueryBuild;
	}

}