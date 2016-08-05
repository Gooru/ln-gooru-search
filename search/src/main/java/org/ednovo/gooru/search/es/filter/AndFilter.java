/**
 * 
 */
package org.ednovo.gooru.search.es.filter;

import java.util.List;

/**
 * @author SearchTeam
 * 
 */
public class AndFilter {

	private List<Object> and;
	
	public AndFilter(List<Object> and) {
		this.and = and;
	}
	
	public List<Object> getAnd() {
		return and;
	}

	public void setAnd(List<Object> and) {
		this.and = and;
	}

}
