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
public class GutBasedCollectionSuggestRepositoryImpl extends BaseRepository implements GutBasedCollectionSuggestRepository {
	
	private static final Logger LOG = LoggerFactory.getLogger(GutBasedCollectionSuggestRepositoryImpl.class);

	@Override
	public List<String> getSuggestionByCompetency(List<String> idsToFilter, String performanceRange) {
		List<String> ids = null;
		try {
			Type textArrayType = new TypeLocatorImpl(new TypeResolver()).custom(StringArrayType.class);
			String sql = "select ids_to_suggest from gut_based_collection_suggest where competency_internal_code in (:IDS) ";
			if (performanceRange != null) {
				sql += " and performance_range = '"+performanceRange+"'";
			}
			Query query = getSessionFactory().getCurrentSession().createSQLQuery(sql)
					.addScalar("ids_to_suggest", textArrayType)
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
	public List<String> getSuggestionByMicroCompetency(List<String> idsToFilter, String performanceRange) {
		List<String> ids = null;
		try {
			Type textArrayType = new TypeLocatorImpl(new TypeResolver()).custom(StringArrayType.class);
			String sql = "select ids_to_suggest from gut_based_collection_suggest where micro_competency_internal_code in (:IDS) ";
			if (performanceRange != null) {
				sql += " and performance_range = '"+performanceRange+"'";
			}
			Query query = getSessionFactory().getCurrentSession().createSQLQuery(sql)
					.addScalar("ids_to_suggest", textArrayType)
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
