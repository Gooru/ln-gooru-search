package org.ednovo.gooru.search.responses;

import java.util.List;
import java.util.Map;

public class Metadata {

	private List<String> publisher;

	private Boolean curated;

	private Map<String, Object> license;

	private List<Map<String, String>> twentyOneCenturySkills;

	private String grade;

	private List<String> subject;
	
	private List<String> course;
	
	private List<String> domain;
	
	private List<Map<String, String>> standards;

	private String language;
	
	private String dok;

	public List<String> getPublisher() {
		return publisher;
	}

	public void setPublisher(List<String> publisher) {
		this.publisher = publisher;
	}

	public Boolean getCurated() {
		return curated;
	}

	public void setCurated(Boolean curated) {
		this.curated = curated;
	}

	public Map<String, Object> getLicense() {
		return license;
	}

	public void setLicense(Map<String, Object> license) {
		this.license = license;
	}

	public List<Map<String, String>> getTwentyOneCenturySkills() {
		return twentyOneCenturySkills;
	}

	public void setTwentyOneCenturySkills(List<Map<String, String>> twentyOneCenturySkills) {
		this.twentyOneCenturySkills = twentyOneCenturySkills;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public List<String> getSubject() {
		return subject;
	}

	public void setSubject(List<String> subject) {
		this.subject = subject;
	}

	public List<String> getCourse() {
		return course;
	}

	public void setCourse(List<String> course) {
		this.course = course;
	}

	public List<String> getDomain() {
		return domain;
	}

	public void setDomain(List<String> domain) {
		this.domain = domain;
	}

	public List<Map<String, String>> getStandards() {
		return standards;
	}

	public void setStandards(List<Map<String, String>> standards) {
		this.standards = standards;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getDok() {
		return dok;
	}

	public void setDok(String dok) {
		this.dok = dok;
	}

}
