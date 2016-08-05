package org.ednovo.gooru.search.es.repository;

import java.util.List;
import java.util.Map;

public interface ContentRepository {

	Map<String, Object> getContent(String id);
	List<Map<String, Object>> getItems(String collectionId);
	
}
