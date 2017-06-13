package org.ednovo.gooru.search.model;

public class Concept {

	private String target_entity_id;
	private String target_entity_type;
	private String description;

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
}
