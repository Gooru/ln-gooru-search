package org.ednovo.gooru.search.es.model;

public enum ContentFormat {

	RESOURCE("resource"), 
	QUESTION("question"), 
	COLLECTION("collection"), 
	ASSESSMENT("assessment"),
	;

	private String contentFormat;

	ContentFormat(String contentFormat) {
		this.contentFormat = contentFormat;
	}

	public String getContentFormat() {
		return this.contentFormat;
	}

}
