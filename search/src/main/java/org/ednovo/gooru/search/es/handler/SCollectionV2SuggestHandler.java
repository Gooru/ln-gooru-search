/**
 * 
 */
package org.ednovo.gooru.search.es.handler;

import static org.ednovo.gooru.search.es.processor.SearchProcessorType.ContentUserProficiency;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.ContentUserPreference;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.DictionaryQueryExpansion;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.Elasticsearch;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.EsDslQueryBuild;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.FilterDetection;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.LimitValidation;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.SCollectionDeserializer;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.SCollectionFilterConstruction;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.SCollectionSuggest;
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
public class SCollectionV2SuggestHandler extends SuggestHandler<SuggestData, Map<String, Object>> {

	private static final SearchProcessorType[][] searchProcessorTypes = new SearchProcessorType[][] { {SCollectionSuggest}, { FilterDetection }, { LimitValidation }, { TaxonomyQueryExpansion }, { DictionaryQueryExpansion}, { ContentUserPreference }, { ContentUserProficiency }, { SCollectionFilterConstruction }, { EsDslQueryBuild }, { Elasticsearch }, { SCollectionDeserializer } };

	@Override
	protected SearchProcessorType[][] getProcessorTypeChain() {
		return searchProcessorTypes;
	}

	@Override
	protected SearchHandlerType getType() {
		return SearchHandlerType.SCOLLECTION;
	}

	@Override
	protected EsIndex getIndexType() {
		return EsIndex.COLLECTION;
	}

}
