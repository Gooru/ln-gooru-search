package org.ednovo.gooru.search.domain.service;

import java.io.Serializable;
import java.util.List;

import org.ednovo.gooru.search.model.CompetencyNodeDTO;

public class CompetencySearchResult implements Serializable {

	private static final long serialVersionUID = 5673214015280665103L;

	private List<CompetencyNodeDTO> competency_graph;

	public CompetencySearchResult() {
	}

	public List<CompetencyNodeDTO> getCompetency_graph() {
		return competency_graph;
	}

	public void setCompetency_graph(List<CompetencyNodeDTO> competency_graph) {
		this.competency_graph = competency_graph;
	}
	
}
