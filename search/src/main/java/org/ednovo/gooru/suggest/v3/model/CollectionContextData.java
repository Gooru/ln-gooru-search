package org.ednovo.gooru.suggest.v3.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.json.JsonObject;

public class CollectionContextData {

	private String id;
		
	private String title;
	
	private String learningObjective;

	private String format;
			
	private JsonObject taxonomy;
	
	private Map<String, JsonObject> resourcesTaxonomy;
					
	private List<String> taxonomySubjectId;
	
	private List<String> taxonomyCourseId;
	
	private List<String> taxonomyLeafSLInternalCodes;
	
	private List<String> taxonomyConceptNodeNeighbours;
	
	private String courseId;
	
	private String courseTitle;
	
	private List<String> taxonomyDomains;

	private List<String> standards;
	
	private List<String> grade;

	private Integer itemCount;

	private Set<String> itemIds;
	
	private List<String> itemTitles;

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

	public String getLearningObjective() {
		return learningObjective;
	}

	public void setLearningObjective(String learningObjective) {
		this.learningObjective = learningObjective;
	}

	public String getFormat() {
		return format;
	}
	
	public void setFormat(String format) {
		this.format = format;
	}

	public JsonObject getTaxonomy() {
		return taxonomy;
	}

	public void setTaxonomy(JsonObject taxonomy) {
		this.taxonomy = taxonomy;
	}

	public Map<String, JsonObject> getResourcesTaxonomy() {
		return resourcesTaxonomy;
	}

	public void setResourcesTaxonomy(Map<String, JsonObject> resourcesTaxonomy) {
		this.resourcesTaxonomy = resourcesTaxonomy;
	}

	public List<String> getTaxonomySubjectId() {
		return taxonomySubjectId;
	}

	public void setTaxonomySubjectId(List<String> taxonomySubjectId) {
		this.taxonomySubjectId = taxonomySubjectId;
	}

	public List<String> getTaxonomyCourseId() {
		return taxonomyCourseId;
	}

	public void setTaxonomyCourseId(List<String> taxonomyCourseId) {
		this.taxonomyCourseId = taxonomyCourseId;
	}

	public List<String> getTaxonomyLeafSLInternalCodes() {
		return taxonomyLeafSLInternalCodes;
	}

	public void setTaxonomyLeafSLInternalCodes(List<String> taxonomyLeafSLInternalCodes) {
		this.taxonomyLeafSLInternalCodes = taxonomyLeafSLInternalCodes;
	}
	
	public List<String> getTaxonomyConceptNodeNeighbours() {
		return taxonomyConceptNodeNeighbours;
	}

	public void setTaxonomyConceptNodeNeighbours(List<String> taxonomyConceptNodeNeighbours) {
		this.taxonomyConceptNodeNeighbours = taxonomyConceptNodeNeighbours;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getCourseTitle() {
		return courseTitle;
	}

	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}

	public List<String> getTaxonomyDomains() {
		return taxonomyDomains;
	}

	public void setTaxonomyDomains(List<String> taxonomyDomains) {
		this.taxonomyDomains = taxonomyDomains;
	}

	public List<String> getStandards() {
		return standards;
	}

	public void setStandards(List<String> standards) {
		this.standards = standards;
	}

	public List<String> getGrade() {
		return grade;
	}

	public void setGrade(List<String> grade) {
		this.grade = grade;
	}

	public Integer getItemCount() {
		return itemCount;
	}

	public void setItemCount(Integer itemCount) {
		this.itemCount = itemCount;
	}

	public Set<String> getItemIds() {
		return itemIds;
	}

	public void setItemIds(Set<String> itemIds) {
		this.itemIds = itemIds;
	}

	public List<String> getItemTitles() {
		return itemTitles;
	}

	public void setItemTitles(List<String> itemTitles) {
		this.itemTitles = itemTitles;
	}
	
	public List<String> getTaxonomyLearningTargets() {
		return taxonomyLearningTargets;
	}

	public void setTaxonomyLearningTargets(List<String> taxonomyLearningTargets) {
		this.taxonomyLearningTargets = taxonomyLearningTargets;
	}

}
