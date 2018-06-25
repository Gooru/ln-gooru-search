package org.ednovo.gooru.search.es.handler;

import static org.ednovo.gooru.search.es.processor.SearchProcessorType.BlackListQueryValidation;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.Elasticsearch;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.EsDslQueryBuild;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.EsSuggestDslQueryBuild;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.LimitValidation;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.SearchQueryDeserializer;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.SearchQueryFilterConstruction;

import java.util.Map;

import org.ednovo.gooru.search.es.constant.EsIndex;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.ednovo.gooru.search.responses.SearchResponse;
import org.springframework.stereotype.Component;

@Component
public class AutoCompleteV2SearchHandler extends SearchHandler<SearchData, Map<String, Object>> {

	private static final SearchProcessorType[][] searchProcessorTypes = new SearchProcessorType[][] { { BlackListQueryValidation },
			{ LimitValidation }, { SearchQueryFilterConstruction }, { EsSuggestDslQueryBuild }, { EsDslQueryBuild }, { Elasticsearch },
			{ SearchQueryDeserializer } };

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
		return SearchHandlerType.AUTOCOMPLETE;
	}

	@Override
	protected EsIndex getIndexType() {
		return EsIndex.SEARCH_QUERY;
	}

}
