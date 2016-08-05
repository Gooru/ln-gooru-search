package org.ednovo.gooru.search.es.processor.deserializer;

import java.util.List;
import java.util.Map;

import org.ednovo.gooru.search.es.model.MapWrapper;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class LibraryDeserializeProcessor extends DeserializeProcessor<JSONObject, JSONObject> {

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.LibraryDeserializer;
	}

	@Override
	JSONObject deserialize(Map<String, Object> model, SearchData searchData, JSONObject output) {
		Map<String, Object> hitsMap = (Map<String, Object>) model.get(SEARCH_HITS);
		List<Map<String, Object>> hits = (List<Map<String, Object>>) (hitsMap).get(SEARCH_HITS);
		output = (JSONObject) new JSONObject();
		for (Map<String, Object> hit : hits) {
			Map<String, Object> fields = (Map<String, Object>) hit.get(SEARCH_SOURCE);
			collect(fields, searchData, output);
			break;
		}
		return output;
	}

	@Override
	JSONObject collect(Map<String, Object> model, SearchData searchData, JSONObject output) {
		MapWrapper<Object> parameters = searchData.getParameters();
		try {
			if (parameters != null && !parameters.getString(SEARCH_MODE).equalsIgnoreCase(SEARCH_MODE_SIMPLE)) {
				output.put("collections", new JSONObject((String) model.get(SEARCH_SCOLLECTIONS)));
				output.put("quiz", new JSONObject((String) model.get(SEARCH_QUIZZES)));
			} else {
				output.put("collections", new JSONArray((String) model.get("simple_collections")));
				output.put("quizzes", new JSONArray((String) model.get(SEARCH_SIMPLE_QUIZZESS)));
			}
		} catch (Exception e) {
			LOG.error(e + "");
		}
		
		return output;
	}

}
