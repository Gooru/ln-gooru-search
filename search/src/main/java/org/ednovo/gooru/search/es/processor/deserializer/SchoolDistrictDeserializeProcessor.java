package org.ednovo.gooru.search.es.processor.deserializer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.springframework.stereotype.Service;

@Service
public class SchoolDistrictDeserializeProcessor <O extends Set<Map<String, Object>>, S extends Map<String, Object>> extends DeserializeProcessor<O, S> { 

   
private Map<String, Object> result;
	
@SuppressWarnings("unchecked")
@Override
O deserialize(Map<String, Object> model, SearchData input, O output) {
	Map<String, Object> hitsMap = (Map<String, Object>) model.get(SEARCH_HITS);
	List<Map<String, Object>> hits = (List<Map<String, Object>>) (hitsMap).get(SEARCH_HITS);
	output = (O) new HashSet<Map<String, Object>>();
	result = new HashMap<String, Object>();
	

	for (Map<String, Object> hit : hits) {
		if (hit.isEmpty()) {
			return output;
		}
		Map<String, Object> fields = (Map<String, Object>) hit.get(SEARCH_SOURCE);
		Map<String, Object> results = new HashMap<String, Object>();
		results.put(SEARCH_DISTRICTCODE,(String) fields.get(SEARCH_DISTRICTCODE));
		results.put(SEARCH_DISTRICTID,(String)fields.get(SEARCH_DISTRICTID));
		results.put(SEARCH_DISTRICTNAME,(String)fields.get(SEARCH_DISTRICTNAME));
		/*List<SchoolCio> school =  (List<SchoolCio>) fields.get(SEARCH_SCHOOL);
		results.put(SEARCH_SCHOOL, school);*/
		Map<String,String> stateProvience =(Map<String, String>) fields.get(SEARCH_STATE_PROVIENCE);
		results.put(SEARCH_STATE, stateProvience);
		output.add(results);
	}
	
		return output;
}

@SuppressWarnings("unchecked")
@Override
S collect(Map<String, Object> model, SearchData input, S output) {
	return  output = (S)new HashMap<String, Object>();
    
}

@Override
protected SearchProcessorType getType() {
	return SearchProcessorType.SchoolDistrictDeserializer;
}

}
