package org.ednovo.gooru.search.es.processor.deserializer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.springframework.stereotype.Component;

@Component
public class SearchQueryDeserializeProcessor<O extends Set<Map<String, String>>, S extends Map<String, String>> extends DeserializeProcessor<O, S> {

	@Override
	O deserialize(Map<String, Object> model, SearchData searchData, O output) {
		Map<String, Object> hitsMap = (Map<String, Object>) model.get(SEARCH_HITS);
		List<Map<String, Object>> hits = (List<Map<String, Object>>) (hitsMap).get(SEARCH_HITS);
		output = (O) new HashSet<Map<String, String>>();
		for (Map<String, Object> hit : hits) {
			if (hit.isEmpty()) {
				return output;
			}
			Map<String, Object> fields = (Map<String, Object>) hit.get(SEARCH_SOURCE);
			output.add(collect(fields, searchData, null));
		}
		return output;
	}

	@Override
	S collect(Map<String, Object> model, SearchData searchData, S output) {
		output = (S) new HashMap<String, String>();
		output.put("keyword", (String) model.get(SEARCH_SEARCH_QUERY));
		output.put("resultUId", null);
		return output;
	}

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.SearchQueryDeserializer;
	}

}
