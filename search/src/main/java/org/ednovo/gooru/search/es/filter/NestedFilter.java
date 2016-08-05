/**
 * 
 */
package org.ednovo.gooru.search.es.filter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author SearchTeam
 * 
 */
public class NestedFilter {

	private Map<String, Object> nested = new HashMap<String, Object>(2);

	public NestedFilter(String path,
			Object filter) {
		nested.put("path", path);
		nested.put("filter", filter);
	}

	public Map<String, Object> getNested() {
		return nested;
	}

	public void setNested(Map<String, Object> nested) {
		this.nested = nested;
	}

}
