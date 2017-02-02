package org.ednovo.gooru.responses.auth;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
class AuthPrefsMessageBusJsonResponseHolder implements AuthPrefsResponseHolder {

  private static final Logger LOG = LoggerFactory.getLogger(AuthPrefsResponseHolder.class);
  private JSONObject message = null;
  private boolean isAuthorized = false;
  public static final String MSG_KEY_PREFS = "prefs";
  public static final String MSG_USER_ID = "user_id";
  public static final String MSG_USER_ANONYMOUS = "anonymous";
  public static final String MSG_CLIENT_ID = "client_id";
  public static final String MSG_CDN_URLS = "cdn_urls";
  public static final String MSG_CONTENT_CDN_URL = "content_cdn_url";
  public static final String MSG_TENANT = "tenant";

  public AuthPrefsMessageBusJsonResponseHolder(JSONObject message) {
    System.out.println("json on auth pref class : " + message.toString());
    this.message = message;
    String userId;
    try {
      userId = message.getString(MSG_USER_ID);
      if (userId != null) {
        isAuthorized = true;
      }
    } catch (JSONException e) {
      LOG.error("Error while processing access token data" + e);
    }
  }

  public AuthPrefsMessageBusJsonResponseHolder(){
    
  }
  
  @Override
  public boolean isAuthorized() {
    return isAuthorized;
  }

  @Override
  public JSONObject getPreferences() throws JSONException {
    if (!isAuthorized) {
      return null;
    }
    return message.getJSONObject(MSG_KEY_PREFS);
  }

  @Override
  public boolean isAnonymous() throws JSONException {
    String userId = message.getString(MSG_USER_ID);
    return !(userId != null && !userId.isEmpty() && !userId.equalsIgnoreCase(MSG_USER_ANONYMOUS));
  }

  @Override
  public String getUser() throws JSONException {
    return message.getString(MSG_USER_ID);
  }
  
	@Override
	public String getClientId() throws JSONException {
		return message.getString(MSG_CLIENT_ID);
	}

	@Override
	public String getContentCDN() throws JSONException {
		String contentCDNURL = null;
		JSONObject json = message.getJSONObject(MSG_CDN_URLS);
		if (json != null) {
			contentCDNURL = json.getString(MSG_CONTENT_CDN_URL);
		}
		return contentCDNURL;
	}

	@Override
	public JSONObject getTenant() throws JSONException {
		return message.getJSONObject(MSG_TENANT);
	}

}
