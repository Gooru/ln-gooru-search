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


@Repository
public class CollectionRepositoryImpl extends BaseRepository implements CollectionRepository {
	
	private static final Logger LOG = LoggerFactory.getLogger(CollectionRepositoryImpl.class);

	@Override
	public Map<String, Object> getCollectionData(String id) {
		UUID uuid = UUID.fromString(id);
		String sql = "select id, title, learning_objective, course_id, taxonomy from collection where id =:ID and is_deleted = false"; 
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(sql)
				.addScalar("id", PostgresUUIDType.INSTANCE)
				.addScalar("title", StringType.INSTANCE)
				.addScalar("learning_objective", StringType.INSTANCE)
				.addScalar("course_id", PostgresUUIDType.INSTANCE)
				.addScalar("taxonomy", StringType.INSTANCE)
				.setParameter("ID", uuid, PostgresUUIDType.INSTANCE);
		Map<String, Object> resultMap = null;
		if (query != null && list(query).size() > 0) {
			resultMap = new HashMap<>();
			Object[] object = arrayList(query).get(0);
			resultMap.put("id", ((UUID) object[0]).toString());
			resultMap.put("title", object[1] != null ? object[1].toString() : null);
			resultMap.put("description", object[2] != null ? object[2].toString() : null);
			resultMap.put("course_id", object[3] != null ? ((UUID) object[3]).toString() : null);
			String taxonomyString = object[4] != null ? object[4].toString() : null;
			try {
				resultMap.put("taxonomy", (taxonomyString != null) ? new JSONObject(taxonomyString) : null);
			} catch (JSONException e) {
				resultMap.put("taxonomy", null);
				LOG.error("CRI: Error while converting taxonomy to JSONObect");			
			}
		}
		return resultMap;
	}

}
