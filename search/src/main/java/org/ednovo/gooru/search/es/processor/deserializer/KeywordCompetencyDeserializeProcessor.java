package org.ednovo.gooru.search.es.processor.deserializer;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.ednovo.gooru.search.domain.service.CompetencySearchResult;
import org.ednovo.gooru.search.es.constant.IndexFields;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.springframework.stereotype.Component;

@Component
public class KeywordCompetencyDeserializeProcessor extends DeserializeProcessor<CompetencySearchResult, CompetencySearchResult> {

	@SuppressWarnings("unchecked")
	@Override
	CompetencySearchResult deserialize(Map<String, Object> model, SearchData searchData, CompetencySearchResult output) {
		Map<String, Object> hitsMap = (Map<String, Object>) model.get(SEARCH_HITS);
		List<Map<String, Object>> hits = (List<Map<String, Object>>) (hitsMap).get(SEARCH_HITS);
		output = new CompetencySearchResult();
		if (hits != null && hits.size() > 0) {
			Set<String> gutCodes = new HashSet<String>();
			Set<String> ids = new HashSet<String>();
			for (Map<String, Object> hit : hits) {
				Map<String, Object> fields = (Map<String, Object>) hit.get(SEARCH_SOURCE);
				List<String> gutCode = (List<String>) fields.get(IndexFields.GUT_CODES);
				String id = (String) fields.get(IndexFields.ID);
				if (gutCode != null) {
					gutCodes.addAll(gutCode);
					ids.add(id);
				}
			}
			LOG.info("Matched Ids : {} GutCodes : {}", ids, gutCodes);
			if (gutCodes != null && gutCodes.size() > 0) {
				output.setGutCodes(gutCodes.stream().collect(Collectors.toList()));
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
