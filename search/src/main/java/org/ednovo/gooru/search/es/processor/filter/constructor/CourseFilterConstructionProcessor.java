package org.ednovo.gooru.search.es.processor.filter.constructor;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.ednovo.gooru.search.es.model.PublishedStatus;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.ednovo.gooru.search.es.service.SearchSettingService;
import org.springframework.stereotype.Component;

@Component
public class CourseFilterConstructionProcessor extends FilterConstructionProcessor {

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
				searchData.putFilter(FLT_TENANT_ID, searchData.getUserTenantId());
				if (searchData.getFeaturedCourseTenantPreferences() != null) {
					List<String> tenantPermits = new ArrayList<>();
					tenantPermits = processTenantPreferenceSetting(searchData, tenantPermits);
					if (!tenantPermits.isEmpty()) searchData.putFilter(FLT_TENANT_ID, StringUtils.join(tenantPermits, ","));
				}
			}
		} else {
			searchData.putFilter(FLT_PUBLISH_STATUS, PublishedStatus.PUBLISHED.getStatus());
		}
		if(!searchData.getFilters().containsKey(FLT_TENANT_ID)) searchData.putFilter(FLT_TENANT_ID, StringUtils.join(searchData.getUserPermits(), ","));
	}

	private List<String> processTenantPreferenceSetting(SearchData searchData, List<String> tenantPermits) {
		for (String preference : searchData.getFeaturedCourseTenantPreferences()) {
			switch (preference) {
			case TENANT:
				tenantPermits.add(searchData.getUserTenantId());
				break;
			case GLOBAL:
				tenantPermits.addAll(SearchSettingService.getListByName(GLOBAL_TENANT_IDS));
				break;
			case DISCOVERABLE:
				tenantPermits.addAll(SearchSettingService.getListByName(DISCOVERABLE_TENANT_IDS));
				break;
			}
		}
		return tenantPermits;
	}
	
	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.CourseFilterConstruction;
	}
	
}
