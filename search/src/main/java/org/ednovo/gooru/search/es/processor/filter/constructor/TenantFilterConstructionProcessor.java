package org.ednovo.gooru.search.es.processor.filter.constructor;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.ednovo.gooru.search.es.constant.Constants;
import org.ednovo.gooru.search.es.constant.EsIndex;
import org.ednovo.gooru.search.es.constant.IndexFields;
import org.ednovo.gooru.search.es.handler.SearchHandler;
import org.ednovo.gooru.search.es.handler.SearchHandlerType;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.ednovo.gooru.search.es.service.SearchSettingService;
import org.springframework.stereotype.Component;

@Component
public class TenantFilterConstructionProcessor extends FilterConstructionProcessor {

	@Override
	public void process(SearchData searchData, SearchResponse<Object> response) {
		super.process(searchData, response);

		// add user tenant
		Set<String> userPermits = new HashSet<>();
		userPermits.add(searchData.getUserTenantId());

		// add all discoverable tenants
		List<String> allDiscoverableTenantIds = SearchSettingService.getAllDiscoverableTenantIds(Constants.ALL_DISCOVERABLE_TENANT_IDS);
		if (allDiscoverableTenantIds != null && !allDiscoverableTenantIds.isEmpty()) {
			userPermits.addAll(allDiscoverableTenantIds);
		}
		searchData.setUserPermits(userPermits.stream().distinct().collect(Collectors.toList()));
		searchData.putFilter(FLT_TENANT_ID, StringUtils.join(searchData.getUserPermits(), ","));

		if (searchData.getFilters() != null && searchData.getFilters().containsKey(FLT_COURSE_TYPE)) {
			searchData.putFilter(FLT_TENANT_ID, searchData.getUserTenantId());
			searchData.setFeaturedCourseTenantPreference(SearchSettingService.getFeaturedCourseTenantPreference(searchData.getUserTenantId()));
			if (searchData.getFeaturedCourseTenantPreference() != null) processTenantPreferenceSetting(searchData);
		}
	}

	private void processTenantPreferenceSetting(SearchData searchData) {
		Set<String> tenantPermits = new HashSet<>();
		switch (searchData.getFeaturedCourseTenantPreference()) {
		case TENANT:
			searchData.putFilter(FLT_TENANT_ID, searchData.getUserTenantId());
			break;
		case TENANT_TREE:
			tenantPermits.add(searchData.getUserTenantId());
			if (StringUtils.isNotBlank(searchData.getUserTenantRootId()) && !searchData.getUserTenantRootId().equalsIgnoreCase(NULL_STRING)) {
				searchData.putFilter("&^orFilters", IndexFields.TENANT + DOT + IndexFields.TENANT_ID + COLON + StringUtils.join(tenantPermits, ",") + OR_SYMBOL + IndexFields.TENANT + DOT
						+ IndexFields.TENANT_ROOT_ID + COLON + searchData.getUserTenantRootId());
				searchData.getFilters().remove(FLT_TENANT_ID);
			} else {
				searchData.putFilter(FLT_TENANT_ID, StringUtils.join(tenantPermits, ","));
			}
			break;
		case GLOBAL:
			searchData.getFilters().remove(FLT_TENANT_ID);
			tenantPermits.add(searchData.getUserTenantId());
			tenantPermits.addAll(SearchSettingService.getListByName(ALL_DISCOVERABLE_TENANT_IDS));
			if (StringUtils.isNotBlank(searchData.getUserTenantRootId()) && !searchData.getUserTenantRootId().equalsIgnoreCase(NULL_STRING)) {
				searchData.putFilter("&^orFilters", IndexFields.TENANT + DOT + IndexFields.TENANT_ID + COLON + StringUtils.join(tenantPermits, ",") + OR_SYMBOL + IndexFields.TENANT + DOT
						+ IndexFields.TENANT_ROOT_ID + COLON + searchData.getUserTenantRootId());
				searchData.getFilters().remove(FLT_TENANT_ID);
			} else {
				searchData.putFilter(FLT_TENANT_ID, StringUtils.join(tenantPermits, ","));
			}
			break;
		}
	}

	@SuppressWarnings("unchecked")
	private void setFCVisibility(SearchData searchData) {
		SearchData tenant = new SearchData();
		tenant.setPretty(searchData.getPretty());
		tenant.setIndexType(EsIndex.TENANT);
		tenant.putFilter("&^id", searchData.getUserTenantId());
		tenant.setQueryString("*");
		tenant.setFrom(0);
		tenant.setSize(1);
		List<Map<String, Object>> searchResponse = (List<Map<String, Object>>) SearchHandler.getSearcher(SearchHandlerType.TENANT.name()).search(tenant).getSearchResults();
		if (searchResponse != null && !searchResponse.isEmpty()) {
			String fcVisibility = (String) ((Map<?, ?>) searchResponse.get(0).get("_source")).get("fcVisibility");
			if (fcVisibility != null) {
				searchData.setFeaturedCourseTenantPreference(fcVisibility);
			}
		}
	}
	
	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.TenantFilterConstruction;
	}

}
