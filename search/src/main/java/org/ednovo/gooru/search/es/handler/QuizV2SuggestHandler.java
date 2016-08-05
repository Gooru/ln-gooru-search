/**
 * 
 */
package org.ednovo.gooru.search.es.handler;

import static org.ednovo.gooru.search.es.processor.SearchProcessorType.BlackListQueryValidation;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.ContentFilterConstruction;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.Elasticsearch;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.EsDslQueryBuild;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.FilterDetection;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.LimitValidation;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.QuizDeserializer;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.QuizSuggest;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.TaxonomyQueryExpansion;

import java.util.Map;

import org.ednovo.gooru.search.es.constant.EsIndex;
import org.ednovo.gooru.search.es.model.SuggestData;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.springframework.stereotype.Component;

/**
 * @author SearchTeam
 * 
 */
@Component
public class QuizV2SuggestHandler extends SuggestHandler<SuggestData, Map<String, Object>> {

	private static final SearchProcessorType[][] searchProcessorTypes = new SearchProcessorType[][] { {QuizSuggest}, { BlackListQueryValidation }, { LimitValidation },{ FilterDetection }, { TaxonomyQueryExpansion }, { ContentFilterConstruction}, { EsDslQueryBuild },
			{ Elasticsearch }, { QuizDeserializer } };

	@Override
	protected SearchProcessorType[][] getProcessorTypeChain() {
		return searchProcessorTypes;
	}

	@Override
	protected SearchHandlerType getType() {
		return SearchHandlerType.QUIZ;
	}

	@Override
	protected EsIndex getIndexType() {
		return EsIndex.RESOURCE;
	}

}
