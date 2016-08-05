/**
 * 
 */
package org.ednovo.gooru.search.es.handler;

import java.util.List;
import java.util.Map;

import org.ednovo.gooru.search.es.model.SuggestData;
import org.ednovo.gooru.search.es.model.SuggestResponse;
import org.springframework.stereotype.Component;

@Component
public class QuizSuggestHandler extends SuggestV2Handler<Map<String, Object>> {
	
	private static final String QUIZ_SUGGEST_PREFIX="QUIZ_";

	
	@Override
	protected SearchHandlerType getType() {
		return SearchHandlerType.QUIZ;
	}
	
	@Override
	protected String getName() {
		return QUIZ_SUGGEST_PREFIX + getType().name();
	}

	@Override
	public List<SuggestDataProviderType> suggestDataProviderTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	// Logic to be added for suggest results combiner with data provider Input 
	@Override
	public SuggestResponse<Object> suggest(SuggestData suggestData, Map<SuggestDataProviderType, Object> dataProviderInput) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
