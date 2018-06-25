/**
 * 
 */
package org.ednovo.gooru.search.es.processor.filter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.search.es.handler.SearchHandler;
import org.ednovo.gooru.search.es.handler.SearchHandlerType;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.processor.SearchProcessor;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.ednovo.gooru.search.es.processor.util.FilterBuilderUtils;
import org.ednovo.gooru.search.responses.SearchResponse;
import org.springframework.stereotype.Component;

/**
 * @author SearchTeam
 * 
 */
@Component
public class SubjectFacetFilterProcessor extends SearchProcessor<SearchData, Object>  {


	
	@Override
	public void process(SearchData searchData, SearchResponse<Object> response) {
		
		if(!searchData.isShowSingleSubjectResults()){
			return;
		}
		
		Map<String, Object> searchFilters = searchData.getFilters();
		String subjectFilter = (String) searchFilters.get("&^"+SUBJECT_FILTER);
		if(subjectFilter != null){
			return;
		}
		Object boolQuery = FilterBuilderUtils.buildFilters(searchData.getFilters());
		if(boolQuery != null) searchData.getQueryDsl().put(POST_FILTER, boolQuery);		

		Map<String, Object> facetTerms = new HashMap<String, Object>();
		Map<String, Object> facetFilter = new HashMap<String, Object>();
		Map<String, Object> facets = new HashMap<String, Object>();

		facetTerms.put(FIELD, FACET_FIELD);
		facetFilter.put(TERMS, facetTerms);
		facetFilter.put(FACET_FILTER, boolQuery);
		facets.put(FACET_NAME, facetFilter);
		searchData.getQueryDsl().put(FACETS, facets);
		searchData.setFacetSubjectSearch(true);
		List<Map<String, Object>> searchResponse = (List<Map<String, Object>>) SearchHandler.getSearcher(SearchHandlerType.SUBJECT_FACET_FILTER.name()).search(searchData).getSearchResults();
			
		if (searchResponse != null && !searchResponse.isEmpty()) {
			if((Integer)searchResponse.get(0).get("count") >= 10){
				searchData.putFilter(SUBJECT_FILTER, searchResponse.get(0).get(TERM));
				//response.setCurrentSubject((String) searchResponse.get(0).get(TERM));
				searchResponse.remove(0);
			//	response.setRelatedSubjectMap(searchResponse);
			}
		}
		
		searchData.getQueryDsl().remove(FACETS);
		searchData.setFacetSubjectSearch(false);
	}
		
	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.SubjectFacetFilter;
	}
}
