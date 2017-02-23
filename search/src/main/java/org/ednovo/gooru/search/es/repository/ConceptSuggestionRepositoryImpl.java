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
	public List<String> getSuggestionByPerfConceptNode(List<String> conceptNode, String perf) {
		List<String> ids = null;
		try {
			Type intArrayType = new TypeLocatorImpl(new TypeResolver()).custom(StringArrayType.class);
			String sql = "select suitable_suggestions from concept_node_suggest where context_type = 'collection-study' and id in (:IDS) and performance = :PERF";
			Query query = getSessionFactory().getCurrentSession().createSQLQuery(sql).addScalar("suitable_suggestions", intArrayType).setParameter("PERF", perf).setParameterList("IDS", conceptNode);
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
			}
		} catch (Exception e) {
			LOG.error("Unable to fetch suggestions from DB for Concept Node : {} : Exception :: {}", conceptNode.toString(), e.getMessage());
		}
		return ids;
	}


}
