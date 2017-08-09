package org.ednovo.gooru.search.es.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.hibernate.Query;
import org.hibernate.type.PostgresUUIDType;
import org.hibernate.type.StringType;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public class LessonRepositoryImpl extends BaseRepository implements LessonRepository {
	
	private static final Logger LOG = LoggerFactory.getLogger(LessonRepositoryImpl.class);

	@Override
	public Map<String, Object> getLessonData(String id) {
		UUID uuid = UUID.fromString(id);
		String sql = "select lesson_id, unit_id, title, taxonomy from lesson where lesson_id =:ID and is_deleted = false"; 
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(sql)
				.addScalar("lesson_id", PostgresUUIDType.INSTANCE)
				.addScalar("unit_id", PostgresUUIDType.INSTANCE)
				.addScalar("title", StringType.INSTANCE)
				.addScalar("taxonomy", StringType.INSTANCE)
				.setParameter("ID", uuid, PostgresUUIDType.INSTANCE);
		Map<String, Object> resultMap = null;
		if (query != null && list(query).size() > 0) {
			resultMap = new HashMap<>();
			Object[] object = arrayList(query).get(0);
			resultMap.put("id", ((UUID) object[0]).toString());
			resultMap.put("unit_id", object[1] != null ? ((UUID) object[1]).toString() : null);
			resultMap.put("title", object[2] != null ? object[2].toString() : null);
			String taxonomyString = object[3] != null ? object[3].toString() : null;
			try {
				resultMap.put("taxonomy", (taxonomyString != null) ? new JSONObject(taxonomyString) : null);
			} catch (JSONException e) {
				resultMap.put("taxonomy", null);
				LOG.error("LRI: Error while converting taxonomy to JSONObect");			
			}
		}
		return resultMap;
	}

}
