/**
 * 
 */
package org.ednovo.gooru.search.es.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ednovo.gooru.search.es.constant.EsIndex;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.json.JSONObject;

/**
 * @author SearchTeam
 * 
 */

public class SearchData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2962322595275238671L;

    //original query string put in by user
    private String userQueryString;

    private String spellCheckQueryString;

	private String queryString;

	private String sessionToken;

	private String remoteAddress;

	private User user;

	private String type;

	private EsIndex indexType;

	private boolean isPaginated = false;

	private Integer startFrom = 0;

	private Integer size = 10;

	private boolean isRestricted;

	private MapWrapper<Object> parameters;

	private MapWrapper<Object> queryDsl;
	
	private List<String> resourceGooruOIds;

	private List<Object> queries;

	private HashSet<String> customFilters;

	private Map<String, Object> filters;

	private String searchResultText;

	private List<SearchProcessorType> skipTypes;

	private String queryType;

	private String multiQueryDsl;

	private String queryValues;

	private String defaultOperator = "and";

	private boolean allowLeadingWildcard = false;

	private String pretty = "0";

	private Exception exception;
	
	private String originalQuery;
	
	private String facet;
	
	private String protocolType;
	
	private boolean isBsSearch;
	
	private boolean isFacetSubjectSearch = false;
	
	private JSONObject userTaxonomyPreference;

	private boolean showSingleSubjectResults;
	
	private boolean showCanonicalOnly;
	
	private boolean  isActiveEnable;
	
	private boolean isStandardsSearch = false;
	
	private Integer pageNum;
	
	private String contributorType;
	
	private String contentCdnUrl;

	private List<String> userPermits;
	
	private boolean isCrosswalk;
	
	private String userTenantId;
		
	private boolean isFeaturedCourseSearch = false;

	private List<String> featuredCourseTenantPreferences;

	public boolean isShowCanonicalOnly() {
		return showCanonicalOnly;
	}
	
	public void setShowCanonicalOnly(boolean showCanonicalOnly) {
		this.showCanonicalOnly = showCanonicalOnly;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

    public String getUserQueryString() {
      return userQueryString;
    }

    public void setUserQueryString(String userQueryString) {
      this.userQueryString = userQueryString;
    }

    public String getSpellCheckQueryString() {
      return spellCheckQueryString;
    }

    public void setSpellCheckQueryString(String spellCheckQueryString) {
      this.spellCheckQueryString = spellCheckQueryString;
    }

    public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getType() {
		return type;
	}

	public EsIndex getIndexType() {
		return indexType;
	}

	public void setIndexType(EsIndex indexType) {
		this.indexType = indexType;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isPaginated() {
		return isPaginated;
	}

	public void setPaginated(boolean isPaginated) {
		this.isPaginated = isPaginated;
	}

	public Integer getFrom() {
		return startFrom;
	}

	public void setFrom(Integer from) {
		this.startFrom = from;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public boolean isRestricted() {
		return isRestricted;
	}

	public void setRestricted(boolean isRestricted) {
		this.isRestricted = isRestricted;
	}

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	public String getRemoteAddress() {
		return remoteAddress;
	}

	public void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
	}

	public MapWrapper<Object> getParameters() {
		return parameters;
	}

	public void setParameters(MapWrapper<Object> parameters) {
		this.parameters = parameters;
	}

	public MapWrapper<Object> getQueryDsl() {
		if (queryDsl == null) {
			queryDsl = new MapWrapper<Object>();
		}
		return queryDsl;
	}

	public void setQueryDsl(MapWrapper<Object> queryDsl) {
		this.queryDsl = queryDsl;
	}

	public boolean isQueriesEmpty() {
		return queries == null || queries.size() == 0;
	}

	public List<Object> getQueries() {
		if (queries == null) {
			queries = new ArrayList<Object>();
		}
		return queries;
	}

	public Set<String> getCustomFilters() {
		if (customFilters == null) {
			customFilters = new HashSet<String>();
		}
		return customFilters;
	}

	
	public void setCustomFilters(HashSet<String> customFilters) {
		this.customFilters = (HashSet<String>) customFilters;
	}

	public synchronized void putQuery(Object query) {
		getQueries().add(query);
	}

	public String getSearchResultText() {
		return searchResultText;
	}

	public void setSearchResultText(String searchResultText) {
		this.searchResultText = searchResultText;
	}

	public boolean isSkipType(SearchProcessorType type) {
		return skipTypes != null && skipTypes.contains(type);
	}

	private List<SearchProcessorType> getSkipTypes() {
		if (skipTypes == null) {
			skipTypes = new ArrayList<SearchProcessorType>();
		}
		return skipTypes;
	}

	public void skipType(SearchProcessorType skipType) {
		getSkipTypes().add(skipType);
	}

	public Map<String, Object> getFilters() {
		return filters;
	}

	public void putFilter(String key,
			Object value) {
		if (this.filters == null) {
			this.filters = new HashMap<String, Object>(1);
		}
		this.filters.put(key, value);
	}

	public synchronized String getQueryType() {
		return queryType;
	}

	public synchronized void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public String getMultiQueryDsl() {
		return multiQueryDsl;
	}

	public void setMultiQueryDsl(String multiQueryDsl) {
		this.multiQueryDsl = multiQueryDsl;
	}

	public String getQueryValues() {
		return queryValues;
	}

	public void setQueryValues(String queryValues) {
		this.queryValues = queryValues;
	}

	public String getDefaultOperator() {
		return defaultOperator;
	}

	public void setDefaultOperator(String defaultOperator) {
		this.defaultOperator = defaultOperator;
	}

	public boolean isAllowLeadingWildcard() {
		return allowLeadingWildcard;
	}

	public void setAllowLeadingWildcard(boolean allowLeadingWildcard) {
		this.allowLeadingWildcard = allowLeadingWildcard;
	}

	public String getPretty() {
		return pretty;
	}

	public void setPretty(String pretty) {
		this.pretty = pretty;
	}

	public synchronized Exception getException() {
		return exception;
	}

	public synchronized void setException(Exception exception) {
		this.exception = exception;
	}

	public String getOriginalQuery() {
		return originalQuery;
	}

	public void setOriginalQuery(String originalQuery) {
		//which allow to insert value at once.
		this.originalQuery = this.originalQuery == null ? originalQuery : this.originalQuery;
	}

	public String getFacet() {
		return facet;
	}

	public void setFacet(String facet) {
		this.facet = facet;
	}

	public String getProtocolType() {
		return protocolType;
	}

	public void setProtocolType(String protocolType) {
		this.protocolType = protocolType;
	}

	public boolean isBsSearch() {
		return isBsSearch;
	}

	public void setBsSearch(boolean isBsSearch) {
		this.isBsSearch = isBsSearch;
	}

	public JSONObject getUserTaxonomyPreference() {
		return userTaxonomyPreference;
	}

	public void setUserTaxonomyPreference(JSONObject userTaxonomyPreference) {
		this.userTaxonomyPreference = userTaxonomyPreference;
	}

	public void setFacetSubjectSearch(boolean isFacetSubjectSearch) {
		this.isFacetSubjectSearch = isFacetSubjectSearch;
	}

	public boolean isFacetSubjectSearch() {
		return isFacetSubjectSearch;
	}

	public void setShowSingleSubjectResults(boolean showSingleSubjectResults) {
		this.showSingleSubjectResults = showSingleSubjectResults;
	}

	public boolean isShowSingleSubjectResults() {
		return showSingleSubjectResults;
	}

	public void setStandardsSearch(boolean isStandardsSearch) {
		this.isStandardsSearch = isStandardsSearch;
	}

	public boolean isStandardsSearch() {
		return isStandardsSearch;
	}

	public List<String> getResourceGooruOIds() {
		return resourceGooruOIds;
	}

	public void setResourceGooruOIds(List<String> resourceGooruOIds) {
		this.resourceGooruOIds = resourceGooruOIds;
	}
	
	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}
	
	public boolean isActiveEnable() {
		return isActiveEnable;
	}

	public void setActiveEnable(boolean isActiveEnable) {
		this.isActiveEnable = isActiveEnable;
	}

	public String getContributorType() {
		return contributorType;
	}
	
	public void setContributorType(String contributorType) {
		this.contributorType = contributorType;
	}

	public String getContentCdnUrl() {
		return contentCdnUrl;
	}

	public void setContentCdnUrl(String contentCdnUrl) {
		this.contentCdnUrl = contentCdnUrl;
	}

	public List<String> getUserPermits() {
		return userPermits;
	}

	public void setUserPermits(List<String> userPermits) {
		this.userPermits = userPermits;
	}
	
	public boolean isCrosswalk() {
		return isCrosswalk;
	}

	public void setCrosswalk(boolean isCrosswalk) {
		this.isCrosswalk = isCrosswalk;
	}
	
	public String getUserTenantId() {
		return userTenantId;
	}

	public void setUserTenantId(String userTenantId) {
		this.userTenantId = userTenantId;
	}

	public boolean isFeaturedCourseSearch() {
		return isFeaturedCourseSearch;
	}

	public void setFeaturedCourseSearch(boolean isFeaturedCourseSearch) {
		this.isFeaturedCourseSearch = isFeaturedCourseSearch;
	}
	
	public List<String> getFeaturedCourseTenantPreferences() {
		return featuredCourseTenantPreferences;
	}

	public void setFeaturedCourseTenantPreferences(List<String> featuredCourseTenantPreferences) {
		this.featuredCourseTenantPreferences = featuredCourseTenantPreferences;
	}

}
