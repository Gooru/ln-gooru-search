/**
 * 
 */
package org.ednovo.gooru.search.es.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author SearchTeam
 * 
 */
public class Filter {

	private Object filter;

	private String boost;
	
	private String boost_factor;

	public Filter(Object filter,
			String boost) {
		Map<String, Object> or = new HashMap<String, Object>(1);
		List<Object> filterList = new ArrayList<Object>();
		or.put("or", filterList);
		filterList.add(filter);
		this.filter = or;
		//this.boost = boost;
		this.boost_factor = boost;
	}

	public Object getFilter() {
		return filter;
	}

	public void setFilter(Object filter) {
		this.filter = filter;
	}

	public String getBoost() {
		return boost;
	}

	public void setBoost(String boost) {
		this.boost = boost;
	}

	public void setBoost_factor(String boost_factor) {
		this.boost_factor = boost_factor;
	}

	public String getBoost_factor() {
		return boost_factor;
	}

}
