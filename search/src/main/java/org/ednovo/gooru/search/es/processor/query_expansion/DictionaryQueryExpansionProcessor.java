/**
 * 
 */
package org.ednovo.gooru.search.es.processor.query_expansion;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.ednovo.gooru.search.es.constant.EsIndex;
import org.ednovo.gooru.search.es.handler.SearchHandler;
import org.ednovo.gooru.search.es.handler.SearchHandlerType;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.processor.SearchProcessor;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.ednovo.gooru.search.responses.SearchResponse;
import org.springframework.stereotype.Component;

/**
 * @author SearchTeam
 * 
 */
@Component
public class DictionaryQueryExpansionProcessor extends SearchProcessor<SearchData, Object> {

	
	@SuppressWarnings("unchecked")
	@Override
	public void process(SearchData searchData, SearchResponse<Object> response) {
		if (searchData.getQueryString().equals("*") || searchData.getQueryString().equals("*:*") || StringUtils.trimToEmpty(searchData.getQueryString()).length() == 0 || emailValidate(searchData.getQueryString().toLowerCase()) || uuidValidate(searchData.getQueryString().toLowerCase()) || expressionValidate(searchData.getQueryString()) || standardValidate(searchData)) {
			if(logger.isDebugEnabled()){
				logger.debug("Skiping Dictionary Procesor !");
			}
			return;
		}
		String query = searchData.getQueryString().toLowerCase().replaceAll(PUNC_MATCH, " ");
		String[] words = query.split("\\s+");
		SearchData abbreviationRequest = new SearchData();
		abbreviationRequest.setPretty(searchData.getPretty());
		abbreviationRequest.setIndexType(EsIndex.DICTIONARY);
		abbreviationRequest.putFilter("&^type", "abbreviation,synonyms");

		StringBuilder builder = new StringBuilder();
		String stopWords = getSearchSetting("search.query.stopwords"); 
		List<String> stopWordList = Arrays.asList(stopWords.split(COMMA));
		for (String word : words) {
			if (word.length() == 0 || stopWordList.contains(word)) {
				continue;
			}
			StringBuilder abbreviationsBuilder = new StringBuilder();
			abbreviationRequest.setQueryString(word);
			List<Map<String, Object>> searchResponse = (List<Map<String, Object>>) SearchHandler.getSearcher(SearchHandlerType.DICTIONARY.name()).search(abbreviationRequest).getSearchResults();
			if (abbreviationsBuilder.length() == 0) {
				abbreviationsBuilder.append(word);
			}
			boolean hasNewAbbreviation = false;
			if (searchResponse != null && !searchResponse.isEmpty()) {
				String result = (String) ((Map<?, ?>) searchResponse.get(0).get("_source")).get("definitions");
				if ((result != null && (result = result.trim()).length() > 0) && (!word.equalsIgnoreCase(result.trim()))) {
					abbreviationsBuilder.append(" OR " + "\"" + result + "\"");
					hasNewAbbreviation = true;
				}
			}
			if (builder.length() > 0) {
				builder.append(" AND ");
			}
			if (hasNewAbbreviation) {
				builder.append("( " + abbreviationsBuilder.toString() + " )");
			} else {
				builder.append(abbreviationsBuilder.toString());
			}
		}
		
		searchData.setQueryString(builder.toString());
	}

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.DictionaryQueryExpansion;
	}
	
}
