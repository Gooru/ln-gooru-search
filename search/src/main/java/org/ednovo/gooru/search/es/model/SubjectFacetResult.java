package org.ednovo.gooru.search.es.model;

import java.io.Serializable;

public class SubjectFacetResult implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2149641840255902089L;
	
	private String term;
	private Long count;
	public void setTerm(String term) {
		this.term = term;
	}
	public String getTerm() {
		return term;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public Long getCount() {
		return count;
	}
	

}
