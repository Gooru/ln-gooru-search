/**
 * 
 */
package org.ednovo.gooru.search.es.service;

import org.ednovo.gooru.search.es.constant.Constants;
import org.ednovo.gooru.search.es.constant.SearchInputType;
import org.ednovo.gooru.search.es.handler.SearchHandler;
import org.ednovo.gooru.search.es.handler.SearchHandlerType;
import org.ednovo.gooru.search.es.model.MapWrapper;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.SearchRequestData;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.ednovo.gooru.search.es.model.User;
import org.springframework.stereotype.Service;

/**
 * @author SearchTeam
 * 
 */
@Service
public class EsSearchV2ServiceImpl implements SearchV2Service, Constants {
	
	@Override
	public SearchResponse<Object> search(MapWrapper<Object> searchDataMap, SearchRequestData  searchRequestData, User apiCaller, SearchData searchData, String type) {
		/*PartyCustomField partyCustomField = partyService.getPartyCustomeField(apiCaller.getPartyUid(), TAXONOMY_ROOT_CODE_USER, null);
		if(partyCustomField != null){
				searchData.setUserTaxonomyPreference(partyCustomField.getOptionalValue());
		}*/
		searchData.setUserTaxonomyPreference(null);
		/*Set<UserRoleAssoc> userRoleAssocs =(Set<UserRoleAssoc>) apiCaller.getUserRoleSet();
		for(UserRoleAssoc set:userRoleAssocs){
			if(set.getRole().getName().toString().equalsIgnoreCase(UserRole.UserRoleType.SUPER_ADMIN.getType().toString())){
			 searchData.skipType(SearchProcessorType.BlackListQueryValidation);
			}
			
		}*/
		
		searchDataMap.put("allowDuplicates", searchRequestData.isAllowDuplicates());
		searchDataMap.put("includeCollectionItem", searchRequestData.isIncludeCollectionItem());	
		searchDataMap.put("includeCIMin",searchRequestData.isIncludeCIMetaData());
	
		
		searchData.setParameters(searchDataMap);
		searchData.setPretty(searchRequestData.getPretty().toString());
		searchData.setQueryType(SINGLE);
		searchData.setBsSearch(searchRequestData.isBsSearch());
		
		if (searchRequestData.getFacet() != null && searchRequestData.getFacet().trim().length() > 0) {
			searchData.setFacet(searchRequestData.getFacet());
		}
		if (type.equalsIgnoreCase(TYPE_STANDARD) || type.equalsIgnoreCase(STANDARD_CODE)) {
			if (type.equalsIgnoreCase(TYPE_STANDARD)) {
				String expandedQuery = searchData.getQueryString().replace(".", "").replace(".--", "").replace("-", "").replace("\"", "").replace(" ", "_");
				searchData.setQueryString("(" + expandedQuery + "*) OR (*" + expandedQuery + "*)");
				searchDataMap.put("searchBy", "standard");
				searchData.setOriginalQuery(expandedQuery);
			}
			if (type.equalsIgnoreCase(STANDARD_CODE)) {
				searchDataMap.put("searchBy", STANDARD_CODE);
			}
			searchData.setParameters(searchDataMap);
			type = TYPE_TAXONOMY;
			
		} else if ( type.equalsIgnoreCase(TYPE_PUBLISHER)||type.equalsIgnoreCase(TYPE_AGGREGATOR)) {
			searchData.setType(type);
			searchData.setQueryString(searchData.getQueryString() + "*");
		} else if(type.equalsIgnoreCase(TYPE_ATTRIBUTION)||type.equalsIgnoreCase(TYPE_SOURCE)){
			String suggestions = searchData.getQueryString();
			if (suggestions != null) {
				suggestions = suggestions.replaceAll("\\s", "_");
			}
			searchData.setType(type);
			searchData.setQueryString(searchData.getQueryString() + "*");
		} else if (type.equalsIgnoreCase(TYPE_RESOURCE) && searchDataMap.getString(QUERY_TYPE) != null) {
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
			if(expandedQuery.equals("")){
				expandedQuery = "*";
			}
			String queryString = "querysuggestion: " + expandedQuery + " OR querysuggestion: " + expandedQuery + "*";
			searchData.setQueryString(queryString);
			
			if (type.equalsIgnoreCase(AUTO_COMPLETE)) {
				type = SearchHandlerType.AUTOCOMPLETE.name();
			}
		} else if (type.equalsIgnoreCase(COLLECTION_SUGGEST)) {
			type = TYPE_COLLECTION;
		} else if (type.equalsIgnoreCase(QUIZ_SUGGEST)) {
			type = TYPE_QUIZ;
		}

		searchData.setType(type);
		searchData.setFrom(searchRequestData.getStartAt());
		if(type.equalsIgnoreCase(TYPE_SCOLLECTION) && searchRequestData.isIncludeCollectionItem()){
			searchData.setSize(5);
		}else{
			searchData.setSize(searchRequestData.getPageSize());
		}
		if (searchData.getFrom() < 1) {
			searchData.setFrom((searchRequestData.getPageNum() - 1) * searchData.getSize());
		}
		searchData.setUser(apiCaller);

		// Set Default Values
		for (SearchInputType searchInputType : SearchInputType.values()) {
			if (searchData.getParameters().getObject(searchInputType.getName()) == null) {
				searchData.getParameters().put(searchInputType.getName(), searchInputType.getDefaultValue());
			}
		}
		return SearchHandler.getSearcher(type.toUpperCase()).search(searchData);
	}
	
}
