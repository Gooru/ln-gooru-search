package org.ednovo.gooru.search.es.filter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author SearchTeam
 * 
 */
public class FunctionScore {

	private Map<String, Object> function_score = new HashMap<String, Object>(2);

	public FunctionScore(Object query, List<Object> filters) {
		function_score.put("query", query);
		function_score.put("functions", filters);
	}

	public Map<String, Object> getFunction_score() {
		return function_score;
	}

	public void setFunction_score(Map<String, Object> function_score) {
		this.function_score = function_score;
	}

}
