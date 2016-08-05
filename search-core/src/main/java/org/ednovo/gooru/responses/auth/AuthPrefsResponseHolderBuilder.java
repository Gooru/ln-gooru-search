package org.ednovo.gooru.responses.auth;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public final class AuthPrefsResponseHolderBuilder {

  public static AuthPrefsResponseHolder build(JSONObject message) {
    return new AuthPrefsMessageBusJsonResponseHolder(message);
  }
}
