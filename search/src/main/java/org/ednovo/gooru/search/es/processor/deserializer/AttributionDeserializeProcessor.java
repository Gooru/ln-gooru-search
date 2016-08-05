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
public class AttributionDeserializeProcessor <O extends Set<Map<String, Object>>, S extends Map<String, Object>> extends DeserializeProcessor<O, S> {

	@Override
	O deserialize(Map<String, Object> model, SearchData input, O output) {
		Map<String, Object> hitsMap = (Map<String, Object>) model.get(SEARCH_HITS);
		List<Map<String, Object>> hits = (List<Map<String, Object>>) (hitsMap).get(SEARCH_HITS);
		output = (O) new HashSet<Map<String, Object>>();
		for (Map<String, Object> hit : hits) {
			if (hit.isEmpty()) {
				return output;
			}
			Map<String, Object> fields = (Map<String, Object>) hit.get(SEARCH_SOURCE);
			output.add(collect(fields, input, null));
		}
		return output;
	}

	@Override
	S collect(Map<String, Object> model, SearchData input, S output) {
		Set<String> values = new HashSet<String>();
        output = (S)new HashMap<String, Object>();
		values.add((String) model.get(RESOURCESOURCE_ATTRIBUTION));
		output.put("attribution", (String) model.get(RESOURCESOURCE_ATTRIBUTION));
		if(model.containsKey(CUSTOMFIELD_CUSTOMFIELDS_CFHOST)){
			values.add((String) model.get(CUSTOMFIELD_CUSTOMFIELDS_CFHOST));
		}
		output.put("values", values);
		return output;
	}

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.AttributionDeserializer;
	}


}
