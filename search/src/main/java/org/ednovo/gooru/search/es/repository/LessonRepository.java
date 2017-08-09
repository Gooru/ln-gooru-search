package org.ednovo.gooru.search.es.repository;

import java.util.Map;

public interface LessonRepository {
  
  Map<String, Object> getLessonData(String id);

}
