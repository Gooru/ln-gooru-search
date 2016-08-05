/**
 * 
 */
package org.ednovo.gooru.search.es.service;

import org.ednovo.gooru.search.es.model.MapWrapper;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.SearchRequestData;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.ednovo.gooru.search.es.model.User;


/**
 * @author SearchTeam
 *
 */
public interface SearchV2Service {
	
	SearchResponse<Object>  search(MapWrapper<Object> searchDataMap, SearchRequestData  searchRequestData, User apiCaller, SearchData searchData, String type);
	
}
