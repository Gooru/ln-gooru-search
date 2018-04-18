package org.ednovo.gooru.search.es.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.ednovo.gooru.search.domain.service.CompetencySearchResult;
import org.ednovo.gooru.search.es.constant.Constants;
import org.ednovo.gooru.search.es.constant.IndexFields;
import org.ednovo.gooru.search.es.handler.SearchHandler;
import org.ednovo.gooru.search.es.handler.SearchHandlerType;
import org.ednovo.gooru.search.es.model.MapWrapper;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Renuka
 * 
 */
@Service
public class PedagogyNavigatorServiceImpl implements PedagogyNavigatorService, Constants {

	private static final Logger logger = LoggerFactory.getLogger(PedagogyNavigatorServiceImpl.class);
	
	@Autowired
	private LearningMapsService learningMapsService;
	
	@Override
	public SearchResponse<Object> searchPedagogy(SearchData searchData) {
		SearchResponse<Object> searchResponse = new SearchResponse<Object>();
		Map<String, Object> searchResult = new HashMap<>();
		Map<String, Object> contentResultAsMap = new HashMap<>();
		String codes = null;
		String key = null;
		String fwCode = null;
		if (searchData.getParameters() != null && searchData.getParameters().getValues().size() != 0) {
			MapWrapper<Object> parameters = searchData.getParameters();
			if (parameters != null) {
				fwCode = (parameters.getString(FLT_FWCODE));
				if (parameters.containsKey(FLT_STANDARD_DISPLAY)) {
					codes = (parameters.getString(FLT_STANDARD_DISPLAY));
					key = IndexFields.CODE;
				} else if (parameters.containsKey(FLT_STANDARD)) {
					codes = (parameters.getString(FLT_STANDARD));
					key = IndexFields.ID;
				} else if (parameters.containsKey(FLT_TAXONOMY_GUT_CODE)) {
					codes = (parameters.getString(FLT_TAXONOMY_GUT_CODE));
					key = TAXONOMY_GUT_CODE;
				}
			}
		}
		if (key != null && codes != null) {
			if (!searchData.getTaxFilterType().equals(KEYWORD_COMPETENCY)) getLearningMapsService().generateRequestedCodeInfo(searchData, key, codes, fwCode, searchResult);
			else getLearningMapsService().generateRequestedCodesInfo(searchData, key, codes, fwCode, searchResult);
			getLearningMapsService().generateStandardFilter(searchData, key, codes, fwCode);
		}

		getLearningMapsService().search(searchData, TYPE_RESOURCE, contentResultAsMap);
		getLearningMapsService().search(searchData, TYPE_QUESTION, contentResultAsMap);
		getLearningMapsService().search(searchData, TYPE_SCOLLECTION, contentResultAsMap);
		getLearningMapsService().search(searchData, TYPE_ASSESSMENT, contentResultAsMap);
		getLearningMapsService().search(searchData, TYPE_RUBRIC, contentResultAsMap);
		getLearningMapsService().search(searchData, TYPE_COURSE, contentResultAsMap);
		getLearningMapsService().search(searchData, TYPE_UNIT, contentResultAsMap);
		getLearningMapsService().search(searchData, TYPE_LESSON, contentResultAsMap);
		
		searchResult.put(Constants.CONTENTS, contentResultAsMap);
		searchResponse.setSearchResults(searchResult);
		return searchResponse;
	}
	
	@Override
	public SearchResponse<Object> fetchLearningMapStats(SearchData searchData, String subjectClassification, String subjectCode, String courseCode, String domainCode, String codeType) {
		SearchResponse<Object> searchResponse = new SearchResponse<Object>();
		Map<String, Object> searchResult = new HashMap<>();
		getLearningMapsService().getLearningMapStats(searchData, searchResult, subjectClassification, subjectCode, courseCode, domainCode, codeType);
		searchResponse.setSearchResults(searchResult);
		return searchResponse;
	}
	
	@Override
	public String fetchKwToCompetency(String query, String pretty) {
		String standardCode = null;
		SearchData kwToCompRequest = new SearchData();
		kwToCompRequest.setOriginalQuery(query);
		kwToCompRequest.setQueryString(query);
		kwToCompRequest.setType(KEYWORD_COMPETENCY);
		kwToCompRequest.setPretty(pretty);
		CompetencySearchResult kwToCompResponse = (CompetencySearchResult) SearchHandler.getSearcher(SearchHandlerType.KEYWORDCOMPETENCY.name()).search(kwToCompRequest).getSearchResults();
		if (kwToCompResponse.getGutCodes() != null && kwToCompResponse.getGutCodes().size() > 0)
			standardCode = StringUtils.join(kwToCompResponse.getGutCodes(), COMMA);
		return standardCode;
	}
	
	private LearningMapsService getLearningMapsService() {
		return learningMapsService;
	}
	
}
