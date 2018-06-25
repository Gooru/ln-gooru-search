package org.ednovo.gooru.search.responses;

import java.io.Serializable;
import java.util.List;

import org.ednovo.gooru.search.es.model.UserV2;

public class PedagogyRubricSearchResult extends PedagogySearchResult implements Serializable {

	private static final long serialVersionUID = 6586694154253184566L;

	private static final String INDEX_TYPE = "rubric";

	private String url;

	private String thumbnail;

	private String description;

	private UserV2 originalCreator;

	private UserV2 creator;

	private Long views = 0L;

	private Integer questionCount;

	private String contentFormat;
	
	private List<String> audience;

	private Double efficacy;
	
	private Double engagement;
	
	private Double relevance;

	public static String getIndexType() {
		return INDEX_TYPE;
	}

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

	public UserV2 getOriginalCreator() {
		return originalCreator;
	}

	public void setOriginalCreator(UserV2 originalCreator) {
		this.originalCreator = originalCreator;
	}

	public UserV2 getCreator() {
		return creator;
	}

	public void setCreator(UserV2 creator) {
		this.creator = creator;
	}

	public Long getViews() {
		return views;
	}

	public void setViews(Long views) {
		this.views = views;
	}

	public Integer getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(Integer questionCount) {
		this.questionCount = questionCount;
	}

	public String getContentFormat() {
		return contentFormat;
	}

	public void setContentFormat(String contentFormat) {
		this.contentFormat = contentFormat;
	}

	public List<String> getAudience() {
		return audience;
	}

	public void setAudience(List<String> audience) {
		this.audience = audience;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
