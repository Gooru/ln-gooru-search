package org.ednovo.gooru.search.es.repository;

import java.util.Map;

public interface CourseRepository {

	Map<String, Object> getCourseData(String id);
}
