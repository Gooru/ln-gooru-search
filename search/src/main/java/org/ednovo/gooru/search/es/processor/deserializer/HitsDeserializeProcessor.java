/**
 * 
 */
package org.ednovo.gooru.search.es.processor.deserializer;

import java.util.List;
import java.util.Map;

import org.ednovo.gooru.search.es.exception.SearchException;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.processor.SearchProcessor;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.ednovo.gooru.search.responses.SearchResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * @author SearchTeam
 * 
 */
@Component
public class HitsDeserializeProcessor extends SearchProcessor<SearchData, List<Map<String,Object>>> {

	@SuppressWarnings("unchecked")
	@Override
	public void process(SearchData searchData,
			SearchResponse<List<Map<String,Object>>> response) {
		try {
			Map<String,Object> result = (Map<String,Object>) SERIAILIZER.readValue(searchData.getSearchResultText(), new TypeReference<Map<String,Object>>() {
			});
			if (result != null && result.get(SEARCH_HITS) != null) {
				response.setSearchResults((List<Map<String, Object>>) (((Map<String,Object>)result.get(SEARCH_HITS)).get(SEARCH_HITS)));
			}
		} catch (Exception e) {
			logger.error("Search Error", e);
			throw new SearchException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.HitsDeserializer;
	}

}
