package org.ednovo.gooru.search.es.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.search.model.GutPrerequisites;

public class Gut implements Serializable {

	private static final long serialVersionUID = 6586694154253184566L;

	private String id;
	private String codeType;
	private String title;
	private String description;
	private String displayCode;
	private String subjectLabel;
	private String courseLabel;
	private String domainLabel;
	private List<String> keywords;
	private List<GutPrerequisites> gutPrerequisites;
	private Map<String, Object> signatureContents;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
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

	public String getDisplayCode() {
		return displayCode;
	}

	public void setDisplayCode(String displayCode) {
		this.displayCode = displayCode;
	}

	public String getSubjectLabel() {
		return subjectLabel;
	}

	public void setSubjectLabel(String subjectLabel) {
		this.subjectLabel = subjectLabel;
	}

	public String getCourseLabel() {
		return courseLabel;
	}

	public void setCourseLabel(String courseLabel) {
		this.courseLabel = courseLabel;
	}

	public String getDomainLabel() {
		return domainLabel;
	}

	public void setDomainLabel(String domainLabel) {
		this.domainLabel = domainLabel;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public List<GutPrerequisites> getGutPrerequisites() {
		return gutPrerequisites;
	}

	public void setGutPrerequisites(List<GutPrerequisites> gutPrerequisites) {
		this.gutPrerequisites = gutPrerequisites;
	}

	public Map<String, Object> getSignatureContents() {
		return signatureContents;
	}

	public void setSignatureContents(Map<String, Object> signatureContents) {
		this.signatureContents = signatureContents;
	}
}
