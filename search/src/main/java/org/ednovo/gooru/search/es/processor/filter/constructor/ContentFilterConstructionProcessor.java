/**
 * 
 */
package org.ednovo.gooru.search.es.processor.filter.constructor;

import org.apache.commons.lang3.StringUtils;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.ednovo.gooru.search.es.processor.util.GradeUtils;
import org.springframework.stereotype.Component;

/**
 * @author SearchTeam
 * 
 */
@Component
public class ContentFilterConstructionProcessor extends FilterConstructionProcessor {

	@Override
	public void process(SearchData searchData,
			SearchResponse<Object> response) {
		super.process(searchData, response);
		
		// As default search will serve published contents only. If publish filter passed results will be filtered based on filter value.
		if(searchData != null && searchData.getFilters() != null) { 
			if (!searchData.getFilters().containsKey(FLT_PUBLISH_STATUS)) {
				searchData.putFilter(FLT_PUBLISH_STATUS, PublishedStatus.PUBLISHED.getStatus());
			}
			if ((searchData.getTaxFilterType() != null && TAX_FILTERS.matcher(searchData.getTaxFilterType()).matches()) && searchData.isCrosswalk()) {
				if (searchData.getFilters().containsKey(AMPERSAND_STANDARD) && StringUtils.isNotBlank(searchData.getFilters().get(AMPERSAND_STANDARD).toString())) {
					searchData.putFilter(AMPERSAND_EQ_INTERNAL_CODE, searchData.getFilters().get(AMPERSAND_STANDARD).toString().toLowerCase());
					searchData.getFilters().remove(AMPERSAND_STANDARD);
				}
				
				if (searchData.getFilters().containsKey(AMPERSAND_STANDARD_DISPLAY) && StringUtils.isNotBlank(searchData.getFilters().get(AMPERSAND_STANDARD_DISPLAY).toString())) {
					searchData.putFilter(AMPERSAND_EQ_DISPLAY_CODE, searchData.getFilters().get(AMPERSAND_STANDARD_DISPLAY).toString().toLowerCase());
					searchData.getFilters().remove(AMPERSAND_STANDARD_DISPLAY);
				}
				
/*				if (searchData.getFilters().containsKey(AMPERSAND_COURSE) && StringUtils.isNotBlank(searchData.getFilters().get(AMPERSAND_COURSE).toString())) {
					searchData.putFilter(AMPERSAND_EQ_COURSE_INTERNAL_CODE, searchData.getFilters().get(AMPERSAND_COURSE).toString().toLowerCase());
					searchData.getFilters().remove(AMPERSAND_COURSE);
				}
				
				if (searchData.getFilters().containsKey(AMPERSAND_DOMAIN) && StringUtils.isNotBlank(searchData.getFilters().get(AMPERSAND_DOMAIN).toString())) {
					searchData.putFilter(AMPERSAND_EQ_DOMAIN_INTERNAL_CODE, searchData.getFilters().get(AMPERSAND_DOMAIN).toString().toLowerCase());
					searchData.getFilters().remove(AMPERSAND_DOMAIN);
				}*/
			}
		} else {
			searchData.putFilter(FLT_PUBLISH_STATUS, PublishedStatus.PUBLISHED.getStatus());
		}

	}   

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.ContentFilterConstruction;
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
		} else if (key.equalsIgnoreCase(SEARCH_ADD_DATE) || key.equalsIgnoreCase(SEARCH_LAST_MDIFIED)) {
			String filterDate[] = ((String) values).split("--");
			if (filterDate != null && filterDate.length > 0) {
				if (filterDate.length == 2) {
					searchData.putFilter(type + "^" + key, filterDate[0] + "<>" + filterDate[1]);
				} else {
					searchData.putFilter(type + "^" + key, values + "<>" + values);
				}
			}
		} else if (key.equalsIgnoreCase(SEARCH_HAS_ATTRIBUTION)) {
			String value = "resourceSource.attribution:|#^resourceSource.attribution:";
			if (((String) values).equalsIgnoreCase(SEARCH_VALUE_EMPTY)) {
				searchData.putFilter("&^" + key, value);
			} else if (((String) values).equalsIgnoreCase(SEARCH_VALUE_NOTEMPTY)) {
				searchData.putFilter("!^" + key, value);
			}
		} else if (key.equalsIgnoreCase(SEARCH_HASNO_DESCRIPTION) || key.equalsIgnoreCase(SEARCH_HASNO_TAXONOMY)) {
			if (((String) values).equalsIgnoreCase(SEARCH_VALUE_EMPTY)) {
				searchData.putFilter(type + "^" + key, "1");
			} else if (((String) values).equalsIgnoreCase(SEARCH_VALUE_NOTEMPTY)) {
				searchData.putFilter(type + "^" + key, "0");
			}
		} else if (key.equalsIgnoreCase(SEARCH_HAS_NOSTANDARDS)) {
			if (((String) values).equalsIgnoreCase(SEARCH_VALUE_EMPTY)) {
				searchData.putFilter(type + "^" + SEARCH_HAS_STANDARDS, "0");
			} else if (((String) values).equalsIgnoreCase(SEARCH_VALUE_NOTEMPTY)) {
				searchData.putFilter(type + "^" + SEARCH_HAS_STANDARDS, "1");
			}
		} else {
			return false;
		}
		return true;
	}

	@Override
	protected boolean processParam(SearchData searchData,
			SearchResponse<Object> response,
			String key,
			Object values) {
		if (key.equals(SEARCH_CATEGORY) && !((String) values).equalsIgnoreCase(SEARCH_CATEGORY_VALUES)) {
			searchData.putFilter("&^category", values);
			return true;
		}
		return false;
	}

}
