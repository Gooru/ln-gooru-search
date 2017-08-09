package org.ednovo.gooru.search.model;

public class SignatureCollectionDTO {

	private String collection_id;
	private String score_range_name;

	public String getCollection_id() {
		return collection_id;
	}

	public void setCollection_id(String string) {
		this.collection_id = string;
	}

	public String getScore_range_name() {
		return score_range_name;
	}

	public void setScore_range_name(String score_range_name) {
		this.score_range_name = score_range_name;
	}

}
