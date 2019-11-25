package org.ednovo.gooru.search.responses;

import java.io.Serializable;
import java.util.List;

import org.ednovo.gooru.search.es.model.UserV2;
/**
 * @author Renuka
 * 
 */
public class PedagogyContentSearchResult extends PedagogySearchResult implements Serializable {

	private static final long serialVersionUID = 6586694154253184566L;

	private static final String INDEX_TYPE = "resource";

	private String url;

	private String thumbnail;

	private String description;

	private UserV2 user;

	private UserV2 creator;

	private Long viewCount;

	private Boolean hasFrameBreaker;

	private List<String> publisher;

	private String contentFormat;

	private String contentSubFormat;
				
	private Long remixedInCollectionCount;
	
	private Long remixedInAssessmentCount;
		
	private String grade;

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

	public static String getIndexType() {
		return INDEX_TYPE;
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
