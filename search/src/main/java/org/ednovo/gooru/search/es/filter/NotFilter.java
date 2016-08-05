/**
 * 
 */
package org.ednovo.gooru.search.es.filter;

/**
 * @author SearchTeam
 * 
 */
public class NotFilter {

	private Object not;

	public NotFilter(Object notFilter) {
		this.not = notFilter;
	}

	public Object getNot() {
		return not;
	}

	public void setNot(Object not) {
		this.not = not;
	}

}
