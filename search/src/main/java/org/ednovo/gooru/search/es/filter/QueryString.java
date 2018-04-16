package org.ednovo.gooru.search.es.filter;

public class QueryString {

	private Object query_string = new Object();

	public QueryString(
			Object query) {
		query_string = query;
	}
	
	public Object getQuery_string() {
		return query_string;
	}

	public void setQuery_string(Object query_string) {
		this.query_string = query_string;
	}

}
