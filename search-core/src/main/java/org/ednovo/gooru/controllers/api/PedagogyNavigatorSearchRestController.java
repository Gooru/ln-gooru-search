package org.ednovo.gooru.controllers.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.ednovo.gooru.search.es.service.SearchSettingService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = { "/v1/pedagogy-search" })
public class PedagogyNavigatorSearchRestController extends SerializerUtil implements Constants {

	protected static final Logger logger = LoggerFactory.getLogger(PedagogyNavigatorSearchRestController.class);

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
		if (type.equalsIgnoreCase(TYPE_COMPETENCY_GRAPH)) {
			query = checkQueryValidity(query, (Map<String, Object>) request.getParameterMap());
		}
		
		// Set user permits
		UserGroupSupport userGroup = (UserGroupSupport) request.getAttribute(Constants.TENANT);
		List<String> userPermits = new ArrayList<>();
		String userTenantId = userGroup.getTenantId();
		searchData.setUserTenantId(userTenantId);
		userPermits.add(userTenantId);
		List<String> discoverableTenantIds = SearchSettingService.getAllDiscoverableTenantIds(Constants.ALL_DISCOVERABLE_TENANT_IDS);
		if (discoverableTenantIds != null && !discoverableTenantIds.isEmpty())
			userPermits.addAll(discoverableTenantIds);
		searchData.setUserPermits(userPermits.stream().distinct().collect(Collectors.toList()));

		searchData.setUserTaxonomyPreference((JSONObject) request.getAttribute(Constants.USER_PREFERENCES));
		MapWrapper<Object> searchDataMap = new MapWrapper<Object>(request.getParameterMap());

		if (searchDataMap.containsKey("flt.standard") || searchDataMap.containsKey("flt.standardDisplay")) {
			searchData.setStandardsSearch(true);
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
			SearchResponse<Object> searchResponse = SearchHandler.getSearcher(type.toUpperCase()).search(searchData);
			logger.info("Elapsed time to complete search process :" + (System.currentTimeMillis() - start) + " ms");
			searchResponse.setExecutionTime(System.currentTimeMillis() - start);

			if (type.equalsIgnoreCase(KEYWORD_COMPETENCY)) {
				return toModelAndView(serialize(searchResponse.getSearchResults(), JSON, excludeAttributeArray, true, false));
			}
			return toModelAndView(serialize(searchResponse, JSON, excludeAttributeArray, true, false));
		} catch (SearchException searchException) {
			response.setStatus(searchException.getStatus().value());
			return toModelAndView(searchException.getMessage());
		}
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
