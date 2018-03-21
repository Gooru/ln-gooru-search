package org.ednovo.gooru.search.es.service;

import java.util.Map;

import org.ednovo.gooru.search.es.model.SearchData;
/**
 * @author Renuka
 * 
 */
public interface LearningMapsService {
	
	Map<String, Object> search(SearchData searchData, String type, Map<String, Object> resultAsMap);

	void generateStandardFilter(SearchData searchData, String key, String[] codes, String fwCode);

	void generateRequestedCodeInfo(SearchData searchData, String key, String[] codes, String requestedFwCode, Map<String, Object> searchResult);

	void setFilterWithExtractedKeywordsAndSubjects(SearchData searchData, String key, String[] codes, String fwCode);

	void getLearningMapStats(SearchData searchData, Map<String, Object> searchResult, String subjectClassification, String subjectCode, String courseCode, String domainCode, String codeType);
		
}
