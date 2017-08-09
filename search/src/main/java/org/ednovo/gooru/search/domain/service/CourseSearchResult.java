package org.ednovo.gooru.search.domain.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.search.es.model.License;
import org.ednovo.gooru.search.es.model.UserV2;

public class CourseSearchResult implements Serializable {

	/**
	* 
	*/
	private static final long serialVersionUID = 6280104303486215659L;

	private String id;

	private String lastModified;

	private String addDate;

	private String lastModifiedBy;

	private UserV2 creator;

	private UserV2 owner;

	private UserV2 originalCreator;

	private License license;

	private boolean visibleOnProfile;

	private String publishStatus;

	private Integer unitCount;

	private Integer courseRemixCount;

	private Long viewCount;

	private String subjectBucket;

	private Integer sequence;

	private Map<String, Object> taxonomy;

	private Integer collaboratorCount;

	private String thumbnail;

	private String title;

	private String description;

	private Integer subjectSequence;

	private String format;

	private List<String> unitIds;

	private List<String> lessonIds;

	private List<String> collectionIds;

	private Long lessonCount;

	private Long containingCollectionCount;

	private Boolean isFeatured;

	private Long collectionCount;

	private Long assessmentCount;

	private Long externalAssessmentCount;
	
	private Long remixedInClassCount;

	private Long usedByStudentCount;

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

	public License getLicense() {
		return license;
	}

	public void setLicense(License license) {
		this.license = license;
	}

	public boolean isVisibleOnProfile() {
		return visibleOnProfile;
	}

	public void setVisibleOnProfile(boolean visibleOnProfile) {
		this.visibleOnProfile = visibleOnProfile;
	}

	public String getPublishStatus() {
		return publishStatus;
	}

	public void setPublishStatus(String publishStatus) {
		this.publishStatus = publishStatus;
	}

	public Integer getUnitCount() {
		return unitCount;
	}

	public void setUnitCount(Integer unitCount) {
		this.unitCount = unitCount;
	}

	public Integer getCourseRemixCount() {
		return courseRemixCount;
	}

	public void setCourseRemixCount(Integer courseRemixCount) {
		this.courseRemixCount = courseRemixCount;
	}

	public Long getViewCount() {
		return viewCount;
	}

	public void setViewCount(Long viewCount) {
		this.viewCount = viewCount;
	}

	public String getSubjectBucket() {
		return subjectBucket;
	}

	public void setSubjectBucket(String subjectBucket) {
		this.subjectBucket = subjectBucket;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Map<String, Object> getTaxonomy() {
		return taxonomy;
	}

	public void setTaxonomy(Map<String, Object> taxonomy) {
		this.taxonomy = taxonomy;
	}

	public Integer getCollaboratorCount() {
		return collaboratorCount;
	}

	public void setCollaboratorCount(Integer collaboratorCount) {
		this.collaboratorCount = collaboratorCount;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getSubjectSequence() {
		return subjectSequence;
	}

	public void setSubjectSequence(Integer subjectSequence) {
		this.subjectSequence = subjectSequence;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public List<String> getUnitIds() {
		return unitIds;
	}

	public void setUnitIds(List<String> unitIds) {
		this.unitIds = unitIds;
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

	public Long getLessonCount() {
		return lessonCount;
	}

	public void setLessonCount(Long lessonCount) {
		this.lessonCount = lessonCount;
	}

	public Long getContainingCollectionCount() {
		return containingCollectionCount;
	}

	public void setContainingCollectionCount(Long containingCollectionCount) {
		this.containingCollectionCount = containingCollectionCount;
	}

	public Boolean getIsFeatured() {
		return isFeatured;
	}

	public void setIsFeatured(Boolean isFeatured) {
		this.isFeatured = isFeatured;
	}

	public Long getCollectionCount() {
		return collectionCount;
	}

	public void setCollectionCount(Long collectionCount) {
		this.collectionCount = collectionCount;
	}

	public Long getAssessmentCount() {
		return assessmentCount;
	}

	public void setAssessmentCount(Long assessmentCount) {
		this.assessmentCount = assessmentCount;
	}

	public Long getExternalAssessmentCount() {
		return externalAssessmentCount;
	}

	public void setExternalAssessmentCount(Long externalAssessmentCount) {
		this.externalAssessmentCount = externalAssessmentCount;
	}

	public Long getRemixedInClassCount() {
		return remixedInClassCount;
	}

	public void setRemixedInClassCount(Long remixedInClassCount) {
		this.remixedInClassCount = remixedInClassCount;
	}

	public Long getUsedByStudentCount() {
		return usedByStudentCount;
	}

	public void setUsedByStudentCount(Long usedByStudentCount) {
		this.usedByStudentCount = usedByStudentCount;
	}

}
