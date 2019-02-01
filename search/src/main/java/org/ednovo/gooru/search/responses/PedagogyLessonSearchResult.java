package org.ednovo.gooru.search.responses;

import java.io.Serializable;
import java.util.Map;

import org.ednovo.gooru.search.es.model.UserV2;
/**
 * @author Renuka
 * 
 */
public class PedagogyLessonSearchResult extends PedagogySearchResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6280104303486215669L;

	private UserV2 creator;

	private UserV2 owner;

	private UserV2 originalCreator;

	private Map<String, Object> taxonomy;
	
	private String format;

	private Map<String, Object> course;

	private Map<String, Object> unit;
	
	private Boolean isFeatured;

	private Integer collectionCount;
	
	private Integer assessmentCount;

	private Integer externalAssessmentCount;
	
	private Long viewCount;

	private Double efficacy;
	
	private Double engagement;
	
	private Double relevance;

	public UserV2 getCreator() {
		return creator;
	}

	public void setCreator(UserV2 creator) {
		this.creator = creator;
	}

	public UserV2 getOwner() {
		return owner;
	}

	public void setOwner(UserV2 owner) {
		this.owner = owner;
	}

	public UserV2 getOriginalCreator() {
		return originalCreator;
	}

	public void setOriginalCreator(UserV2 originalCreator) {
		this.originalCreator = originalCreator;
	}

	public Map<String, Object> getTaxonomy() {
		return taxonomy;
	}

	public void setTaxonomy(Map<String, Object> taxonomy) {
		this.taxonomy = taxonomy;
	}
	
	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}
	
	public Map<String, Object> getCourse() {
		return course;
	}

	public void setCourse(Map<String, Object> course) {
		this.course = course;
	}

	public Map<String, Object> getUnit() {
		return unit;
	}

	public void setUnit(Map<String, Object> unit) {
		this.unit = unit;
	}

	public Boolean getIsFeatured() {
		return isFeatured;
	}

	public void setIsFeatured(Boolean isFeatured) {
		this.isFeatured = isFeatured;
	}

	public Integer getCollectionCount() {
		return collectionCount;
	}

	public void setCollectionCount(Integer collectionCount) {
		this.collectionCount = collectionCount;
	}

	public Integer getAssessmentCount() {
		return assessmentCount;
	}

	public void setAssessmentCount(Integer assessmentCount) {
		this.assessmentCount = assessmentCount;
	}

	public Integer getExternalAssessmentCount() {
		return externalAssessmentCount;
	}

	public void setExternalAssessmentCount(Integer externalAssessmentCount) {
		this.externalAssessmentCount = externalAssessmentCount;
	}

	public Long getViewCount() {
		return viewCount;
	}

	public void setViewCount(Long viewCount) {
		this.viewCount = viewCount;
	}

	public Double getEfficacy() {
		return efficacy;
	}

	public void setEfficacy(Double efficacy) {
		this.efficacy = efficacy;
	}

	public Double getEngagement() {
		return engagement;
	}

	public void setEngagement(Double engagement) {
		this.engagement = engagement;
	}

	public Double getRelevance() {
		return relevance;
	}

	public void setRelevance(Double relevance) {
		this.relevance = relevance;
	}
}
