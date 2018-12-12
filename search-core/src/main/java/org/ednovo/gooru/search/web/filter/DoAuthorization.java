package org.ednovo.gooru.search.web.filter;

import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.ednovo.gooru.responses.auth.AuthPrefsResponseHolder;
import org.ednovo.gooru.responses.auth.AuthPrefsResponseHolderBuilder;
import org.ednovo.gooru.search.es.constant.Constants;
import org.ednovo.gooru.search.es.constant.EventConstants;
import org.ednovo.gooru.search.es.exception.UnauthorizedException;
import org.ednovo.gooru.search.es.model.User;
import org.ednovo.gooru.search.es.model.UserGroupSupport;
import org.ednovo.gooru.search.es.service.RedisClient;
import org.ednovo.gooru.search.model.GooruAuthenticationToken;
import org.ednovo.gooru.search.model.UserCredential;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class DoAuthorization {

	@Autowired
	private RedisClient redisClient;

	@Autowired
	@Resource(name = "configSettings")
	private Properties searchSettings;

	private Properties getSearchSettings() {
		return this.searchSettings;
	}

	private static final Logger logger = LoggerFactory.getLogger(DoAuthorization.class);

	public static final String PARAM_ACCESS_TOKEN_VALIDITY = "provided_at";

	public void doFilter(final String sessionToken, final HttpServletRequest request) throws JSONException {
		User user = null;

		logger.info("Received session token : " + sessionToken);
		if (sessionToken != null) {
			JSONObject accessToken = getAccessToken(sessionToken);
			if (accessToken == null) {
				throw new UnauthorizedException("Invalid session token : " + sessionToken);
			}
			logger.info("Accesstoken data fetched from redis : " + accessToken);
			try {
				AuthPrefsResponseHolder responseHolder = AuthPrefsResponseHolderBuilder.build(accessToken);
				if (!responseHolder.isAuthorized()) {
					throw new UnauthorizedException("Unauthorized");
				}
				if (!responseHolder.getUser().isEmpty() && responseHolder.getUser() != null) {
					Authentication auth = new GooruAuthenticationToken(responseHolder.getUser(), null, new UserCredential());
					SecurityContextHolder.getContext().setAuthentication(auth);
					logger.info("Authorization succeded for user : " + responseHolder.getUser() + " , forwarding request to next route !");
					user = new User();
					String gooruUId = responseHolder.getUser();
					user.setPartyUid(gooruUId);
					user.setGooruUId(gooruUId);
					request.setAttribute(Constants.USER, user);
					request.setAttribute(Constants.SESSION_TOKEN_SEARCH, sessionToken);
					if (responseHolder.getAppId() != null) request.setAttribute(EventConstants.APP_ID, responseHolder.getAppId());
					if (responseHolder.getPartnerId() != null) request.setAttribute(EventConstants.PARTNER_ID, responseHolder.getPartnerId());
					request.setAttribute(Constants.CONTENT_CDN_URL, responseHolder.getContentCDN());
					request.setAttribute(Constants.USER_CDN_URL, responseHolder.getUserCDN());
					UserGroupSupport userGroup = new UserGroupSupport();
					JSONObject tenant = responseHolder.getTenant();
					userGroup.setTenantId(tenant.getString(EventConstants.TENANT_ID));
					if (tenant.getString(Constants.TENANT_ROOT) != null && !tenant.getString(Constants.TENANT_ROOT).equalsIgnoreCase(Constants.NULL_STRING)) userGroup.setTenantRoot(tenant.getString(Constants.TENANT_ROOT));
					request.setAttribute(Constants.TENANT, userGroup);
					if (responseHolder.getTaxonomyPreference() != null) request.setAttribute(Constants.USER_PREFERENCES, responseHolder.getTaxonomyPreference());
					if (responseHolder.getLanguagePreference() != null) request.setAttribute(Constants.USER_LANGUAGE_PREFERENCES, responseHolder.getLanguagePreference());
					renewAccessToken(accessToken, sessionToken);
				}
			} catch (Exception e) {
				logger.error("Error processing authorize request " + e);
			}
		}
	}

	private JSONObject getAccessToken(String token) throws JSONException {
		try {

			JSONObject accessToken = redisClient.getJsonObject(token);
			return accessToken;
		} catch (Exception e) {
			logger.error("Read from redis failed", e);
		}
		return null;
	}
	
	private void renewAccessToken(JSONObject message, String sessionToken) {
		try {
			Integer expireToken = message.has(Constants.ACCESS_TOKEN_VALIDITY) ? message.getInt(Constants.ACCESS_TOKEN_VALIDITY)
					: Integer.valueOf(getSearchSettings().getProperty(Constants.ACCESS_TOKEN_VALIDITY));

			redisClient.expire(sessionToken, expireToken);
		} catch (Exception e) {
			logger.error("Error while renewing sessionToken" + e);
		}
	}
 
}
