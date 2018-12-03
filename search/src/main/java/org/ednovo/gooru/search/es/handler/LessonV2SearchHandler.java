package org.ednovo.gooru.search.es.handler;

import static org.ednovo.gooru.search.es.processor.SearchProcessorType.BlackListQueryValidation;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.ContentUserPreference;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.DictionaryQueryExpansion;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.Elasticsearch;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.EsDslQueryBuild;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.LessonDeserializeProcessor;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.LessonFilterConstruction;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.LimitValidation;
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
public class LessonV2SearchHandler extends SearchHandler<SearchData, Map<String, Object>> {

	private static final SearchProcessorType[][] searchProcessorTypes = new SearchProcessorType[][] { { BlackListQueryValidation }, { LimitValidation }, { ContentUserPreference }, { DictionaryQueryExpansion },
			{ TenantFilterConstruction }, { ScopeFilterConstruction }, { LessonFilterConstruction }, { EsDslQueryBuild }, { Elasticsearch }, { LessonDeserializeProcessor } };

	@Override
	protected SearchProcessorType[][] getProcessorTypeChain() {
		return searchProcessorTypes;
	}

	@Override
	protected SearchHandlerType getType() {
		return SearchHandlerType.LESSON;
	}

	@Override
	protected EsIndex getIndexType() {
		return EsIndex.LESSON;
	}
}
