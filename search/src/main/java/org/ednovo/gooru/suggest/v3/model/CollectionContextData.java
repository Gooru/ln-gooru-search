package org.ednovo.gooru.suggest.v3.model;

import java.util.List;
import java.util.Set;

public class CollectionContextData {

	private String id;
		
	private String title;
	
	private String learningObjective;

	private String format;
					
	private List<String> taxonomySubjectId;
	
	private List<String> taxonomyCourseId;
	
	private List<String> taxonomyLeafSLInternalCodes;
		
	private String courseId;
	
	private String courseTitle;
	
	private List<String> taxonomyDomains;

	private List<String> standards;
	
	private List<String> grade;

	private Integer itemCount;

	private Set<String> itemIds;
	
	private List<String> itemTitles;

	private List<String> taxonomyLearningTargets;
	
	private List<String> gutStdCodes;
	
	private List<String> gutLtCodes;

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

	public List<String> getGutStdCodes() {
		return gutStdCodes;
	}

	public void setGutStdCodes(List<String> gutStdCodes) {
		this.gutStdCodes = gutStdCodes;
	}

	public List<String> getGutLtCodes() {
		return gutLtCodes;
	}

	public void setGutLtCodes(List<String> gutLtCodes) {
		this.gutLtCodes = gutLtCodes;
	}

}
