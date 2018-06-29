package org.ednovo.gooru.search.es.service;

import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.responses.SearchResponse;

public interface PedagogyNavigatorService {

	SearchResponse<Object> searchPedagogy(SearchData searchData);

	SearchResponse<Object> fetchLearningMapStats(SearchData searchData, String subjectClassification, String subjectCode, String courseCode, String domainCode, String gutIds, String codeType);

	String fetchKwToCompetency(String query, String pretty, Integer from, Integer size);

	SearchResponse<Object> searchPedagogyFromStaticTable(SearchData searchData);

}
