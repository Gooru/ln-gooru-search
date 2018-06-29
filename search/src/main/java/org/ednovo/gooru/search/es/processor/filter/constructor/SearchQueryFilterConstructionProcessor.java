/**
 * 
 */
package org.ednovo.gooru.search.es.processor.filter.constructor;

import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.ednovo.gooru.search.responses.SearchResponse;
import org.springframework.stereotype.Component;

/**
 * @author SearchTeam
 * 
 */
@Component
public class SearchQueryFilterConstructionProcessor extends FilterConstructionProcessor {

	@Override
	public void process(SearchData searchData,
			SearchResponse<Object> response) {
		super.process(searchData, response);
		//searchData.putFilter("&^organization.partyUid", UserGroupSupport.getUserOrganizationUids());
	}
	
	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.SearchQueryFilterConstruction;
	}
}
