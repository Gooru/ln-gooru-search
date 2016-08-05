package org.ednovo.gooru.search.es.handler;

import static org.ednovo.gooru.search.es.processor.SearchProcessorType.BlackListQueryValidation;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.Elasticsearch;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.EsDslQueryBuild;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.FilterDetection;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.LimitValidation;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.SchoolDistrictDeserializer;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.SchoolDistrictFilterConstruction;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.SpellChecker;

import java.util.Map;

import org.ednovo.gooru.search.es.constant.EsIndex;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.springframework.stereotype.Service;

@Service
public class SchoolDistrictV2SearchHandler extends SearchHandler<SearchData, Map<String, Object>> {

	
	private static final SearchProcessorType[][] searchProcessorTypes = new SearchProcessorType[][] {{SpellChecker},{FilterDetection},{BlackListQueryValidation},{LimitValidation},{SchoolDistrictFilterConstruction},{ EsDslQueryBuild },{ Elasticsearch },{SchoolDistrictDeserializer}};
	
	@Override
	protected SearchProcessorType[][] getProcessorTypeChain() {
		return searchProcessorTypes;	
	}

	@Override
	protected SearchHandlerType getType() {
		return SearchHandlerType.SCHOOLDISTRICT;
	}

	@Override
	protected EsIndex getIndexType() {
		return EsIndex.SCHOOL_DISTRICT;
	}

}
