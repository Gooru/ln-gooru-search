/**
 * 
 */
package org.ednovo.gooru.search.es.processor.filter.constructor;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.ednovo.gooru.search.es.constant.IndexFields;
import org.ednovo.gooru.search.es.constant.SearchFilterConstants;
import org.ednovo.gooru.search.es.exception.BadRequestException;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.ednovo.gooru.search.es.service.SearchSettingService;
import org.ednovo.gooru.search.responses.SearchResponse;
import org.springframework.stereotype.Component;
/**
 * @author SearchTeam
 * 
 */
@Component
public class ResourceFilterConstructionProcessor extends ContentFilterConstructionProcessor {

	@Override
	public void process(SearchData searchData,
			SearchResponse<Object> response) {
		super.process(searchData, response);
		
		if(searchData.getFilters()!= null && searchData.getFilters().containsKey("&^type") && searchData.getFilters().get("&^type").equals(SEARCH_QUESTION)) {
			searchData.putFilter(AMPERSAND + CARET_SYMBOL + IndexFields.CONTENT_FORMAT, SEARCH_QUESTION);
			searchData.getFilters().remove("&^type");
		}
		
		// Default filter to get non-broken and student content
		searchData.putFilter(FLT_STATUS_BROKEN, 0);
		
		String scopeKey = null;
		if (searchData.getScope() != null && searchData.getScope().getKey() != null && SCOPE_MATCH.matcher(searchData.getScope().getKey()).matches()) {
			scopeKey = searchData.getScope().getKey();
		}

		if (searchData != null && searchData.getFilters() != null) {
			String contentFormat = null;
			String courseId = null;
			if (searchData.getFilters().containsKey(AMPERSAND_CONTENT_FORMAT)) {
				contentFormat = (String) searchData.getFilters().get(AMPERSAND_CONTENT_FORMAT);
			}
			if ((contentFormat != null && contentFormat.equalsIgnoreCase(TYPE_RESOURCE))) {
				if (searchData.getFilters().containsKey(AMPERSAND_PQI_GTE) && StringUtils.isNotBlank((String) searchData.getFilters().get(AMPERSAND_PQI_GTE))) {
					searchData.putFilter(FLT_PUBLISHER_QUALITY_INDICATOR, StringUtils.join(parseEditorialTag((String) searchData.getFilters().get(AMPERSAND_PQI_GTE)), COMMA));
					searchData.getFilters().remove(AMPERSAND_PQI_GTE);
				} else if ((scopeKey == null || (scopeKey != null && !SCOPE_MYCONTENT_LIBRARY_MATCH.matcher(scopeKey).matches())) 
					&& (searchData.getFilters().containsKey(FLT_PUBLISH_STATUS) && ((String) searchData.getFilters().get(FLT_PUBLISH_STATUS)).equalsIgnoreCase(PublishedStatus.PUBLISHED.getStatus()))) {
					searchData.putFilter(FLT_PUBLISHER_QUALITY_INDICATOR, "3,4,5");
				}
			}
			String audience = null;
			if (searchData.getFilters().containsKey(AMPERSAND_AUDIENCE)) audience = (String) searchData.getFilters().get(AMPERSAND_AUDIENCE);
			if (audience == null || (audience != null && !audience.equalsIgnoreCase(AUDIENCE_TEACHERS))) {
				if (audience != null && audience.contains(AUDIENCE_TEACHERS) && audience.contains(AUDIENCE_ALL_STUDENTS) && audience.trim().length() == 21) {
					searchData.getFilters().remove(AMPERSAND_AUDIENCE);
				} else {
					searchData.putFilter(NOT_SYMBOL + CARET_SYMBOL + IndexFields.AUDIENCE, AUDIENCE_TEACHERS);
					if (audience != null) searchData.getFilters().remove(AMPERSAND_AUDIENCE);
				}
			}
        			
			if (searchData.getFilters().containsKey(FLT_COURSE_ID)) {
				courseId = (String) searchData.getFilters().get(FLT_COURSE_ID);
			}

			if (courseId != null || (contentFormat != null && contentFormat.equalsIgnoreCase(SEARCH_QUESTION))) {
				searchData.getFilters().remove(FLT_COURSE_MISSING);
			}
		}

		String contentSubFormat = null;
		if (searchData.getFilters() != null) {
			if (searchData.getFilters().containsKey("&^resourceFormat")) contentSubFormat = (String) searchData.getFilters().get("&^resourceFormat");
			if (searchData.getFilters().containsKey("&^subFormat")) contentSubFormat = (String) searchData.getFilters().get("&^subFormat");
		}
		if (contentSubFormat != null) {
			if (contentSubFormat.equalsIgnoreCase(SEARCH_QUESTION)) {
				searchData.putFilter(AMPERSAND + CARET_SYMBOL + IndexFields.CONTENT_FORMAT, SEARCH_QUESTION);
				
				// Removing default filter for question search, because all questions are original questions. So, this filter not applicable for question search
				searchData.getFilters().remove(FLT_COURSE_MISSING);
			} else if (contentSubFormat.contains(COMMA)) {
				String[] contentSubFormats = contentSubFormat.split(COMMA);
				StringBuilder subformats = new StringBuilder();
				for (String subFormat : contentSubFormats) {
					if (SearchFilterConstants.contentSubFormatKeySetContains(subFormat)) {
						if(subformats.length() > 0) {
							subformats.append(COMMA);
						}
						String mappingSubFormat = SearchFilterConstants.getContentSubFormatMapValue(subFormat);
						subformats.append(mappingSubFormat);
					}
				}
				if(subformats.length() > 0) {
					searchData.putFilter(AMPERSAND + CARET_SYMBOL + IndexFields.CONTENT_SUB_FORMAT, subformats.toString().split(COMMA));
				} else {
					searchData.putFilter(AMPERSAND + CARET_SYMBOL + IndexFields.CONTENT_SUB_FORMAT, contentSubFormats);
				}
			} else if (SearchFilterConstants.contentSubFormatKeySetContains(contentSubFormat)) {
				searchData.putFilter(AMPERSAND + CARET_SYMBOL + IndexFields.CONTENT_SUB_FORMAT, SearchFilterConstants.getContentSubFormatMapValue(contentSubFormat));
			} else {
				searchData.putFilter(AMPERSAND + CARET_SYMBOL + IndexFields.CONTENT_SUB_FORMAT, contentSubFormat);
			}
			searchData.getFilters().remove("&^resourceFormat");
			searchData.getFilters().remove("&^subFormat");
		}
		
	}
	
	private List<String> parseEditorialTag(String etTag) {
		List<String> etSet = new ArrayList<>();
		if (StringUtils.isNotBlank(etTag)) {
			try {
				int start = Integer.parseInt(etTag);
				int stop = Integer.parseInt(SearchSettingService.getByName(ET_MAX_VAL));
				for (int i = start; i <= stop; i++) {
					etSet.add(i + EMPTY_STRING);
				}
			} catch (NumberFormatException npe) {
				throw new BadRequestException("Number format Exception at publisherQualityIndicator filter");
			}
		}
		return etSet;
	}

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.ResourceFilterConstruction;
	}
}
