package org.ednovo.gooru.search.es.repository;

import java.util.List;

public interface ConceptBasedResourceSuggestRepository {

	List<String> getSuggestionByCompetency(List<String> idsToFilter, String ctxType, String performanceRange, String suggestType);

	List<String> getSuggestionByMicroCompetency(List<String> idsToFilter, String ctxType, String performanceRange, String suggestType);

}
