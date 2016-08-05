package org.ednovo.gooru.search.es.filter;

import java.util.List;

public class MustNotQuery {

	private List<Object> mustNot;

	public MustNotQuery(List<Object> mustNot) {
		this.mustNot = mustNot;
	}

	public List<Object> getMustNot() {
		return mustNot;
	}

	public void setMustNot(List<Object> mustNot) {
		this.mustNot = mustNot;
	}

}