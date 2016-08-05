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
public class MissingFilter {

	private Map<String, Object> missing = new HashMap<String, Object>(1);

	public MissingFilter(String fieldName) {
		missing.put("field", fieldName);
	}

	public Map<String, Object> getMissing() {
		return missing;
	}

	public void setMissing(Map<String, Object> missing) {
		this.missing = missing;
	}

}
