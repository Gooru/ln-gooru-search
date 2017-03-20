package org.ednovo.gooru.search.domain.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.search.es.model.License;

public class CollectionSearchResult extends CollectionUnUsedFields implements Serializable {

	/**
	* 
	*/
	private static final long serialVersionUID = 5673214015280665109L;

	private String id;
	private String title;
	private String type;
	private String lastModified;
	private String addDate;
	private Integer viewCount;
	private String collectionType;
	private String narrationLink;
	private String notes;
	private String keyPoints;
	private String language;
	private String goals;
	private String grade;
	private Integer collectionItemCount;
	private List<Map<String, Object>> collectionItems = new ArrayList<>();
	private String lastModifiedBy;
	private boolean profileUserVisibility;
	private List<String> audience;
	private List<String> depthOfknowledge;
	private String instructionalMethod;
	private String learningAndInovation;
	private String questionCount;
	private String resourceCount;

	private Long averageTimeSpent;
	private Integer scollectionRemixCount;
	private Integer collaboratorCount;
	private String category;
	private List<String> collaboratorIds;
	private String thumbnail;
	private String creatorId;
	private String creatorFirstname;
	private String creatorLastname;
	private String creatornameDisplay;
	private String gooruUId;
	private String userFirstName;
	private String userLastName;
	private String usernameDisplay;
	private String resultUId;
	private String taxonomyDataSet;
	private String subject;
	private License license;
	private String publishStatus;
	private String creatorProfileImage;
	private String userProfileImage;
	private Map<String, Object> course;
	private Map<String, Object> taxonomySet;
	private Boolean isCrosswalked = false;
	private List<Map<String, Object>> taxonomyEquivalentCompetencies;
	
	public CollectionSearchResult() {
	}

	public List<String> getAudience() {
		return audience;
	}

	public void setAudience(List<String> audience) {
		this.audience = audience;
	}

	public List<String> getDepthOfknowledge() {
		return depthOfknowledge;
	}

	public void setDepthOfknowledge(List<String> depthOfknowledge) {
		this.depthOfknowledge = depthOfknowledge;
	}

	public String getInstructionalMethod() {
		return instructionalMethod;
	}

	public void setInstructionalMethod(String instructionalMethod) {
		this.instructionalMethod = instructionalMethod;
	}

	public String getLearningAndInovation() {
		return learningAndInovation;
	}

	public void setLearningAndInovation(String learningAndInovation) {
		this.learningAndInovation = learningAndInovation;
	}

	public String getCollectionType() {
		return collectionType;
	}

	public void setCollectionType(String collectionType) {
		this.collectionType = collectionType;
	}

	public String getNarrationLink() {
		return narrationLink;
	}

	public void setNarrationLink(String narrationLink) {
		this.narrationLink = narrationLink;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getKeyPoints() {
		return keyPoints;
	}

	public void setKeyPoints(String keyPoints) {
		this.keyPoints = keyPoints;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
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

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public boolean isProfileUserVisibility() {
		return profileUserVisibility;
	}

	public void setProfileUserVisibility(boolean profileUserVisibility) {
		this.profileUserVisibility = profileUserVisibility;
	}

	public Integer getCollectionItemCount() {
		return collectionItemCount;
	}

	public void setCollectionItemCount(Integer collectionItemCount) {
		this.collectionItemCount = collectionItemCount;
	}

	public void setQuestionCount(String questionCount) {
		this.questionCount = questionCount;
	}

	public String getQuestionCount() {
		return questionCount;
	}

	public void setResourceCount(String resourceCount) {
		this.resourceCount = resourceCount;
	}

	public String getResourceCount() {
		return resourceCount;
	}

	public void setAverageTimeSpent(Long averageTimeSpent) {
		this.averageTimeSpent = averageTimeSpent;
	}

	public Long getAverageTimeSpent() {
		return averageTimeSpent;
	}

	public Integer getScollectionRemixCount() {
		return scollectionRemixCount;
	}

	public void setScollectionRemixCount(Integer scollectionRemixCount) {
		this.scollectionRemixCount = scollectionRemixCount;
	}

	public List<Map<String, Object>> getCollectionItems() {
		return collectionItems;
	}

	public void setCollectionItems(List<Map<String, Object>> collectionItems) {
		this.collectionItems = collectionItems;
	}

	public Integer getCollaboratorCount() {
		return collaboratorCount;
	}

	public void setCollaboratorCount(Integer collaboratorCount) {
		this.collaboratorCount = collaboratorCount;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public List<String> getCollaboratorIds() {
		return collaboratorIds;
	}

	public void setCollaboratorIds(List<String> collaboratorIds) {
		this.collaboratorIds = collaboratorIds;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
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

	public String getCreatornameDisplay() {
		return creatornameDisplay;
	}

	public void setCreatornameDisplay(String creatornameDisplay) {
		this.creatornameDisplay = creatornameDisplay;
	}

	public String getGooruUId() {
		return gooruUId;
	}

	public void setGooruUId(String gooruUId) {
		this.gooruUId = gooruUId;
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

	public String getUsernameDisplay() {
		return usernameDisplay;
	}

	public void setUsernameDisplay(String usernameDisplay) {
		this.usernameDisplay = usernameDisplay;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public Integer getViewCount() {
		return viewCount;
	}

	public void setViewCount(Integer viewCount) {
		this.viewCount = viewCount;
	}

	public String getResultUId() {
		return resultUId;
	}

	public void setResultUId(String resultUId) {
		this.resultUId = resultUId;
	}

	public String getTaxonomyDataSet() {
		return taxonomyDataSet;
	}

	public void setTaxonomyDataSet(String taxonomyDataSet) {
		this.taxonomyDataSet = taxonomyDataSet;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public License getLicense() {
		return license;
	}

	public void setLicense(License license) {
		this.license = license;
	}

	public String getPublishStatus() {
		return publishStatus;
	}

	public void setPublishStatus(String publishStatus) {
		this.publishStatus = publishStatus;
	}

	public String getCreatorProfileImage() {
		return creatorProfileImage;
	}

	public void setCreatorProfileImage(String creatorProfileImage) {
		this.creatorProfileImage = creatorProfileImage;
	}

	public String getUserProfileImage() {
		return userProfileImage;
	}

	public void setUserProfileImage(String userProfileImage) {
		this.userProfileImage = userProfileImage;
	}

	public Map<String, Object> getCourse() {
		return course;
	}

	public void setCourse(Map<String, Object> course) {
		this.course = course;
	}

	public Map<String, Object> getTaxonomySet() {
		return taxonomySet;
	}

	public void setTaxonomySet(Map<String, Object> taxonomySet) {
		this.taxonomySet = taxonomySet;
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

}
