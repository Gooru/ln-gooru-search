package org.ednovo.gooru.search.responses.v3;

import java.io.Serializable;
import java.util.List;

import org.ednovo.gooru.search.es.model.License;
import org.ednovo.gooru.search.responses.SearchResult;

public class CourseSearchResult extends SearchResult implements Serializable {

	/**
	* 
	*/
	private static final long serialVersionUID = 6280104303486215659L;

	private String createdAt;

	private Long assessmentCount;

	private List<String> audience;

	private Integer collaboratorCount;

	private Long collectionCount;

	private List<String> collectionIds;

	private Integer courseRemixCount;

	private UserV3 creator;

	private String description;

	private Long externalAssessmentCount;

	private String contentFormat;

	private String lastModified;

	private String lastModifiedBy;

	private Long lessonCount;

	private List<String> lessonIds;

	private License license;

	private UserV3 originalCreator;

	private UserV3 owner;

	private Long remixedInClassCount;

	private Integer sequence;

	private String subjectBucket;

	private Integer subjectSequence;

	private String thumbnail;

	private Integer unitCount;

	private List<String> unitIds;

	private Long usedByStudentCount;

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

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public UserV3 getCreator() {
		return creator;
	}

	public void setCreator(UserV3 creator) {
		this.creator = creator;
	}

	public UserV3 getOwner() {
		return owner;
	}

	public void setOwner(UserV3 owner) {
		this.owner = owner;
	}

	public UserV3 getOriginalCreator() {
		return originalCreator;
	}

	public void setOriginalCreator(UserV3 originalCreator) {
		this.originalCreator = originalCreator;
	}

	public License getLicense() {
		return license;
	}

	public void setLicense(License license) {
		this.license = license;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getSubjectSequence() {
		return subjectSequence;
	}

	public void setSubjectSequence(Integer subjectSequence) {
		this.subjectSequence = subjectSequence;
	}

	public String getContentFormat() {
		return contentFormat;
	}

	public void setContentFormat(String contentFormat) {
		this.contentFormat = contentFormat;
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

	public List<String> getAudience() {
		return audience;
	}

	public void setAudience(List<String> audience) {
		this.audience = audience;
	}

}
