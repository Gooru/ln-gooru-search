package org.ednovo.gooru.search.es.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.ednovo.gooru.search.es.constant.Constants;
import org.hibernate.Query;
import org.hibernate.type.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class LearningMapStatsRepositoryImpl extends BaseRepository implements LearningMapStatsRepository, Constants {

	private static final Logger LOG = LoggerFactory.getLogger(LearningMapStatsRepositoryImpl.class);
	protected static final ObjectMapper SERIAILIZER = new ObjectMapper();

	@Override
	public List<Map<String, Object>> getStats(String subjectClassification, String subjectCode, String courseCode, String domainCode, String codeType, int offset, int limit) {
		List<Map<String, Object>> resultSet = null;
		try {
			String sql = "select id, subject_code, course_code, domain_code, resource_count, question_count,collection_count, assessment_count, rubric_count, course_count, unit_count, lesson_count, signature_resource_count, signature_collection_count, signature_assessment_count, code_type, parent_id, sequence_id from learning_maps where subject_classification = '"+subjectClassification+"'";

			if (StringUtils.isNotBlank(subjectCode))  sql += " and subject_code in (:SUBJECT_CODE)";
			if (StringUtils.isNotBlank(courseCode)) sql += " and course_code in (:COURSE_CODE)";
			if (StringUtils.isNotBlank(domainCode))  sql += " and domain_code in (:DOMAIN_CODE)";
			if(codeType != null) {
				if (codeType.equalsIgnoreCase("competency"))  sql += " and code_type in ('standard_level_1','standard_level_2')" ;
				else sql += " and code_type = 'learning_target_level_0'" ;
			}
			if (limit > 0) sql += " offset " + offset + " limit " + limit;
			Query query = getSessionFactory().getCurrentSession().createSQLQuery(sql);
			if (StringUtils.isNotBlank(subjectCode)) query.setParameterList("SUBJECT_CODE", subjectCode.split(","));
			if (StringUtils.isNotBlank(courseCode)) query.setParameterList("COURSE_CODE", courseCode.split(","));
			if (StringUtils.isNotBlank(domainCode)) query.setParameterList("DOMAIN_CODE", domainCode.split(","));
			if (query != null && arrayList(query).size() > 0) {
				resultSet = generateStatsResponse(query);
			}
		} catch (Exception e) {
			LOG.error("Unable to fetch LM stats : Exception :: {}", e.getMessage());
		}
		return resultSet;
	}
	
	@Override
	public List<Map<String, Object>> getStatsByIds(String gutIds, String codeType, int offset, int limit) {
		List<Map<String, Object>> resultSet = null;
		try {
			if (StringUtils.isNotBlank(gutIds)) {
				String sql = "select id, subject_code, course_code, domain_code, resource_count, question_count,collection_count, assessment_count, rubric_count, course_count, unit_count, lesson_count, signature_resource_count, signature_collection_count, signature_assessment_count, code_type, parent_id, sequence_id from learning_maps WHERE id IN (:GUT_IDS)";
				if (codeType != null) {
					if (codeType.equalsIgnoreCase("competency"))
						sql += " and code_type in ('standard_level_1','standard_level_2')";
					else
						sql += " and code_type = 'learning_target_level_0'";
				}
				if (limit > 0)
					sql += " offset " + offset + " limit " + limit;
				Query query = getSessionFactory().getCurrentSession().createSQLQuery(sql);
				if (StringUtils.isNotBlank(gutIds)) query.setParameterList("GUT_IDS", gutIds.split(","));
				if (query != null && arrayList(query).size() > 0) {
					resultSet = generateStatsResponse(query);
				}
			}
		} catch (Exception e) {
			LOG.error("Unable to fetch LM stats by Ids : {} Exception :: {}", gutIds, e.getMessage());
		}
		return resultSet;
	}

	private List<Map<String, Object>> generateStatsResponse(Query query) {
		List<Map<String, Object>> resultSet;
		resultSet = new ArrayList<>();
		for (Object[] object : arrayList(query)) {
			Map<String, Object> resultMap = new HashMap<>();
			String id = object[0].toString();
			resultMap.put("id", id);
			resultMap.put("subjectCode", object[1].toString());
			resultMap.put("courseCode", object[2].toString());
			resultMap.put("domainCode", object[3].toString());
			resultMap.put("resource", Integer.valueOf(object[4].toString()));
			resultMap.put("question", Integer.valueOf(object[5].toString()));
			resultMap.put("collection", Integer.valueOf(object[6].toString()));
			resultMap.put("assessment", Integer.valueOf(object[7].toString()));
			resultMap.put("rubric", Integer.valueOf(object[8].toString()));
			resultMap.put("course", Integer.valueOf(object[9].toString()));
			resultMap.put("unit", Integer.valueOf(object[10].toString()));
			resultMap.put("lesson", Integer.valueOf(object[11].toString()));
			resultMap.put("signatureResource", Integer.valueOf(object[12].toString()));
			resultMap.put("signatureCollection", Integer.valueOf(object[13].toString()));
			resultMap.put("signatureAssessment", Integer.valueOf(object[14].toString()));
			resultMap.put("codeType", object[15].toString());
			resultMap.put("parentId", object[16] == null ? null : object[16].toString());
			resultMap.put("sequenceId", Integer.valueOf(object[17].toString()));
			resultSet.add(resultMap);
		}
		return resultSet;
	}
	
	@Override
	public Long getTotalCount(String subjectClassification, String subjectCode, String courseCode, String domainCode, String codeType) {
		Long totalHitCount = 0l;
		try {
			String sql = "select count(*) from learning_maps where subject_classification = '"+subjectClassification+"'";
			if (StringUtils.isNotBlank(subjectCode))  sql += " and subject_code in (:SUBJECT_CODE)";
			if (StringUtils.isNotBlank(courseCode)) sql += " and course_code in (:COURSE_CODE)";
			if (StringUtils.isNotBlank(domainCode))  sql += " and domain_code in (:DOMAIN_CODE)";
			if (codeType != null) {
				if (codeType.equalsIgnoreCase("competency")) sql += " and code_type in ('standard_level_1','standard_level_2')";
				else sql += " and code_type = 'learning_target_level_0'";
			}
			
			Query query = getSessionFactory().getCurrentSession().createSQLQuery(sql);
			if (StringUtils.isNotBlank(subjectCode)) query.setParameterList("SUBJECT_CODE", subjectCode.split(","));
			if (StringUtils.isNotBlank(courseCode)) query.setParameterList("COURSE_CODE", courseCode.split(","));
			if (StringUtils.isNotBlank(domainCode)) query.setParameterList("DOMAIN_CODE", domainCode.split(","));
			
			totalHitCount = ((Number) query.list().get(0)).longValue();
		} catch (Exception e) {
			LOG.error("getTotalCount :: Unable to fetch LM stats total : Exception :: {}", e.getMessage());
		}
		return totalHitCount;
	}
	
	@Override
	public Long getTotalCountByIds(String gutIds, String codeType) {
		Long totalHitCount = 0l;
		try {
			String sql = "select count(*) from learning_maps WHERE id IN (:GUT_IDS)";
			if (codeType != null) {
				if (codeType.equalsIgnoreCase("competency")) sql += " and code_type in ('standard_level_1','standard_level_2')";
				else sql += " and code_type = 'learning_target_level_0'";
			}
			
			Query query = getSessionFactory().getCurrentSession().createSQLQuery(sql);
			if (StringUtils.isNotBlank(gutIds)) query.setParameterList("GUT_IDS", gutIds.split(","));

			totalHitCount = ((Number) query.list().get(0)).longValue();
		} catch (Exception e) {
			LOG.error("getTotalCount :: Unable to fetch LM stats search total : Exception :: {}", e.getMessage());
		}
		return totalHitCount;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getLearningMapsById(String gutIds) {
		Map<String, Object> resultMap = null;
		try {
			if (StringUtils.isNotBlank(gutIds)) {
				String sql = "select id, resource, question, collection, assessment, rubric, course, unit, lesson from learning_maps WHERE id IN (:GUT_IDS)";
				Query query = getSessionFactory().getCurrentSession().createSQLQuery(sql)
						.addScalar("id", StringType.INSTANCE)
						.addScalar("resource", StringType.INSTANCE)
						.addScalar("question", StringType.INSTANCE)				
						.addScalar("collection", StringType.INSTANCE)
						.addScalar("assessment", StringType.INSTANCE)
						.addScalar("rubric", StringType.INSTANCE)				
						.addScalar("course", StringType.INSTANCE)
						.addScalar("unit", StringType.INSTANCE)
						.addScalar("lesson", StringType.INSTANCE);
				if (StringUtils.isNotBlank(gutIds)) query.setParameterList("GUT_IDS", gutIds.split(","));
				if (query != null && arrayList(query).size() > 0) {
					resultMap = new HashMap<>();
					for (Object[] object : arrayList(query)) {
						String resourceString = object[1] != null ? object[1].toString() : null;
						resultMap.put("resource", (resourceString != null) ? serializeJsonToMap(resourceString) : null);
						String questionString = object[2] != null ? object[2].toString() : null;
						resultMap.put("question", (questionString != null) ? serializeJsonToMap(questionString) : null);
						String collectionString = object[3] != null ? object[3].toString() : null;
						resultMap.put("collection", (collectionString != null) ? serializeJsonToMap(collectionString) : null);
						String assessmentString = object[4] != null ? object[4].toString() : null;
						resultMap.put("assessment", (assessmentString != null) ? serializeJsonToMap(assessmentString) : null);
						String rubricString = object[5] != null ? object[5].toString() : null;
						resultMap.put("rubric", (rubricString != null) ? serializeJsonToMap(rubricString) : null);
						String courseString = object[6] != null ? object[6].toString() : null;
						resultMap.put("course", (courseString != null) ? serializeJsonToMap(courseString) : null);
						String unitString = object[7] != null ? object[7].toString() : null;
						resultMap.put("unit", (unitString != null) ? serializeJsonToMap(unitString) : null);
						String lessonString = object[8] != null ? object[8].toString() : null;
						resultMap.put("lesson", (lessonString != null) ? serializeJsonToMap(lessonString) : null);
					}
				}
			}
		} catch (Exception e) {
			LOG.error("Unable to fetch LM by Ids : {} Exception :: {}", gutIds, e.getMessage());
		}
		return resultMap;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> serializeJsonToMap(String resourceString) throws IOException, JsonParseException, JsonMappingException {
		return (Map<String,Object>) SERIAILIZER.readValue(resourceString, new TypeReference<Map<String,Object>>() {
		});
	}

}
