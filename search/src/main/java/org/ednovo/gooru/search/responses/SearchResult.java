package org.ednovo.gooru.search.responses;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class SearchResult implements Serializable {

	private static final long serialVersionUID = 6586694154253184566L;

	private String title;

	private String id;
	
	private String publishStatus;

	private Boolean isCrosswalked = false;

	private Map<String, Object> taxonomy;

	private Boolean isFeatured = false;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPublishStatus() {
		return publishStatus;
	}

	public void setPublishStatus(String publishStatus) {
		this.publishStatus = publishStatus;
	}

	public Boolean getIsCrosswalked() {
		return isCrosswalked;
	}

	public void setIsCrosswalked(Boolean isCrosswalked) {
		this.isCrosswalked = isCrosswalked;
	}
	
	public Map<String, Object> getTaxonomy() {
		return taxonomy;
	}

	public void setTaxonomy(Map<String, Object> taxonomy) {
		this.taxonomy = taxonomy;
	}

    public Boolean getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(Boolean isFeatured) {
        this.isFeatured = isFeatured;
    }

}
