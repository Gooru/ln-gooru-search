package org.ednovo.gooru.search.es.processor.filter.constructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.ednovo.gooru.search.es.constant.Constants;
import org.ednovo.gooru.search.es.constant.EsIndex;
import org.ednovo.gooru.search.es.handler.SearchHandler;
import org.ednovo.gooru.search.es.handler.SearchHandlerType;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.ednovo.gooru.search.es.service.SearchSettingService;
import org.springframework.stereotype.Component;

@Component
public class TenantFilterConstructionProcessor extends FilterConstructionProcessor {

	@SuppressWarnings("unchecked")
	@Override
	public void process(SearchData searchData, SearchResponse<Object> response) {

		// add user tenant
		Set<String> userPermits = new HashSet<>();
		userPermits.add(searchData.getUserTenantId());

		// add user tenant's parents
		SearchData tenant = new SearchData();
		tenant.setPretty(searchData.getPretty());
		tenant.setIndexType(EsIndex.TENANT);
		tenant.putFilter("&^id", searchData.getUserTenantId());
		tenant.setQueryString("*");
		tenant.setFrom(0);
		tenant.setSize(1);
		List<Map<String, Object>> searchResponse = (List<Map<String, Object>>) SearchHandler.getSearcher(SearchHandlerType.TENANT.name()).search(tenant).getSearchResults();
		if (searchResponse != null && !searchResponse.isEmpty()) {
			List<String> parentTenantIds = (List<String>) ((Map<?, ?>) searchResponse.get(0).get("_source")).get("parentTenantIds");
			if (parentTenantIds != null && parentTenantIds.size() > 0) {
				userPermits.addAll(parentTenantIds);
				searchData.setUserTenantParentIds(parentTenantIds);
			}
		}

		// add all discoverable tenants
		List<String> allDiscoverableTenantIds = SearchSettingService.getAllDiscoverableTenantIds(Constants.ALL_DISCOVERABLE_TENANT_IDS);
		if (allDiscoverableTenantIds != null && !allDiscoverableTenantIds.isEmpty()) {
			userPermits.addAll(allDiscoverableTenantIds);
		}
		searchData.setUserPermits(userPermits.stream().distinct().collect(Collectors.toList()));

		if (searchData.getFilters() != null && !searchData.getFilters().containsKey(FLT_COURSE_TYPE)) {
			searchData.putFilter(FLT_TENANT_ID, StringUtils.join(searchData.getUserPermits(), ","));
		} else {
			searchData.setFeaturedCourseTenantPreferences(SearchSettingService.getFeaturedCourseTenantPreference(searchData.getUserTenantId()));
			List<String> userTenantPermits = new ArrayList<>();
			userTenantPermits.add(searchData.getUserTenantId());
			if (searchData.getUserTenantParentIds() != null) userTenantPermits.addAll(searchData.getUserTenantParentIds());
			searchData.putFilter(FLT_TENANT_ID, StringUtils.join(userTenantPermits, ","));
			if (searchData.getFeaturedCourseTenantPreferences() != null) {
				List<String> tenantPreferences = new ArrayList<>();
				tenantPreferences = processTenantPreferenceSetting(searchData, tenantPreferences);
				if (!tenantPreferences.isEmpty())
					searchData.putFilter(FLT_TENANT_ID, StringUtils.join(tenantPreferences, ","));
			}
		}
	}

	private List<String> processTenantPreferenceSetting(SearchData searchData, List<String> tenantPermits) {
		for (String preference : searchData.getFeaturedCourseTenantPreferences()) {
			switch (preference) {
			case TENANT:
				tenantPermits.add(searchData.getUserTenantId());
				if (searchData.getUserTenantParentIds() != null) tenantPermits.addAll(searchData.getUserTenantParentIds());
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
		return SearchProcessorType.TenantFilterConstruction;
	}
}
