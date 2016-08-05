package org.ednovo.gooru.search.es.constant;

import java.util.HashMap;
import java.util.Map;

public enum SearchInputType {

	QUERY("query", "*"),
	QUERY_TYPE("queryType", "single"),
	ADMIN_FLAG("adminFlag", "false"),
	UN_RESTRICTED_SEARCH("unrestrictedSearch", "false"),
	WILDCARD_FLAG("wildcardFlag", "false"),
	PAGE_NUM("pageNum", "1"),
	PAGE_SIZE("pageSize", "10"),
	SKIP_CACHE("skipCache", "false"),
	SEARCH_LOG_ENTRY("searchLogEntry", "true"),
	GET_USER_COLLECTIONS_INFO("getUserCollectionsInfo", "false"),
	SEARCH_BY("searchBy", "all"),
	EXCLUDE_ATTRIBUTES("excludeAttributes", ""),
	ALLOW_DUPLICATES("allowDuplicates", "true"),
	START_AT("startAt", "0"),
	FETCH_HITS_IN_MULTI("fetchHitsInMulti", "false"),
	BS_SEARCH("bsSearch", "false"),
	ALLOW_SCRIPTING("allowScripting", "false"),
	FORMAT("format", "json"),
	DETAIL_TYPE("detailType", "simple"),
	SEARCH_MODE("searchMode","expanded");
	
	String name;
	Object defaultValue;
	private static Map<String, Object> value;
	
	
	private SearchInputType(String name,
			Object defaultValue) {
		this.name = name;
		this.defaultValue = defaultValue;
	}

	public String getName() {
		return name;
	}

	public Object getDefaultValue() {
		return defaultValue;
	}
	
	public String getDefaultValueAsString() {
		return defaultValue.toString();
	}
	
	 public static Object getValue(String fieldName) {
	        if (value == null) {
	            initializeMapping();
	        }
	        if (value.containsKey(fieldName)) {
	            return value.get(fieldName);
	        }
	        return null;
	    }
	 
	 private static void initializeMapping() {
	        value = new HashMap<String, Object>();
	        for (SearchInputType searchInputType : SearchInputType.values()) {
	            value.put(searchInputType.getName(), searchInputType.getDefaultValue());
	        }
	    }
	
}
