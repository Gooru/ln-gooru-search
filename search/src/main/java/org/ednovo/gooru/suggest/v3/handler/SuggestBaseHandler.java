package org.ednovo.gooru.suggest.v3.handler;

import java.util.List;
import java.util.Map;

import org.ednovo.gooru.search.responses.SuggestResponse;
import org.ednovo.gooru.suggest.v3.data.provider.model.SuggestDataProviderType;
import org.ednovo.gooru.suggest.v3.model.SuggestData;

public interface SuggestBaseHandler <O extends Object> {

	public List<SuggestDataProviderType> suggestDataProviderTypes();

	public SuggestResponse<Object> suggest(SuggestData suggestData, Map<SuggestDataProviderType, Object> dataProviderInput);
	
}