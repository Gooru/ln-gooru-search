package org.ednovo.gooru.web.spring.exception;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.NotImplementedException;
import org.ednovo.gooru.search.es.constant.Constants;
import org.ednovo.gooru.search.es.constant.EventConstants;
import org.ednovo.gooru.search.es.exception.BadRequestException;
import org.ednovo.gooru.search.es.exception.MethodFailureException;
import org.ednovo.gooru.search.es.exception.NotAllowedException;
import org.ednovo.gooru.search.es.exception.NotFoundException;
import org.ednovo.gooru.search.es.exception.UnauthorizedException;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import flexjson.JSONSerializer;

public class GooruExceptionResolver extends SimpleMappingExceptionResolver {

	private final Logger logger = LoggerFactory.getLogger(GooruExceptionResolver.class);

	@Override
	public ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		ErrorObject errorObject = null;
		boolean isLogError = false;
		if (ex instanceof AccessDeniedException) {
			errorObject = new ErrorObject(403, ex.getMessage());
			response.setStatus(403);
			logger.error("input parameters --- " + getRequestInfo(request).toString());
		} else if (ex instanceof BadCredentialsException) {
			errorObject = new ErrorObject(400, ex.getMessage());
			response.setStatus(400);
		} else if (ex instanceof BadRequestException) {
			errorObject = new ErrorObject(400, ((BadRequestException) ex).getErrorCode() != null ? "400-" + ((BadRequestException) ex).getErrorCode() : "400", ex.getMessage());
			response.setStatus(400);
		} else if (ex instanceof UnauthorizedException) {
			errorObject = new ErrorObject(401, ((UnauthorizedException) ex).getErrorCode() != null ? "401-" + ((UnauthorizedException) ex).getErrorCode() : "401", ex.getMessage());
			response.setStatus(401);
		} else if (ex instanceof NotFoundException) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			isLogError = true;
			errorObject = new ErrorObject(404, ex.getMessage());
		} else if (ex instanceof NotImplementedException || ex instanceof NotAllowedException) {
			response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			errorObject = new ErrorObject(405, ex.getMessage());
		} else if (ex instanceof MethodFailureException) {
			response.setStatus(420);
			errorObject = new ErrorObject(420, ex.getMessage());
			logger.error("Error in Resolver -- ", ex);
			logger.error("input parameters --- " + getRequestInfo(request).toString());
		} else {
			errorObject = new ErrorObject(500, "Internal Server Error");
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			logger.error("Error in Resolver -- ", ex);
			logger.error("input parameters --- " + getRequestInfo(request).toString());
		}

		if (!isLogError) {
			logger.debug("Error in Resolver -- ", ex);
			logger.debug("input parameters --- " + getRequestInfo(request).toString());
		}
		ModelAndView jsonModel = new ModelAndView("rest/model");
		String errorJsonResponse = new JSONSerializer().exclude("*.class").serialize(errorObject);
		if (ex instanceof UnauthorizedException) {
			response.setHeader("Unauthorized", errorJsonResponse);
		}
		jsonModel.addObject("code", errorObject.getCode());
		jsonModel.addObject("status", errorObject.getStatus());
		//below two attributes To be deprecated after checking with FE & partners
		jsonModel.addObject("errorCode", errorObject.getErrorCode());
		jsonModel.addObject("errorMessage", errorObject.getErrorMessage());
		return jsonModel;
	}

	private JSONObject getRequestInfo(HttpServletRequest request) {
		JSONObject inputParams = new JSONObject();
		Map map = request.getParameterMap();
		if (map != null) {
			try {
				inputParams.put("parameters", new JSONObject(map));
				inputParams.put(Constants.SESSION_TOKEN, request.getHeader(Constants.GOORU_SESSION_TOKEN));
				inputParams.put(EventConstants.API_KEY, request.getHeader(Constants.GOORU_API_KEY));
			} catch (JSONException e) {
			}
		}
		return inputParams;
	}

}
