package org.ednovo.gooru.search.es.handler;

import static org.ednovo.gooru.search.es.processor.SearchProcessorType.BlackListQueryValidation;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.Elasticsearch;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.EsDslQueryBuild;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.LimitValidation;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.ScopeFilterConstruction;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.TenantFilterConstruction;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.CourseDeserializeProcessor;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.CourseFilterConstruction;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.DictionaryQueryExpansion;

import java.util.Map;

import org.ednovo.gooru.search.es.constant.EsIndex;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.springframework.stereotype.Component;

@Component
public class CourseV2SearchHandler extends SearchHandler<SearchData, Map<String, Object>> {

	private static final SearchProcessorType[][] searchProcessorTypes = new SearchProcessorType[][] { { BlackListQueryValidation }, { LimitValidation }, { DictionaryQueryExpansion },
			{ TenantFilterConstruction }, { ScopeFilterConstruction }, { CourseFilterConstruction }, { EsDslQueryBuild }, { Elasticsearch }, { CourseDeserializeProcessor } };

	@Override
	protected SearchProcessorType[][] getProcessorTypeChain() {
		return searchProcessorTypes;
	}

	@Override
	protected SearchHandlerType getType() {
		return SearchHandlerType.COURSE;
	}

	@Override
	protected EsIndex getIndexType() {
		return EsIndex.COURSE;
	}

}
