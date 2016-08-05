/**
 * 
 */
package org.ednovo.gooru.search.es.processor;

import org.ednovo.gooru.search.es.model.SearchResponse;
import org.ednovo.gooru.search.es.model.SuggestData;
import org.springframework.stereotype.Component;

/**
 * @author SearchTeam
 * 
 */
@Component
public class CollectionSuggestProcessor extends SearchProcessor<SuggestData, Object> {

	public CollectionSuggestProcessor() {
		super();
		setTransactional(true);
	}

	@Override
	public void process(SuggestData suggestData,
			SearchResponse<Object> response) {
	}

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.CollectionSuggest;
	}

}
