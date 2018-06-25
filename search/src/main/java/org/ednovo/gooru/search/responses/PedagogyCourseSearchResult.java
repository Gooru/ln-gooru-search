package org.ednovo.gooru.search.responses;

import java.io.Serializable;
import java.util.Map;

import org.ednovo.gooru.search.es.model.UserV2;
/**
 * @author Renuka
 * 
 */
public class PedagogyCourseSearchResult extends PedagogySearchResult implements Serializable {

	/**
	* 
	*/
	private static final long serialVersionUID = 6280104303486215659L;

	private UserV2 creator;

	private UserV2 owner;

	private UserV2 originalCreator;

	private Integer unitCount;

	private Integer courseRemixCount;

	private Long viewCount;

	private Map<String, Object> taxonomy;

	private Integer collaboratorCount;

	private String thumbnail;

	private String description;

	private String format;

	private Long lessonCount;

	private Boolean isFeatured;

	private Long collectionCount;

	private Long assessmentCount;

	private Long externalAssessmentCount;
	
	private Long remixedInClassCount;

	private Long usedByStudentCount;

	private Map<String, Object> taxonomyEquivalentCompetencies;

	private Double efficacy;
	
	private Double engagement;
	
	private Double relevance;
	
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
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public Long getLessonCount() {
		return lessonCount;
	}

	public void setLessonCount(Long lessonCount) {
		this.lessonCount = lessonCount;
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

	public Map<String, Object> getTaxonomyEquivalentCompetencies() {
		return taxonomyEquivalentCompetencies;
	}

	public void setTaxonomyEquivalentCompetencies(Map<String, Object> taxonomyEquivalentCompetencies) {
		this.taxonomyEquivalentCompetencies = taxonomyEquivalentCompetencies;
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
