package org.ednovo.gooru.search.es.processor.filter.constructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.ednovo.gooru.search.domain.service.CourseSearchResult;
import org.ednovo.gooru.search.es.constant.Constants;
import org.ednovo.gooru.search.es.constant.EsIndex;
import org.ednovo.gooru.search.es.constant.IndexFields;
import org.ednovo.gooru.search.es.handler.SearchHandler;
import org.ednovo.gooru.search.es.handler.SearchHandlerType;
import org.ednovo.gooru.search.es.model.MapWrapper;
import org.ednovo.gooru.search.es.model.Scope;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.ednovo.gooru.search.es.service.SearchSettingService;
import org.springframework.stereotype.Component;

@Component
public class ScopeFilterConstructionProcessor extends FilterConstructionProcessor {

    @Override
    public void process(SearchData searchData, SearchResponse<Object> response) {
        super.process(searchData, response);
        Scope scope = searchData.getScope();
        if (scope != null) {
            processScope(searchData, scope.getKey(), scope);
        }
    }

    private void processScope(SearchData searchData, String target, Scope scope) {
        switch (target) {
        case "my-content":
            searchData.putFilter("&^creator.userId", searchData.getUser().getGooruUId());
            break;
        case "tenant-library":
            searchData.putFilter("&^tenant.tenantId", searchData.getUserTenantRootId());
            break;
        case "subtenant-library":
            searchData.putFilter("&^tenant.tenantRootId", searchData.getUserTenantRootId());
            break;
        case "library":
            break;
        case "gooru-fc":
            String key = "course.id";
            if (searchData.getType().equalsIgnoreCase(TYPE_COURSE)) key = IndexFields.ID;
            if (scope.getIdList() != null && !scope.getIdList().isEmpty()) {
                searchData.putFilter("&^" + key, StringUtils.join(scope.getIdList(), COMMA));
            } else {
                Set<String> courses = getDiscoverableCourses(searchData);
                if (!courses.isEmpty()) searchData.putFilter("&^" + key, StringUtils.join(courses, COMMA));
            }
            break;
        case "gooru":
            searchData.putFilter("&^publishStatus", "published");
            break;
        case "course":
        	String courseKey = "course.id";
            if (searchData.getType().equalsIgnoreCase(TYPE_COURSE)) courseKey = IndexFields.ID;
            if (scope.getIdList() != null && !scope.getIdList().isEmpty()) {
                searchData.putFilter("&^" + courseKey, StringUtils.join(scope.getIdList(), COMMA));
            }
            break;
        case "signature-content":
            break;
        default:
            LOG.info("Invalid Scope : {}", target);
        }

        if (SCOPE_MYCONTENT_LIBRARY_MATCH.matcher(target).matches()) {
            searchData.putFilter("&^publishStatus", "published,unpublished");
        }
        if (target.contains(LIBRARY)) {
            searchData.putFilter("&^statistics.isLibraryContent", true);
            if (scope.getIdList() != null && !scope.getIdList().isEmpty()) {
                searchData.putFilter("&^library.id", StringUtils.join(scope.getIdList(), COMMA));
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
