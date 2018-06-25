package org.ednovo.gooru.search.es.processor;

import static org.ednovo.gooru.search.es.constant.SearchSettingType.S_ES_INDEX_PREFIX;
import static org.ednovo.gooru.search.es.constant.SearchSettingType.S_ES_INDEX_SUFFIX;
import static org.ednovo.gooru.search.es.constant.SearchSettingType.S_ES_POINT;
import static org.ednovo.gooru.search.es.constant.SearchSettingType.S_ES_POINT_PASSWORD;
import static org.ednovo.gooru.search.es.constant.SearchSettingType.S_ES_POINT_USERNAME;

import org.ednovo.gooru.search.es.constant.SearchType;
import org.ednovo.gooru.search.es.exception.SearchException;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.responses.SearchResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class MultiResourceSearchProcesssor extends SearchProcessor<SearchData, Object> {

	@Override
	public void process(SearchData searchData, SearchResponse<Object> response) {

		String indexName = searchData.getIndexType().getName();
		String indexType = SearchType.RESOURCE.getType();
		ClientResource clientResource = new ClientResource(getSetting(S_ES_POINT).split(",")[0] + "/" + getSetting(S_ES_INDEX_PREFIX) + indexName+ getSetting(S_ES_INDEX_SUFFIX) + "/" + indexType + "/_msearch");
		String username = getSetting(S_ES_POINT_USERNAME);
		if (username != null && username.length() > 0) {
			clientResource.setChallengeResponse(ChallengeScheme.HTTP_BASIC, username, getSetting(S_ES_POINT_PASSWORD));
		}
		try {
			Representation rep = new StringRepresentation(searchData.getMultiQueryDsl(),MediaType.APPLICATION_JSON);
			Representation responseRep = clientResource.post(rep);
			searchData.setSearchResultText(responseRep.getText());
			
		} catch (Exception e) {
			LOG.error("Search Error", e);
			throw new SearchException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.MultiResourceSearch;
	}

}
