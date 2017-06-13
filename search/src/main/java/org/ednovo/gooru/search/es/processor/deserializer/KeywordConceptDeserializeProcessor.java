package org.ednovo.gooru.search.es.processor.deserializer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.ednovo.gooru.search.domain.service.ConceptsSearchResult;
import org.ednovo.gooru.search.es.constant.IndexFields;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.ednovo.gooru.search.es.service.ConceptNodeDataProviderService;
import org.ednovo.gooru.search.model.ConceptNodeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KeywordConceptDeserializeProcessor extends DeserializeProcessor<ConceptsSearchResult, ConceptsSearchResult> {

	@Autowired
	private ConceptNodeDataProviderService conceptNodeDataProviderService;
	
	@SuppressWarnings("unchecked")
	@Override
	ConceptsSearchResult deserialize(Map<String, Object> model, SearchData searchData, ConceptsSearchResult output) {
		Map<String, Object> hitsMap = (Map<String, Object>) model.get(SEARCH_HITS);
		List<Map<String, Object>> hits = (List<Map<String, Object>>) (hitsMap).get(SEARCH_HITS);
		if (hits != null && hits.size() > 0) {
			Set<String> conceptSet = new HashSet<String>();
			for (Map<String, Object> hit : hits) {
				Map<String, Object> fields = (Map<String, Object>) hit.get(SEARCH_SOURCE);
				String gutCode = (String) fields.get(IndexFields.GUT_CODE);
				if (gutCode != null) conceptSet.add((String) fields.get(IndexFields.GUT_CODE));
			}
			LOG.info("Matched conceptList : "+conceptSet);
			if (conceptSet != null && conceptSet.size() > 0) {
				List<ConceptNodeDTO> conceptList = new ArrayList<>();
				output = new ConceptsSearchResult();
				output.setConcept_graph(conceptNodeDataProviderService.getConceptNodes(conceptSet.parallelStream().collect(Collectors.toList()), searchData, conceptList));
			}
		}
		return output;
	}

	@Override
	ConceptsSearchResult collect(Map<String, Object> model, SearchData input, ConceptsSearchResult conceptsSearchResult) {
		return null;
	}

	
	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.KeywordConceptDeserializer;
	}

}
