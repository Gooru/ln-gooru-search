package org.ednovo.gooru.search.domain.service;

import java.io.Serializable;
import java.util.List;

import org.ednovo.gooru.search.model.ConceptNodeDTO;

public class ConceptsSearchResult implements Serializable {

	private static final long serialVersionUID = 5673214015280665103L;

	private List<ConceptNodeDTO> concept_graph;

	public ConceptsSearchResult() {
	}

	public List<ConceptNodeDTO> getConcept_graph() {
		return concept_graph;
	}

	public void setConcept_graph(List<ConceptNodeDTO> concept_graph) {
		this.concept_graph = concept_graph;
	}
	
}
