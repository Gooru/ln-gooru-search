package org.ednovo.gooru.search.model;

import java.util.List;
import java.util.Map;

public class ConceptNodeDTO {

	private String target_entity_id;
	private String target_entity_type;
	private String description;
	private ConceptNodePerformanceDTO performance;
	private List<Concept> dependencies;

	public String getTarget_entity_id() {
		return target_entity_id;
	}

	public void setTarget_entity_id(String target_entity_id) {
		this.target_entity_id = target_entity_id;
	}

	public String getTarget_entity_type() {
		return target_entity_type;
	}

	public void setTarget_entity_type(String target_entity_type) {
		this.target_entity_type = target_entity_type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ConceptNodePerformanceDTO getPerformance() {
		return performance;
	}

	public void setPerformance(ConceptNodePerformanceDTO performance) {
		this.performance = performance;
	}

	public List<Concept> getDependencies() {
		return dependencies;
	}

	public void setDependencies(List<Concept> dependencies) {
		this.dependencies = dependencies;
	}

}
