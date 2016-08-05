package org.ednovo.gooru.search.es.model;

import java.io.Serializable;

public class Hint implements Serializable {

	private static final long serialVersionUID = 5773944979028571352L;

	private Integer hintId;

	private String hintText;

	public Integer getHintId() {
		return hintId;
	}

	public void setHintId(Integer hintId) {
		this.hintId = hintId;
	}

	public String getHintText() {
		return hintText;
	}

	public void setHintText(String hintText) {
		this.hintText = hintText;
	}

}
