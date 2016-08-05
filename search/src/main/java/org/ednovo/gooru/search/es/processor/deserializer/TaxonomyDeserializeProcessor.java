/**
 * 
 */
package org.ednovo.gooru.search.es.processor.deserializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.search.es.model.Code;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.springframework.stereotype.Component;

/**
 * @author SearchTeam
 * 
 */
@Component
public class TaxonomyDeserializeProcessor extends DeserializeProcessor<List<Code>, Code> {

	@Override
	List<Code> deserialize(Map<String, Object> model, SearchData searchData, List<Code> output) {
		Map<String, Object> hitsMap = (Map<String, Object>) model.get(SEARCH_HITS);
		List<Map<String, Object>> hits = (List<Map<String, Object>>) (hitsMap).get(SEARCH_HITS);
		output = new ArrayList<Code>();
		for (Map<String, Object> hit : hits) {
			Map<String, Object> fields = (Map<String, Object>) hit.get(SEARCH_SOURCE);
			output.add((Code) collect(fields, searchData, null));
		}
		return output;
	}

	@Override
	Code collect(Map<String, Object> model, SearchData input, Code code) {
		if(code == null){
			code = new Code();
		}
		//TODO disabled while removing coreAPI jar. Need to re-enable
		/*code.setCodeId((String) model.get(SEARCH_CODE_ID));
		code.setCodeUid((String) model.get(SEARCH_CODE_UID));
		code.setLabel((String) model.get(SEARCH_LABEL));
		code.setCode((String) model.get(SEARCH_CODE));
		code.setDisplayOrder((Integer) model.get(SEARCH_DISPLAY_ORDER));
		code.setParentId((Integer) model.get(SEARCH_PARENT_ID));

		CodeType codeType = new CodeType();
		codeType.setTypeId((Integer) model.get(SEARCH_TYPE_ID));
		code.setCodeType(codeType);

		code.setDescription((String) model.get(SEARCH_DESCRIPTION));
		code.setDepth(((Integer) model.get(SEARCH_DEPTH)).shortValue());
		code.setRootNodeId((Integer) model.get(SEARCH_ROOT_NODE_ID));
		code.setCodeImage((String) model.get(SEARCH_CODE_IMAGE));
		code.setS3UploadFlag((Integer) model.get(SEARCH_S3_UPLOAD_FLAG));*/

		return code;

	}

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.TaxonomyDeserializer;
	}

}
