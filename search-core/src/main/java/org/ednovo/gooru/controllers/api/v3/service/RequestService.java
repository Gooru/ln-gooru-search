package org.ednovo.gooru.controllers.api.v3.service;

import org.ednovo.gooru.suggest.v3.model.SuggestContextData;
import org.ednovo.gooru.suggest.v3.model.SuggestData;
import org.json.JSONObject;

public interface RequestService {

	void processContextPayload(JSONObject requestContext, SuggestContextData suggestContext, SuggestData suggestData) throws Exception;

	void processCodeContextPayload(JSONObject requestContext, SuggestContextData suggestContext, SuggestData suggestData) throws Exception;

}
