package org.ednovo.gooru.search.domain.service;

import java.io.Serializable;
import java.util.Map;

import org.ednovo.gooru.search.es.model.UserV2;
/**
 * @author Renuka
 * 
 */
public class PedagogyCollectionSearchResult implements Serializable {

	/**
	* 
	*/
	private static final long serialVersionUID = 5673214015280665109L;

	private String id;
	private String title;
	private String format;
	private Integer viewCount;
	private String grade;
	private String learningObjective;
	private Integer isFeatured;
	private Integer collectionItemCount;
	private String questionCount;
	private String resourceCount;

	private Integer collectionRemixCount;
	private Integer collaboratorCount;
	private String thumbnail;
	private UserV2 creator;
	private UserV2 user;
	private String publishStatus;
	private Map<String, Object> taxonomy;
	private Boolean isCrosswalked = false;
	private Long remixedInCourseCount;
	private Long usedByStudentCount;
	private Map<String, Object> taxonomyEquivalentCompetencies;

	private Double efficacy;
	
	private Double engagement;
	
	private Double relevance;
	
	public PedagogyCollectionSearchResult() {
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

	public Integer getIsFeatured() {
		return isFeatured;
	}

	public void setIsFeatured(Integer isFeatured) {
		this.isFeatured = isFeatured;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public UserV2 getCreator() {
		return creator;
	}

	public void setCreator(UserV2 creator) {
		this.creator = creator;
	}

	public UserV2 getUser() {
		return user;
	}

	public void setUser(UserV2 user) {
		this.user = user;
	}

	public Map<String, Object> getTaxonomy() {
		return taxonomy;
	}

	public void setTaxonomy(Map<String, Object> taxonomy) {
		this.taxonomy = taxonomy;
	}

	public Map<String, Object> getTaxonomyEquivalentCompetencies() {
		return taxonomyEquivalentCompetencies;
	}

	public void setTaxonomyEquivalentCompetencies(Map<String, Object> taxonomyEquivalentCompetencies) {
		this.taxonomyEquivalentCompetencies = taxonomyEquivalentCompetencies;
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

	public Integer getCollectionRemixCount() {
		return collectionRemixCount;
	}

	public void setCollectionRemixCount(Integer collectionRemixCount) {
		this.collectionRemixCount = collectionRemixCount;
	}

	public Integer getCollaboratorCount() {
		return collaboratorCount;
	}

	public void setCollaboratorCount(Integer collaboratorCount) {
		this.collaboratorCount = collaboratorCount;
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

	public Integer getViewCount() {
		return viewCount;
	}

	public void setViewCount(Integer viewCount) {
		this.viewCount = viewCount;
	}

	public String getPublishStatus() {
		return publishStatus;
	}

	public void setPublishStatus(String publishStatus) {
		this.publishStatus = publishStatus;
	}

	public Boolean getIsCrosswalked() {
		return isCrosswalked;
	}

	public void setIsCrosswalked(Boolean isCrosswalked) {
		this.isCrosswalked = isCrosswalked;
	}

	public Long getRemixedInCourseCount() {
		return remixedInCourseCount;
	}

	public void setRemixedInCourseCount(Long remixedInCourseCount) {
		this.remixedInCourseCount = remixedInCourseCount;
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
}
