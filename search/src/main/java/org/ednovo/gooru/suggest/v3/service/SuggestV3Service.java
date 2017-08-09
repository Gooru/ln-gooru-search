package org.ednovo.gooru.suggest.v3.service;

import java.util.List;
import org.ednovo.gooru.search.es.model.SuggestResponse;
import org.ednovo.gooru.suggest.v3.model.SuggestData;

public interface SuggestV3Service {
	public List<SuggestResponse<Object>> suggest(SuggestData suggestData) throws Exception;
}
