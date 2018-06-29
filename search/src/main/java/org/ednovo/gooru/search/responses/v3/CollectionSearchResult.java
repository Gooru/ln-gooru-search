package org.ednovo.gooru.search.responses.v3;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.search.es.model.License;
import org.ednovo.gooru.search.responses.SearchResult;

public class CollectionSearchResult extends SearchResult implements Serializable {

    /**
    * 
    */
    private static final long serialVersionUID = 5673214015280665109L;

    private String createdAt;
    private List<String> audience;
    private Integer collaboratorCount;
    private List<String> collaboratorIds;
    private String contentFormat;
    private Map<String, Object> course;
    private UserV3 creator;
    private List<String> depthOfknowledge;
    private String grade;
    private String learningObjective;
    private String lastModified;
    private String lastModifiedBy;
    private License license;
    private UserV3 originalCreator;
    private UserV3 owner;
    private Integer questionCount;
    private Long remixedInCourseCount;
    private Integer resourceCount;
    private String resultUId;
    private Map<String, Object> taxonomy;
    private String thumbnail;
    private Long usedByStudentCount;
	private Integer collectionRemixCount;
    private Double efficacy;
    private Double engagement;
    private Double relevance;
    private Long viewCount;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<String> getAudience() {
        return audience;
    }

    public void setAudience(List<String> audience) {
        this.audience = audience;
    }

    public Integer getCollaboratorCount() {
        return collaboratorCount;
    }

    public void setCollaboratorCount(Integer collaboratorCount) {
        this.collaboratorCount = collaboratorCount;
    }

    public List<String> getCollaboratorIds() {
        return collaboratorIds;
    }

    public void setCollaboratorIds(List<String> collaboratorIds) {
        this.collaboratorIds = collaboratorIds;
    }

    public String getContentFormat() {
        return contentFormat;
    }

    public void setContentFormat(String contentFormat) {
        this.contentFormat = contentFormat;
    }

    public Map<String, Object> getCourse() {
        return course;
    }

    public void setCourse(Map<String, Object> course) {
        this.course = course;
    }

    public UserV3 getCreator() {
        return creator;
    }

    public void setCreator(UserV3 creator) {
        this.creator = creator;
    }

    public List<String> getDepthOfknowledge() {
        return depthOfknowledge;
    }

    public void setDepthOfknowledge(List<String> depthOfknowledge) {
        this.depthOfknowledge = depthOfknowledge;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getLearningObjective() {
        return learningObjective;
    }

    public void setLearningObjective(String learningObjective) {
        this.learningObjective = learningObjective;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public License getLicense() {
        return license;
    }

    public void setLicense(License license) {
        this.license = license;
    }

    public UserV3 getOriginalCreator() {
        return originalCreator;
    }

    public void setOriginalCreator(UserV3 originalCreator) {
        this.originalCreator = originalCreator;
    }

    public UserV3 getOwner() {
        return owner;
    }

    public void setOwner(UserV3 owner) {
        this.owner = owner;
    }

    public Integer getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(Integer questionCount) {
        this.questionCount = questionCount;
    }

    public Long getRemixedInCourseCount() {
        return remixedInCourseCount;
    }

    public void setRemixedInCourseCount(Long remixedInCourseCount) {
        this.remixedInCourseCount = remixedInCourseCount;
    }

    public Integer getResourceCount() {
        return resourceCount;
    }

    public void setResourceCount(Integer resourceCount) {
        this.resourceCount = resourceCount;
    }

    public String getResultUId() {
        return resultUId;
    }

    public void setResultUId(String resultUId) {
        this.resultUId = resultUId;
    }

    public Map<String, Object> getTaxonomy() {
        return taxonomy;
    }

    public void setTaxonomy(Map<String, Object> taxonomy) {
        this.taxonomy = taxonomy;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Long getUsedByStudentCount() {
        return usedByStudentCount;
    }

    public void setUsedByStudentCount(Long usedByStudentCount) {
        this.usedByStudentCount = usedByStudentCount;
    }

    public Integer getCollectionRemixCount() {
        return collectionRemixCount;
    }

    public void setCollectionRemixCount(Integer collectionRemixCount) {
        this.collectionRemixCount = collectionRemixCount;
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

    public Long getViewCount() {
        return viewCount;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }

}
