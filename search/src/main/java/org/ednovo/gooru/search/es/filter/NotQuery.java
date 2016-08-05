package org.ednovo.gooru.search.es.filter;

public class NotQuery {

	private Object mustNot;

	public NotQuery(Object mustNot) {
		this.mustNot = mustNot;
	}

	public Object getMustNot() {
		return mustNot;
	}

	public void setMustNot(Object mustNot) {
		this.mustNot = mustNot;
	}

}
