package org.ednovo.gooru.search.es.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.internal.TypeLocatorImpl;
import org.hibernate.type.Type;
import org.hibernate.type.TypeResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ConceptSuggestionRepositoryImpl extends BaseRepository implements ConceptSuggestionRepository {
	
	private static final Logger LOG = LoggerFactory.getLogger(CollectionRepositoryImpl.class);

	@Override
	public List<String> getSuggestionByPerfConceptNode(List<String> idsToFilter, String ctxPath, String performance_range, String suggestType) {
		List<String> ids = null;
		try {
			Type textArrayType = new TypeLocatorImpl(new TypeResolver()).custom(StringArrayType.class);
			String sql = "select suitable_suggestions from concept_based_suggest where ctx_path = :CTX_PATH and competency_internal_code in (:IDS) and performance = :PERFORMANCE and suggest_type = :SUGGEST_TYPE";
			Query query = getSessionFactory().getCurrentSession().createSQLQuery(sql)
					.addScalar("suitable_suggestions", textArrayType)
					.setParameter("PERFORMANCE", performance_range)
					.setParameter("CTX_PATH", ctxPath)
					.setParameter("SUGGEST_TYPE", suggestType)
					.setParameterList("IDS", idsToFilter);
			Map<String, Object> resultMap = null;
			if (query != null && list(query).size() > 0) {
				resultMap = new HashMap<>();
				ids = new ArrayList<>();
				for (Object[] object : arrayList(query)) {
					for (int i = 0; i < object.length; i++) {
						ids.add(object[i].toString());
					}
				}
				resultMap.put("suggestion_ids", ids);
			} else {
				System.out.println("No Results");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Unable to fetch suggestions from DB for Concept Node : {} : Exception :: {}"+ idsToFilter.toString()+ e.getMessage());
		}
		return ids;
	}


}
