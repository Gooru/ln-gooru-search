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
public class TermsFilter {

	private Map<String, Object> terms = new HashMap<String, Object>(1);

	public TermsFilter(String key,
			Object value) {
		terms.put(key, value);
	}

	public Map<String, Object> getTerms() {
		return terms;
	}

	public void setTerms(Map<String, Object> term) {
		this.terms = term;
	}

}
