package org.ednovo.gooru.web.interceptor;

import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ednovo.gooru.kafka.producer.KafkaRegistry;
import org.ednovo.gooru.search.es.constant.Constants;
import org.ednovo.gooru.search.es.exception.MethodFailureException;
import org.ednovo.gooru.search.es.model.SessionContextSupport;
import org.ednovo.gooru.search.es.model.User;
import org.ednovo.gooru.search.model.GooruAuthenticationToken;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class GooruSearchInterceptor extends HandlerInterceptorAdapter {

	private Properties gooruConstants;

	private static final String EVENT_NAME = "eventName";

	private static final Logger logger = LoggerFactory.getLogger(GooruSearchInterceptor.class);

	@Autowired
	private KafkaRegistry kafkaHandler;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		GooruAuthenticationToken authenticationContext = (GooruAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
		if (authenticationContext != null && authenticationContext.getErrorMessage() != null) {
			if (authenticationContext.getErrorCode() == 403) {
				throw new AccessDeniedException(authenticationContext.getErrorMessage());
			} else {

				throw new MethodFailureException(authenticationContext.getErrorMessage());
			}
		}
		if (authenticationContext == null) {
			logger.debug("*******Inside interceptor");
			throw new AccessDeniedException("Invalid Session Token");
		}
		Enumeration<?> e = gooruConstants.propertyNames();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			request.setAttribute(key, gooruConstants.getProperty(key));
		}

		Long startTime = System.currentTimeMillis();
		request.setAttribute("startTime", startTime);
		
		SessionContextSupport.putLogParameter("startTime", startTime);
		String eventUUID = UUID.randomUUID().toString();
		response.setHeader("X-REQUEST-UUID", eventUUID);
		SessionContextSupport.putLogParameter("eventId", eventUUID);
		JSONObject context = new JSONObject();
		context.put(Constants.SEARCH_URL, request.getRequestURI());
		context.put(Constants.CLIENT_SOURCE, Constants.CAMELCASE_SEARCH);
		SessionContextSupport.putLogParameter("context", context);

		request.getHeader("VIA");
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null) {
			ipAddress = request.getRemoteAddr();
		}

		JSONObject user = new JSONObject();
		User party = (User) request.getAttribute(Constants.USER);
		if (party != null) {
			user.put("gooruUId", party.getGooruUId());
		}
		user.put("userAgent", request.getHeader("User-Agent"));
		user.put("userIp", ipAddress);
		SessionContextSupport.putLogParameter("user", user);

		JSONObject version = new JSONObject();
		version.put("logApi", "4.0");
		SessionContextSupport.putLogParameter("version", version);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		Long endTime = System.currentTimeMillis();
		JSONObject payloadObject = SessionContextSupport.getLog().get("payLoadObject") == null ? new JSONObject() : (JSONObject) SessionContextSupport.getLog().get("payLoadObject");

		JSONObject filter = new JSONObject();
		Enumeration<?> filterParamenters = request.getParameterNames();
		while (filterParamenters.hasMoreElements()) {
			String filterKeys = new String();
			filterKeys = (String) filterParamenters.nextElement();
			if (filterKeys.contains("flt")) {
				String[] filterKey = filterKeys.split("\\.");
				if (filterKey[0].length() == 3) {
					filter.put(filterKeys.substring(4, filterKeys.length()), request.getParameter(filterKeys));
				} else if (filterKey[0].length() == 5) {
					filter.put(filterKeys.substring(6, filterKeys.length()), request.getParameter(filterKeys));
				} else if (filterKey[0].length() == 6) {
					filter.put(filterKeys.substring(7, filterKeys.length()), request.getParameter(filterKeys));
				}
			}
		}
		try {
			if (request.getAttribute("type") != null) {
				payloadObject.put("itemType", request.getAttribute("type"));
			}
			payloadObject.put("filters", filter);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		SessionContextSupport.putLogParameter("payLoadObject", payloadObject);
		SessionContextSupport.putLogParameter("endTime", endTime);
		Long startTime = SessionContextSupport.getLog() != null ? (Long) SessionContextSupport.getLog().get("startTime") : 0;
		Long totalTimeSpentInMs = endTime - startTime;
		JSONObject metrics = new JSONObject();
		metrics.put("totalTimeSpentInMs", totalTimeSpentInMs);
		SessionContextSupport.putLogParameter("metrics", metrics);
		Map<String, Object> log = SessionContextSupport.getLog();
		if (request.getRequestURL().toString().contains("scollection") || request.getRequestURL().toString().contains("resource")) {
			if (log != null && !log.isEmpty() && request.getAttribute("action") != null && request.getAttribute("action").equals("search") && SessionContextSupport.getLog().get(EVENT_NAME) != null) {
				try {
					String logJson = new JSONObject(log).toString();
					logger.debug("Event Log : " + logJson);
					kafkaHandler.send(SessionContextSupport.getLog().get(EVENT_NAME).toString(), logJson);
				} catch (Exception e) {
					logger.error("Error while pushing event log data to kafka : " + e.getMessage());
				}
			}
		}
	}

	public Properties getGooruConstants() {
		return gooruConstants;
	}

	public void setGooruConstants(Properties gooruConstants) {
		this.gooruConstants = gooruConstants;
	}

}
