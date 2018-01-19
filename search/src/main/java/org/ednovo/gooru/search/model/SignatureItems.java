package org.ednovo.gooru.search.model;

import org.ednovo.gooru.search.es.model.UserV2;

public class SignatureItems {

	private String id;
	private String title;
	private String thumbnail;
	private UserV2 creator;
	private UserV2 owner;
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
