/**
 * 
 */
package org.ednovo.gooru.search.es.processor.deserializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.search.es.constant.IndexFields;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.Taxonomy;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.springframework.stereotype.Component;

/**
 * @author SearchTeam
 * 
 */
@Component
public class TaxonomyDeserializeProcessor extends DeserializeProcessor<List<Taxonomy>, Taxonomy> {

	@SuppressWarnings("unchecked")
	@Override
	List<Taxonomy> deserialize(Map<String, Object> model, SearchData searchData, List<Taxonomy> output) {
		Map<String, Object> hitsMap = (Map<String, Object>) model.get(SEARCH_HITS);
		List<Map<String, Object>> hits = (List<Map<String, Object>>) (hitsMap).get(SEARCH_HITS);
		output = new ArrayList<Taxonomy>();
		for (Map<String, Object> hit : hits) {
			Map<String, Object> fields = (Map<String, Object>) hit.get(SEARCH_SOURCE);
			output.add((Taxonomy) collect(fields, searchData, null));
		}
		return output;
	}

	@SuppressWarnings("unchecked")
	@Override
	Taxonomy collect(Map<String, Object> model, SearchData input, Taxonomy code) {
		if(code == null){
			code = new Taxonomy();
		}
		//TODO disabled while removing coreAPI jar. Need to re-enable
		code.setId((String) model.get(IndexFields.ID));
		code.setTitle((String) model.get(IndexFields.TITLE));
		code.setDescription((String) model.get(IndexFields.DESCRIPTION));
		code.setGutCode((String) model.get(IndexFields.GUT_CODE));
		code.setCodeType((String) model.get(IndexFields.CODE_TYPE));
		code.setKeywords((List<String>) model.get(IndexFields.KEYWORDS));
		code.setGutPrerequisites((List<Map<String, String>>) model.get(IndexFields.GUT_PREREQUISITES));
		return code;

	}

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.TaxonomyDeserializer;
	}

}
