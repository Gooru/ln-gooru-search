package org.ednovo.gooru.search.es.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.hibernate.Query;
import org.hibernate.type.PostgresUUIDType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

@Repository
public class CourseRepositoryImpl extends BaseRepository implements CourseRepository {
	
	@Override
	public Map<String, Object> getCourseData(String id) {
		UUID uuid = UUID.fromString(id);
		String sql = "select id, title, description from course where id =:ID and is_deleted = false"; 
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(sql)
				.addScalar("id", PostgresUUIDType.INSTANCE)
				.addScalar("title", StringType.INSTANCE)
				.addScalar("description", StringType.INSTANCE)
				.setParameter("ID", uuid, PostgresUUIDType.INSTANCE);
		Map<String, Object> resultMap = null;
		if (query != null && list(query).size() > 0) {
			resultMap = new HashMap<>();
			Object[] object = arrayList(query).get(0);
			resultMap.put("id", ((UUID) object[0]).toString());
			resultMap.put("title", object[1] != null ? object[1].toString() : null);
			resultMap.put("description", object[2] != null ? object[2].toString() : null);
		}
		return resultMap;
	}

}