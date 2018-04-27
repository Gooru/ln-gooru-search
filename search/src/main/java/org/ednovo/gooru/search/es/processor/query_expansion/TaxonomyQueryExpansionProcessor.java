/**
 * 
 */
package org.ednovo.gooru.search.es.processor.query_expansion;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.ednovo.gooru.search.es.constant.EsIndex;
import org.ednovo.gooru.search.es.filter.MultiMatchQuery;
import org.ednovo.gooru.search.es.handler.SearchHandler;
import org.ednovo.gooru.search.es.handler.SearchHandlerType;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.ednovo.gooru.search.es.processor.SearchProcessor;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.springframework.stereotype.Component;

/**
 * @author SearchTeam
 * 
 */
@Component
public class TaxonomyQueryExpansionProcessor extends SearchProcessor<SearchData, Object> {

	@Override
	public void process(SearchData searchData,
			SearchResponse<Object> response) {
		if(searchData.getQueryString().equals("*") || searchData.getQueryString().equals("*:*")  || StringUtils.trimToEmpty(searchData.getQueryString()).length() == 0 || emailValidate(searchData.getQueryString().toLowerCase()) || uuidValidate(searchData.getQueryString().toLowerCase())){
			return;
		}
		String queryFields = getSetting("S_" + searchData.getIndexType().name() + "_QUERY_FIELDS");
		if(queryFields == null) {
			return;
		}
		SearchData taxonomyRequest = new SearchData();
		taxonomyRequest.setPretty(searchData.getPretty());
		taxonomyRequest.setIndexType(EsIndex.DICTIONARY);
		taxonomyRequest.putFilter("&^type", "taxonomy");
		taxonomyRequest.setQueryString("\""+searchData.getQueryString().toLowerCase().replaceAll(FIND_SPECIAL_CHARACTERS_REGEX, " ")+"\"");
		taxonomyRequest.setFrom(0);
		taxonomyRequest.setSize(1);
		List<Map<String, Object>> searchResponse = null;
		try {
			searchResponse = (List<Map<String, Object>>) SearchHandler.getSearcher(SearchHandlerType.DICTIONARY.name()).search(taxonomyRequest).getSearchResults();
		} catch (Exception e) { 
			
		}
		if(searchResponse!=null && !searchResponse.isEmpty()){
			String nextLevels = (String) ((Map)searchResponse.get(0).get("_source")).get("definitions");
			if (nextLevels != null && (nextLevels = nextLevels.trim()).length() > 0 && !nextLevels.equalsIgnoreCase(searchData.getQueryString())) {
				searchData.putQuery(new MultiMatchQuery(nextLevels, queryFields.split(","), getSettingAsFloat("S_" + searchData.getIndexType().name() + "_QUERY_EXAPNSION_TAXONOMY_LABEL_BOOST"), getSetting("S_" + searchData.getType() + "_MULTIMATCH_MINSHOULDMATCH"), getSettingAsFloat("S_" + searchData.getType() + "_MULTIMATCH_CUTOFF_FREQUENCY")));
			}
		}
	}

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.TaxonomyQueryExpansion;
	}
	
}
