/**
 * 
 */
package org.ednovo.gooru.search.es.filter;

import java.util.List;

/**
 * @author SearchTeam
 * 
 */
public class OrFilter {

	private List<Object> or;

	public OrFilter(List<Object> data) {
		or = data;
	}

	public List<Object> getOr() {
		return or;
	}

	public void setOr(List<Object> or) {
		this.or = or;
	}

}
