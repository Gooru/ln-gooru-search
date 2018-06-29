package org.ednovo.gooru.responses.auth;

import org.json.JSONException;
import org.json.JSONObject;

public interface AuthResponseHolder {

	boolean isAuthorized() throws JSONException;

	boolean isAnonymous() throws JSONException;

	String getUser() throws JSONException;

	String getAppId() throws JSONException;

	String getContentCDN() throws JSONException;

	JSONObject getTenant() throws JSONException;

	String getPartnerId() throws JSONException;

	String getUserCDN() throws JSONException;
}
