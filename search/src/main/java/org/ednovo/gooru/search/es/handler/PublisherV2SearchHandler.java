package org.ednovo.gooru.search.es.handler;

import static org.ednovo.gooru.search.es.processor.SearchProcessorType.AutoCompletePublisherDeserializeProcessor;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.AutoCompletePublisherDslQueryBuild;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.BlackListQueryValidation;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.Elasticsearch;

import java.util.Map;

import org.ednovo.gooru.search.es.constant.EsIndex;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.springframework.stereotype.Component;

@Component
public class PublisherV2SearchHandler extends SearchHandler<SearchData, Map<String, Object>>{

	private static final SearchProcessorType[][] searchProcessorTypes = new SearchProcessorType[][] {
		{ BlackListQueryValidation }, { AutoCompletePublisherDslQueryBuild }, { Elasticsearch }, { AutoCompletePublisherDeserializeProcessor } };
	
	@Override
	public SearchResponse<Map<String, Object>> search(SearchData searchData) {
		searchData.setAllowLeadingWildcard(true);
		return super.search(searchData);
	}

	
	@Override
	protected SearchProcessorType[][] getProcessorTypeChain() {
		return searchProcessorTypes;
	}

	@Override
	protected SearchHandlerType getType() {
		return SearchHandlerType.PUBLISHER;
	}

	@Override
	protected EsIndex getIndexType() {
		return EsIndex.CONTENT_PROVIDER;
		
	}

}
