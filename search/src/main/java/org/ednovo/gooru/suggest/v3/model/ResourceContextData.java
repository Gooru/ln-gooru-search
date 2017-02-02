package org.ednovo.gooru.suggest.v3.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.ednovo.gooru.search.es.model.Code;

public class ResourceContextData {

	private String id;

	private String title;

	private String description;

	private String contentFormat;

	private String contentSubFormat;

	private String creatorId;

	private List<String> grade;

	private Set<Code> taxonomySet;

	private String keywords;

	private Set<Code> collectionTaxonomySet;

	private List<String> subjectId;

	private List<String> courseId;

	private ArrayList<String> standards;

	private ArrayList<String> conceptNodeNeighbours;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getContentFormat() {
		return contentFormat;
	}

	public void setContentFormat(String contentFormat) {
		this.contentFormat = contentFormat;
	}

	public String getContentSubFormat() {
		return contentSubFormat;
	}

	public void setContentSubFormat(String contentSubFormat) {
		this.contentSubFormat = contentSubFormat;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public List<String> getGrade() {
		return grade;
	}

	public void setGrade(List<String> grade) {
		this.grade = grade;
	}

	public Set<Code> getTaxonomySet() {
		return taxonomySet;
	}

	public void setTaxonomySet(Set<Code> taxonomySet) {
		this.taxonomySet = taxonomySet;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public Set<Code> getCollectionTaxonomySet() {
		return collectionTaxonomySet;
	}

	public void setCollectionTaxonomySet(Set<Code> collectionTaxonomySet) {
		this.collectionTaxonomySet = collectionTaxonomySet;
	}

	public List<String> getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(List<String> subjectId) {
		this.subjectId = subjectId;
	}

	public List<String> getCourseId() {
		return courseId;
	}

	public void setCourseId(List<String> courseId) {
		this.courseId = courseId;
	}

	public ArrayList<String> getStandards() {
		return standards;
	}

	public void setStandards(ArrayList<String> standards) {
		this.standards = standards;
	}

	public ArrayList<String> getConceptNodeNeighbours() {
		return conceptNodeNeighbours;
	}

	public void setConceptNodeNeighbours(ArrayList<String> conceptNodeNeighbours) {
		this.conceptNodeNeighbours = conceptNodeNeighbours;
	}

}
