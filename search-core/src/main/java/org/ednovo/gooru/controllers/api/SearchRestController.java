


package org.ednovo.gooru.controllers.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
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
import org.ednovo.gooru.search.es.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = { "/search" })
public class SearchRestController extends BaseController implements Constants{
	
	@Autowired
	private SearchService searchService;
	
	protected static final Logger logger = LoggerFactory.getLogger(SearchRestController.class);

	
	@RequestMapping(method = RequestMethod.GET, value = { "/index/filters", "/v2/filters" })
	public ModelAndView getSearchFilters(HttpServletRequest request, @RequestParam(required = false) String sessionToken, @RequestParam(value = "codeId", required = false) Integer codeId, @RequestParam(value = "type", required = false, defaultValue = "resource") String type, HttpServletResponse response,
			final ModelMap model) throws Exception {
		return toModelAndView(serializeToJson(getSearchService().getSearchFilters(codeId, type), SEARCH_FORMAT));
	}
		
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/{type}")
	public ModelAndView search(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false) String sessionToken, @RequestParam(defaultValue = "8") Integer pageSize, @RequestParam(defaultValue = "0") String pretty, @RequestParam(defaultValue = "0") Integer startAt,
			@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "*") String query, @RequestParam(required = false) String excludeAttributes, @RequestParam(required = false) String facet, @RequestParam(required = false, defaultValue= "false") boolean userDetails, @PathVariable String type, @RequestParam(required = false, defaultValue= "false") boolean includeCollectionItem,@RequestParam(required = false, defaultValue= "false") boolean includeCIMetaData , @RequestParam(required = false) String protocolSupported, @RequestParam(required = false, defaultValue= "false") boolean bsSearch, @RequestParam(required = false, defaultValue= "true") boolean showCanonicalOnly, @RequestParam(required = false, defaultValue= "false") boolean disableSpellCheck,@RequestParam(required = false) String contributorType) {
	
		SearchData searchData = new SearchData();
		  if(type.contains("scollection")|| type.contains("resource")){
			  if(type.equalsIgnoreCase("scollection")){
				  request.setAttribute("type", new String("collection"));
			  }
			  else {
				  request.setAttribute("type", type);
			  }
		  }
		  searchData.setOriginalQuery(query);
			User apiCaller = (User) request.getAttribute(Constants.USER);
			if(sessionToken == null){
			 sessionToken = getSessionToken(request);
			} 
			//Disabled while removing api-jar dependency
			/*String oAuthToken = request.getHeader("OAuth-Authorization");
			if (oAuthToken != null) {
				if (oAuthToken.contains("Bearer")) {
						try{
							oAuthToken = StringUtils.substringAfterLast(oAuthToken, "Bearer").trim();
							apiCaller = oAuthService.getUserByOAuthAccessToken(BaseUtil.extractToken(oAuthToken));
						}catch(Exception e){
							throw new AccessDeniedException("Invalid OAuth Token : " + oAuthToken);
						}
				}else  if((StringUtils.trimToNull(sessionToken) == null || sessionToken.equalsIgnoreCase("NA")|| sessionToken.equalsIgnoreCase("null") || sessionToken.equalsIgnoreCase("undefined") || apiCaller == null)){
		      				//throw new AccessDeniedException("Invalid SessionToken : " + sessionToken);
	    		 }
			}*/
		// FIXME Duplicate attribute of searchType
		SessionContextSupport.putLogParameter("eventName",  "item.search");
		
		long start = System.currentTimeMillis();
		
		//Disabled while removing api-jar dependency
		/*if (!(type.equalsIgnoreCase(SEARCH_QUERY) || type.equalsIgnoreCase(AUTO_COMPLETE))) {		
			PartyCustomField partyCustomField = partyService.getPartyCustomeField(apiCaller.getPartyUid(),USER_TAXONOMY_ROOT_CODE, null);
			if(partyCustomField != null){
				searchData.setUserTaxonomyPreference(partyCustomField.getOptionalValue());
			}else{
				final String taxonomyCodeIds = (partyCustomField != null && partyCustomField.getOptionalValue() != null && partyCustomField.getOptionalValue().length() > 0) ? partyCustomField.getOptionalValue() : this.getTaxonomyRespository().getFindTaxonomyList(
						settingService.getConfigSetting(ConfigConstants.GOORU_EXCLUDE_TAXONOMY_PREFERENCE, 0, apiCaller.getOrganization().getPartyUid()));
				searchData.setUserTaxonomyPreference(taxonomyCodeIds);
			}
		}*/
		//Disabled while removing api-jar dependency
		/*Set<UserRoleAssoc> userRoleAssocs =(Set<UserRoleAssoc>) apiCaller.getUserRoleSet();
		for(UserRoleAssoc set:userRoleAssocs){
			if(set.getRole().getName().toString().equalsIgnoreCase(UserRole.UserRoleType.SUPER_ADMIN.getType().toString())){
			 searchData.skipType(SearchProcessorType.BlackListQueryValidation);
			}
		}
		if((apiCaller.getUserRoleSetString().contains(CONTENT_ADMIN)||apiCaller.getUserRoleSetString().contains(UserRole.UserRoleType.SUPER_ADMIN.getType().toString()))){
			searchData.setActiveEnable(true);
			searchData.setUser(apiCaller);
		}*/
		
		
		MapWrapper<Object> searchDataMap = new MapWrapper<Object>(request.getParameterMap());
		
		searchDataMap.put("allowDuplicates", true);
		searchDataMap.put("includeCollectionItem", includeCollectionItem);
		searchDataMap.put("includeCIMin",includeCIMetaData);
        //client controlled value to enable / disable spell check.
        searchDataMap.put("disableSpellCheck", disableSpellCheck);

		// This temp fix for http://collab.ednovo.org/jira/browse/DO-4187 
		
	  	if(searchDataMap.containsKey("flt.audience")) {
			searchDataMap.put("flt.audience",searchDataMap.getString("flt.audience").toLowerCase());
	    }
		
		if(searchDataMap.containsKey("flt.depthOfknowledge")){
			searchDataMap.put("flt.depthOfknowledge",searchDataMap.getString("flt.depthOfknowledge").toLowerCase());
		}
     
		 if(searchDataMap.containsKey("flt.educationalUse")){
			 searchDataMap.put("flt.educationalUse",searchDataMap.getString("flt.educationalUse").toLowerCase());
		 }
		 if(searchDataMap.containsKey("flt.momentsofLearning")){
			 searchDataMap.put("flt.momentsofLearning",searchDataMap.getString("flt.momentsofLearning").toLowerCase());
		 }	
		
		if(searchDataMap.containsKey("flt.learningAndInovation")){
			searchDataMap.put("flt.learningAndInovation",searchDataMap.getString("flt.learningAndInovation").toLowerCase());
		}
		if(searchDataMap.containsKey("flt.instructionalMethod")){
			searchDataMap.put("flt.instructionalMethod",searchDataMap.getString("flt.instructionalMethod").toLowerCase());
		}
				
		if(query.matches(SEARCH_SPLCHR)){
			throw new BadRequestException("Please Enter the Valid Query for search Resources");
		}
		
		
		if(searchDataMap.containsKey("flt.standard")){
			String oldStandards = searchDataMap.getString("flt.standard");
			
			String newStandards = new String();
			int count = 0;
			for(String standard : oldStandards.split(",")){
				if(count > 0){
					newStandards += ",";
				}
				//Disabled while removing api-jar dependency
				/*if(redisService.getStandardValue(standard) != null){
					newStandards += redisService.getStandardValue(standard);
				}
				else {*/
					newStandards += standard;
				//}
				count++;
			}
			searchDataMap.put("flt.standard", newStandards);
		}
		searchData.setPageNum(pageNum);
	    
	    if(query.contains("!")) {
	    	query = query.replace("!","");
	    }
		searchData.setQueryString(query);
        //original query string from user
        searchData.setUserQueryString(query);
		searchData.setParameters(searchDataMap);
		searchData.setPretty(pretty);
		searchData.setQueryType(SINGLE);
		searchData.setSessionToken((String)request.getAttribute(SESSION_TOKEN_SEARCH));
		searchData.setBsSearch(bsSearch);
		searchData.setShowCanonicalOnly(showCanonicalOnly);
		
		//set protocol supported
		if (!(type.equalsIgnoreCase(SEARCH_QUERY) || type.equalsIgnoreCase(AUTO_COMPLETE))) {
			//Disabled while removing api-jar dependency
			//this.setRequestProtocol(protocolSupported, searchDataMap, request, searchData);
		}
		if (facet != null && facet.trim().length() > 0) {
			searchData.setFacet(facet);
		}
		 if (type.equalsIgnoreCase("skills")) {
			 searchData.setQueryString("(" + query.toLowerCase() + "*) OR (*" +query.toLowerCase() + "*)");
			 searchDataMap.put("fltNot.depth", new String[]{"1","2"});
		 }
		 
		 if (type.equalsIgnoreCase(CONTRIBUTOR)) {
			 if(query.length()< 3) {
				 throw new BadRequestException("Characters should Not less than three");
			 }
			 searchData.setQueryString("("+query.toLowerCase()+"*)");
			 searchData.setType(CONTRIBUTOR);
			 searchData.setUser(apiCaller);
			 searchData.setContributorType(contributorType);
		 }
		 
		if (type.equalsIgnoreCase(TYPE_STANDARD) || type.equalsIgnoreCase(STANDARD_CODE)) {
			if (type.equalsIgnoreCase(TYPE_STANDARD)) {
				String expandedQuery = searchData.getQueryString().replace(".", "").replace(".--", "").replace("-", "").replace("\"", "").replace(" ", "_");
				searchData.setQueryString("(" + expandedQuery + "*) OR (*" + expandedQuery + "*)");
				searchDataMap.put("searchBy", "standard");
				searchData.setOriginalQuery(expandedQuery);
			}
			if (type.equalsIgnoreCase(STANDARD_CODE) ) {
				searchDataMap.put("searchBy", "standardCode");
			}
			searchData.setParameters(searchDataMap);
			type = TYPE_TAXONOMY;
			//Attribution & Aggergation
			
		} else if ( type.equalsIgnoreCase(PUBLISHER)||type.equalsIgnoreCase(AGGREGATOR)) {
			searchData.setType(type);
			searchData.setQueryString(searchData.getQueryString().replaceAll("\\:", "\\\\:") + " OR " + searchData.getQueryString().replaceAll("([^a-z0-9A-Z])", "\\\\$1") + "*");
		} else if(type.equalsIgnoreCase(TYPE_ATTRIBUTION)||type.equalsIgnoreCase(TYPE_SOURCE)){
			String Suggestions = searchData.getQueryString();
			if (Suggestions != null) {
				Suggestions = Suggestions.replaceAll("\\s", "_");
				
			}
			searchData.setType(type);
			searchData.setQueryString(searchData.getQueryString() + "*");
		} else if (type.equalsIgnoreCase(Constants.RESOURCE) && searchDataMap.getString(QUERY_TYPE) != null) {
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
			if(expandedQuery == ""){
				expandedQuery = "*";
			}
			String queryString = "querysuggestion: " + expandedQuery + " OR querysuggestion: " + expandedQuery + "*";
			searchData.setQueryString(queryString);
			
			if (type.equalsIgnoreCase(AUTO_COMPLETE)) {
				type = SearchHandlerType.AUTOCOMPLETE.name();
			}
		} else if (type.equalsIgnoreCase(COLLECTION_SUGGEST)) {
			type = Constants.COLLECTION;
		} else if (type.equalsIgnoreCase(QUIZ_SUGGEST)) {
			type = TYPE_QUIZ;
		}

		searchData.setType(type);
		searchData.setFrom(startAt);
		if(type.equalsIgnoreCase(TYPE_SCOLLECTION) && includeCollectionItem){
			searchData.setSize(5);
		}
		else{
			searchData.setSize(pageSize);			
		}
		if (searchData.getFrom() < 1) {
			searchData.setFrom((pageNum - 1) * searchData.getSize());
		}
		searchData.setRemoteAddress(request.getRemoteAddr());
		searchData.setUser(apiCaller);
		//Disabled while removing api-jar dependency
		/*if (!(type.equalsIgnoreCase(SEARCH_QUERY) || type.equalsIgnoreCase(AUTO_COMPLETE))) {
			searchData.setRestricted(hasUnrestrictedContentAccess());
		}*/

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
		if(includeCIMetaData == true){
			excludeAttributeArray = (String[]) ArrayUtils.addAll(COLLECTION_ITEM_EXCLUDES, excludeAttributeArray);
		}
		
		//Disabled while removing api-jar dependency
		SessionContextSupport.putLogParameter("pageSize", pageSize);
		SessionContextSupport.putLogParameter("pageNum", pageNum);
		SessionContextSupport.putLogParameter("startAt", startAt);
		SessionContextSupport.putLogParameter("queryType", searchData.getQueryType());
		request.setAttribute("action","search");
		try {
			SearchResponse<Object> searchResponse = SearchHandler.getSearcher(type.toUpperCase()).search(searchData);
			if(logger.isDebugEnabled()){
				logger.debug("Elapsed time to complete search process :" +(System.currentTimeMillis() - start) + " ms");
			}
			searchResponse.getStats().put("executionTime", System.currentTimeMillis() - start);
			searchResponse.getStats().put("pageResultCount", searchResponse.getResultCount());
			searchResponse.setExecutionTime(System.currentTimeMillis() - start);
			//Disabled while removing api-jar dependency
			SessionContextSupport.putLogParameter("resultSize", searchResponse.getResultCount());
			SessionContextSupport.putLogParameter("resourceGooruOIds", searchData.getResourceGooruOIds());
			SessionContextSupport.putLogParameter("hitCount", searchResponse.getTotalHitCount());
			SessionContextSupport.putLogParameter("searchExecutionTime", searchResponse.getExecutionTime());
			if (type.equalsIgnoreCase(Constants.RESOURCE)) {
				if(!userDetails){
					//excludeAttributeArray = (String[]) ArrayUtils.addAll(excludeAttributeArray, new String[]{"*.user","*.creator"});
					excludeAttributeArray = (String[]) ArrayUtils.addAll(excludeAttributeArray, new String[]{"*.user"});
				}
				return toModelAndView(serialize(searchResponse, JSON, (String[]) ArrayUtils.addAll(SINGLE_EXCLUDES, excludeAttributeArray), true,true));
			} else if (type.equalsIgnoreCase(SearchHandlerType.MULTI_RESOURCE.name()) && searchDataMap.getString(QUERY_TYPE) != null) {
				Object serializeResult = searchResponse;
				if (searchDataMap.getString(SearchInputType.FETCH_HITS_IN_MULTI.getName()).equals(SearchInputType.FETCH_HITS_IN_MULTI.getDefaultValue())) {
					if (searchResponse.getSearchResults() instanceof List && ((List) searchResponse.getSearchResults()).size() > 0) {
						serializeResult = ((List) searchResponse.getSearchResults()).get(0);
					} else {
						serializeResult = null;
					}
				}
				
				String resultsJSON = serialize(serializeResult, JSON, (String[]) ArrayUtils.addAll(MULTI_EXCLUDES, excludeAttributeArray), true, true);
				if (resultsJSON != null && !resultsJSON.startsWith("[") && resultsJSON.length() > 2) {
					resultsJSON = resultsJSON.substring(0, resultsJSON.length() - 1) + " , \"executionTime\" : " + searchResponse.getExecutionTime() + "}";
				}
				return toModelAndView(resultsJSON);
			} else if (type.equalsIgnoreCase(TYPE_LIBRARY)) {
				return toModelAndView(searchResponse.getSearchResults().toString());
			} else if (type.equalsIgnoreCase(TYPE_ATTRIBUTION) || type.equalsIgnoreCase(SEARCH_QUERY) || type.equalsIgnoreCase(PUBLISHER) || type.equalsIgnoreCase(AGGREGATOR)) {
				return toModelAndView(serialize(searchResponse.getSearchResults(), JSON, excludeAttributeArray, true, true));
			}
			return toModelAndView(serialize(searchResponse, JSON, excludeAttributeArray, true, true));
		} catch (SearchException searchException) {
			response.setStatus(searchException.getStatus().value());
			return toModelAndView(searchException.getMessage());
		} catch (BadRequestException badRequestException) {
			response.setStatus(400);
			return toModelAndView(badRequestException.getMessage());
		}
			
	}

		public SearchService getSearchService() {
			return searchService;
		}

		public void setSearchService(SearchService searchService) {
			this.searchService = searchService;
		}
	 
}

