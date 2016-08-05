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
public class PublisherDeserilaizeProcessor<O extends Set<Map<String, Object>>, S extends Map<String, Object>> extends DeserializeProcessor<O, S> { 



@Override
O deserialize(Map<String, Object> model, SearchData input, O output) {
	Map<String, Object> hitsMap = (Map<String, Object>) model.get(SEARCH_HITS);
	List<Map<String, Object>> hits = (List<Map<String, Object>>) (hitsMap).get(SEARCH_HITS);
	output = (O) new HashSet<Map<String, Object>>();
	Map<String, Object> result = new HashMap<String, Object>();
	Set<String> values = new HashSet<String>();

	for (Map<String, Object> hit : hits) {
		if (hit.isEmpty()) {
			return output;
		}
		Map<String, Object> fields = (Map<String, Object>) hit.get(SEARCH_SOURCE);
		values.add((String) fields.get(SEARCH_PUBLISHER_NAME));
	}
	
	
result.put("values", values);
output.add(result);
	return output;
}


@Override
protected SearchProcessorType getType() {
	
	return SearchProcessorType.PublisherDeserializer ;
}


@Override
S collect(Map<String, Object> model, SearchData input, S output) {
    output = (S)new HashMap<String, Object>();    
	return output;
}
}