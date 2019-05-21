package org.ednovo.gooru.search.responses;

import java.io.Serializable;
import java.util.Map;

import org.ednovo.gooru.search.es.model.UserV2;

/**
 * @author Renuka
 * 
 */
public class PedagogyCollectionSearchResult extends PedagogySearchResult implements Serializable {

	/**
	* 
	*/
	private static final long serialVersionUID = 5673214015280665109L;

	private String url;

	private String format;

	private Integer viewCount;

	private String grade;

	private String learningObjective;

	private Integer collectionItemCount;

	private Integer questionCount;

	private Integer resourceCount;

	private Integer collectionRemixCount;

	private Integer collaboratorCount;

	private String thumbnail;

	private UserV2 creator;

	private UserV2 user;

	private Map<String, Object> taxonomy;

	private Long remixedInCourseCount;

	private Long usedByStudentCount;

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

	public Integer getCollectionItemCount() {
		return collectionItemCount;
	}

	public void setCollectionItemCount(Integer collectionItemCount) {
		this.collectionItemCount = collectionItemCount;
	}

	public void setQuestionCount(Integer questionCount) {
		this.questionCount = questionCount;
	}

	public Integer getQuestionCount() {
		return questionCount;
	}

	public void setResourceCount(Integer resourceCount) {
		this.resourceCount = resourceCount;
	}

	public Integer getResourceCount() {
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

	public Integer getViewCount() {
		return viewCount;
	}

	public void setViewCount(Integer viewCount) {
		this.viewCount = viewCount;
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
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
}
