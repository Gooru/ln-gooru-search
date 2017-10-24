/**
 * 
 */
package org.ednovo.gooru.search.es.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.ednovo.gooru.search.es.constant.Constants;
import org.ednovo.gooru.search.es.constant.SearchSettingType;
import org.ednovo.gooru.search.es.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author SearchTeam
 * 
 */
@Component
public final class SearchSettingService {

	private static SearchSettingService instance = null;

	private static String FILTER_ALIAS = "filter-alias@";

	private static String FILTER_DETECTION = "filter-detection@";

	// Don't change order. Add new keys at the end since key position is used.
	private static String[] settingsListKeys = { "filter-case@lowercase", "filter-splitBy@approx", "filter-skipWords@all", "search.resource.filter_detection", "search.scollection.filter_detection", "search.question.filter_detection", "search.collection.filter_detection",
		"search.quiz.filter_detection", "index-splitBy@singleTilta", "search-splitBy@singleTilta", "index-case@lowercase","search.content.colon_filter_detection","search.schooldistirct.filter_detection" };

	private static String[] settingsMapKeys = { "filter-detection@url", "filter-detection@category", "filter-detection@grade","filter-detection@resourceFormat","filter-detection@grade_colon","filter-detection@resourceFormat_colon","filter-detection@subjectName_colon","filter-detection@filter-name" };
	
	private static Map<String, Object> cache = new HashMap<String, Object>();

	private static String cachedVersion = "";

	private static String profileName = "default";

	private static Map<String, Object> allDiscoverableTenants = new HashMap<String, Object>(); 
			
	@Autowired
	@Resource(name = "configSettings")
	private Properties searchSettings;

	protected Properties getSearchSettings() {
		return this.searchSettings;
	}
    
	@Autowired
	private TenantRepository tenantRepository;

	public SearchSettingService() {
		if (instance == null)
			instance = this;
	}

	@PostConstruct
	public final void init() {
		validateCache();
	}

	private static String getProfileName() {
		return profileName;
	}

	private static String getSearchSetting(String key) {
		if (cache.containsKey(key + getProfileName())) {
			return cache.get(key + getProfileName()).toString();
		} else {
			return null;
		}
	}

	/**
	 * Check if the 'search.profile' is changed changed in config_setting table.
	 * If Yes, reset the cache of search settings. else, skip reset cache.
	 */
	public static final void validateCache() {

		String version = instance.getSearchSettings().getProperty("setting.version");
		// Check if the search version is changed.
		if (version.equals(cachedVersion)) {
			return;
		}
		synchronized (cache) {
			profileName = instance.getSearchSettings().getProperty("search.profile");
			if (profileName == null) {
				profileName = "default";
			}

			for (String setting : instance.getSearchSettings().stringPropertyNames()) {
				cache.put(setting + profileName, instance.getSearchSettings().getProperty(setting));
			}
			cachedVersion = version;
			for (String key : settingsListKeys) {
				initListFilters(key);
			}
			for (String key : settingsMapKeys) {
				initMapFilters(key);
			}
			setDiscoverableTenants();
		}
	}

	private static void setDiscoverableTenants() {
		allDiscoverableTenants = instance.tenantRepository.getAllDiscoverableTenants();
		cache.put("allDiscoverableTenantIds" + getProfileName(), ((allDiscoverableTenants != null && !allDiscoverableTenants.isEmpty()) ? allDiscoverableTenants.get("discoverableTenantIds") : null));
		cache.put("globalTenantIds" + getProfileName(), (instance.tenantRepository.getGlobalTenantIds()));
		cache.put("discoverableTenantIds" + getProfileName(), instance.tenantRepository.getDiscoverableTenantIds());
	}

	private static void initListFilters(String key) {
		String settingData = getSearchSetting(key);
		if (settingData == null) {
			return;
		}
		cache.put(key + getProfileName(), new ArrayList<String>(0));
		getCacheList(key).addAll(Arrays.asList(settingData.split(",")));
	}

	private static void initMapFilters(String key) {
		String settingData = getSearchSetting(key);
		if (settingData == null) {
			return;
		}
		cache.put(key + getProfileName(), new HashMap<String, String>(0));
		Map<String, String> filters = getCacheMap(key);
		for (String filter : settingData.split(",")) {
			String[] filterParam = filter.split("~~");
			filters.put(filterParam[0], filterParam[1]);
		}
		if(filters!= null){
			cache.put(key + getProfileName(), filters);
		}
	}
	
	@SuppressWarnings("unchecked")
	protected static List<String> getCacheList(String key) {
		Object result = cache.get(key + getProfileName());
		return result instanceof List ? (List<String>) result : null;
	}

	@SuppressWarnings("unchecked")
	protected static Map<String, String> getCacheMap(String key) {
		Object result = cache.get(key + getProfileName());
		return result instanceof Map ? (Map<String, String>) result : null;
	}

	protected static String getCache(String key) {
		return ((String) cache.get(key + getProfileName()));
	}

	public static String getFilterAlias(String name) {
		String value = getCache(FILTER_ALIAS + name);
		return value != null ? value : name;
	}

	public static Map<String, String> getFilterDetection(String name) {
		return getCacheMap(FILTER_DETECTION + name);
	}

	public static List<String> getListByName(String name) {
		return getCacheList(name);
	}

	public static String getByName(String name) {
		return getCache(name);
	}

	public static boolean isLowerCaseFilter(String name) {
		return getCacheList(settingsListKeys[0]) != null ? getCacheList(settingsListKeys[0]).contains(name) : false;
	}

	public static boolean isSplitByApproxFilter(String name) {
		return getCacheList(settingsListKeys[1]) != null ? getCacheList(settingsListKeys[1]).contains(name) : false;
	}

	public static boolean isSkipAllValueFilter(String name) {
		return getCacheList(settingsListKeys[2]) != null ? getCacheList(settingsListKeys[2]).contains(name) : false;
	}

	public static String getSetting(String name) {
		SearchSettingType configConstant = SearchSettingType.valueOf(name);
		return getSetting(configConstant);
	}

	public static Float getSettingAsFloat(String name) {
		SearchSettingType configConstant = SearchSettingType.valueOf(name);
		return getSettingAsFloat(configConstant);
	}

	public static Float getSettingAsFloat(String name, Float defaultValue) {
		String value = getByName(name);
		return value != null ? Float.valueOf(value) : defaultValue;
	}
	
	 public static int getSettingAsInteger(String name, Integer defaultValue) {
	    String value = getByName(name);
	    return value != null ? Integer.valueOf(value) : defaultValue;
	  }

	public static String getSetting(SearchSettingType settingConstant) {
		String value = getCache(settingConstant.getName());
		return value != null ? value : (String) settingConstant.getDefaultValue();
	}

	public static Float getSettingAsFloat(SearchSettingType settingConstant) {
		String value = getCache(settingConstant.getName());
		return value != null ? Float.valueOf(value) : (Float) settingConstant.getDefaultValue();
	}

	public static boolean isSplitBySingleTiltaForIndex(String name) {
		return getCacheList(settingsListKeys[8]) != null ? getCacheList(settingsListKeys[8]).contains(name) : false;
	}
	
	public static boolean isSplitBySingleTiltaForSearch(String name) {
		return getCacheList(settingsListKeys[9]) != null ? getCacheList(settingsListKeys[9]).contains(name) : false;
	}
	
	public static boolean isLowercaseForIndex(String name) {
		return getCacheList(settingsListKeys[10]) != null ? getCacheList(settingsListKeys[10]).contains(name) : false;
	}
	
	public static List<String> getAllDiscoverableTenantIds(String name) {
		return getCacheList(name);
	}
	
	@SuppressWarnings("unchecked")
	public static List<String> getDiscoverableTenantIds() {
		Map<String, Object> discoverableTenants = instance.tenantRepository.getAllDiscoverableTenants();
		return (discoverableTenants != null && !discoverableTenants.isEmpty()) ? (List<String>) discoverableTenants.get("discoverableTenantIds") : null;
	}
	
	public static List<String> getFeaturedCourseTenantPreference(String tenant) {
		String value = instance.tenantRepository.getTenantSetting(tenant, "featured-course-tenant-preferences");
		return value != null ? Arrays.asList(value.split(Constants.COMMA)) : null;
	}

	public static void refreshTenants() {
		setDiscoverableTenants();
	}

	public static String getCompetencyNodeURI() {
		return (getCache(Constants.DNS_ENV) != null && getCache(Constants.API_COMPETENCY_NODE) != null) ? (getByName(Constants.DNS_ENV) + getByName(Constants.API_COMPETENCY_NODE)) : null;
	}
	
}
