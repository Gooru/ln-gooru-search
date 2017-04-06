package org.ednovo.gooru.search.es.processor.filter.constructor;

import org.apache.commons.lang.StringUtils;
import org.ednovo.gooru.search.es.model.PublishedStatus;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.springframework.stereotype.Component;

@Component
public class CourseFilterConstructionProcessor extends FilterConstructionProcessor {


	@Override
	public void process(SearchData searchData, SearchResponse<Object> response) {
		super.process(searchData, response);
		if(searchData != null && searchData.getFilters() != null) {
			searchData.putFilter(FLT_TENANT_ID, StringUtils.join(searchData.getUserPermits(), ","));	
			if (!(searchData.getFilters().containsKey(FLT_PUBLISH_STATUS) || searchData.getFilters().containsKey(FLT_COURSE_TYPE))) {
				searchData.putFilter(FLT_PUBLISH_STATUS, PublishedStatus.PUBLISHED.getStatus());
			}
			if (searchData.getFilters().containsKey(FLT_COURSE_TYPE)) {
				// searchData.putFilter(IS_FEATURED, 1);
				searchData.putFilter(FLT_PUBLISH_STATUS, "published,featured");
				searchData.getFilters().remove(FLT_COURSE_TYPE);
				searchData.putFilter(FLT_TENANT_ID, searchData.getUserTenantId());
			}
		}
	}

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.CourseFilterConstruction;
	}

}
