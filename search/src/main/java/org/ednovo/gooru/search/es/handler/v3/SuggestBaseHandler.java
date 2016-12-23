package org.ednovo.gooru.search.es.handler.v3;

import java.util.List;
import java.util.Map;

import org.ednovo.gooru.search.es.handler.SuggestDataProviderType;
import org.ednovo.gooru.search.es.model.SuggestData;
import org.ednovo.gooru.search.es.model.SuggestResponse;

public interface SuggestBaseHandler <O extends Object> {

	public List<SuggestDataProviderType> suggestDataProviderTypes();

	public SuggestResponse<Object> suggest(SuggestData suggestData, Map<SuggestDataProviderType, Object> dataProviderInput);
	
}