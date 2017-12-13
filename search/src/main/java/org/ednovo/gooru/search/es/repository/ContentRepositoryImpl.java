package org.ednovo.gooru.search.es.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hibernate.Query;
import org.hibernate.type.PostgresUUIDType;
import org.hibernate.type.StringType;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

@Repository
public class ContentRepositoryImpl extends BaseRepository implements ContentRepository {

	@Override
	public Map<String, Object> getContent(String id) throws JSONException {
		Map<String, Object> resultMap = null;
		UUID uuid = UUID.fromString(id);
		String sql = "select id, title, description, taxonomy from original_resource where id =:ID and is_deleted = false";
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(sql)
				.addScalar("id", PostgresUUIDType.INSTANCE)
				.addScalar("title", StringType.INSTANCE)
				.addScalar("description", StringType.INSTANCE)
				.addScalar("taxonomy", StringType.INSTANCE)
				.setParameter("ID", uuid, PostgresUUIDType.INSTANCE);
		if (query != null && arrayList(query).size() > 0) {
			resultMap = new HashMap<>();
			Object[] object = arrayList(query).get(0);
			resultMap.put("id", ((UUID) object[0]).toString());
			resultMap.put("title", object[1] != null ? object[1].toString() : null);
			resultMap.put("description", object[2] != null ? object[2].toString() : null);
			String taxonomyString = object[3] != null ? object[3].toString() : null;
			resultMap.put("taxonomy", (taxonomyString != null) ? new JSONObject(taxonomyString) : null);
		}
		return resultMap;
	}	
	
	@Override
	public List<Map<String, Object>> getItems(String collectionId) {
		UUID uuid = UUID.fromString(collectionId);
		String sql = "select id, title, course_id, taxonomy from content where collection_id =:ID and is_deleted = false"; 
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(sql)
				.addScalar("id", PostgresUUIDType.INSTANCE)
				.addScalar("title", StringType.INSTANCE)
				.addScalar("course_id", PostgresUUIDType.INSTANCE)
				.addScalar("taxonomy", StringType.INSTANCE)
				.setParameter("ID", uuid, PostgresUUIDType.INSTANCE);
		List<Map<String, Object>> resultMapAsList = null;
		if (query != null && arrayList(query).size() > 0) {
			resultMapAsList = new ArrayList<>();
			for (Object[] object : arrayList(query)) {
				Map<String, Object> resultMap = new HashMap<>();
				resultMap.put("id", ((UUID) object[0]).toString());
				resultMap.put("title", object[1] != null ? object[1].toString() : null);
				resultMap.put("course_id", object[2] != null ? ((UUID) object[2]).toString() : null);
				resultMap.put("taxonomy", object[3] != null ? object[3].toString() : null);
				resultMapAsList.add(resultMap);
			}
		}
		return resultMapAsList;
	}
	
}
