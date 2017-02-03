package org.ednovo.gooru.search.es.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.hibernate.Query;
import org.hibernate.type.PostgresUUIDType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class TenantRepositoryImpl extends BaseRepository implements TenantRepository {

	public static final String QUERY_FETCH_TENANT = "select id, content_visibility, user_visibility, class_visibility, parent_tenant from tenant where status = "
			            + "'active'";
	@Override
	public Map<String, Object> getAllDiscoverableTenants() {
		Map<String, Object> discoverableTenants = null;
		List<Map<String, Object>> resultList = new ArrayList<>();
		Set<String> gobalTenantIds = new HashSet<>();
		String sql = "select id, content_visibility, parent_tenant from tenant where content_visibility in ('global','discoverable') and status = 'active'";
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(sql)
				.addScalar("id", PostgresUUIDType.INSTANCE)
				.addScalar("parent_tenant", PostgresUUIDType.INSTANCE);
		if (query != null && arrayList(query).size() > 0) {
			discoverableTenants = new HashMap<>();
			for (Object[] object : arrayList(query)) {
				Map<String, Object> resultMap = new HashMap<>();
				String id = ((UUID) object[0]).toString();
				resultMap.put("id", id);
				resultMap.put("content_visibility", object[1] != null ? object[1].toString() : null);
				resultMap.put("parent_tenant", object[2] != null ? object[2].toString() : null);
				gobalTenantIds.add(id);
				resultList.add(resultMap);
			}
			discoverableTenants.put("discoverableTenantIds", gobalTenantIds);
			discoverableTenants.put("discoverableTenants", resultList);
		}
		return discoverableTenants;
	}	
	
}
