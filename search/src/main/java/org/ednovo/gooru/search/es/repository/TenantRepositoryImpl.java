package org.ednovo.gooru.search.es.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.hibernate.Query;
import org.hibernate.type.PostgresUUIDType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class TenantRepositoryImpl extends BaseRepository implements TenantRepository {

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
				gobalTenantIds.add(id);
				resultList.add(resultMap);
			}
			discoverableTenants.put("discoverableTenantIds", gobalTenantIds.stream().collect(Collectors.toList()));
			discoverableTenants.put("discoverableTenants", resultList);
		}
		return discoverableTenants;
	}	
	
	@Override
	public List<String> getGlobalTenantIds() {
		List<String> gobalTenantIds = new ArrayList<>();
		String sql = "select id from tenant where content_visibility = 'global' and status = 'active'";
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(sql).addScalar("id", PostgresUUIDType.INSTANCE);
		if (query != null && list(query).size() > 0) {
			return list(query).stream().map(Object::toString).collect(Collectors.toList());
		}
		return gobalTenantIds;
	}
	
	@Override
	public List<String> getDiscoverableTenantIds() {
		List<String> discoverableTenantIds = null;
		String sql = "select id from tenant where content_visibility = 'discoverable' and status = 'active'";
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(sql).addScalar("id", PostgresUUIDType.INSTANCE);
		if (query != null && list(query).size() > 0) {
			return list(query).stream().map(Object::toString).collect(Collectors.toList());
		}
		return discoverableTenantIds;
	}
	
	@Override
	public String getFCVisibility(String tenant) {
		UUID uuid = UUID.fromString(tenant);
		String sql = "select fc_visibility from tenant where id =:ID";
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(sql)
				.addScalar("fc_visibility", StringType.INSTANCE)
				.setParameter("ID", uuid, PostgresUUIDType.INSTANCE);
		String fcVisibility = null;
		if (query != null && list(query).size() > 0 && list(query).get(0) != null) {
			fcVisibility = list(query).get(0).toString();
		}
		return fcVisibility;
	}
	
}
