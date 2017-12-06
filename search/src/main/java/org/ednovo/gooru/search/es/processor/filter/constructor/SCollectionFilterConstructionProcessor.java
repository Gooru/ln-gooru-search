/**
 * 
 */
package org.ednovo.gooru.search.es.processor.filter.constructor;

import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.springframework.stereotype.Component;

/**
 * @author SearchTeam
 * 
 */
@Component
public class SCollectionFilterConstructionProcessor extends ContentFilterConstructionProcessor {

	@Override
	public void process(SearchData searchData,
			SearchResponse<Object> response) {
		super.process(searchData, response);
		searchData.putFilter(NOT_SYMBOL + CARET_SYMBOL + CONTENT_SUB_FORMAT, CollectionSubFormat.BENCHMARK.name());
	}

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.SCollectionFilterConstruction;
	}
}
