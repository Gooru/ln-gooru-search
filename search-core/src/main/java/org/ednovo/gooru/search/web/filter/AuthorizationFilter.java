package org.ednovo.gooru.search.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ednovo.gooru.search.es.constant.Constants;
import org.ednovo.gooru.search.model.GooruAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthorizationFilter implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(AuthorizationFilter.class);

	@Autowired
	private DoAuthorization doAuthorization;

	private final String SESSION_TOKEN = "sessionToken";
	
	@Override
	public void init(FilterConfig objFConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;

		HttpServletResponse response = (HttpServletResponse) res;

		if (logger.isDebugEnabled()) {
			logger.debug("Request URI: " + ((HttpServletRequest) request).getRequestURI());
		}
		try {
			// check the authentication object in security
			String sessionToken = getSessionToken(request);
			getDoAuthorization().doFilter(sessionToken, request);

		} catch (Exception ex) {

			logger.error("Authentication Error :::::::: " + ex);
			int errorCode = 500;
			if (ex instanceof AccessDeniedException) {
				errorCode = 403;
			}
			Authentication auth = new GooruAuthenticationToken(ex, null, ex.getMessage(), errorCode);
			SecurityContextHolder.getContext().setAuthentication(auth);
		}
		chain.doFilter(request, response);

	}

	private String getSessionToken(HttpServletRequest request) {
		String sessionToken = request.getHeader(Constants.GOORU_HEADER_SESSION_TOKEN);
		String headerSessionToken = request.getHeader(Constants.GOORU_HEADER_AUTHORIZATION);
		String header20Request = request.getHeader(Constants.HEADER_CODEPATH);
		if(header20Request != null && !header20Request.isEmpty() && header20Request.equalsIgnoreCase(Constants.SEARCH_REQ_20)){
			sessionToken = Constants.SESSION_TOKEN_20;
		}
		else {
			if (headerSessionToken != null) {
				sessionToken = headerSessionToken.substring(Constants.TOKEN.length()).trim();
			}
			
			if (sessionToken == null || sessionToken.trim().length() == 0) {
				sessionToken = request.getParameter(SESSION_TOKEN);
			}
		}
		return sessionToken;
	}

	@Override
	public void destroy() {
		SecurityContextHolder.clearContext();
	}

	public DoAuthorization getDoAuthorization() {
		return doAuthorization;
	}
}
