package org.ednovo.gooru.search.model;

import java.io.Serializable;

public class CompetencyNodesDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CompetencyNodeDTO competency_nodes;

	public CompetencyNodeDTO getCompetency_nodes() {
		return competency_nodes;
	}

	public void setCompetency_nodes(CompetencyNodeDTO competency_nodes) {
		this.competency_nodes = competency_nodes;
	}

}
