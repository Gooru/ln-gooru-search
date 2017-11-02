package org.ednovo.gooru.search.es.repository;

import java.util.List;

public interface GutBasedCollectionSuggestRepository {

	List<String> getSuggestionByCompetency(List<String> idsToFilter, String performanceRange);

	List<String> getSuggestionByMicroCompetency(List<String> idsToFilter, String performanceRange);

}
