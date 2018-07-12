package org.ednovo.gooru.search.es.processor.filter.constructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.ednovo.gooru.search.es.processor.util.FilterBuilderUtils;
import org.ednovo.gooru.search.responses.SearchResponse;
import org.springframework.stereotype.Component;

@Component
public class FacetFilterConstructionProcessor extends ContentFilterConstructionProcessor {

	
	
	
	private static List<String> excludeFilters = new ArrayList<String>();
	
	@PostConstruct
	public void startUp() {
		excludeFilters.add("&^category");
		excludeFilters.add("&^resourceFormat");
	}
	
	@Override
	public void process(SearchData searchData, SearchResponse<Object> response) {
		
		if(searchData.getFacet() != null && searchData.getFacet().trim().length() > 0){
			
			if (searchData.getFilters() != null && searchData.getFilters().size() > 0) {
				
				Map<String, Object> searchFilters = searchData.getFilters();
				for(String field : excludeFilters){
					searchFilters.remove(field);
				}
				
				Object boolQuery = FilterBuilderUtils.buildFilters(searchData.getFilters());
				if(boolQuery != null) searchData.getQueryDsl().put(POST_FILTER, boolQuery);
				Map<String, Object> facets = new HashMap<String, Object>();
				for(String facet : searchData.getFacet().split("\\,")){
					Map<String, Object> facetFilter = new HashMap<String, Object>();
					Map<String, Object> facetTerms = new HashMap<String, Object>();
					String facetValue = facet;
					if(FacetFilterAliasConstants.getFields(facet) != null){
						facetValue = FacetFilterAliasConstants.getFields(facet);
					}
					facetTerms.put(FIELD, facetValue);
					facetTerms.put(SIZE, 20);
					facetFilter.put(TERMS, facetTerms);
					facetFilter.put(FACET_FILTER, boolQuery);
					facets.put(facet, facetFilter);
				}
				searchData.getQueryDsl().put(FACETS, facets);
				
			}
		}
	}
	
	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.FacetFilterConstruction;
	}
	
}
