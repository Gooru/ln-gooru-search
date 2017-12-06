/**
 * 
 */
package org.ednovo.gooru.search.es.filter;

/**
 * @author SearchTeam
 * 
 */
public class Filter {

	private Object filter;
	
	private String weight;

	public Filter(Object filter,
			String weight) {
		this.filter = filter;
		this.weight = weight;
	}

	public Object getFilter() {
		return filter;
	}

	public void setFilter(Object filter) {
		this.filter = filter;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getWeight() {
		return weight;
	}

}
