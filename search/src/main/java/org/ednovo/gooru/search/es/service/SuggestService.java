package org.ednovo.gooru.search.es.service;

import java.util.List;

import org.ednovo.gooru.search.es.model.SuggestData;
import org.ednovo.gooru.search.es.model.SuggestResponse;

public interface SuggestService {
	public List<SuggestResponse<Object>> suggest(SuggestData suggestData) throws Exception;
}
