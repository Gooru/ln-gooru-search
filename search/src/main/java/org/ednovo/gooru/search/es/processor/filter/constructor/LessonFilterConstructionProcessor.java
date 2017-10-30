package org.ednovo.gooru.search.es.processor.filter.constructor;

import org.ednovo.gooru.search.es.model.PublishedStatus;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.springframework.stereotype.Component;

@Component
public class LessonFilterConstructionProcessor extends FilterConstructionProcessor {


	@Override
	public void process(SearchData searchData, SearchResponse<Object> response) {
		super.process(searchData, response);
		if (searchData != null && searchData.getFilters() != null) {
			if (!(searchData.getFilters().containsKey(FLT_PUBLISH_STATUS) || searchData.getFilters().containsKey(FLT_IS_FEATURED))) {
				searchData.putFilter(FLT_PUBLISH_STATUS, PublishedStatus.PUBLISHED.getStatus());
			} else if (searchData.getFilters().containsKey(FLT_IS_FEATURED)) {
				if(Boolean.valueOf(searchData.getFilters().get(FLT_IS_FEATURED).toString())) {
					searchData.putFilter(FLT_PUBLISH_STATUS, PublishedStatus.FEATURED.getStatus());
				} else {
					searchData.putFilter(FLT_PUBLISH_STATUS, PublishedStatus.PUBLISHED.getStatus());
				}
			}
		} else {
			searchData.putFilter(FLT_PUBLISH_STATUS, PublishedStatus.PUBLISHED.getStatus());
		}
	}

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.LessonFilterConstruction;
	}
}