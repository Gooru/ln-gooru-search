package org.ednovo.gooru.search.es.handler;

import static org.ednovo.gooru.search.es.processor.SearchProcessorType.BlackListQueryValidation;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.Elasticsearch;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.EsDslQueryBuild;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.LimitValidation;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.GutDeserializer;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.TaxonomyFilterConstruction;

import java.util.Map;

import org.ednovo.gooru.search.es.constant.EsIndex;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.springframework.stereotype.Component;

@Component
public class GutV2SearchHandler extends SearchHandler<SearchData, Map<String, Object>> {

	private static final SearchProcessorType[][] searchProcessorTypes = new SearchProcessorType[][] { { BlackListQueryValidation }, { LimitValidation }, { TaxonomyFilterConstruction },
			{ EsDslQueryBuild }, { Elasticsearch }, { GutDeserializer } };

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
		return SearchHandlerType.GUT;
	}

	@Override
	protected EsIndex getIndexType() {
		return EsIndex.GUT;
	}

}
