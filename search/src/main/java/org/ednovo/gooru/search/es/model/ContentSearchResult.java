package org.ednovo.gooru.search.es.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ContentSearchResult extends ContentUnusedFields implements Serializable {

	private static final long serialVersionUID = 6586694154253184566L;

	private static final String INDEX_TYPE = "resource";

	private String url;

	private String title;

	private Map<String, String> resourceFormat;

	private Map<String, String> resourceType;

	private String thumbnail;

	private String description;

	private Date addDate;

	private String sharing;

	private String gooruOid;

	private Date lastModified;

	private String lastModifiedString;

	private User user;

	private User creator;

	private Long views = 0L;

	private Long viewCount;

	private Integer collaboratorCount;

	private List<String> scollectionTitles;

	private List<String> scollectionIds;

	private Boolean hasFrameBreaker;

	private Integer brokenStatus;

	private String taxonomyDataSet;

	private Map<String, Object> taxonomySet;

	private List<String> depthOfKnowledges;

	private List<String> educationalUse;

	private List<String> momentsOfLearning;

	private List<String> publisher;

	private List<String> aggregator;

	private String resultUId;

	@Deprecated
	private Long contentId;

	private Integer collectionCount;

	private String publishStatus;

	private String contentFormat;

	private String contentSubFormat;

	private Integer isDeleted;

	private Integer isOer;
	
	private String mediaType;
	
	private String category;

	private Boolean isCrosswalked = false;
	
	private List<Map<String, Object>> taxonomyEquivalentCompetencies;
	
	private Map<String, Object> license;

	private Map<String, Object> course;
	
	private List<String> twentyOneCenturySkills;
	
	private Long remixedInCollectionCount;
	
	private Long remixedInAssessmentCount;
	
	private Long remixedInExternalAssessmentCount;
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Map<String, String> getResourceFormat() {
		return resourceFormat;
	}

	public void setResourceFormat(Map<String, String> contentSubFormatValueAsMap) {
		this.resourceFormat = contentSubFormatValueAsMap;
	}

	public Map<String, String> getResourceType() {
		return resourceType;
	}

	public void setResourceType(Map<String, String> resourceType) {
		this.resourceType = resourceType;
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

	public Date getAddDate() {
		return addDate;
	}

	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}

	public String getSharing() {
		return sharing;
	}

	public void setSharing(String sharing) {
		this.sharing = sharing;
	}

	public String getGooruOid() {
		return gooruOid;
	}

	public void setGooruOid(String gooruOid) {
		this.gooruOid = gooruOid;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public Long getViews() {
		return views;
	}

	public void setViews(Long views) {
		this.views = views;
	}

	public Long getViewCount() {
		return viewCount;
	}

	public void setViewCount(Long viewCount) {
		this.viewCount = viewCount;
	}

	public Integer getCollaboratorCount() {
		return collaboratorCount;
	}

	public void setCollaboratorCount(Integer collaboratorCount) {
		this.collaboratorCount = collaboratorCount;
	}

	public List<String> getScollectionTitles() {
		return scollectionTitles;
	}

	public void setScollectionTitles(List<String> scollectionTitles) {
		this.scollectionTitles = scollectionTitles;
	}

	public List<String> getScollectionIds() {
		return scollectionIds;
	}

	public void setScollectionIds(List<String> scollectionIds) {
		this.scollectionIds = scollectionIds;
	}

	public Boolean getHasFrameBreaker() {
		return hasFrameBreaker;
	}

	public void setHasFrameBreaker(Boolean hasFrameBreaker) {
		this.hasFrameBreaker = hasFrameBreaker;
	}

	public Integer getBrokenStatus() {
		return brokenStatus;
	}

	public void setBrokenStatus(Integer brokenStatus) {
		this.brokenStatus = brokenStatus;
	}

	public String getTaxonomyDataSet() {
		return taxonomyDataSet;
	}

	public void setTaxonomyDataSet(String taxonomyDataSet) {
		this.taxonomyDataSet = taxonomyDataSet;
	}

	public Map<String, Object> getTaxonomySet() {
		return taxonomySet;
	}

	public void setTaxonomySet(Map<String, Object> taxonomySet) {
		this.taxonomySet = taxonomySet;
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

	public List<String> getMomentsOfLearning() {
		return momentsOfLearning;
	}

	public void setMomentsOfLearning(List<String> momentsOfLearning) {
		this.momentsOfLearning = momentsOfLearning;
	}

	public List<String> getTwentyOneCenturySkills() {
		return twentyOneCenturySkills;
	}

	public void setTwentyOneCenturySkills(List<String> twentyOneCenturySkills) {
		this.twentyOneCenturySkills = twentyOneCenturySkills;
	}
	
	public static String getIndexType() {
		return INDEX_TYPE;
	}

	public String getResultUId() {
		return resultUId;
	}

	public void setResultUId(String resultUId) {
		this.resultUId = resultUId;
	}

	public Long getContentId() {
		return contentId;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}

	public Integer getCollectionCount() {
		return collectionCount;
	}

	public void setCollectionCount(Integer collectionCount) {
		this.collectionCount = collectionCount;
	}

	public String getPublishStatus() {
		return publishStatus;
	}

	public void setPublishStatus(String publishStatus) {
		this.publishStatus = publishStatus;
	}

	public List<String> getPublisher() {
		return publisher;
	}

	public void setPublisher(List<String> publisher) {
		this.publisher = publisher;
	}

	public List<String> getAggregator() {
		return aggregator;
	}

	public void setAggregator(List<String> aggregator) {
		this.aggregator = aggregator;
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

	public Map<String, Object> getCourse() {
		return course;
	}

	public void setCourse(Map<String, Object> course) {
		this.course = course;
	}

	public Map<String, Object> getLicense() {
		return license;
	}

	public void setLicense(Map<String, Object> license) {
		this.license = license;
	}

	public Integer getIsDeleted() {
		if (isDeleted == null)
			return 0;
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		if (isDeleted == null) {
			this.isDeleted = 0;
		}
		this.isDeleted = isDeleted;
	}

	public Integer getIsOer() {
		if (isOer == null)
			return 0;
		return isOer;
	}

	public void setIsOer(Integer isOer) {
		if (isOer == null) {
			this.isOer = 0;
		}
		this.isOer = isOer;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Boolean getIsCrosswalked() {
		return isCrosswalked;
	}

	public void setIsCrosswalked(Boolean isCrosswalked) {
		this.isCrosswalked = isCrosswalked;
	}
	
	public List<Map<String, Object>> getTaxonomyEquivalentCompetencies() {
		return taxonomyEquivalentCompetencies;
	}

	public void setTaxonomyEquivalentCompetencies(List<Map<String, Object>> taxonomyEquivalentCompetencies) {
		this.taxonomyEquivalentCompetencies = taxonomyEquivalentCompetencies;
	}
	
	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
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

}
