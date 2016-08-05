package org.ednovo.gooru.search.es.model;

import java.util.Map;

import org.ednovo.gooru.search.model.GooruAuthenticationToken;
import org.ednovo.gooru.search.model.UserCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

public class SessionContextSupport {

	private static final Logger LOGGER = LoggerFactory.getLogger(SessionContextSupport.class);

	public static GooruAuthenticationToken getAuthentication() {
		try {
			return (GooruAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
		} catch (Exception ex) {
			return null;
		}
	}

	public static UserCredential getUserCredential() {
		try {
			GooruAuthenticationToken authentication = getAuthentication();

			return authentication.getUserCredential();
		} catch (Exception ex) {
			return null;
		}
	}

	public static Map<String, Object> getLog() {
		try {
			return RequestSupport.getSessionContext().getLog();
		} catch (Exception ex) {
			return null;
		}
	}

	public static void putLogParameter(String field, Object value) {
		try {
			RequestSupport.getSessionContext().getLog().put(field, value);
		} catch (Exception ex) {
			LOGGER.error("Error in put log parameter : ", ex);
		}
	}

	public static Map<String, Object> getMoveContentMeta() {
		try {
			return RequestSupport.getSessionContext().getMoveContentMeta();
		} catch (Exception ex) {
			return null;
		}
	}

	public static void putMoveContentMeta(String field, Object value) {
		try {
			RequestSupport.getSessionContext().getMoveContentMeta().put(field, value);
		} catch (Exception ex) {
			LOGGER.error("Error in put move meta content parameter : ", ex);
		}
	}

	public static String getSessionToken() {
		UserCredential credential = getUserCredential();
		if (credential != null) {
			return (String) credential.getToken();
		} else {
			return "NA";
		}
	}

}
