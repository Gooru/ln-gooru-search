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
public class RangeFilter {

	private Map<String, Object> range = new HashMap<String, Object>(1);

	public RangeFilter(String fieldName,
			String from,
			String to) {
		Map<String, Object> field = new HashMap<String, Object>(2);
		this.range.put(fieldName, field);
		field.put("from", from);
		field.put("to", to);

	}
	public RangeFilter(String value, String fieldName){
		Map<String, Object> field = new HashMap<String, Object>(1);
		range.put(fieldName,field);
		field.put("gte", value);
	}

	public Map<String, Object> getRange() {
		return range;
	}

	public void setRange(Map<String, Object> range) {
		this.range = range;
	}

}
