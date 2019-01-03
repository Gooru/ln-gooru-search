package org.ednovo.gooru.responses.auth;

import org.json.JSONException;
import org.json.JSONObject;

public interface AuthPrefsResponseHolder extends AuthResponseHolder {
  JSONObject getPreferences() throws JSONException;

  String getLanguagePreference() throws JSONException;

  JSONObject getTaxonomyPreference() throws JSONException;
}
