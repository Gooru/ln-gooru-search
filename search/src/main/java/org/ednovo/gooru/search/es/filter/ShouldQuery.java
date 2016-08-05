package org.ednovo.gooru.search.es.filter;

import java.util.List;

public class ShouldQuery {
	
	private List<Object> should;

	public ShouldQuery(List<Object> should) {
		this.should = should;
	}

	public List<Object> getShould() {
		return should;
	}

	public void setShould(List<Object> should) {
		this.should = should;
	}

}
