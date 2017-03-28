package org.ednovo.gooru.suggest.v3.model;

import java.util.List;

import javax.json.JsonObject;

public class LessonContextData {

	private String id;

	private String title;
	
	private String format;

	private String creatorId;
	
	private JsonObject taxonomy;
	
	private List<String> taxonomyLeafSLInternalCodes;
	
	private List<String> standards;
		
	private List<String> taxonomyLearningTargets;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public JsonObject getTaxonomy() {
		return taxonomy;
	}

	public void setTaxonomy(JsonObject taxonomy) {
		this.taxonomy = taxonomy;
	}

	public List<String> getTaxonomyLeafSLInternalCodes() {
		return taxonomyLeafSLInternalCodes;
	}

	public void setTaxonomyLeafSLInternalCodes(List<String> taxonomyLeafSLInternalCodes) {
		this.taxonomyLeafSLInternalCodes = taxonomyLeafSLInternalCodes;
	}

	public List<String> getStandards() {
		return standards;
	}

	public void setStandards(List<String> standards) {
		this.standards = standards;
	}

	public List<String> getTaxonomyLearningTargets() {
		return taxonomyLearningTargets;
	}

	public void setTaxonomyLearningTargets(List<String> taxonomyLearningTargets) {
		this.taxonomyLearningTargets = taxonomyLearningTargets;
	}

}
