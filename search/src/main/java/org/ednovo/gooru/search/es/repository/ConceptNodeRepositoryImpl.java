package org.ednovo.gooru.search.es.repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.ednovo.gooru.search.model.Concept;
import org.ednovo.gooru.search.model.ConceptNodeBadge;
import org.ednovo.gooru.search.model.ConceptNodeBenchmark;
import org.ednovo.gooru.search.model.ConceptNodePostTest;
import org.ednovo.gooru.search.model.ConceptNodePreTest;
import org.hibernate.Query;
import org.hibernate.type.BigIntegerType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.PostgresUUIDType;
import org.hibernate.type.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ConceptNodeRepositoryImpl extends BaseRepository implements ConceptNodeRepository {

	private static final Logger LOG = LoggerFactory.getLogger(ConceptNodeRepositoryImpl.class);

	private static final String SELECT_CONCEPT_NODE = "select id, target_entity_id, description, target_entity_type from concept_node where target_entity_id =:ID";
	
	private static final String SELECT_CONCEPT_NODES = "select target_entity_id, description, target_entity_type from concept_node where id in (:IDS)";

	private static final String SELECT_CONCEPT_NODE_DEPENDENCIES = "select target_concept_code_id from concept_node_dependencies where source_concept_node_id =:ID";

	private static final String SELECT_CONCEPT_NODE_PRE_TEST = "select assessment_id, score_range_name, linked_content_id, linked_content_type from cn_pretest_map where concept_node_id =:ID";

	private static final String SELECT_CONCEPT_NODE_POST_TEST = "select assessment_id, threshold_score from cn_posttest_map where concept_node_id =:ID";

	private static final String SELECT_CONCEPT_NODE_BENCHMARK = "select assessment_id from cn_benchmark_map where concept_node_id =:ID";

	private static final String SELECT_CONCEPT_NODE_BADGE = "select badge_id, min_performance_score, max_performance_score, min_breadth_value, max_breadth_value from cn_badge_map where concept_node_id =:ID";

	@Override
	public Map<String, Object> getConceptNode(String id) {
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(SELECT_CONCEPT_NODE)
				.addScalar("id", BigIntegerType.INSTANCE)
				.addScalar("target_entity_id", StringType.INSTANCE)	
				.addScalar("target_entity_type", StringType.INSTANCE)
				.addScalar("description", StringType.INSTANCE)
				.setParameter("ID", id);
		Map<String, Object> resultMap = null;
		if (query != null && list(query).size() > 0) {
			resultMap = new HashMap<>();
			Object[] object = arrayList(query).get(0);
			resultMap.put("id", (BigInteger)object[0]);
			resultMap.put("target_entity_id", object[1] != null ? object[1].toString() : null);
			resultMap.put("target_entity_type", object[2] != null ? object[2].toString() : null);
			resultMap.put("description", object[3] != null ? object[3].toString() : null);
		}
		return resultMap;
	}
	
	@Override
	public List<Concept> getConceptNodes(List<BigInteger> ids) {
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(SELECT_CONCEPT_NODES)
				.addScalar("target_entity_id", StringType.INSTANCE)				
				.addScalar("description", StringType.INSTANCE)
				.addScalar("target_entity_type", StringType.INSTANCE)
				.setParameterList("IDS", ids);
		List<Concept> resultList = null;
		if (query != null && list(query).size() > 0) {
			resultList = new ArrayList<>();
			for(Object[] object :arrayList(query)) {
				Concept concept = new Concept();
				concept.setTarget_entity_id(object[0].toString());
				concept.setDescription(object[1] != null ? object[1].toString() : null);
				concept.setTarget_entity_type((object[2] != null ? object[2].toString() : null));
				resultList.add(concept);
			}
		}
		return resultList;
	}
	
	@Override
	public List<BigInteger> getConceptNodeProgression(BigInteger id) {
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(SELECT_CONCEPT_NODE_DEPENDENCIES).addScalar("target_concept_code_id", BigIntegerType.INSTANCE).setParameter("ID", id);
		List<BigInteger> resultList = null;
		if (query != null && list(query).size() > 0) {
			resultList = new ArrayList<>();
			for (Object object : list(query)) {
				resultList.add((BigInteger) object);
			}
		}
		return resultList;
	}
	
	@Override
	public List<ConceptNodePreTest> getConceptNodePreTest(BigInteger id) {
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(SELECT_CONCEPT_NODE_PRE_TEST)
				.addScalar("assessment_id", PostgresUUIDType.INSTANCE)
				.addScalar("score_range_name", StringType.INSTANCE)
				.addScalar("linked_content_id", PostgresUUIDType.INSTANCE)
				.addScalar("linked_content_type", StringType.INSTANCE)
				.setParameter("ID", id);
		List<ConceptNodePreTest> resultList = null;
		if (query != null && list(query).size() > 0) {
			resultList = new ArrayList<>();
			for (Object[] object : arrayList(query)) {
				ConceptNodePreTest conceptNodePreTest = new ConceptNodePreTest();
				conceptNodePreTest.setAssessment_id(((UUID) object[0]).toString());
				conceptNodePreTest.setScore_range_name(object[1] != null ? object[1].toString() : null);
				conceptNodePreTest.setLinked_content_id(object[2] != null ? ((UUID) object[2]).toString() : null);
				conceptNodePreTest.setLinked_content_type(object[3] != null ? object[3].toString() : null);
				resultList.add(conceptNodePreTest);
			}
		}
		return resultList;
	}
	
	@Override
	public List<ConceptNodePostTest> getConceptNodePostTest(BigInteger id) {
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(SELECT_CONCEPT_NODE_POST_TEST)
				.addScalar("assessment_id", PostgresUUIDType.INSTANCE)
				.addScalar("threshold_score", IntegerType.INSTANCE)
				.setParameter("ID", id);
		List<ConceptNodePostTest> resultList = null;
		if (query != null && list(query).size() > 0) {
			resultList = new ArrayList<>();
			for (Object[] object : arrayList(query)) {
				ConceptNodePostTest conceptNodePostTest = new ConceptNodePostTest();
				conceptNodePostTest.setAssessment_id(((UUID) object[0]).toString());
				conceptNodePostTest.setThreshold_score(object[1] != null ? ((Integer)object[1]) : null);
				resultList.add(conceptNodePostTest);
			}
		}
		return resultList;
	}
	
	@Override
	public List<ConceptNodeBenchmark> getConceptNodeBenchmark(BigInteger id) {
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(SELECT_CONCEPT_NODE_BENCHMARK)
				.addScalar("assessment_id", PostgresUUIDType.INSTANCE)
				.setParameter("ID", id);
		List<ConceptNodeBenchmark> resultList = null;
		if (query != null && list(query).size() > 0) {
			resultList = new ArrayList<>();
			for (Object[] object : arrayList(query)) {
				ConceptNodeBenchmark conceptNodeBenchmark = new ConceptNodeBenchmark();
				conceptNodeBenchmark.setAssessment_id(((UUID) object[0]).toString());
				resultList.add(conceptNodeBenchmark);
			}
		}
		return resultList;
	}

	@Override
	public List<ConceptNodeBadge> getConceptNodeBadges(BigInteger id) {
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(SELECT_CONCEPT_NODE_BADGE)
				.addScalar("badge_id", StringType.INSTANCE)
				.addScalar("min_performace_score", IntegerType.INSTANCE)
				.addScalar("max_performace_score", IntegerType.INSTANCE)
				.addScalar("min_breadth_value", IntegerType.INSTANCE)
				.addScalar("max_breadth_value", IntegerType.INSTANCE)
				.setParameter("ID", id);
		List<ConceptNodeBadge> resultList = null;
		if (query != null && list(query).size() > 0) {
			resultList = new ArrayList<>();
			for (Object[] object : arrayList(query)) {
				ConceptNodeBadge conceptNodeBadge = new ConceptNodeBadge();
				conceptNodeBadge.setBadge_id(object[0].toString());
				conceptNodeBadge.setMin_performance_score(object[1] != null ? ((Integer)object[1]) : null);
				conceptNodeBadge.setMax_performance_score(object[2] != null ? ((Integer)object[2]) : null);
				conceptNodeBadge.setMin_breadth_value(object[3] != null ? ((Integer)object[3]) : null);
				conceptNodeBadge.setMax_breadth_value(object[4] != null ? ((Integer)object[4]) : null);
				resultList.add(conceptNodeBadge);
			}
		}
		return resultList;
	}

}
