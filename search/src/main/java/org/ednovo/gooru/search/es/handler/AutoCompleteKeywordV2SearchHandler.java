package org.ednovo.gooru.search.es.handler;

import static org.ednovo.gooru.search.es.processor.SearchProcessorType.BlackListQueryValidation;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.Elasticsearch;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.AutoCompleteKeywordDeserializeProcessor;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.AutoCompleteKeywordDslQueryBuild;

import java.util.Map;

import org.ednovo.gooru.search.es.constant.EsIndex;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.ednovo.gooru.search.responses.SearchResponse;
import org.springframework.stereotype.Component;

@Component
public class AutoCompleteKeywordV2SearchHandler extends SearchHandler<SearchData, Map<String, Object>> {

	private static final SearchProcessorType[][] searchProcessorTypes = new SearchProcessorType[][] { { BlackListQueryValidation },
			{ AutoCompleteKeywordDslQueryBuild }, { Elasticsearch }, { AutoCompleteKeywordDeserializeProcessor } };

	@Override
	public SearchResponse<Map<String, Object>> search(SearchData searchData) {
		searchData.setAllowLeadingWildcard(true);
		searchData.setDefaultOperator("or");
		return super.search(searchData);
	}

	@Override
	protected SearchProcessorType[][] getProcessorTypeChain() {
		return searchProcessorTypes;
	}

	@Override
	protected SearchHandlerType getType() {
		return SearchHandlerType.AUTOCOMPLETE_KEYWORD;
	}

	@Override
	protected EsIndex getIndexType() {
		return EsIndex.QUERY;
	}

}
