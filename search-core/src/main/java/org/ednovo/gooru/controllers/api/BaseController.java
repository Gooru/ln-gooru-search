package org.ednovo.gooru.controllers.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.ednovo.gooru.search.es.constant.Constants;
import org.ednovo.gooru.search.es.model.User;
import org.ednovo.gooru.search.es.processor.util.JsonDeserializer;
import org.ednovo.gooru.search.es.processor.util.SerializerUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.access.AccessDeniedException;

import com.fasterxml.jackson.core.type.TypeReference;


public class BaseController extends SerializerUtil  implements Constants  {
	
	private final static String OAUTH_AUTHORIZATION = "OAuth-Authorization";

	protected User getUser(HttpServletRequest request) {
		return (User) request.getAttribute(Constants.USER);
	}

	public static String getValue(String key, JSONObject json) throws Exception {
		try {
			if (json.isNull(key)) {
				return null;
			}
			return json.getString(key);

		} catch (JSONException e) {
			throw new Exception(e.getMessage());
		}
	}

	public static JSONObject requestData(String data) throws Exception {

		return data != null ? new JSONObject(data) : null;
	}

	public static String[] getFields(String data) {
		List<String> fields = JsonDeserializer.deserialize(data, new TypeReference<List<String>>() {
		});
		return fields.toArray(new String[fields.size()]);
	}

	public boolean isMobileDevice(HttpServletRequest request) {
		if (request == null || request.getHeader(USER_AGENT) == null || request.getHeader(USER_AGENT).indexOf(MOBILE) == -1) {
			return false;
		}
		return true;
	}
	
	public static String getSessionToken(HttpServletRequest request) {
		String sessionToken = request.getHeader(GOORU_HEADER_SESSION_TOKEN);
		String headerSessionToken = request.getHeader(GOORU_HEADER_AUTHORIZATION);
		String header20Request = request.getHeader(Constants.HEADER_CODEPATH);

		if(header20Request != null && !header20Request.isEmpty() && header20Request.equalsIgnoreCase(Constants.SEARCH_REQ_20)){
			sessionToken = Constants.SESSION_TOKEN_20;
		}
		if (headerSessionToken != null) {
			sessionToken = headerSessionToken.substring(TOKEN.length()).trim();
		}
		if (sessionToken == null || (StringUtils.trimToNull(sessionToken)) == null || sessionToken.equalsIgnoreCase("null")) {
			sessionToken = request.getParameter(SESSION_TOKEN);
		}
		if(sessionToken == null ){
            sessionToken = request.getHeader(OAUTH_AUTHORIZATION);
       }
        if ((StringUtils.trimToNull(sessionToken) == null || sessionToken.equalsIgnoreCase("NA") || sessionToken.equalsIgnoreCase("null") || sessionToken.equalsIgnoreCase("undefined"))) {
            throw new AccessDeniedException("Invalid SessionToken : " + sessionToken);
        }
		return sessionToken;
	}

}
