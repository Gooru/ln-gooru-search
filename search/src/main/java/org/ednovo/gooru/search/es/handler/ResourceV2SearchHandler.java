/**
 * 
 */
package org.ednovo.gooru.search.es.handler;

import static org.ednovo.gooru.search.es.processor.SearchProcessorType.BlackListQueryValidation;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.DictionaryQueryExpansion;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.Elasticsearch;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.EsSuggestDslQueryBuild;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.FacetFilterConstruction;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.FilterDetection;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.LimitValidation;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.ResourceDeserializer;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.ResourceEsDslQueryBuild;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.ResourceFilterConstruction;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.SubjectFacetFilter;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.TaxonomyQueryExpansion;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.TenantFilterConstruction;

import java.util.Map;

import org.ednovo.gooru.search.es.constant.EsIndex;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.springframework.stereotype.Component;

/**
 * @author SearchTeam
 * 
 */
@Component
public class ResourceV2SearchHandler extends SearchHandler<SearchData, Map<String, Object>> {

	private static final SearchProcessorType[][] searchProcessorTypes = new SearchProcessorType[][] {
			{ BlackListQueryValidation }, { FilterDetection }, { LimitValidation }, { TaxonomyQueryExpansion }, { DictionaryQueryExpansion }, { TenantFilterConstruction },
			 { ResourceFilterConstruction }, {SubjectFacetFilter}, { ResourceEsDslQueryBuild }, { EsSuggestDslQueryBuild },{ FacetFilterConstruction }, { Elasticsearch },
			{ ResourceDeserializer } };

	@Override
	protected SearchProcessorType[][] getProcessorTypeChain() {
		return searchProcessorTypes;
	}

	@Override
	protected SearchHandlerType getType() {
		return SearchHandlerType.RESOURCE;
	}

	@Override
	protected EsIndex getIndexType() {
		return EsIndex.RESOURCE;
	}

}
