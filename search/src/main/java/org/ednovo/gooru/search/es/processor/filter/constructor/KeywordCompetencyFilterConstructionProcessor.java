package org.ednovo.gooru.search.es.processor.filter.constructor;

import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.ednovo.gooru.search.responses.SearchResponse;
import org.springframework.stereotype.Component;

@Component
public class KeywordCompetencyFilterConstructionProcessor extends FilterConstructionProcessor {

	@Override
	public void process(SearchData searchData, SearchResponse<Object> response) {
		super.process(searchData, response);
		searchData.putFilter("#^gutCodes", "gutCodes");
	}

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.KeywordCompetencyFilterConstruction;
	}
}