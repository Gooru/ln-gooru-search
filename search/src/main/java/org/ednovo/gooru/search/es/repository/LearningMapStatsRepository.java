package org.ednovo.gooru.search.es.repository;

import java.util.List;
import java.util.Map;

public interface LearningMapStatsRepository {

	List<Map<String, Object>> getStats(String subjectClassification, String subjectCode, String courseCode, String domainCode, String codeType, int offset, int limit);

	Long getTotalCount(String subjectClassification, String subjectCode, String courseCode, String domainCode, String codeType);

	List<Map<String, Object>> getStatsByIds(String gutIds, String codeType, int offset, int limit);

	Long getTotalCountByIds(String gutIds, String codeType);

}
