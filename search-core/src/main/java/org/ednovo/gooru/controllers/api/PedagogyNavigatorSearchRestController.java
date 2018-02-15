package org.ednovo.gooru.controllers.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.ednovo.gooru.search.es.constant.Constants;
import org.ednovo.gooru.search.es.constant.SearchInputType;
import org.ednovo.gooru.search.es.exception.BadRequestException;
import org.ednovo.gooru.search.es.exception.SearchException;
import org.ednovo.gooru.search.es.handler.SearchHandler;
import org.ednovo.gooru.search.es.model.MapWrapper;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.ednovo.gooru.search.es.model.User;
import org.ednovo.gooru.search.es.model.UserGroupSupport;
import org.ednovo.gooru.search.es.processor.util.SerializerUtil;
import org.ednovo.gooru.search.es.service.LearningMapsService;
import org.ednovo.gooru.search.es.service.SearchSettingService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
/**
 * @author Renuka
 * 
 */
@Controller
@RequestMapping(value = { "/v1/pedagogy-search" })
public class PedagogyNavigatorSearchRestController extends SerializerUtil implements Constants {

	protected static final Logger logger = LoggerFactory.getLogger(PedagogyNavigatorSearchRestController.class);
	
	@Autowired
	private LearningMapsService pedagogySearchService;
		
	@SuppressWarnings("unchecked")
	@RequestMapping(method = { RequestMethod.GET }, value = "/{type}")
	public ModelAndView search(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false) String sessionToken,
			@RequestParam(defaultValue = "10", value = "length") Integer limit, @RequestParam(defaultValue = "0") Integer startAt, @RequestParam(defaultValue = "1", value = "start") Integer pageNum,
			@RequestParam(defaultValue = "0") String pretty, @RequestParam(value = "q") String query, @PathVariable String type,
			@RequestParam(required = false, defaultValue = "true") boolean isCrosswalk) throws Exception {
		long start = System.currentTimeMillis();

		SearchData searchData = new SearchData();
		User apiCaller = (User) request.getAttribute(USER);

		/**
		 * Here, when no filter is chosen, * search and keyword request with length less than 3 without * are skipped.
		 **/
		if (type.equalsIgnoreCase(TYPE_COMPETENCY_GRAPH) || type.equalsIgnoreCase(LEARNING_MAPS)) {
			query = checkQueryValidity(query, (Map<String, Object>) request.getParameterMap());
		}

		// Set user permits
		UserGroupSupport userGroup = (UserGroupSupport) request.getAttribute(Constants.TENANT);
		List<String> userPermits = new ArrayList<>();
		String userTenantId = userGroup.getTenantId();
		String userTenantRootId = userGroup.getTenantRoot();
		searchData.setUserTenantId(userTenantId);
		searchData.setUserTenantRootId(userTenantRootId);
		userPermits.add(userTenantId);
		List<String> discoverableTenantIds = SearchSettingService.getAllDiscoverableTenantIds(Constants.ALL_DISCOVERABLE_TENANT_IDS);
		if (discoverableTenantIds != null && !discoverableTenantIds.isEmpty())
			userPermits.addAll(discoverableTenantIds);
		searchData.setUserPermits(userPermits.stream().distinct().collect(Collectors.toList()));

		searchData.setUserTaxonomyPreference((JSONObject) request.getAttribute(Constants.USER_PREFERENCES));
		MapWrapper<Object> searchDataMap = new MapWrapper<Object>(request.getParameterMap());

		if (searchDataMap.containsKey("flt.standard") || searchDataMap.containsKey("flt.standardDisplay") ) {
			searchData.setTaxFilterType("standard");
		}
		if (searchDataMap.containsKey("flt.course")) {
			searchData.setTaxFilterType("course");
		}
		if (searchDataMap.containsKey("flt.domain")) {
			searchData.setTaxFilterType("domain");
		}
		searchData.setParameters(searchDataMap);

		if (query.contains("!")) {
			query = query.replace("!", "");
		}
		searchData.setOriginalQuery(query);
		searchData.setQueryString(query);
		searchData.setPretty(pretty);
		searchData.setSessionToken(sessionToken);
		searchData.setCrosswalk(isCrosswalk);

		if (type.equalsIgnoreCase(TYPE_COMPETENCY_GRAPH)) {
			type = KEYWORD_COMPETENCY;
		}

		searchData.setType(type);
		searchData.setFrom(startAt);
		searchData.setPageNum(pageNum);
		searchData.setSize(limit);
		if (searchData.getFrom() < 1) {
			searchData.setFrom((pageNum - 1) * searchData.getSize());
		}
		searchData.setRemoteAddress(request.getRemoteAddr());
		searchData.setUser(apiCaller);

		// Set Default Values
		for (SearchInputType searchInputType : SearchInputType.values()) {
			if (searchData.getParameters().getObject(searchInputType.getName()) == null) {
				searchData.getParameters().put(searchInputType.getName(), searchInputType.getDefaultValue());
			}
		}

		String excludeAttributeArray[] = {};
		try {
			SearchResponse<Object> searchResponse = new SearchResponse<>();
			if (type.equalsIgnoreCase(LEARNING_MAPS)) {
				searchResponse = pedagogySearchService.searchPedagogy(searchData);
			} else {
				searchResponse = SearchHandler.getSearcher(type.toUpperCase()).search(searchData);
			}
			logger.info("Elapsed time to complete search process :" + (System.currentTimeMillis() - start) + " ms");
			if (type.equalsIgnoreCase(KEYWORD_COMPETENCY)) searchResponse.setExecutionTime(System.currentTimeMillis() - start);
			if (type.equalsIgnoreCase(KEYWORD_COMPETENCY) || type.equalsIgnoreCase(LEARNING_MAPS)) {
				return toModelAndView(serialize(searchResponse.getSearchResults(), JSON, excludeAttributeArray, true, true));
			}
			return toModelAndView(serialize(searchResponse, JSON, excludeAttributeArray, true, false));
		} catch (SearchException searchException) {
			response.setStatus(searchException.getStatus().value());
			return toModelAndView(searchException.getMessage());
		}
	}
	
	@RequestMapping(method = { RequestMethod.GET }, value = "/learning-maps/standard/{standardCode:.+}")
	public ModelAndView searchLearningMaps(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String standardCode,
			@RequestParam(required = false) String fwCode,
			@RequestParam(required = false) String sessionToken,
			@RequestParam(defaultValue = "10", value = "length") Integer limit, 
			@RequestParam(defaultValue = "0") Integer startAt, 
			@RequestParam(defaultValue = "1", value = "start") Integer pageNum,
			@RequestParam(defaultValue = "0") String pretty, 
			@RequestParam(required = false, defaultValue = "*", value = "q") String query,
			@RequestParam(defaultValue = "true") boolean isCrosswalk,
			@RequestParam(defaultValue = "true") boolean isDisplayCode) throws Exception {
		long start = System.currentTimeMillis();

		SearchData searchData = generateLMSearchData(request, standardCode, fwCode, sessionToken, limit, startAt, pageNum, pretty, query, isCrosswalk, isDisplayCode);
		String excludeAttributeArray[] = {};
		try {
			SearchResponse<Object> searchResponse = pedagogySearchService.searchPedagogy(searchData);
			logger.info("Elapsed time to complete search process :" + (System.currentTimeMillis() - start) + " ms");
			return toModelAndView(serialize(searchResponse.getSearchResults(), JSON, excludeAttributeArray, true, false));
		} catch (SearchException searchException) {
			response.setStatus(searchException.getStatus().value());
			return toModelAndView(searchException.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	private SearchData generateLMSearchData(HttpServletRequest request, String standardCode, String fwCode, String sessionToken, Integer limit, Integer startAt, Integer pageNum, String pretty,
			String query, boolean isCrosswalk, boolean isDisplayCode) {
		SearchData searchData = new SearchData();
		User apiCaller = (User) request.getAttribute(USER);

		// Set user permits
		UserGroupSupport userGroup = (UserGroupSupport) request.getAttribute(Constants.TENANT);
		List<String> userPermits = new ArrayList<>();
		String userTenantId = userGroup.getTenantId();
		String userTenantRootId = userGroup.getTenantRoot();
		searchData.setUserTenantId(userTenantId);
		searchData.setUserTenantRootId(userTenantRootId);
		userPermits.add(userTenantId);
		List<String> discoverableTenantIds = SearchSettingService.getAllDiscoverableTenantIds(Constants.ALL_DISCOVERABLE_TENANT_IDS);
		if (discoverableTenantIds != null && !discoverableTenantIds.isEmpty())
			userPermits.addAll(discoverableTenantIds);
		searchData.setUserPermits(userPermits.stream().distinct().collect(Collectors.toList()));

		searchData.setUserTaxonomyPreference((JSONObject) request.getAttribute(Constants.USER_PREFERENCES));
		MapWrapper<Object> searchDataMap = new MapWrapper<Object>(request.getParameterMap());
		searchData.setParameters(searchDataMap);

		if (StringUtils.isBlank(fwCode) && StringUtils.isNotBlank(standardCode)) {
			searchData.getParameters().put(FLT_TAXONOMY_GUT_CODE, standardCode);
		} else if (StringUtils.isNotBlank(fwCode)){
			if (isDisplayCode) {
				searchData.getParameters().put(FLT_STANDARD_DISPLAY, standardCode);
			} else {
				searchData.getParameters().put(FLT_STANDARD, standardCode);
			}
			searchData.getParameters().put(FLT_FWCODE, fwCode);
		}
		if (searchDataMap.containsKey("flt.standard") || searchDataMap.containsKey("flt.standardDisplay") ) {
			searchData.setTaxFilterType("standard");
		}
		if (searchDataMap.containsKey("flt.course")) {
			searchData.setTaxFilterType("course");
		}
		if (searchDataMap.containsKey("flt.domain")) {
			searchData.setTaxFilterType("domain");
		}
		
		searchData.setOriginalQuery(query);
		searchData.setQueryString(query);
		searchData.setPretty(pretty);
		searchData.setSessionToken(sessionToken);
		searchData.setCrosswalk(isCrosswalk);

		searchData.setType(LEARNING_MAPS);
		searchData.setFrom(startAt);
		searchData.setPageNum(pageNum);
		searchData.setSize(limit);
		if (searchData.getFrom() < 1) {
			searchData.setFrom((pageNum - 1) * searchData.getSize());
		}
		searchData.setRemoteAddress(request.getRemoteAddr());
		searchData.setUser(apiCaller);

		// Set Default Values
		for (SearchInputType searchInputType : SearchInputType.values()) {
			if (searchData.getParameters().getObject(searchInputType.getName()) == null) {
				searchData.getParameters().put(searchInputType.getName(), searchInputType.getDefaultValue());
			}
		}
		return searchData;
	}
	
	private String checkQueryValidity(String query, Map<String, Object> parameterMap) {
		if (!parameterMap.isEmpty()) {
			Set<String> parameterSet = parameterMap.keySet();
			boolean hasFilter = false;
			for (String parameterKey : parameterSet) {
				parameterKey = parameterKey.trim();
				if (parameterKey.contains(FLT) && (!parameterKey.equalsIgnoreCase(FLT_COLLECTION_TYPE) && !(parameterKey.equalsIgnoreCase(FLT_RESOURCE_FORMAT) && parameterMap.get(FLT_RESOURCE_FORMAT).toString().equalsIgnoreCase(TYPE_QUESTION)) && !(parameterKey.equalsIgnoreCase(FLT_RATING) && parameterMap.get(FLT_RATING).toString().contains(STR_ZERO)))) {
					hasFilter = true;
					break;
				}
			}
			query = query.replaceAll(ESCAPED_STAR, EMPTY_STRING);
			if (!hasFilter && query.length() < 3) {
				throw new BadRequestException(INVALID_KEYWORD_ERROR_MESSAGE);
			} else if (hasFilter && StringUtils.isBlank(query)) {
				query = STAR;
			}
		}
		return query;
	}
	
}
