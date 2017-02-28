package org.ednovo.gooru.search.es.repository;

import java.util.List;

public interface ConceptSuggestionRepository {

	List<String> getSuggestionByCompetency(List<String> idsToFilter, String ctxPath, String performance_range, String suggestType);

	List<String> getSuggestionByMicroCompetency(List<String> idsToFilter, String ctxPath, String performance_range, String suggestType);

}
