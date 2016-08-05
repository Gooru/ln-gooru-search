package org.ednovo.gooru.search.es.processor.deserializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.SkillsResult;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.springframework.stereotype.Component;

@Component
public class SkillsDeserializeProcessor extends DeserializeProcessor<List<SkillsResult>, SkillsResult> {

	@Override
	List<SkillsResult> deserialize(Map<String, Object> model, SearchData searchData, List<SkillsResult> output) {
		Map<String, Object> hitsMap = (Map<String, Object>) model.get(SEARCH_HITS);
		List<Map<String, Object>> hits = (List<Map<String, Object>>) (hitsMap).get(SEARCH_HITS);
		output = new ArrayList<SkillsResult>();
		for (Map<String, Object> hit : hits) {
			Map<String, Object> fields = (Map<String, Object>) hit.get(SEARCH_SOURCE);
			output.add((SkillsResult) collect(fields, searchData, null));
		}
		return output;
	}

    @Override
	SkillsResult collect(Map<String, Object> model, SearchData input, SkillsResult skills) {
		if(skills == null){
			skills = new SkillsResult();
		}
		if (model == null){
			return skills;
		}
		if(model.get(SEARCH_CODE_ID) != null){
			skills.setCodeId(Integer.parseInt((String) model.get(SEARCH_CODE_ID)));
			skills.setName((String) model.get(SEARCH_LABEL));
		}
      return skills;
     }


	@Override
	protected SearchProcessorType getType() {		
		return SearchProcessorType.SkillsDeserializeProcessor;
	}
}