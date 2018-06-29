/**
 * 
 */
package org.ednovo.gooru.search.es.processor.filter.constructor;

import org.apache.commons.lang3.StringUtils;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.ednovo.gooru.search.es.processor.util.GradeUtils;
import org.ednovo.gooru.search.responses.SearchResponse;
import org.springframework.stereotype.Component;

/**
 * @author SearchTeam
 * 
 */
@Component
public class UserFilterConstructionProcessor extends FilterConstructionProcessor {

	@Override
	public void process(SearchData searchData,SearchResponse<Object> response) {
		super.process(searchData, response);
		if (!searchData.isRestricted()) {
			searchData.putFilter("&^confirmStatus", "1");
		}
	}
	
	@Override
	protected boolean processFilter(SearchData searchData,
			SearchResponse<Object> response,
			String type,
			String key,
			Object values) {
		super.processFilter(searchData, response, type, key, values);
		
		if (key.equals(SEARCH_GRADE)) {
			searchData.putFilter(type + "^grade", StringUtils.join(GradeUtils.parseGrade((String) values), ","));
		} else if (key.equalsIgnoreCase(SEARCH_CREATED_ON)) {
			String filterDate[] = ((String) values).split("--");
			if (filterDate != null && filterDate.length > 0) {
				if (filterDate.length == 2) {
					searchData.putFilter(type + "^" + key, filterDate[0] + "<>" + filterDate[1]);
				} else {
					searchData.putFilter(type + "^" + key, values + "<>" + values);
				}
			}
		} else {
			return false;
		}
		return true;
	}

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.UserFilterConstruction;
	}
}
