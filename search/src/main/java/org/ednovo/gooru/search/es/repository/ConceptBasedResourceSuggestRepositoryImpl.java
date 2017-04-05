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
public class ConceptBasedResourceSuggestRepositoryImpl extends BaseRepository implements ConceptBasedResourceSuggestRepository {
	
	private static final Logger LOG = LoggerFactory.getLogger(CollectionRepositoryImpl.class);

	@Override
	public List<String> getSuggestionByCompetency(List<String> idsToFilter, String ctxType, String performanceRange, String suggestType) {
		List<String> ids = null;
		try {
			Type textArrayType = new TypeLocatorImpl(new TypeResolver()).custom(StringArrayType.class);
			String sql = "select ids_to_suggest from concept_based_resource_suggest where ctx_type = :CTX_TYPE and competency_internal_code in (:IDS) and performance_range = :PERFORMANCE and suggest_type = :SUGGEST_TYPE";
			Query query = getSessionFactory().getCurrentSession().createSQLQuery(sql)
					.addScalar("ids_to_suggest", textArrayType)
					.setParameter("PERFORMANCE", performanceRange)
					.setParameter("CTX_TYPE", ctxType)
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
				LOG.info("There are no suggestions for c_internal_code" + idsToFilter);
			}
		} catch (Exception e) {
			LOG.error("Unable to fetch suggestions from DB for Concept Node : {} : Exception :: {}"+ idsToFilter.toString()+ e.getMessage());
		}
		return ids;
	}
	
	@Override
	public List<String> getSuggestionByMicroCompetency(List<String> idsToFilter, String ctxType, String performanceRange, String suggestType) {
		List<String> ids = null;
		try {
			Type textArrayType = new TypeLocatorImpl(new TypeResolver()).custom(StringArrayType.class);
			String sql = "select ids_to_suggest from concept_based_resource_suggest where ctx_type = :CTX_TYPE and micro_competency_internal_code in (:IDS) and performance_range = :PERFORMANCE and suggest_type = :SUGGEST_TYPE";
			Query query = getSessionFactory().getCurrentSession().createSQLQuery(sql)
					.addScalar("ids_to_suggest", textArrayType)
					.setParameter("PERFORMANCE", performanceRange)
					.setParameter("CTX_TYPE", ctxType)
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
				LOG.info("There are no suggestions for mc_internal_code" + idsToFilter);
			}
		} catch (Exception e) {
			LOG.error("Unable to fetch suggestions from DB for Micro Concept Node : {} : Exception :: {}"+ idsToFilter.toString()+ e.getMessage());
		}
		return ids;
	}


}
