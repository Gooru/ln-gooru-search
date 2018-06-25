/**
 * 
 */
package org.ednovo.gooru.search.es.processor.deserializer;

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
public class MapDeserializeProcessor extends SearchProcessor<SearchData, Map<String, Object>> {

	@SuppressWarnings("unchecked")
	@Override
	public void process(SearchData searchData,
			SearchResponse<Map<String, Object>> response) {
		try {
			response.setSearchResults((Map<String, Object>) SERIAILIZER.readValue(searchData.getSearchResultText(), new TypeReference<Map<String, Object>>() {
			}));
		} catch (Exception e) {
			LOG.error("Search Error", e);
			throw new SearchException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.MapDeserializer;
	}

}
