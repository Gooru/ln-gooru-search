/**
 * 
 */
package org.ednovo.gooru.search.es.handler;

import java.util.Map;

import org.ednovo.gooru.search.es.constant.EsIndex;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.springframework.stereotype.Component;

import static org.ednovo.gooru.search.es.processor.SearchProcessorType.*;

/**
 * @author SearchTeam
 * 
 */
@Component
public class SCollectionV2SearchHandler extends SearchHandler<SearchData, Map<String, Object>> {

	private static final SearchProcessorType[][] searchProcessorTypes = new SearchProcessorType[][] {{ BlackListQueryValidation }, { FilterDetection },
			{ LimitValidation }, { TaxonomyQueryExpansion }, { DictionaryQueryExpansion }, { TenantFilterConstruction },
			{ SCollectionFilterConstruction }, { EsDslQueryBuild }, { EsSuggestDslQueryBuild },{ FacetFilterConstruction }, { Elasticsearch }, { SCollectionDeserializer } };

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
