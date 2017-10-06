package org.ednovo.gooru.search.es.processor.filter.constructor;

import org.apache.commons.lang3.StringUtils;
import org.ednovo.gooru.search.es.model.PublishedStatus;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.springframework.stereotype.Component;

@Component
public class RubricFilterConstructionProcessor extends FilterConstructionProcessor {

	@Override
	public void process(SearchData searchData, SearchResponse<Object> response) {
		super.process(searchData, response);
		if (((searchData != null && searchData.getFilters() != null) && (!searchData.getFilters().containsKey(FLT_PUBLISH_STATUS))) || (searchData == null || searchData.getFilters() == null)) {
			searchData.putFilter(FLT_PUBLISH_STATUS, PublishedStatus.PUBLISHED.getStatus());
		}
		searchData.putFilter(FLT_TENANT_ID, StringUtils.join(searchData.getUserPermits(), ","));
	}

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.RubricFilterConstruction;
	}

}
