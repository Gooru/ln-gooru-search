package org.ednovo.gooru.search.es.handler;

import static org.ednovo.gooru.search.es.processor.SearchProcessorType.BlackListQueryValidation;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.ContentUserPreference;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.Elasticsearch;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.EsDslQueryBuild;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.LimitValidation;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.RubricDeserializeProcessor;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.RubricFilterConstruction;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.ScopeFilterConstruction;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.TenantFilterConstruction;

import java.util.Map;

import org.ednovo.gooru.search.es.constant.EsIndex;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.springframework.stereotype.Component;
/**
 * @author Renuka
 * 
 */
@Component
public class RubricV2SearchHandler extends SearchHandler<SearchData, Map<String, Object>> {

	private static final SearchProcessorType[][] searchProcessorTypes = new SearchProcessorType[][] { { BlackListQueryValidation }, { LimitValidation }, { ContentUserPreference }, { TenantFilterConstruction }, { ScopeFilterConstruction },
			{ RubricFilterConstruction }, { EsDslQueryBuild }, { Elasticsearch }, { RubricDeserializeProcessor } };

	@Override
	protected SearchProcessorType[][] getProcessorTypeChain() {
		return searchProcessorTypes;
	}

	@Override
	protected SearchHandlerType getType() {
		return SearchHandlerType.RUBRIC;
	}

	@Override
	protected EsIndex getIndexType() {
		return EsIndex.RUBRIC;
	}
}
