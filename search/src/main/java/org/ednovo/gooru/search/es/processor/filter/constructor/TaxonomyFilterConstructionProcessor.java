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
public class TaxonomyFilterConstructionProcessor extends FilterConstructionProcessor {

	@Override
	public void process(SearchData searchData,
			SearchResponse<Object> response) {
		super.process(searchData, response);
		//searchData.putFilter("&^organization.partyUid", UserGroupSupport.getUserOrganizationUids());
		if(searchData.getParameters().containsKey("searchBy") && searchData.getParameters().getString("searchBy").equalsIgnoreCase("standard")){
			if(searchData.getUserTaxonomyPreference() != null && searchData.getUserTaxonomyPreference().trim().length() > 0){
				searchData.putFilter("&^rootNodeId", searchData.getUserTaxonomyPreference());
			} else {
				searchData.putFilter("&^rootNodeId", 0);
			}
		}
		
		if (searchData.getParameters().getString("searchBy").equalsIgnoreCase("standard")) {
			searchData.putFilter("&^isAssociateToCode", "1");
		} else if (searchData.getParameters().getString("searchBy").equalsIgnoreCase("standardCode")) {
			String expandedQuery = searchData.getQueryString().replace(".", "").replace(".--", "").replace("-", "").replace("\"", "").replace(" ", "_");
			searchData.putFilter("&^codeQuery", expandedQuery);
		}
	}

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.TaxonomyFilterConstruction;
	}
}
