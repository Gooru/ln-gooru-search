package org.ednovo.gooru.search.es.handler;

import static org.ednovo.gooru.search.es.processor.SearchProcessorType.BlackListQueryValidation;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.DictionaryQueryExpansion;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.Elasticsearch;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.EsDslQueryBuild;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.KeywordCompetencyDeserializer;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.KeywordCompetencyFilterConstruction;

import java.util.Map;

import org.ednovo.gooru.search.es.constant.EsIndex;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.springframework.stereotype.Component;

@Component
public class KeywordCompetencyV2SearchHandler extends SearchHandler<SearchData, Map<String, Object>> {

	private static final SearchProcessorType[][] searchProcessorTypes = new SearchProcessorType[][] { { BlackListQueryValidation }, { DictionaryQueryExpansion },
			{ KeywordCompetencyFilterConstruction }, { EsDslQueryBuild }, { Elasticsearch }, { KeywordCompetencyDeserializer } };

	@Override
	protected SearchProcessorType[][] getProcessorTypeChain() {
		return searchProcessorTypes;
	}

	@Override
	protected SearchHandlerType getType() {
		return SearchHandlerType.KEYWORDCOMPETENCY;
	}

	@Override
	protected EsIndex getIndexType() {
		return EsIndex.TAXONOMY;
	}

}
