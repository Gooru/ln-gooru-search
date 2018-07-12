package org.ednovo.gooru.search.es.processor.filter.constructor;

import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.ednovo.gooru.search.responses.SearchResponse;
import org.springframework.stereotype.Component;

@Component
public class CourseFilterConstructionProcessor extends ContentFilterConstructionProcessor {

	@Override
	public void process(SearchData searchData, SearchResponse<Object> response) {
		super.process(searchData, response);
		if(searchData != null && searchData.getFilters() != null) {
			if (!(searchData.getFilters().containsKey(FLT_PUBLISH_STATUS) || searchData.getFilters().containsKey(FLT_COURSE_TYPE))) {
				searchData.putFilter(FLT_PUBLISH_STATUS, PublishedStatus.PUBLISHED.getStatus());
			} else if (searchData.getFilters().containsKey(FLT_COURSE_TYPE)) {
				searchData.setFeaturedCourseSearch(true);
				searchData.putFilter(FLT_PUBLISH_STATUS, PublishedStatus.PUBLISHED.getStatus() + COMMA + PublishedStatus.FEATURED.getStatus());
				searchData.getFilters().remove(FLT_COURSE_TYPE);
			}
		} else {
			searchData.putFilter(FLT_PUBLISH_STATUS, PublishedStatus.PUBLISHED.getStatus());
		}
	}

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.CourseFilterConstruction;
	}
	
}
