package org.ednovo.gooru.search.es.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

@Repository
public class TaxonomyRepositoryImpl extends BaseRepository implements TaxonomyRepository {

	@Override
	public String getParentTaxonomyCode(String code) {
		String sql = "select parent_taxonomy_code_id from taxonomy_code where id =:ID";
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(sql)
				.addScalar("parent_taxonomy_code_id", StringType.INSTANCE)
				.setParameter("ID", code, StringType.INSTANCE);
		String parentCode = null;
		if (query != null && list(query).size() > 0 && list(query).get(0) != null) {
			parentCode = list(query).get(0).toString();
		}
		return parentCode;
	}
	
	@Override
	public List<String> getConceptNeighbours(String code, String parentCode) {
		String sql = "select id from taxonomy_code where parent_taxonomy_code_id =:PARENT_ID and id !=:ID";
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(sql)
				.addScalar("id", StringType.INSTANCE)
				.setParameter("PARENT_ID", parentCode, StringType.INSTANCE)
				.setParameter("ID", code, StringType.INSTANCE);
		List<String> resultList = null;
		if (query != null && list(query).size() > 0) {
			resultList = list(query);
		}
		return resultList;
	}
	
	@Override
	public Map<String, String> getTaxonomyCodeByInternalCode(String code) {
		String sql = "select id,code,code_type,parent_taxonomy_code_id,title,standard_framework_id from taxonomy_code where id =:ID";
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(sql)
				.addScalar("id", StringType.INSTANCE)
				.addScalar("code", StringType.INSTANCE)
				.addScalar("code_type", StringType.INSTANCE)
				.addScalar("parent_taxonomy_code_id", StringType.INSTANCE)
				.addScalar("title", StringType.INSTANCE)
				.addScalar("standard_framework_id", StringType.INSTANCE)
				.setParameter("ID", code, StringType.INSTANCE);
		Map<String, String> resultMap = null;
		if (query != null && list(query).size() > 0 && list(query).get(0) != null) {
			resultMap = new HashMap<>();
			Object[] object = (Object[]) query.list().get(0);	
			resultMap.put("id", object[0].toString());
			resultMap.put("code", object[1].toString());
			resultMap.put("code_type", object[2].toString());
			resultMap.put("parent_taxonomy_code_id", object[3] != null ? object[3].toString() : null);
			resultMap.put("title", object[4] != null ? object[4].toString() : null);
			resultMap.put("standard_framework_id", object[5].toString());
		}
		return resultMap;
	}
	
	@Override
	public Map<String, String> getTaxonomyCodeByDisplayCode(String code) {
		String sql = "select id,code,code_type,parent_taxonomy_code_id,title,standard_framework_id from taxonomy_code where code =:ID";
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(sql)
				.addScalar("id", StringType.INSTANCE)
				.addScalar("code", StringType.INSTANCE)
				.addScalar("code_type", StringType.INSTANCE)
				.addScalar("parent_taxonomy_code_id", StringType.INSTANCE)
				.addScalar("title", StringType.INSTANCE)
				.addScalar("standard_framework_id", StringType.INSTANCE)
				.setParameter("ID", code, StringType.INSTANCE);
		Map<String, String> resultMap = null;
		if (query != null && list(query).size() > 0 && list(query).get(0) != null) {
			resultMap = new HashMap<>();
			Object[] object = (Object[]) query.list().get(0);	
			resultMap.put("id", object[0].toString());
			resultMap.put("code", object[1].toString());
			resultMap.put("code_type", object[2].toString());
			resultMap.put("parent_taxonomy_code_id", object[3] != null ? object[3].toString() : null);
			resultMap.put("title", object[4] != null ? object[4].toString() : null);
			resultMap.put("standard_framework_id", object[5].toString());
		}
		return resultMap;
	}

}
