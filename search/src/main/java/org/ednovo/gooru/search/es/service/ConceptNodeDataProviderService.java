package org.ednovo.gooru.search.es.service;

import java.util.List;

import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.model.ConceptNodeDTO;

public interface ConceptNodeDataProviderService {

	List<ConceptNodeDTO> getConceptNodes(List<String> concepts, SearchData searchData, List<ConceptNodeDTO> output);

}
