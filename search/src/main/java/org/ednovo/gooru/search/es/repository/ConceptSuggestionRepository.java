package org.ednovo.gooru.search.es.repository;

import java.util.List;
import java.util.Map;

public interface ConceptSuggestionRepository {

	List<String> getSuggestionByPerfConceptNode(List<String> conceptNode, String perf);

}
