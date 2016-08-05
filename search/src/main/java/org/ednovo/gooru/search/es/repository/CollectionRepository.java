package org.ednovo.gooru.search.es.repository;

import java.util.Map;

public interface CollectionRepository {
  
  Map<String, Object> getCollectionData(String id);

}
