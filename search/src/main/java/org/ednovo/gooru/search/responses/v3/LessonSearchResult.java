package org.ednovo.gooru.search.responses.v3;

import java.io.Serializable;
import java.util.Date;

import org.ednovo.gooru.search.responses.Metadata;

public class LessonSearchResult extends SearchResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6280104303486215669L;

	private String playerUrl;
	
	private Date modifiedAt;

	private Date createdAt;

	private UserV3 creator;
	
	private Metadata metadata;

	public String getPlayerUrl() {
		return playerUrl;
	}

	public void setPlayerUrl(String playerUrl) {
		this.playerUrl = playerUrl;
	}

	public Date getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(Date modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public UserV3 getCreator() {
		return creator;
	}

	public void setCreator(UserV3 creator) {
		this.creator = creator;
	}

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}
	
	
}
