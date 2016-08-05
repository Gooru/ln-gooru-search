package org.ednovo.gooru.search.es.processor.filter.constructor;

import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class SchoolDistrictFilterConstructionProcessor extends FilterConstructionProcessor {
	
	@Override
	public void process(SearchData searchData,SearchResponse<Object> response) {
		super.process(searchData, response);
}
	
	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.SchoolDistrictFilterConstruction;
	}
	
}
