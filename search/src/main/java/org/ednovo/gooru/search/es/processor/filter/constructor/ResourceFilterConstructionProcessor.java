/**
 * 
 */
package org.ednovo.gooru.search.es.processor.filter.constructor;

import org.ednovo.gooru.search.es.constant.IndexFields;
import org.ednovo.gooru.search.es.constant.SearchFilterConstants;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
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
		
		//searchData.putFilter("&^statistics.invalidResource", "0");
/*		if(!(searchData.getUser().getUserRoleSetString().contains(SEARCH_SUPER_ADMIN) || searchData.getUser().getUserRoleSetString().contains(SEARCH_CONTENT_ADMIN))) {
			searchData.putFilter("&^statistics.statusIsBroken", "0");
		}*/	
		
/*		// Include unpublished content in search results or not  
		
		String includeUnPublished = SearchSettingService.getByName(SearchSettingConstants.INCLUDE_UNPUBLISHED_CONTENT);
		if(includeUnPublished != null && includeUnPublished.equalsIgnoreCase("false")){
			searchData.putFilter(AMPERSAND + CARET_SYMBOL + IndexFields.PUBLISH_STATUS, PublishedStatus.PUBLISHED.name());
		}
*/		
		if(searchData.getFilters()!= null && searchData.getFilters().containsKey("&^type") && searchData.getFilters().get("&^type").equals(SEARCH_QUESTION)) {
			searchData.putFilter(AMPERSAND + CARET_SYMBOL + IndexFields.CONTENT_FORMAT, SEARCH_QUESTION);
			searchData.getFilters().remove("&^type");
		}
		
		// Default filter to get non-broken content 
		searchData.putFilter(FLT_STATUS_BROKEN, 0);

		// Default filter to only get resources that is not mapped to user created course
		searchData.putFilter(FLT_COURSE_MISSING, "");
		
		
		if(searchData != null && searchData.getFilters() != null){
			String contentFormat = null;
			String courseId = null;
			if(searchData.getFilters().containsKey(FLT_CONTENT_FORMAT)){
				contentFormat = (String) searchData.getFilters().get(FLT_CONTENT_FORMAT);
			}
			if(searchData.getFilters().containsKey(FLT_COURSE_ID)){
				courseId = (String) searchData.getFilters().get(FLT_COURSE_ID);
			}
			
			if(courseId != null || (contentFormat != null && contentFormat.equalsIgnoreCase(SEARCH_QUESTION))){
				searchData.getFilters().remove(FLT_COURSE_MISSING);
			}
		}

		if (searchData.getFilters() != null && searchData.getFilters().containsKey("&^resourceFormat")) {
			String contentSubFormat = (String) searchData.getFilters().get("&^resourceFormat");
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
		}
		
	}

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.ResourceFilterConstruction;
	}
}
