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
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.ednovo.gooru.search.es.service.SearchSettingService;
import org.ednovo.gooru.search.responses.SearchResponse;
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
		searchData.putFilter(FLT_TENANT_ID, StringUtils.join(searchData.getUserPermits(), COMMA));

		if (searchData.getFilters() != null && searchData.getFilters().containsKey(FLT_COURSE_TYPE)) {
			setTenantDetails(searchData.getUserTenantId(), searchData);
			if (searchData.getFeaturedCourseTenantPreference() == null && searchData.getParentTenantFCVisibility() != null) {
				LOG.debug("Using parent tenant's FC_visibility setting");
				searchData.setFeaturedCourseTenantPreference(searchData.getParentTenantFCVisibility());
			}
			if (searchData.getFeaturedCourseTenantPreference() == null && searchData.getParentTenantFCVisibility() == null) {
				LOG.info("FC visibility not set for user tenant and its parent tenant, setting 'global' as default");
				searchData.setFeaturedCourseTenantPreference(SearchSettingService.getByName(DEFAULT_FC_VISIBILITY));
			}
			LOG.debug("FC visibility : {}", searchData.getFeaturedCourseTenantPreference());
			if(searchData.getFeaturedCourseTenantPreference() != null) processTenantPreferenceSetting(searchData);
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
				searchData.putFilter("&^orFilters", IndexFields.TENANT + DOT + IndexFields.TENANT_ID + COLON + StringUtils.join(tenantPermits, COMMA) + OR_SYMBOL + IndexFields.TENANT + DOT
						+ IndexFields.TENANT_ROOT_ID + COLON + searchData.getUserTenantRootId());
				searchData.getFilters().remove(FLT_TENANT_ID);
			} else {
				searchData.putFilter(FLT_TENANT_ID, StringUtils.join(tenantPermits, COMMA));
			}
			break;
		case GLOBAL:
			searchData.getFilters().remove(FLT_TENANT_ID);
			tenantPermits.add(searchData.getUserTenantId());
			tenantPermits.addAll(SearchSettingService.getListByName(ALL_DISCOVERABLE_TENANT_IDS));
			if (StringUtils.isNotBlank(searchData.getUserTenantRootId()) && !searchData.getUserTenantRootId().equalsIgnoreCase(NULL_STRING)) {
				searchData.putFilter("&^orFilters", IndexFields.TENANT + DOT + IndexFields.TENANT_ID + COLON + StringUtils.join(tenantPermits, COMMA) + OR_SYMBOL + IndexFields.TENANT + DOT
						+ IndexFields.TENANT_ROOT_ID + COLON + searchData.getUserTenantRootId());
				searchData.getFilters().remove(FLT_TENANT_ID);
			} else {
				searchData.putFilter(FLT_TENANT_ID, StringUtils.join(tenantPermits, COMMA));
			}
			break;
		}
	}

	@SuppressWarnings("unchecked")
	private void setTenantDetails(String tenantId, SearchData searchData) {
		SearchData tenant = new SearchData();
		tenant.setPretty(searchData.getPretty());
		tenant.setIndexType(EsIndex.TENANT);
		tenant.putFilter("&^id", tenantId);
		tenant.setQueryString(STAR);
		tenant.setFrom(0);
		tenant.setSize(1);
		List<Map<String, Object>> searchResponse = (List<Map<String, Object>>) SearchHandler.getSearcher(SearchHandlerType.TENANT.name()).search(tenant).getSearchResults();
		if (searchResponse != null && !searchResponse.isEmpty()) {
			Map<?, ?> sourceAsMap = ((Map<?, ?>) searchResponse.get(0).get(IndexFields._SOURCE));
			String parentTenantId = (String) sourceAsMap.get(IndexFields.PARENT_TENANT_ID);
			if (parentTenantId != null) searchData.setUserTenantParentId(parentTenantId);
			List<String> parentTenantIds = (List<String>) sourceAsMap.get(IndexFields.PARENT_TENANT_IDS);
			if (parentTenantIds != null) searchData.setUserTenantParentIds(parentTenantIds);
			String fcVisibility = (String) sourceAsMap.get(IndexFields.FC_VISIBILITY);
			if (fcVisibility != null) searchData.setFeaturedCourseTenantPreference(fcVisibility);
			String parentTenantFCVisibility = (String) sourceAsMap.get(IndexFields.PARENT_TENANT_FC_VISIBILITY);
			if (parentTenantFCVisibility != null) searchData.setParentTenantFCVisibility(parentTenantFCVisibility);
		}
	}
	
	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.TenantFilterConstruction;
	}

}
