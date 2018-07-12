package org.ednovo.gooru.search.responses.v3;

import java.io.Serializable;

public class SearchResult implements Serializable {

	private static final long serialVersionUID = 6586694154253184566L;

	private String title;

	private String id;
	
	
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
}
