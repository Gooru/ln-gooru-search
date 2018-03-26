package org.ednovo.gooru.search.es.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class LearningMapStatsRepositoryImpl extends BaseRepository implements LearningMapStatsRepository {

	private static final Logger LOG = LoggerFactory.getLogger(LearningMapStatsRepositoryImpl.class);

	@Override
	public List<Map<String, Object>> getStats(String subjectClassification, String subjectCode, String courseCode, String domainCode, String codeType, int offset, int limit) {
		List<Map<String, Object>> resultSet = null;
		try {
			String sql = "select id, subject_code, course_code, domain_code, resource, question,collection, assessment, rubric, course, unit, lesson, signature_resource, signature_collection, signature_assessment from learning_map_stats where subject_classification = '"+subjectClassification+"'";

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
					resultSet.add(resultMap);
				}
			}
		} catch (Exception e) {
			LOG.error("Unable to fetch LM stats : Exception :: {}", e.getMessage());
		}
		return resultSet;
	}
	
	@Override
	public Long getTotalCount(String subjectClassification, String subjectCode, String courseCode, String domainCode, String codeType) {
		Long totalHitCount = 0l;
		try {
			String sql = "select count(*) from learning_map_stats where subject_classification = '"+subjectClassification+"'";
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
}
