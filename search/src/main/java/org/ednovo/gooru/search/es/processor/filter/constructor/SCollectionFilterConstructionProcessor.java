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
		String audience = null;
		if (searchData.getFilters().containsKey(AMPERSAND_AUDIENCE)) audience = (String) searchData.getFilters().get(AMPERSAND_AUDIENCE);
		if (audience == null || (audience != null && !audience.equalsIgnoreCase(AUDIENCE_TEACHERS))) searchData.putFilter(NOT_SYMBOL + CARET_SYMBOL + IndexFields.AUDIENCE, AUDIENCE_TEACHERS);
	}

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.SCollectionFilterConstruction;
	}
}
