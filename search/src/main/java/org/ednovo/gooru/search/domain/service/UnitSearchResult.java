package org.ednovo.gooru.search.domain.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.search.es.model.UserV2;

public class UnitSearchResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6280104303486215679L;

	private String id;

	private String lastModified;

	private String addDate;

	private String lastModifiedBy;

	private UserV2 creator;

	private UserV2 owner;

	private UserV2 orginalCreator;

	private String publishStatus;

	private Map<String, Object> taxonomy;

	private String title;
	
	private String format;

	private Map<String, Object> course;
	
	private List<String> lessonIds;

	private List<String> collectionIds;

	private Integer lessonCount;

	private Integer containingCollectionCount;
	
	private Boolean isFeatured;

	private Integer collectionCount;
	
	private Integer assessmentCount;

	private Integer externalAssessmentCount;

	private Long viewCount;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public UserV2 getOrginalCreator() {
		return orginalCreator;
	}

	public void setOrginalCreator(UserV2 orginalCreator) {
		this.orginalCreator = orginalCreator;
	}

	public String getPublishStatus() {
		return publishStatus;
	}

	public void setPublishStatus(String publishStatus) {
		this.publishStatus = publishStatus;
	}

	public Map<String, Object> getTaxonomy() {
		return taxonomy;
	}

	public void setTaxonomy(Map<String, Object> taxonomy) {
		this.taxonomy = taxonomy;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Map<String, Object> getCourse() {
		return course;
	}

	public void setCourse(Map<String, Object> course) {
		this.course = course;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public List<String> getLessonIds() {
		return lessonIds;
	}

	public void setLessonIds(List<String> lessonIds) {
		this.lessonIds = lessonIds;
	}

	public List<String> getCollectionIds() {
		return collectionIds;
	}

	public void setCollectionIds(List<String> collectionIds) {
		this.collectionIds = collectionIds;
	}

	public Integer getLessonCount() {
		return lessonCount;
	}

	public void setLessonCount(Integer lessonCount) {
		this.lessonCount = lessonCount;
	}

	public Integer getContainingCollectionCount() {
		return containingCollectionCount;
	}

	public void setContainingCollectionCount(Integer containingCollectionCount) {
		this.containingCollectionCount = containingCollectionCount;
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

}
