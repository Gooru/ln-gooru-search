package org.ednovo.gooru.search.es.handler;

import static org.ednovo.gooru.search.es.processor.SearchProcessorType.BlackListQueryValidation;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.Elasticsearch;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.EsDslQueryBuild;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.EsSuggestDslQueryBuild;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.LimitValidation;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.SkillsDeserializeProcessor;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.FilterConstruction;
import static org.ednovo.gooru.search.es.processor.SearchProcessorType.FilterDetection;
import java.util.Map;

import org.ednovo.gooru.search.es.constant.EsIndex;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.springframework.stereotype.Component;

@Component
public class SkillsV2SearchHandler extends SearchHandler<SearchData, Map<String, Object>> {

	private static final SearchProcessorType[][] searchProcessorTypes = new SearchProcessorType[][] { { BlackListQueryValidation },
			{ LimitValidation },{ FilterDetection },{ FilterConstruction },{ EsDslQueryBuild },{ Elasticsearch },
			{ SkillsDeserializeProcessor } };
	
	
	@Override
	public SearchResponse<Map<String, Object>> search(SearchData searchData) {
		searchData.setAllowLeadingWildcard(true);
		return super.search(searchData);
	}
	
	@Override
	protected SearchProcessorType[][] getProcessorTypeChain() {
		return searchProcessorTypes;
	}

	@Override
	protected SearchHandlerType getType() {
		return SearchHandlerType.SKILLS;
	}

	@Override
	protected EsIndex getIndexType() {
		return EsIndex.TAXONOMY;
	}
	
}
