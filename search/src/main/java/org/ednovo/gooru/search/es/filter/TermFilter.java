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
public class TermFilter {

	private Map<String, Object> term = new HashMap<String, Object>(1);

	public TermFilter(String key,
			Object value) {
		term.put(key, value);
	}

	public Map<String, Object> getTerm() {
		return term;
	}

	public void setTerm(Map<String, Object> term) {
		this.term = term;
	}

}
