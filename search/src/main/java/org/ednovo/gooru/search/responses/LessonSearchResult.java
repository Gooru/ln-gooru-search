package org.ednovo.gooru.search.responses;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.search.es.model.UserV2;

public class LessonSearchResult extends SearchResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6280104303486215669L;

	private String lastModified;

	private String addDate;

	private String lastModifiedBy;

	private UserV2 creator;

	private UserV2 owner;

	private UserV2 originalCreator;
	
	private String format;

	private Map<String, Object> course;

	private Map<String, Object> unit;
	
	private List<String> collectionIds;

	private Boolean isFeatured;

	private Integer collectionCount;
	
	private Integer assessmentCount;

	private Integer externalAssessmentCount;
	
	private Integer containingCollectionCount;

	private Long viewCount;

	private Double efficacy;
	
	private Double engagement;
	
	private Double relevance;

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	public String getAddDate() {
		return addDate;
	}

	public void setAddDate(String addDate) {
		this.addDate = addDate;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

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

	public List<String> getCollectionIds() {
		return collectionIds;
	}

	public void setCollectionIds(List<String> collectionIds) {
		this.collectionIds = collectionIds;
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

	public Integer getContainingCollectionCount() {
		return containingCollectionCount;
	}

	public void setContainingCollectionCount(Integer containingCollectionCount) {
		this.containingCollectionCount = containingCollectionCount;
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
