package org.ednovo.gooru.controllers.api.v2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.ednovo.gooru.controllers.api.BaseController;
import org.ednovo.gooru.search.es.constant.Constants;
import org.ednovo.gooru.search.es.constant.SearchInputType;
import org.ednovo.gooru.search.es.exception.BadRequestException;
import org.ednovo.gooru.search.es.exception.SearchException;
import org.ednovo.gooru.search.es.handler.SearchHandler;
import org.ednovo.gooru.search.es.handler.SearchHandlerType;
import org.ednovo.gooru.search.es.model.MapWrapper;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.ednovo.gooru.search.es.model.SessionContextSupport;
import org.ednovo.gooru.search.es.model.User;
import org.ednovo.gooru.search.es.model.UserGroupSupport;
import org.ednovo.gooru.search.es.processor.util.SerializerUtil;
import org.ednovo.gooru.search.es.service.SearchService;
import org.ednovo.gooru.search.es.service.SearchSettingService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = { "/v2/search" })
public class SearchV2RestController  extends SerializerUtil implements Constants {
	
	@Autowired
	private SearchService searchService;
	
	protected static final Logger logger = LoggerFactory.getLogger(SearchV2RestController.class);
	
	@RequestMapping(method = RequestMethod.GET, value = { "/index/filters"})
	public ModelAndView getSearchFilters(HttpServletRequest request,@RequestParam(required = false) String sessionToken, @RequestParam(value = "codeId", required = false) Integer codeId, @RequestParam(value = "type", required = false, defaultValue = "resource") String type, HttpServletResponse response,
			final ModelMap model) throws Exception {
		
		return toModelAndView(serializeToJson(searchService.getSearchFilters(codeId, type), SEARCH_FORMAT));
	}
	  
	@RequestMapping(method = {RequestMethod.GET}, value = "/{type}")
	public ModelAndView search(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam(required = false) String sessionToken,
			@RequestParam(defaultValue = "8", value="length") Integer pageSize, 
			@RequestParam(defaultValue = "0") String pretty, 
			@RequestParam(defaultValue = "0") Integer startAt,
			@RequestParam(defaultValue = "1", value="start") Integer pageNum, 
			@RequestParam(value="q") String query,
			@RequestParam(required = false) String info, 
			@RequestParam(required = false) String excludeAttributes, 
			@RequestParam(required = false) String facet, 
			@RequestParam(required = false, defaultValue= "false") boolean userDetails, 
			@PathVariable String type, 
			@RequestParam(required = false, defaultValue= "false") boolean includeCollectionItem,
			@RequestParam(required = false, defaultValue= "false") boolean showSingleSubjectResults,
			@RequestParam(required = false) String protocolSupported, 
			@RequestParam(required = false, defaultValue= "false") boolean includeCIMetaData, 
			@RequestParam(required = false, defaultValue= "false") boolean bsSearch,
			@RequestParam(required = false, defaultValue= "true") boolean showCanonicalOnly,
			@RequestParam(required = false, defaultValue= "true") boolean isCrosswalk,
			@RequestParam(required = false, defaultValue = "false") boolean disableSpellCheck) throws Exception {

		SearchData searchData = new SearchData();
		searchData.setShowSingleSubjectResults(showSingleSubjectResults);
		if (type.contains(TYPE_SCOLLECTION) || type.contains(RESOURCE)) {
			if (type.equalsIgnoreCase(TYPE_SCOLLECTION)) {
				request.setAttribute(SEARCH_TYPE, COLLECTION);
			} else {
				request.setAttribute(SEARCH_TYPE, type);
			}
		}

		// original query string from user
		searchData.setUserQueryString(query);

		/**
		 * Here, when no filter is chosen, * search and keyword request with length less than 3 without * are skipped.
		 **/
		if (type.equalsIgnoreCase(TYPE_SCOLLECTION) || type.equalsIgnoreCase(RESOURCE)) {
			query = checkQueryValidity(query, (Map<String, Object>) request.getParameterMap());
		}

		if (query.matches(SEARCH_SPLCHR)) {
			throw new BadRequestException("Please Enter the Valid Query for search Resources");
		}
		// FIXME Duplicate attribute of searchType
		JSONObject payloadObject = new JSONObject();
		JSONObject session = new JSONObject();
		SessionContextSupport.putLogParameter("eventName", "item.search");
		payloadObject.put("text", query);
		long start = System.currentTimeMillis();
		User apiCaller = (User) request.getAttribute(USER);
		
        // Set content cdn url 
		String contentCdnUrl = (String) request.getAttribute(Constants.CONTENT_CDN_URL);
		searchData.setContentCdnUrl(contentCdnUrl);
		
		// Set user permits
		UserGroupSupport userGroup = (UserGroupSupport) request.getAttribute(Constants.TENANT);
		List<String> userPermits = new ArrayList<>();
		String userTenantId = userGroup.getTenantId();
		userPermits.add(userTenantId);
		List<String> discoverableTenantIds = SearchSettingService.getDiscoverableTenantIds(Constants.DISCOVERABLE_TENANT_IDS);
		if (discoverableTenantIds != null && !discoverableTenantIds.isEmpty())
			userPermits.addAll(discoverableTenantIds);
		searchData.setUserPermits(userPermits.stream().distinct().collect(Collectors.toList()));

		if (sessionToken == null) {
			sessionToken = BaseController.getSessionToken(request);
		}

		if (sessionToken != null) {
			session.put(SESSION_TOKEN_SEARCH, sessionToken);
			session.put(PARTNER_ID, request.getAttribute(PARTNER_ID));
			session.put(APP_ID, request.getAttribute(APP_ID));
			session.put(TENANT_ID, userTenantId);
			session.put(API_KEY, "");
			session.put(Constants.SEARCH_ORGANIZATION_UID, "");
		}

		SessionContextSupport.putLogParameter(SESSION_SEARCH, session);
		/*if (!(type.equalsIgnoreCase(SEARCH_QUERY) || type.equalsIgnoreCase(AUTO_COMPLETE))) {
			PartyCustomField partyCustomField = partyService.getPartyCustomeField(apiCaller.getPartyUid(), USER_TAXONOMY_ROOT_CODE, null);
			if (partyCustomField != null) {
				searchData.setUserTaxonomyPreference(partyCustomField.getOptionalValue());
			} else {
				final String taxonomyCodeIds = (partyCustomField != null && partyCustomField.getOptionalValue() != null && partyCustomField.getOptionalValue().length() > 0)
						? partyCustomField.getOptionalValue()
						: this.getTaxonomyRespository().getFindTaxonomyList(settingService.getConfigSetting(ConfigGOORU_EXCLUDE_TAXONOMY_PREFERENCE, 0, apiCaller.getOrganization().getPartyUid()));
				searchData.setUserTaxonomyPreference(taxonomyCodeIds);
			}
		}

		Set<UserRoleAssoc> userRoleAssocs = (Set<UserRoleAssoc>) apiCaller.getUserRoleSet();
		for (UserRoleAssoc set : userRoleAssocs) {
			if (set.getRole().getName().toString().equalsIgnoreCase(UserRole.UserRoleType.SUPER_ADMIN.getType().toString())) {
				searchData.skipType(SearchProcessorType.BlackListQueryValidation);
			}
		}*/
		searchData.setUserTaxonomyPreference((JSONObject) request.getAttribute(Constants.USER_PREFERENCES));
		MapWrapper<Object> searchDataMap = new MapWrapper<Object>(request.getParameterMap());

		searchDataMap.put("allowDuplicates", true);
		searchDataMap.put("includeCollectionItem", includeCollectionItem);
		searchDataMap.put("includeCIMin", includeCIMetaData);
		if (searchDataMap.containsKey("flt.standard") || searchDataMap.containsKey("flt.standardDisplay")) {
			searchData.setStandardsSearch(true);
		}

		// client controlled value to enable / disable spell check.
		searchDataMap.put("disableSpellCheck", disableSpellCheck);
		searchData.setParameters(searchDataMap);

		if (query.contains("!")) {
			query = query.replace("!", "");
		}
		searchData.setOriginalQuery(query);
		searchData.setQueryString(query);
		searchData.setPretty(pretty);
		searchData.setQueryType(SINGLE);
		searchData.setSessionToken(sessionToken);
		searchData.setBsSearch(bsSearch);
		searchData.setShowCanonicalOnly(showCanonicalOnly);
		searchData.setCrosswalk(isCrosswalk);

		if (facet != null && facet.trim().length() > 0) {
			searchData.setFacet(facet);
		}
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
		} else if (type.equalsIgnoreCase(TYPE_PUBLISHER) || type.equalsIgnoreCase(TYPE_AGGREGATOR)) {
			searchData.setType(type);
			searchData.setQueryString(searchData.getQueryString() + "*");
		} else if (type.equalsIgnoreCase(TYPE_ATTRIBUTION) || type.equalsIgnoreCase(TYPE_SOURCE)) {
			String Suggestions = searchData.getQueryString();
			if (Suggestions != null) {
				Suggestions = Suggestions.replaceAll("\\s", "_");
			}
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
		} else if (type.equalsIgnoreCase(COLLECTION_QUIZ)) {
			type = TYPE_LIBRARY;
		} else if (type.equalsIgnoreCase(SEARCH_QUERY) || type.equalsIgnoreCase(AUTO_COMPLETE)) {
			String expandedQuery = searchData.getQueryString().replace("\"", "").replace(" ", "_").replaceAll("([^a-z0-9A-Z_])", "\\\\$1");
			if (expandedQuery == "") {
				expandedQuery = "*";
			}
			String queryString = "querysuggestion: " + expandedQuery + " OR querysuggestion: " + expandedQuery + "*";
			searchData.setQueryString(queryString);
			if (type.equalsIgnoreCase(AUTO_COMPLETE)) {
				type = SearchHandlerType.AUTOCOMPLETE.name();
			}
		} else if (type.equalsIgnoreCase(COLLECTION_SUGGEST)) {
			type = COLLECTION;
		} else if (type.equalsIgnoreCase(QUIZ_SUGGEST)) {
			type = TYPE_QUIZ;
		} else if(type.equalsIgnoreCase(TYPE_USER)) {
			throw new SearchException(HttpStatus.NOT_IMPLEMENTED, "Not supported");
		}

		searchData.setType(type);
		searchData.setFrom(startAt);
		searchData.setPageNum(pageNum);
		if (type.equalsIgnoreCase(TYPE_SCOLLECTION) && includeCollectionItem) {
			searchData.setSize(5);
		} else {
			searchData.setSize(pageSize);
		}
		if (searchData.getFrom() < 1) {
			searchData.setFrom((pageNum - 1) * searchData.getSize());
		}
		searchData.setRemoteAddress(request.getRemoteAddr());
		searchData.setUser(apiCaller);
		if (!(type.equalsIgnoreCase(SEARCH_QUERY) || type.equalsIgnoreCase(AUTO_COMPLETE))) {
			// searchData.setRestricted(hasUnrestrictedContentAccess());
			searchData.setRestricted(false);
		}

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
		if (includeCIMetaData == true) {
			excludeAttributeArray = (String[]) ArrayUtils.addAll(COLLECTION_ITEM_EXCLUDES, excludeAttributeArray);
		}

		payloadObject.put("pageSize", pageSize);
		payloadObject.put("pageNum", pageNum);
		payloadObject.put("startAt", startAt);

		request.setAttribute("action", "search");
		try {
			SearchResponse<Object> searchResponse = SearchHandler.getSearcher(type.toUpperCase()).search(searchData);
			logger.info("Elapsed time to complete search process :" + (System.currentTimeMillis() - start) + " ms");
			searchResponse.setExecutionTime(System.currentTimeMillis() - start);

			payloadObject.put("resultSize", searchResponse.getResultCount());
			payloadObject.put("hitCount", searchResponse.getTotalHitCount());
			payloadObject.put("searchExecutionTime", searchResponse.getExecutionTime());
			SessionContextSupport.putLogParameter("payLoadObject", payloadObject);

			if (type.equalsIgnoreCase(RESOURCE)) {
				if (!userDetails) {
					// excludeAttributeArray = (String[]) ArrayUtils.addAll(excludeAttributeArray, new String[]{"*.user"});
					excludeAttributeArray = (String[]) ArrayUtils.addAll(excludeAttributeArray, new String[] {});
				}
				return toModelAndView(serialize(searchResponse, JSON, (String[]) ArrayUtils.addAll(SINGLE_EXCLUDES, excludeAttributeArray), true, true));
			} else if (type.equalsIgnoreCase(SearchHandlerType.MULTI_RESOURCE.name()) && searchDataMap.getString(QUERY_TYPE) != null) {
				Object serializeResult = searchResponse;
				if (searchDataMap.getString(SearchInputType.FETCH_HITS_IN_MULTI.getName()).equals(SearchInputType.FETCH_HITS_IN_MULTI.getDefaultValue())) {
					if (searchResponse.getSearchResults() instanceof List && ((List) searchResponse.getSearchResults()).size() > 0) {
						serializeResult = ((List) searchResponse.getSearchResults()).get(0);
					} else {
						serializeResult = null;
					}
				}

				String resultsJSON = serialize(serializeResult, JSON, (String[]) ArrayUtils.addAll(MULTI_EXCLUDES, excludeAttributeArray), true, false);
				if (resultsJSON != null && !resultsJSON.startsWith("[") && resultsJSON.length() > 2) {
					resultsJSON = resultsJSON.substring(0, resultsJSON.length() - 1) + " , \"executionTime\" : " + searchResponse.getExecutionTime() + "}";
				}
				return toModelAndView(resultsJSON);
			} else if (type.equalsIgnoreCase(TYPE_LIBRARY)) {
				return toModelAndView(searchResponse.getSearchResults().toString());
			} else if (type.equalsIgnoreCase(TYPE_ATTRIBUTION) || type.equalsIgnoreCase(SEARCH_QUERY) || type.equalsIgnoreCase(TYPE_PUBLISHER) || type.equalsIgnoreCase(TYPE_AGGREGATOR)) {
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
