package org.ednovo.gooru.search.es.repository;

import java.util.List;

public interface ConceptBasedCollectionSuggestRepository {

	List<String> getSuggestionByCompetency(List<String> idsToFilter, String ctxType, String performanceRange, String subTypeToSuggest);

	List<String> getSuggestionByMicroCompetency(List<String> idsToFilter, String ctxType, String performanceRange, String subTypeToSuggest);

}
