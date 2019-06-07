package org.ednovo.gooru.search.es.processor.filter.constructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.ednovo.gooru.search.es.constant.Constants;
import org.ednovo.gooru.search.es.constant.EsIndex;
import org.ednovo.gooru.search.es.constant.IndexFields;
import org.ednovo.gooru.search.es.handler.SearchHandler;
import org.ednovo.gooru.search.es.handler.SearchHandlerType;
import org.ednovo.gooru.search.es.model.MapWrapper;
import org.ednovo.gooru.search.es.model.Scope;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.ednovo.gooru.search.es.service.SearchSettingService;
import org.ednovo.gooru.search.responses.CourseSearchResult;
import org.ednovo.gooru.search.responses.SearchResponse;
import org.springframework.stereotype.Component;

@Component
public class ScopeFilterConstructionProcessor extends FilterConstructionProcessor {

    @Override
    public void process(SearchData searchData, SearchResponse<Object> response) {
        super.process(searchData, response);
        Scope scope = searchData.getScope();
        if (scope != null && scope.getKey() != null && SCOPE_MATCH.matcher(scope.getKey()).matches()) {
            processScope(searchData, scope.getKey(), scope);
        }
    }

    private void processScope(SearchData searchData, String target, Scope scope) {
		switch (target) {
		case "my-content":
			if (!searchData.getFilters().containsKey(AMPERSAND_CREATOR_ID)) {
				searchData.putFilter("&^orFilters", "creator.userId:" + searchData.getUser().getGooruUId() + "|owner.userId:" + searchData.getUser().getGooruUId() + "|collaboratorIds:" + searchData.getUser().getGooruUId());
			} else if (searchData.getFilters().containsKey(AMPERSAND_CREATOR_ID) && StringUtils.isNotBlank(searchData.getFilters().get(AMPERSAND_CREATOR_ID).toString())) {
				searchData.putFilter("&^orFilters", "creator.userId:" + searchData.getUser().getGooruUId() + "|owner.userId:" + searchData.getFilters().get(AMPERSAND_CREATOR_ID).toString() + "|collaboratorIds:" + searchData.getFilters().get(AMPERSAND_CREATOR_ID).toString());
				searchData.getFilters().remove(AMPERSAND_CREATOR_ID);
				searchData.getParameters().remove(FLT_CREATOR_ID);
			}
			break;
		case "tenant-library":
			searchData.putFilter("&^tenant.tenantId", (searchData.getUserTenantRootId() != null) ? searchData.getUserTenantRootId() : searchData.getUserTenantId());
			break;
		case "subtenant-library":
			if (searchData.getUserTenantRootId() != null) {
				searchData.putFilter("&^tenant.tenantRootId", searchData.getUserTenantRootId());
			} else {
				searchData.putFilter("&^tenant.tenantId", searchData.getUserTenantId());
			}
			break;
		case "open-featured":
			String key = "course.id";
			if (searchData.getType().equalsIgnoreCase(TYPE_COURSE + _V3))
				key = IndexFields.ID;
			if (scope.getIdList() == null && scope.getTargetNames() == null) {
				Set<String> courses = getDiscoverableCourses(searchData);
				if (!courses.isEmpty()) searchData.putFilter("&^" + key, StringUtils.join(courses, COMMA));
			}
			break;
		case "open-all":
			searchData.putFilter("&^publishStatus", "published");
			break;
		case "open-library":
		case "tenant-content":
		case "subtenant-content":
			break;
		default:
			logger.info("Invalid Scope : {}", target);
		}

		if (target.contains(LIBRARY)) {
			searchData.putFilter("&^statistics.isLibraryContent", true);
		}
		
		if (SCOPE_MYCONTENT_LIBRARY_MATCH.matcher(target).matches()) {
			searchData.putFilter("&^publishStatus", "published,unpublished");
			if (scope.getIdList() != null && !scope.getIdList().isEmpty()) {
				String idKey = "course.id";
				if (target.contains(LIBRARY)) idKey = "library.id"; 
				else if (searchData.getType().equalsIgnoreCase(TYPE_COURSE + _V3) || target.equalsIgnoreCase("my-content")) idKey = IndexFields.ID;
				searchData.putFilter("&^" + idKey, StringUtils.join(scope.getIdList(), COMMA));
			} else if (scope.getTargetNames() != null && !scope.getTargetNames().isEmpty()) {
				String titleKey = "courseTitleLowercase";
				if (target.contains(LIBRARY)) titleKey = "libraryShortName";
				else if (searchData.getType().equalsIgnoreCase(TYPE_COURSE + _V3) || target.equalsIgnoreCase("my-content")) titleKey = "titleLowercase";
				searchData.putFilter("&^" + titleKey, StringUtils.join(scope.getTargetNames(), COMMA));
			}
		}
    }

    @SuppressWarnings("unchecked")
    private Set<String> getDiscoverableCourses(SearchData searchData) {
        Set<String> courses = new HashSet<>();
        List<CourseSearchResult> searchResponse = null;
        SearchData courseRequest = new SearchData();
        courseRequest.setPretty(searchData.getPretty());
        courseRequest.setIndexType(EsIndex.COURSE);
        courseRequest.setSize(70);
        courseRequest.setQueryString(STAR);
        courseRequest.setUserPermits(searchData.getUserPermits());
        courseRequest.setUserTenantId(searchData.getUserTenantId());
        courseRequest.setUserTenantParentId(searchData.getUserTenantParentId());
        courseRequest.putFilter(AMPERSAND + CARET_SYMBOL + IndexFields.TENANT + DOT + IndexFields.TENANT_ID,
            (StringUtils.join(SearchSettingService.getAllDiscoverableTenantIds(Constants.ALL_DISCOVERABLE_TENANT_IDS), COMMA)));
        MapWrapper<Object> parameters = new MapWrapper<>();
        parameters.put("flt.courseType", "featured");
        courseRequest.setParameters(parameters);
        try {
            searchResponse = (List<CourseSearchResult>) SearchHandler.getSearcher(SearchHandlerType.COURSE.name())
                .search(courseRequest).getSearchResults();
            searchResponse.forEach(m -> {
                CourseSearchResult course = (CourseSearchResult) m;
                courses.add(course.getId());
            });
        } catch (Exception e) {
            logger.error("No open courses: Exception : {}", e.getMessage());
        }
        return courses;
    }

    @Override
    protected SearchProcessorType getType() {
        return SearchProcessorType.ScopeFilterConstruction;
    }

}
