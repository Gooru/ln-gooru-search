package org.ednovo.gooru.search.es.processor.deserializer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.ednovo.gooru.search.domain.service.CompetencySearchResult;
import org.ednovo.gooru.search.es.constant.IndexFields;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.ednovo.gooru.search.es.service.CompetencyNodeDataProviderService;
import org.ednovo.gooru.search.model.CompetencyNodeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KeywordCompetencyDeserializeProcessor extends DeserializeProcessor<CompetencySearchResult, CompetencySearchResult> {

	@Autowired
	private CompetencyNodeDataProviderService competencyNodeDataProviderService;
	
	@SuppressWarnings("unchecked")
	@Override
	CompetencySearchResult deserialize(Map<String, Object> model, SearchData searchData, CompetencySearchResult output) {
		Map<String, Object> hitsMap = (Map<String, Object>) model.get(SEARCH_HITS);
		List<Map<String, Object>> hits = (List<Map<String, Object>>) (hitsMap).get(SEARCH_HITS);
		if (hits != null && hits.size() > 0) {
			Set<String> gutCodes = new HashSet<String>();
			for (Map<String, Object> hit : hits) {
				Map<String, Object> fields = (Map<String, Object>) hit.get(SEARCH_SOURCE);
				String gutCode = (String) fields.get(IndexFields.GUT_CODE);
				if (gutCode != null)
					gutCodes.add(gutCode);
			}
			LOG.info("Matched gutCodes : " + gutCodes);
			if (gutCodes != null && gutCodes.size() > 0) {
				List<CompetencyNodeDTO> competencyList = new ArrayList<>();
				output = new CompetencySearchResult();
				competencyList = competencyNodeDataProviderService.getCompetencyNode(gutCodes.parallelStream().collect(Collectors.toList()), searchData, competencyList);
				if (competencyList.size() > 0)
					output.setCompetency_graph(competencyList);
			}
		}
		return output;
	}

	@Override
	CompetencySearchResult collect(Map<String, Object> model, SearchData input, CompetencySearchResult competencySearchResult) {
		return null;
	}

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.KeywordCompetencyDeserializer;
	}

}
