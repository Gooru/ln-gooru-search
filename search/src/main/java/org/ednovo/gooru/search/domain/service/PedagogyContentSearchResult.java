package org.ednovo.gooru.search.domain.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.search.es.model.UserV2;
/**
 * @author Renuka
 * 
 */
public class PedagogyContentSearchResult implements Serializable {

	private static final long serialVersionUID = 6586694154253184566L;

	private static final String INDEX_TYPE = "resource";

	private String url;

	private String title;

	private String thumbnail;

	private String description;

	private String id;

	private UserV2 user;

	private UserV2 creator;

	private Long viewCount;

	private Boolean hasFrameBreaker;

	private Map<String, Object> taxonomySet;

	private List<String> publisher;

	private String publishStatus;

	private String contentFormat;

	private String contentSubFormat;
	
	private Boolean isCrosswalked = false;
	
	private Map<String, Object> taxonomyEquivalentCompetencies;
		
	private Long remixedInCollectionCount;
	
	private Long remixedInAssessmentCount;
		
	private String grade;

	private Map<String, Object> taxonomy;

	private Double efficacy;
	
	private Double engagement;
	
	private Double relevance;
	
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public UserV2 getUser() {
		return user;
	}

	public void setUser(UserV2 user) {
		this.user = user;
	}

	public UserV2 getCreator() {
		return creator;
	}

	public void setCreator(UserV2 creator) {
		this.creator = creator;
	}

	public Long getViewCount() {
		return viewCount;
	}

	public void setViewCount(Long viewCount) {
		this.viewCount = viewCount;
	}

	public Boolean getHasFrameBreaker() {
		return hasFrameBreaker;
	}

	public void setHasFrameBreaker(Boolean hasFrameBreaker) {
		this.hasFrameBreaker = hasFrameBreaker;
	}
	
	public Map<String, Object> getTaxonomySet() {
		return taxonomySet;
	}

	public void setTaxonomySet(Map<String, Object> taxonomySet) {
		this.taxonomySet = taxonomySet;
	}
	
	public static String getIndexType() {
		return INDEX_TYPE;
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

	public Boolean getIsCrosswalked() {
		return isCrosswalked;
	}

	public void setIsCrosswalked(Boolean isCrosswalked) {
		this.isCrosswalked = isCrosswalked;
	}
	
	public Map<String, Object> getTaxonomyEquivalentCompetencies() {
		return taxonomyEquivalentCompetencies;
	}

	public void setTaxonomyEquivalentCompetencies(Map<String, Object> taxonomyEquivalentCompetencies) {
		this.taxonomyEquivalentCompetencies = taxonomyEquivalentCompetencies;
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

	public Map<String, Object> getTaxonomy() {
		return taxonomy;
	}
	
	public void setTaxonomy(Map<String, Object> taxonomy) {
		this.taxonomy = taxonomy;
	}
	
	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
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
