package org.ednovo.gooru.search.es.filter;

import java.util.List;

/**
 * @author SearchTeam
 * 
 */
public class BoolQuery {
	
	private Object bool;
	private List<Object> must;
	private List<Object> must_not;
	private List<Object> should;
	private List<Object> filter;

	public BoolQuery() {
	}
	public BoolQuery(List<Object> must, List<Object> must_not, List<Object> should, List<Object> filter) {
		setMust(must);
		setMust_not(must_not);
		setShould(should);
		setFilter(filter);
	}
	public BoolQuery(List<Object> must) {
		setMust(must);
	}
	public Object getBool() {
		return bool;
	}
	public void setBool(Object bool) {
		this.bool = bool;
	}
	public List<Object> getMust() {
		return must;
	}
	public void setMust(List<Object> must) {
		this.must = must;
	}
	public List<Object> getMust_not() {
		return must_not;
	}
	public void setMust_not(List<Object> must_not) {
		this.must_not = must_not;
	}
	public List<Object> getShould() {
		return should;
	}
	public void setShould(List<Object> should) {
		this.should = should;
	}

	public List<Object> getFilter() {
		return filter;
	}

	public void setFilter(List<Object> filter) {
		this.filter = filter;
	}

}
