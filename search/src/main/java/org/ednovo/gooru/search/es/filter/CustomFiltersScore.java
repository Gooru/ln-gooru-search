/**
 * 
 */
package org.ednovo.gooru.search.es.filter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author SearchTeam
 * 
 */
public class CustomFiltersScore {

	private Map<String, Object> custom_filters_score = new HashMap<String, Object>(2);

	public CustomFiltersScore(Object query, List<Object> filters) {
		custom_filters_score.put("query", query);
		custom_filters_score.put("filters", filters);
	}

	public Map<String, Object> getCustom_filters_score() {
		return custom_filters_score;
	}

	public void setCustom_filters_score(Map<String, Object> custom_filters_score) {
		this.custom_filters_score = custom_filters_score;
	}

}
