package org.ednovo.gooru.search.model;

import org.ednovo.gooru.search.es.model.UserV2;

public class SignatureResources {

	private String id;
	private String title;
	private String thumbnail;
	private String contentSubFormat;
	private String url;
	private UserV2 creator;
	private Double efficacy;
	private Double engagement;
	private Double relevance;

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

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getContentSubFormat() {
		return contentSubFormat;
	}

	public void setContentSubFormat(String contentSubFormat) {
		this.contentSubFormat = contentSubFormat;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public UserV2 getCreator() {
		return creator;
	}

	public void setCreator(UserV2 creator) {
		this.creator = creator;
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
