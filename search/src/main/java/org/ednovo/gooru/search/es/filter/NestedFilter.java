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
public class NestedFilter {

	private Map<String, Object> nested = new HashMap<String, Object>(2);

	public NestedFilter(String path,
			Object filter) {
		nested.put("path", path);
		List<Object> filters = new ArrayList<Object>(1);
		filters.add(filter);
		FilterQuery filterQuery = new FilterQuery(filters);
		BoolQuery boolQuery = new BoolQuery();
		boolQuery.setBool(filterQuery);
		nested.put("query", boolQuery);
	}

	public Map<String, Object> getNested() {
		return nested;
	}

	public void setNested(Map<String, Object> nested) {
		this.nested = nested;
	}

}
