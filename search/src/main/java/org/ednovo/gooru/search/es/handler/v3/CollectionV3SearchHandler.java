/**
 * 
 */
package org.ednovo.gooru.search.es.handler.v3;

import java.util.Map;

import org.ednovo.gooru.search.es.constant.EsIndex;
import org.ednovo.gooru.search.es.handler.SearchHandler;
import org.ednovo.gooru.search.es.handler.SearchHandlerType;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.springframework.stereotype.Component;

import static org.ednovo.gooru.search.es.processor.SearchProcessorType.*;

/**
 * @author SearchTeam
 * 
 */
@Component
public class CollectionV3SearchHandler extends SearchHandler<SearchData, Map<String, Object>> {

	private static final SearchProcessorType[][] searchProcessorTypes = new SearchProcessorType[][] {{ BlackListQueryValidation }, { FilterDetection },
			{ LimitValidation }, { ContentUserPreference }, { TaxonomyQueryExpansion }, { DictionaryQueryExpansion }, { TenantFilterConstruction }, { ScopeFilterConstruction },
			{ SCollectionFilterConstruction }, { EsDslQueryBuild }, { EsSuggestDslQueryBuild },{ FacetFilterConstruction }, { Elasticsearch }, { SCollectionV3Deserializer } };

	@Override
	protected SearchProcessorType[][] getProcessorTypeChain() {
		return searchProcessorTypes;
	}

	@Override
	protected SearchHandlerType getType() {
		return SearchHandlerType.SCOLLECTION_V3;
	}

	@Override
	protected EsIndex getIndexType() {
		return EsIndex.COLLECTION;
	}

}
