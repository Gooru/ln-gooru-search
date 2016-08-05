package org.ednovo.gooru.search.es.handler;

import static org.ednovo.gooru.search.es.processor.SearchProcessorType.Elasticsearch;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.EsDslQueryBuild;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.HitsDeserializer;

import java.util.List;
import java.util.Map;

import org.ednovo.gooru.search.es.constant.EsIndex;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.springframework.stereotype.Component;

@Component
public class DictionaryV2SearchHandler extends SearchHandler<SearchData, List<Map<String,Object>>> {

	private static final SearchProcessorType[][] searchProcessorTypes = new SearchProcessorType[][] {{EsDslQueryBuild}, {Elasticsearch}, {HitsDeserializer}};
	
	@Override
	protected SearchProcessorType[][] getProcessorTypeChain() {
		return searchProcessorTypes;
	}

	@Override
	protected SearchHandlerType getType() {
		return SearchHandlerType.DICTIONARY;
	}

	@Override
	protected EsIndex getIndexType() {
		return EsIndex.DICTIONARY;
	}

}
