package org.ednovo.gooru.search.es.filter;

import java.util.List;

public class Filters {

	private List<Object> filters;
	
	public Filters(List<Object> filters) {
		this.filters = filters;
	}
	
	public List<Object> getFilters() {
		return filters;
	}

	public void setFilters(List<Object> filters) {
		this.filters = filters;
	}
	
}
