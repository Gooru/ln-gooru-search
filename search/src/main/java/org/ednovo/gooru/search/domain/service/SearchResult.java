package org.ednovo.gooru.search.domain.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ednovo.gooru.search.es.model.Code;

public class SearchResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7188762870994191134L;
	
	private String id;
	private long contentId;
	private String title;
	private String sharing;
	private Integer userId;
	private String userFirstName;
	private String userLastName;
	private String gooruUId;
	private String collaborators;
	private String thumbnail;
	private String type;
	private String collection_gooru_oid;
	private String assessment_gooru_oid;
	private String creatorId;
	private String creatorFirstname;
	private String creatorLastname;
	private String lastModified;
	private String usernameDisplay;
	private String creatornameDisplay;
	private String addDate;
	private Integer viewCount;
	private Integer collaboratorCount;
	private String averageTime;
	private String description;
	private String category;
	private Map<Integer, List<Code>> taxonomyMapByCode;
	private String taxonomySkills;
	private String taxonomyDataSet;
	private Map<String, Object> taxonomySet;
	private String subject;
	private String course;
	private String domain;
	private String resultUId;
	
	//To be Deprecated
	private String[] fragments;
	private String lesson;
	private String topic;
	private String unit;
	private String goals;
	private String grade;
	private Short distinguish;
	private String taxonomyLesson;
	private String folder;
	private Integer isFeatured;
	private Integer numberOfResources;
	private String collectionQuiz;
	private String network;
	private String assetURI;
	private Integer subscriptionCount;
	private String contentOrganizationUid;
	private String contentOrganizationName;
	private String contentOrganizationCode;
	private Map<String, String> customFields;
	private String segmentTiltlesAndOIds;
	private Integer resourceInstanceCount;
	private Integer votesUp;
	private Integer votesDown;
	private Set<Map<String, Object>> tagSet;
	private String batchId;
	private String active;

	public String getUsernameDisplay() {
		return usernameDisplay;
	}

	public void setUsernameDisplay(String usernameDisplay) {
		this.usernameDisplay = usernameDisplay;
	}

	public String getCreatornameDisplay() {
		return creatornameDisplay;
	}

	public void setCreatornameDisplay(String creatornameDisplay) {
		this.creatornameDisplay = creatornameDisplay;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreatorFirstname() {
		return creatorFirstname;
	}

	public void setCreatorFirstname(String creatorFirstname) {
		this.creatorFirstname = creatorFirstname;
	}

	public String getCreatorLastname() {
		return creatorLastname;
	}

	public void setCreatorLastname(String creatorLastname) {
		this.creatorLastname = creatorLastname;
	}

	public Map<Integer, List<Code>> getTaxonomyMapByCode() {
		return taxonomyMapByCode;
	}

	public void setTaxonomyMapByCode(Map<Integer, List<Code>> taxonomyMapByCode) {
		this.taxonomyMapByCode = taxonomyMapByCode;
	}

	public String getCollaborators() {
		return collaborators;
	}

	public void setCollaborators(String collaborators) {
		this.collaborators = collaborators;
	}

	public String getGooruUId() {
		return gooruUId;
	}

	public void setGooruUId(String gooruUId) {
		this.gooruUId = gooruUId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSharing() {
		return sharing;
	}

	public void setSharing(String sharing) {
		this.sharing = sharing;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserFirstName() {
		return userFirstName;
	}

	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}

	public String getUserLastName() {
		return userLastName;
	}

	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}

	public void setTaxonomySkills(String taxonomySkills) {
		this.taxonomySkills = taxonomySkills;
	}

	public String getTaxonomySkills() {
		return taxonomySkills;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setAssessment_gooru_oid(String assessment_gooru_oid) {
		this.assessment_gooru_oid = assessment_gooru_oid;
	}

	public String getAssessment_gooru_oid() {
		return assessment_gooru_oid;
	}

	public void setCollection_gooru_oid(String collection_gooru_oid) {
		this.collection_gooru_oid = collection_gooru_oid;
	}

	public String getCollection_gooru_oid() {
		return collection_gooru_oid;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	public void setTaxonomyDataSet(String taxonomyDataSet) {
		this.taxonomyDataSet = taxonomyDataSet;
	}

	public String getTaxonomyDataSet() {
		return taxonomyDataSet;
	}

	public Map<String, Object> getTaxonomySet() {
		return taxonomySet;
	}

	public void setTaxonomySet(Map<String, Object> taxonomySet) {
		this.taxonomySet = taxonomySet;
	}

	public String getAddDate() {
		return addDate;
	}

	public void setAddDate(String addDate) {
		this.addDate = addDate;
	}

	public void setViewCount(Integer viewCount) {
		this.viewCount = viewCount;
	}

	public Integer getViewCount() {
		return viewCount;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getContentId() {
		return contentId;
	}

	public void setContentId(long contentId) {
		this.contentId = contentId;
	}

	public Integer getCollaboratorCount() {
		return collaboratorCount;
	}

	public void setCollaboratorCount(Integer collaboratorCount) {
		this.collaboratorCount = collaboratorCount;
	}

	public String getAverageTime() {
		return averageTime;
	}

	public void setAverageTime(String averageTime) {
		this.averageTime = averageTime;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCategory() {
		return category;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getResultUId() {
		return resultUId;
	}

	public void setResultUId(String resultUId) {
		this.resultUId = resultUId;
	}

	public String[] getFragments() {
		return fragments;
	}

	public void setFragments(String[] fragments) {
		this.fragments = fragments;
	}

	public String getLesson() {
		return lesson;
	}

	public void setLesson(String lesson) {
		this.lesson = lesson;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getGoals() {
		return goals;
	}

	public void setGoals(String goals) {
		this.goals = goals;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public Short getDistinguish() {
		return distinguish;
	}

	public void setDistinguish(Short distinguish) {
		this.distinguish = distinguish;
	}

	public String getTaxonomyLesson() {
		return taxonomyLesson;
	}

	public void setTaxonomyLesson(String taxonomyLesson) {
		this.taxonomyLesson = taxonomyLesson;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public Integer getIsFeatured() {
		return isFeatured;
	}

	public void setIsFeatured(Integer isFeatured) {
		this.isFeatured = isFeatured;
	}

	public Integer getNumberOfResources() {
		return numberOfResources;
	}

	public void setNumberOfResources(Integer numberOfResources) {
		this.numberOfResources = numberOfResources;
	}

	public String getCollectionQuiz() {
		return collectionQuiz;
	}

	public void setCollectionQuiz(String collectionQuiz) {
		this.collectionQuiz = collectionQuiz;
	}

	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	public String getAssetURI() {
		return assetURI;
	}

	public void setAssetURI(String assetURI) {
		this.assetURI = assetURI;
	}

	public Integer getSubscriptionCount() {
		return subscriptionCount;
	}

	public void setSubscriptionCount(Integer subscriptionCount) {
		this.subscriptionCount = subscriptionCount;
	}

	public String getContentOrganizationUid() {
		return contentOrganizationUid;
	}

	public void setContentOrganizationUid(String contentOrganizationUid) {
		this.contentOrganizationUid = contentOrganizationUid;
	}

	public String getContentOrganizationName() {
		return contentOrganizationName;
	}

	public void setContentOrganizationName(String contentOrganizationName) {
		this.contentOrganizationName = contentOrganizationName;
	}

	public String getContentOrganizationCode() {
		return contentOrganizationCode;
	}

	public void setContentOrganizationCode(String contentOrganizationCode) {
		this.contentOrganizationCode = contentOrganizationCode;
	}

	public Map<String, String> getCustomFields() {
		return customFields;
	}

	public void setCustomFields(Map<String, String> customFields) {
		this.customFields = customFields;
	}

	public String getSegmentTiltlesAndOIds() {
		return segmentTiltlesAndOIds;
	}

	public void setSegmentTiltlesAndOIds(String segmentTiltlesAndOIds) {
		this.segmentTiltlesAndOIds = segmentTiltlesAndOIds;
	}

	public Integer getResourceInstanceCount() {
		return resourceInstanceCount;
	}

	public void setResourceInstanceCount(Integer resourceInstanceCount) {
		this.resourceInstanceCount = resourceInstanceCount;
	}

	public Integer getVotesUp() {
		return votesUp;
	}

	public void setVotesUp(Integer votesUp) {
		this.votesUp = votesUp;
	}

	public Integer getVotesDown() {
		return votesDown;
	}

	public void setVotesDown(Integer votesDown) {
		this.votesDown = votesDown;
	}

	public Set<Map<String, Object>> getTagSet() {
		return tagSet;
	}

	public void setTagSet(Set<Map<String, Object>> tagSet) {
		this.tagSet = tagSet;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

}
