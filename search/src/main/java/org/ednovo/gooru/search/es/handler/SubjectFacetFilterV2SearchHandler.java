package org.ednovo.gooru.search.es.handler;

import static org.ednovo.gooru.search.es.processor.SearchProcessorType.Elasticsearch;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.EsDslQueryBuild;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.FacetDeserializer;

import java.util.List;
import java.util.Map;

import org.ednovo.gooru.search.es.constant.EsIndex;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.ednovo.gooru.search.es.service.SearchSettingService;
import org.springframework.stereotype.Component;

@Component
public class SubjectFacetFilterV2SearchHandler<I extends SearchData, O extends Object> extends SearchHandler<SearchData, List<Map<String,Object>>> {

	private static final SearchProcessorType[][] searchProcessorTypes = new SearchProcessorType[][] {{EsDslQueryBuild}, {Elasticsearch}, {FacetDeserializer}};
	
	@Override
	protected SearchProcessorType[][] getProcessorTypeChain() {
		return searchProcessorTypes;
	}

	@Override
	protected SearchHandlerType getType() {
		return SearchHandlerType.SUBJECT_FACET_FILTER;
	}

	@Override
	protected EsIndex getIndexType() {
		return EsIndex.RESOURCE;
	}
	
	@Override
	public SearchResponse<List<Map<String, Object>>> search(SearchData searchData) {
		SearchSettingService.validateCache();
		SearchResponse<List<Map<String, Object>>> response = new SearchResponse<List<Map<String, Object>>>();
		searchData.setIndexType(getIndexType());
		searchData.setType(SearchHandlerType.RESOURCE.name());
		processorChain.executeProcessorChain(searchData, (SearchResponse<List<Map<String, Object>>>) response, transactionTemplate);
		return response;
	}


}
