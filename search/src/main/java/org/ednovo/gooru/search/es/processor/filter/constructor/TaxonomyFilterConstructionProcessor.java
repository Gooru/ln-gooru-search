/**
 * 
 */
package org.ednovo.gooru.search.es.processor.filter.constructor;

import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.ednovo.gooru.search.responses.SearchResponse;
import org.springframework.stereotype.Component;

/**
 * @author SearchTeam
 * 
 */
@Component
public class TaxonomyFilterConstructionProcessor extends FilterConstructionProcessor {

	@Override
	public void process(SearchData searchData,
			SearchResponse<Object> response) {
		super.process(searchData, response);
	}

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.TaxonomyFilterConstruction;
	}
}
