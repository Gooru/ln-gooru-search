package org.ednovo.gooru.search.es.processor.filter.constructor;

import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.springframework.stereotype.Component;

@Component
public class QuestionFilterConstructionProcessor extends ContentFilterConstructionProcessor {

	private static String types = "question";

	@Override
	public void process(SearchData searchData, SearchResponse<Object> response) {
		super.process(searchData, response);
		//searchData.putFilter("&^category", types);
		searchData.putFilter("&^resourceFormat", types);
		searchData.putFilter("&^statistics.invalidResource", "0");
	}

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.QuestionFilterConstruction;
	}

}
