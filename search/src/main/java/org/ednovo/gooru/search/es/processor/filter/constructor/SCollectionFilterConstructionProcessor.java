/**
 * 
 */
package org.ednovo.gooru.search.es.processor.filter.constructor;

import org.ednovo.gooru.search.es.constant.IndexFields;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.ednovo.gooru.search.responses.SearchResponse;
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
		searchData.putFilter(NOT_SYMBOL + CARET_SYMBOL + IndexFields.CONTENT_SUB_FORMAT, CollectionSubFormat.BENCHMARK.name());
		if (!searchData.getFilters().containsKey(AMPERSAND_AUDIENCE)) searchData.putFilter(AMPERSAND_AUDIENCE, AUDIENCE_ALL_STUDENTS);
	}

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.SCollectionFilterConstruction;
	}
}
