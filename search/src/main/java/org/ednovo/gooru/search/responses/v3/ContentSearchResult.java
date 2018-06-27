package org.ednovo.gooru.search.responses.v3;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.search.es.model.UserV2;
import org.ednovo.gooru.search.responses.SearchResult;

public class ContentSearchResult extends SearchResult implements Serializable {

	private static final long serialVersionUID = 6586694154253184566L;

	private String url;

	private String thumbnail;

	private String description;

	private Date createdAt;

	private Date lastModified;

	private String lastModifiedString;

	private UserV2 creator;

	private UserV2 originalCreator;

	private Long viewCount = 0L;

	private List<String> collectionTitles;

	private List<String> collectionIds;

	private Boolean hasFrameBreaker;

	private Map<String, Object> taxonomy;

	private List<String> depthOfKnowledges;

	private List<String> educationalUse;

	private List<String> publisher;

	private String resultUId;

	private Integer usedInCollectionCount;

	private String publishStatus;

	private String contentFormat;

	private String contentSubFormat;
			
	private Map<String, Object> license;

	private Map<String, Object> course;
	
	private List<String> twentyOneCenturySkills;
	
	private Map<String, Object> info;
	
	private String grade;
	
	private Long remixedInCollectionCount;
	
	private Long remixedInAssessmentCount;
	
	private Long remixedInExternalAssessmentCount;
	
	private Double efficacy;
	
	private Double engagement;
	
	private Double relevance;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getLastModifiedString() {
        return lastModifiedString;
    }

    public void setLastModifiedString(String lastModifiedString) {
        this.lastModifiedString = lastModifiedString;
    }

    public UserV2 getCreator() {
        return creator;
    }

    public void setCreator(UserV2 creator) {
        this.creator = creator;
    }

    public UserV2 getOriginalCreator() {
        return originalCreator;
    }

    public void setOriginalCreator(UserV2 originalCreator) {
        this.originalCreator = originalCreator;
    }

    public Long getViewCount() {
        return viewCount;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }

    public List<String> getCollectionTitles() {
        return collectionTitles;
    }

    public void setCollectionTitles(List<String> collectionTitles) {
        this.collectionTitles = collectionTitles;
    }

    public List<String> getCollectionIds() {
        return collectionIds;
    }

    public void setCollectionIds(List<String> collectionIds) {
        this.collectionIds = collectionIds;
    }

    public Boolean getHasFrameBreaker() {
        return hasFrameBreaker;
    }

    public void setHasFrameBreaker(Boolean hasFrameBreaker) {
        this.hasFrameBreaker = hasFrameBreaker;
    }

    public Map<String, Object> getTaxonomy() {
        return taxonomy;
    }

    public void setTaxonomy(Map<String, Object> taxonomy) {
        this.taxonomy = taxonomy;
    }

    public List<String> getDepthOfKnowledges() {
        return depthOfKnowledges;
    }

    public void setDepthOfKnowledges(List<String> depthOfKnowledges) {
        this.depthOfKnowledges = depthOfKnowledges;
    }

    public List<String> getEducationalUse() {
        return educationalUse;
    }

    public void setEducationalUse(List<String> educationalUse) {
        this.educationalUse = educationalUse;
    }

    public List<String> getPublisher() {
        return publisher;
    }

    public void setPublisher(List<String> publisher) {
        this.publisher = publisher;
    }

    public String getResultUId() {
        return resultUId;
    }

    public void setResultUId(String resultUId) {
        this.resultUId = resultUId;
    }

    public Integer getUsedInCollectionCount() {
        return usedInCollectionCount;
    }

    public void setUsedInCollectionCount(Integer usedInCollectionCount) {
        this.usedInCollectionCount = usedInCollectionCount;
    }

    public String getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(String publishStatus) {
        this.publishStatus = publishStatus;
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

    public Map<String, Object> getLicense() {
        return license;
    }

    public void setLicense(Map<String, Object> license) {
        this.license = license;
    }

    public Map<String, Object> getCourse() {
        return course;
    }

    public void setCourse(Map<String, Object> course) {
        this.course = course;
    }

    public List<String> getTwentyOneCenturySkills() {
        return twentyOneCenturySkills;
    }

    public void setTwentyOneCenturySkills(List<String> twentyOneCenturySkills) {
        this.twentyOneCenturySkills = twentyOneCenturySkills;
    }

    public Map<String, Object> getInfo() {
        return info;
    }

    public void setInfo(Map<String, Object> info) {
        this.info = info;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Long getRemixedInCollectionCount() {
        return remixedInCollectionCount;
    }

    public void setRemixedInCollectionCount(Long remixedInCollectionCount) {
        this.remixedInCollectionCount = remixedInCollectionCount;
    }

    public Long getRemixedInAssessmentCount() {
        return remixedInAssessmentCount;
    }

    public void setRemixedInAssessmentCount(Long remixedInAssessmentCount) {
        this.remixedInAssessmentCount = remixedInAssessmentCount;
    }

    public Long getRemixedInExternalAssessmentCount() {
        return remixedInExternalAssessmentCount;
    }

    public void setRemixedInExternalAssessmentCount(Long remixedInExternalAssessmentCount) {
        this.remixedInExternalAssessmentCount = remixedInExternalAssessmentCount;
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
