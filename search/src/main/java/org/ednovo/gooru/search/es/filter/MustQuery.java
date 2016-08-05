package org.ednovo.gooru.search.es.filter;

import java.util.List;

public class MustQuery {

	private List<Object> must;

	public MustQuery(List<Object> must) {
		this.must = must;
	}

	public List<Object> getMust() {
		return must;
	}

	public void setMust(List<Object> must) {
		this.must = must;
	}

}
