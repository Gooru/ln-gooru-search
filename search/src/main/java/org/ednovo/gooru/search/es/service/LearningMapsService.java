package org.ednovo.gooru.search.es.service;

import java.util.Map;

import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.SearchResponse;
/**
 * @author Renuka
 * 
 */
public interface LearningMapsService {
	
	SearchResponse<Object> searchPedagogy(SearchData searchData);
	
	Map<String, Object> search(SearchData searchData, String type, Map<String, Object> resultAsMap);
	
}
