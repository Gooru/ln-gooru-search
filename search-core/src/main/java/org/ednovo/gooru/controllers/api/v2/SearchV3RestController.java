package org.ednovo.gooru.controllers.api.v2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.ednovo.gooru.controllers.api.BaseController;
import org.ednovo.gooru.search.es.constant.Constants;
import org.ednovo.gooru.search.es.constant.EventConstants;
import org.ednovo.gooru.search.es.constant.SearchInputType;
import org.ednovo.gooru.search.es.exception.BadRequestException;
import org.ednovo.gooru.search.es.exception.SearchException;
import org.ednovo.gooru.search.es.handler.SearchHandler;
import org.ednovo.gooru.search.es.handler.SearchHandlerType;
import org.ednovo.gooru.search.es.model.MapWrapper;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.SearchRequestBody;
import org.ednovo.gooru.search.es.model.SessionContextSupport;
import org.ednovo.gooru.search.es.model.User;
import org.ednovo.gooru.search.es.model.UserGroupSupport;
import org.ednovo.gooru.search.es.processor.util.SerializerUtil;
import org.ednovo.gooru.search.es.service.SearchService;
import org.ednovo.gooru.search.responses.SearchResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
/**
 * @author Renuka
 * 
 */
@Controller
@RequestMapping(value = { "/v3/search" })
public class SearchV3RestController  extends SerializerUtil implements Constants {
	
	@Autowired
	private SearchService searchService;

	protected static final Logger logger = LoggerFactory.getLogger(SearchV3RestController.class);

	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.POST}, value = "/{type}")
	public ModelAndView search(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam(required = false) String sessionToken,
			@RequestParam(defaultValue = "8", value="length") Integer pageSize, 
			@RequestParam(defaultValue = "0") String pretty, 
			@RequestParam(defaultValue = "0") Integer startAt,
			@RequestParam(defaultValue = "1", value="start") Integer pageNum, 
			@RequestParam(value="q") String query,
			@RequestParam(required = false) String excludeAttributes, 
			@RequestParam(required = false, defaultValue= "false") boolean userDetails, 
			@PathVariable String type, 
			@RequestParam(required = false, defaultValue= "true") boolean isCrosswalk,
			@RequestParam(required = false, defaultValue = "false") boolean disableSpellCheck, 
			@RequestBody(required = false) SearchRequestBody searchRequestPayload) throws Exception {

		SearchData searchData = new SearchData();
		long start = System.currentTimeMillis();

		// original query string from user
		searchData.setUserQueryString(query);

		/**
		 * Here, when no filter is chosen, * search and keyword request with length less than 3 without * are skipped.
		 **/
		if (RQCA_MATCH.matcher(type).matches()) {
			request.setAttribute(SEARCH_TYPE, type);
			query = checkQueryValidity(query, (Map<String, Object>) request.getParameterMap());
		}

		if (query.matches(SEARCH_SPLCHR)) {
			throw new BadRequestException("Please enter a valid search term");
		}
		
		User apiCaller = (User) request.getAttribute(USER);
        
		// Process Parameters
		MapWrapper<Object> searchDataMap = new MapWrapper<Object>(request.getParameterMap());
		processParameters(disableSpellCheck, searchData, searchDataMap);
		searchData.setParameters(searchDataMap);
		searchData.setFrom(startAt > 0 ? startAt : 0);
 		searchData.setPageNum(pageNum > 0 ? pageNum : 1);
 		searchData.setSize(pageSize >= 0 ? pageSize : 8);
 		searchData.setCrosswalk(isCrosswalk);
 		searchData.setPretty(pretty);
 		
		// Set Scope
		processRequestBody(searchRequestPayload, searchData);
        
		// Set content cdn url
		String contentCdnUrl = (String) request.getAttribute(Constants.CONTENT_CDN_URL);
		searchData.setContentCdnUrl(contentCdnUrl);

		// Set content cdn url
		String userCdnUrl = (String) request.getAttribute(Constants.USER_CDN_URL);
		searchData.setUserCdnUrl(userCdnUrl);
		
		// Set user permits
		UserGroupSupport userGroup = (UserGroupSupport) request.getAttribute(Constants.TENANT);
		String userTenantId = userGroup.getTenantId();
		String userTenantRootId = userGroup.getTenantRoot();
		searchData.setUserTenantId(userTenantId);
		searchData.setUserTenantRootId(userTenantRootId);
		searchData.setUserTaxonomyPreference((JSONObject) request.getAttribute(Constants.USER_PREFERENCES));

		if (sessionToken == null) {
			sessionToken = BaseController.getSessionToken(request);
		}

		if (query.contains("!")) {
			query = query.replace("!", EMPTY_STRING);
		}
		searchData.setOriginalQuery(query);
		searchData.setQueryString(query);
		searchData.setQueryType(SINGLE);
		searchData.setSessionToken(sessionToken);

		if (type.equalsIgnoreCase(TYPE_STANDARD) || type.equalsIgnoreCase(STANDARD_CODE)) {
			if (type.equalsIgnoreCase(TYPE_STANDARD)) {
				String expandedQuery = searchData.getQueryString().replace(".", "").replace(".--", "").replace("-", "").replace("\"", "").replace(" ", "_");
				searchData.setQueryString("(" + expandedQuery + "*) OR (*" + expandedQuery + "*)");
				searchDataMap.put("searchBy", "standard");
				searchData.setOriginalQuery(expandedQuery);
			}
			if (type.equalsIgnoreCase(STANDARD_CODE)) {
				searchDataMap.put("searchBy", "standardCode");
			}
			searchData.setParameters(searchDataMap);
			type = TYPE_TAXONOMY;
		} else if (type.equalsIgnoreCase(TYPE_PUBLISHER)) {
			searchData.setType(type);
			searchData.setQueryString(searchData.getQueryString() + "*");
		} else if (type.equalsIgnoreCase(RESOURCE) && searchDataMap.getString(QUERY_TYPE) != null) {
			if (searchDataMap.getString(QUERY_TYPE).equalsIgnoreCase(MULTI_CATEGORY)) {
				type = SearchHandlerType.MULTI_RESOURCE.name();
				searchData.setQueryType(MULTI_CATEGORY);
			} else if (searchDataMap.getString(QUERY_TYPE).equalsIgnoreCase(MULTI_QUERY)) {
				type = SearchHandlerType.MULTI_RESOURCE.name();
				searchData.setQueryType(MULTI_QUERY);
			} else if (searchDataMap.getString(QUERY_TYPE).equalsIgnoreCase(MULTI_RESOURCE_FORMAT)) {
				type = SearchHandlerType.MULTI_RESOURCE.name();
				searchData.setQueryType(MULTI_RESOURCE_FORMAT);
			}
		} else if (type.equalsIgnoreCase(TYPE_USER)) {
			throw new SearchException(HttpStatus.NOT_IMPLEMENTED, "Not supported");
		} else if (type.equalsIgnoreCase(TYPE_COMPETENCY_GRAPH)) {
			type = KEYWORD_COMPETENCY;
		} else if (COLLECTION_MATCH.matcher(type).matches()) {
			if(searchData.getParameters().containsKey(FLT_COLLECTION_TYPE)) searchData.getParameters().remove(FLT_COLLECTION_TYPE);
			searchData.putFilter(AMPERSAND_COLLECTION_TYPE, type);
			type = TYPE_SCOLLECTION;
		} else if (RESOURCE_MATCH.matcher(type).matches()) {
			if (searchData.getParameters().containsKey(FLT_RESOURCE_FORMAT)) searchData.getParameters().remove(FLT_RESOURCE_FORMAT);
			if (searchData.getParameters().containsKey(FLT_CONTENT_FORMAT)) searchData.getParameters().remove(FLT_CONTENT_FORMAT);
			searchData.putFilter(AMPERSAND_CONTENT_FORMAT, type);
			type = RESOURCE;
		}
		if (!SEARCH_TYPES_MATCH.matcher(type).matches() || ((COLLECTION_MATCH.matcher(type).matches() && searchData.getParameters().containsKey(FLT_COLLECTION_TYPE)) ||(RESOURCE_MATCH.matcher(type).matches() && (!searchData.getParameters().containsKey(FLT_CONTENT_FORMAT) && (searchData.getParameters().containsKey(FLT_RESOURCE_FORMAT) && RESOURCE_MATCH.matcher(searchData.getParameters().getString(FLT_RESOURCE_FORMAT)).matches()))))) {
			throw new BadRequestException("Unsupported type! Please pass a valid path variable : type");
		}

		if (searchData.getQueryString() != null && ((StringUtils.startsWithAny(searchData.getQueryString(), START_WITH_BOOLEAN_OPERATORS)) || (StringUtils.endsWithAny(searchData.getQueryString(), END_WITH_BOOLEAN_OPERATORS)))) {
			searchService.trimInvalidExpression(searchData);
		}

		searchData.setType(type);
 		
		if (searchData.getFrom() < 1) {
			searchData.setFrom((searchData.getPageNum() - 1) * searchData.getSize());
		}
		searchData.setRemoteAddress(request.getRemoteAddr());
		searchData.setUser(apiCaller);
		searchData.setRestricted(false);
		
		// Set Default Values
		for (SearchInputType searchInputType : SearchInputType.values()) {
			if (searchData.getParameters().getObject(searchInputType.getName()) == null) {
				searchData.getParameters().put(searchInputType.getName(), searchInputType.getDefaultValue());
			}
		}

		String excludeAttributeArray[] = {};
		if (excludeAttributes != null) {
			excludeAttributeArray = excludeAttributes.split("\\s*,\\s*");
		}

		request.setAttribute("action", "search");
		try {
			SearchResponse<Object> searchResponse = SearchHandler.getSearcher(type.toUpperCase() + _V3.toUpperCase()).search(searchData);
			logger.info("Elapsed time to complete search process :" + (System.currentTimeMillis() - start) + " ms");

			setEventLogObject(request, searchData, searchResponse);

			if (type.equalsIgnoreCase(RESOURCE)) {
				if (!userDetails) {
					// excludeAttributeArray = (String[]) ArrayUtils.addAll(excludeAttributeArray, new String[]{"*.user"});
					excludeAttributeArray = (String[]) ArrayUtils.addAll(excludeAttributeArray, new String[] {});
				}
				return toModelAndView(serialize(searchResponse, JSON, (String[]) ArrayUtils.addAll(SINGLE_EXCLUDES, excludeAttributeArray), true, true));
			} else if (type.equalsIgnoreCase(SearchHandlerType.MULTI_RESOURCE.name()) && searchDataMap.getString(QUERY_TYPE) != null) {
				Object serializeResult = searchResponse;
				if (searchDataMap.getString(SearchInputType.FETCH_HITS_IN_MULTI.getName()).equals(SearchInputType.FETCH_HITS_IN_MULTI.getDefaultValue())) {
					if (searchResponse.getSearchResults() instanceof List && ((List<?>) searchResponse.getSearchResults()).size() > 0) {
						serializeResult = ((List<?>) searchResponse.getSearchResults()).get(0);
					} else {
						serializeResult = null;
					}
				}

				String resultsJSON = serialize(serializeResult, JSON, (String[]) ArrayUtils.addAll(MULTI_EXCLUDES, excludeAttributeArray), true, false);
				if (resultsJSON != null && !resultsJSON.startsWith("[") && resultsJSON.length() > 2) {
					resultsJSON = resultsJSON.substring(0, resultsJSON.length() - 1) + " , \"executionTime\" : " + searchResponse.getExecutionTime() + "}";
				}
				return toModelAndView(resultsJSON);
			} else if (type.equalsIgnoreCase(KEYWORD_COMPETENCY)) {
				return toModelAndView(serialize(searchResponse.getSearchResults(), JSON, excludeAttributeArray, true, false));
			} else if (CUL_MATCH.matcher(type).matches()) {
				return toModelAndView(serialize(searchResponse, JSON, excludeAttributeArray, true, true));
			}
			return toModelAndView(serialize(searchResponse, JSON, excludeAttributeArray, true, true));
		} catch (SearchException searchException) {
			response.setStatus(searchException.getStatus().value());
			return toModelAndView(searchException.getMessage());
		}
	}

	private void processRequestBody(SearchRequestBody requestBody, SearchData searchData) {
		if (requestBody != null) {
			logger.info("Search Request Payload : {}", requestBody.toString());
			searchData.setScope(requestBody.getScope());
			searchData.setPretty(searchData.getPretty().equalsIgnoreCase("1") ? searchData.getPretty() : requestBody.getPretty());
			searchData.setCrosswalk(!searchData.isCrosswalk() ? searchData.isCrosswalk() : requestBody.getIsCrosswalk());
			searchData.setFrom(requestBody.getStartAt() > 0 ? requestBody.getStartAt() : searchData.getFrom());
			searchData.setPageNum(requestBody.getPageNum() > 0 ? requestBody.getPageNum() : searchData.getPageNum());
			searchData.setSize(requestBody.getPageSize() >= 0 ? requestBody.getPageSize() : searchData.getSize());
			if (requestBody.getFilters() != null) {
				MapWrapper<Object> searchDataMap = searchData.getParameters();
				requestBody.getFilters().forEach((k, v) -> {
					searchDataMap.put(k, v);
				});
				searchData.setParameters(searchDataMap);
			}
		}
	}

	private void processParameters(boolean disableSpellCheck, SearchData searchData, MapWrapper<Object> searchDataMap) {
		searchDataMap.put("allowDuplicates", true);
		if (searchDataMap.containsKey(FLT_STANDARD) || searchDataMap.containsKey(FLT_STANDARD_DISPLAY) ) {
			searchData.setTaxFilterType(TYPE_STANDARD);
		}
		if (searchDataMap.containsKey(FLT_COURSE)) {
			searchData.setTaxFilterType(TYPE_COURSE);
		}
		if (searchDataMap.containsKey(FLT_DOMAIN)) {
			searchData.setTaxFilterType(DOMAIN);
		}
		if (searchDataMap.containsKey(AGG_BY)) {
			searchDataMap.put("aggBy.field", searchDataMap.getString(AGG_BY));
			searchDataMap.remove(AGG_BY);
		}
		// client controlled value to enable / disable spell check.
		searchDataMap.put("disableSpellCheck", disableSpellCheck);
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
				} else if (parameterKey.equalsIgnoreCase(AGG_BY)) {
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
	
	@RequestMapping(method = RequestMethod.PUT, value = { "/refresh/tenants"})
	public void refreshGlobalTenantsInCache(HttpServletRequest request,@RequestParam(required = false) String sessionToken, HttpServletResponse response,
			final ModelMap model) throws Exception {
		searchService.refreshGlobalTenantsInCache();
	}
	
	@RequestMapping(method = {RequestMethod.GET}, value = "/autocomplete/{type}")
	public ModelAndView searchAutoComplete(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam(required = false) String sessionToken,
			@RequestParam(defaultValue = "8", value="length") Integer pageSize, 
			@RequestParam(defaultValue = "0") String pretty, 
			@RequestParam(defaultValue = "0") Integer startAt,
			@RequestParam(defaultValue = "1", value="start") Integer pageNum, 
			@RequestParam(value="q") String query, 
			@PathVariable String type) throws Exception {
		SearchData searchData = new SearchData();

		// original query string from user
		searchData.setUserQueryString(query);

		if (query.matches(SEARCH_SPLCHR)) {
			throw new BadRequestException("Please enter a valid search term");
		}

		long start = System.currentTimeMillis();

		if (sessionToken == null) {
			sessionToken = BaseController.getSessionToken(request);
		}

		if (query.contains("!")) {
			query = query.replace("!", EMPTY_STRING);
		}
		searchData.setOriginalQuery(query);
		searchData.setQueryString(query);
		searchData.setPretty(pretty);
		searchData.setQueryType(SINGLE);
		searchData.setSessionToken(sessionToken);
		searchData.setUserTenantId(((UserGroupSupport) request.getAttribute(TENANT)).getTenantId());
		searchData.setUserTenantRootId(((UserGroupSupport) request.getAttribute(TENANT)).getTenantRoot());

		if (type.equalsIgnoreCase(TYPE_PUBLISHER) || type.equalsIgnoreCase(SEARCH_QUERY) || type.equalsIgnoreCase(KEYWORD)) {
			String expandedQuery = searchData.getQueryString().replace("\"", EMPTY_STRING).replace(" ", "_").replaceAll("([^a-z0-9A-Z_])", "\\\\$1");
			if (expandedQuery == EMPTY_STRING) {
				expandedQuery = STAR;
			}
			if (type.equalsIgnoreCase(KEYWORD)) {
				searchData.setQueryString(expandedQuery);
				type = SearchHandlerType.AUTOCOMPLETE_KEYWORD.name();
			} else if (type.equalsIgnoreCase(PUBLISHER)) {
				expandedQuery = expandedQuery.replaceAll(FIND_SPECIAL_CHARACTERS_REGEX, EMPTY_STRING);
				searchData.setQueryString(expandedQuery);
			} else if (type.equalsIgnoreCase(SEARCH_QUERY)) {
				String queryString = "querysuggestion: " + expandedQuery + " OR querysuggestion: " + expandedQuery + STAR;
				searchData.setQueryString(queryString);
			}
		} else {
			throw new BadRequestException("Invalid Autocomplete Type!");
		}

		searchData.setType(type);
		searchData.setFrom(startAt > 0 ? startAt : 0);
		searchData.setPageNum(pageNum > 0 ? pageNum : 1);
		searchData.setSize(pageSize >= 0 ? pageSize : 8);
		if (searchData.getFrom() < 1) {
			searchData.setFrom((searchData.getPageNum() - 1) * searchData.getSize());
		}
		searchData.setRemoteAddress(request.getRemoteAddr());
		User apiCaller = (User) request.getAttribute(USER);
		searchData.setUser(apiCaller);
		if (!(type.equalsIgnoreCase(SEARCH_QUERY) || type.equalsIgnoreCase(KEYWORD))) {
			searchData.setRestricted(false);
		}

		String excludeAttributeArray[] = {};
		try {
			SearchResponse<Object> searchResponse = SearchHandler.getSearcher(type.toUpperCase()).search(searchData);
			logger.info("Elapsed time to complete search process :" + (System.currentTimeMillis() - start) + " ms");
			searchResponse.setExecutionTime(System.currentTimeMillis() - start);

			setEventLogObject(request, searchData, searchResponse);

			if (type.equalsIgnoreCase(SearchHandlerType.AUTOCOMPLETE_KEYWORD.name()) || type.equalsIgnoreCase(SEARCH_QUERY) || type.equalsIgnoreCase(TYPE_PUBLISHER)) {
				return toModelAndView(serialize(searchResponse.getSearchResults(), JSON, excludeAttributeArray, true, false));
			}
			return toModelAndView(serialize(searchResponse, JSON, excludeAttributeArray, true, false));
		} catch (SearchException searchException) {
			response.setStatus(searchException.getStatus().value());
			return toModelAndView(searchException.getMessage());
		}
	}

	private void setEventLogObject(HttpServletRequest request, SearchData searchData, SearchResponse<Object> searchResponse) throws JSONException {
		Map<String, Object> payloadObject = new HashMap<>();
		Map<String, Object> session = new HashMap<>();
		SessionContextSupport.putLogParameter(EventConstants.EVENT_NAME, EventConstants.ITEM_DOT_SEARCH);
		payloadObject.put(EventConstants.TEXT, searchData.getOriginalQuery());
		if (searchData.getSessionToken() != null) {
			session.put(EventConstants.SESSION_TOKEN, searchData.getSessionToken());
			session.put(EventConstants.PARTNER_ID, request.getAttribute(EventConstants.PARTNER_ID));
			session.put(EventConstants.APP_ID, request.getAttribute(EventConstants.APP_ID));
			session.put(EventConstants.TENANT_ID, searchData.getUserTenantId());
			session.put(EventConstants.TENANT_ROOT, searchData.getUserTenantRootId());
		}
		SessionContextSupport.putLogParameter(EventConstants.SESSION, session);
		payloadObject.put(EventConstants.PAGE_SIZE,  searchResponse.getStats().get(EventConstants.MAX));
		payloadObject.put(EventConstants.START_AT,  searchResponse.getStats().get(EventConstants.OFFSET));
		payloadObject.put(EventConstants.RESULT_SIZE, searchResponse.getStats().get(EventConstants.COUNT));
		payloadObject.put(EventConstants.HIT_COUNT, searchResponse.getStats().get(EventConstants.TOTAL));
		SessionContextSupport.putLogParameter(EventConstants.PAYLOAD_OBJECT, payloadObject);
		request.setAttribute(EventConstants.ACTION, SEARCH);
	}

}
