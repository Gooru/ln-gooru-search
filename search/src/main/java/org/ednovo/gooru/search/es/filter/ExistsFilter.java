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
public class ExistsFilter {

	private Map<String, Object> exists = new HashMap<String, Object>(1);

	public ExistsFilter(String fieldName) {
		exists.put("field", fieldName);
	}

	public Map<String, Object> getExists() {
		return exists;
	}

	public void setExists(Map<String, Object> missing) {
		this.exists = missing;
	}

}
