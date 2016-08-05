/**
 * 
 */
package org.ednovo.gooru.search.es.handler;

import static org.ednovo.gooru.search.es.processor.SearchProcessorType.ContentUserProficiency;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.ContentUserPreference;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.DictionaryQueryExpansion;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.Elasticsearch;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.FilterDetection;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.LimitValidation;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.ResourceDeserializer;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.ResourceEsDslQueryBuild;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.ResourceFilterConstruction;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.ResourceSuggest;
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
public class ResourceV2SuggestHandler extends SuggestHandler<SuggestData, Map<String, Object>> {
	

	private static final SearchProcessorType[][] searchProcessorTypes = new SearchProcessorType[][] { 
		{ResourceSuggest}, { FilterDetection },{ LimitValidation }, { TaxonomyQueryExpansion }, { DictionaryQueryExpansion}, { ContentUserPreference }, {ContentUserProficiency}, { ResourceFilterConstruction }, { ResourceEsDslQueryBuild },
			{ Elasticsearch }, { ResourceDeserializer } };

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
