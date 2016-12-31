package org.ednovo.gooru.search.es.repository;

import java.util.List;
import java.util.Map;

import org.json.JSONException;

public interface ContentRepository {

	Map<String, Object> getContent(String id) throws JSONException;
	List<Map<String, Object>> getItems(String collectionId);
	String getParentTaxonomyCode(String code);
	List<String> getConceptNeighbours(String code, String parentCode);
	
}
