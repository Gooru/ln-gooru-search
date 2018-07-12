package org.ednovo.gooru.search.responses.v3;

import java.io.Serializable;
import java.util.Date;

import org.ednovo.gooru.search.responses.Metadata;

public class CourseSearchResult extends SearchResult implements Serializable {

	/**
	* 
	*/
	private static final long serialVersionUID = 6280104303486215659L;

	private Date createdAt;
	
	private Date modifiedAt;

	private UserV3 creator;

	private String description;

	private String format;

	private String thumbnail;

	private String playerUrl;

	private Metadata metadata;
	
	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(Date modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	public UserV3 getCreator() {
		return creator;
	}

	public void setCreator(UserV3 creator) {
		this.creator = creator;
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

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getPlayerUrl() {
		return playerUrl;
	}

	public void setPlayerUrl(String playerUrl) {
		this.playerUrl = playerUrl;
	}

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

}
