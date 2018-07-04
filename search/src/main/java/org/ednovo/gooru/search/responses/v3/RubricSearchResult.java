package org.ednovo.gooru.search.responses.v3;

import java.io.Serializable;
import java.util.Date;

import org.ednovo.gooru.search.responses.Metadata;

public class RubricSearchResult extends SearchResult implements Serializable {

	private static final long serialVersionUID = 5673214015280665128L;

	private String url;

	private String thumbnail;

	private String description;

	private Date createdAt;

	private Date modifiedAt;

	private UserV3 creator;

	private String format;
		
	private Metadata metadata;

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

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}


}
