package org.ednovo.gooru.search.es.repository;

import java.util.List;

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
}
