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
public class ConstantScore {

	private Map<String, Object> constant_score;

	public ConstantScore(String type,
			String key,
			Object value,
			Float boost) {
		constant_score = new HashMap<String, Object>(2);
		if(value instanceof String) {
			constant_score.put(type, new TermFilter(key, value));
		} else {
			constant_score.put(type, new TermsFilter(key, value));
		}
		constant_score.put("boost", boost);
	}

	public Map<String, Object> getConstant_score() {
		return constant_score;
	}

	public void setConstant_score(Map<String, Object> constant_score) {
		this.constant_score = constant_score;
	}

}
