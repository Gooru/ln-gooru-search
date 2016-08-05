/**
 * 
 */
package org.ednovo.gooru.search.es.processor.filter.constructor;

import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.springframework.stereotype.Component;

/**
 * @author SearchTeam
 * 
 */
@Component
public class SCollectionFilterConstructionProcessor extends ContentFilterConstructionProcessor {

	@Override
	public void process(SearchData searchData,
			SearchResponse<Object> response) {
		super.process(searchData, response);
		
/*		// Include unpublished content in search results or not  
		
		String includeUnPublished = SearchSettingService.getByName(SearchSettingConstants.INCLUDE_UNPUBLISHED_COLLECTION);
		if(includeUnPublished != null && includeUnPublished.equalsIgnoreCase("false")){
			searchData.putFilter(AMPERSAND + CARET_SYMBOL + IndexFields.PUBLISH_STATUS, PublishedStatus.PUBLISHED.name());
		}
*/
	}

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.SCollectionFilterConstruction;
	}
}
