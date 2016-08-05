package org.ednovo.gooru.search.es.filter;

import java.util.List;

public class FilterQuery {

	private List<Object> filter;

	public FilterQuery(List<Object> filter) {
		this.filter = filter;
	}

	public List<Object> getFilter() {
		return filter;
	}

	public void setFilter(List<Object> filter) {
		this.filter = filter;
	}

}
