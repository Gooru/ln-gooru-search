package org.ednovo.gooru.search.es.processor.filter.constructor;

import org.ednovo.gooru.search.es.constant.IndexFields;
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
			String audience = null;
			if (searchData.getFilters().containsKey(AMPERSAND_AUDIENCE)) audience = (String) searchData.getFilters().get(AMPERSAND_AUDIENCE);
			if (audience == null || (audience != null && !audience.equalsIgnoreCase(AUDIENCE_TEACHERS))) {
				if (audience != null && audience.contains(AUDIENCE_TEACHERS) && audience.contains(AUDIENCE_ALL_STUDENTS) && audience.trim().length() == 21) {
					searchData.getFilters().remove(AMPERSAND_AUDIENCE);
				} else {
					searchData.putFilter(NOT_SYMBOL + CARET_SYMBOL + IndexFields.AUDIENCE, AUDIENCE_TEACHERS);
					if (audience != null) searchData.getFilters().remove(AMPERSAND_AUDIENCE);
				}
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
