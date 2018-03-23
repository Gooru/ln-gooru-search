package org.ednovo.gooru.controllers.api.v3;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ednovo.gooru.controllers.api.BaseController;
import org.ednovo.gooru.controllers.api.v3.service.RequestService;
import org.ednovo.gooru.search.es.constant.Constants;
import org.ednovo.gooru.search.es.constant.EventConstants;
import org.ednovo.gooru.search.es.exception.BadRequestException;
import org.ednovo.gooru.search.es.exception.SearchException;
import org.ednovo.gooru.search.es.model.MapWrapper;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.ednovo.gooru.search.es.model.SessionContextSupport;
import org.ednovo.gooru.search.es.model.SuggestResponse;
import org.ednovo.gooru.search.es.model.User;
import org.ednovo.gooru.search.es.model.UserGroupSupport;
import org.ednovo.gooru.suggest.v3.model.SuggestContextData;
import org.ednovo.gooru.suggest.v3.model.SuggestData;
import org.ednovo.gooru.suggest.v3.service.SuggestV3Service;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = { "/v3/suggest" })
public class SuggestV3RestController extends BaseController {

	protected static final Logger LOG = LoggerFactory.getLogger(SuggestV3RestController.class);

	public static final String SINGLE_EXCLUDES[] = { "*.text", "*.class", "*.answer", "*.isCorrect", "*.question", "*.sequence", "*.unit", "*.hintId", "*.assets", "*.codes", "*.importCode",
			"*.queryUId", "*.resource.indexType", "*.resource.recordSource", "*.brokenStatus", "*.assetURI", "*.category", "*.contentId", "*.contentPermissions", "creator.isDeleted", "*.emailId",
			"*.organizationName", "*.partyUid", "*.userRoleSetString", "*.distinguish", "*.entryId", "*.folder", "*.indexId", "*.isDeleted", "*.isOer", "*.libraryNames", "*.ratings.*",
			"*.*.recordSource", "*.resourceAddedCount", "*.resourceTags", "*.resourceType", "*.resourceUsedUserCount", "*.resultUId", "*.s3UploadFlag", "*.sharing", "*.taxonomyDataSet", "*.thumbnail",
			"*.thumbnails", "*.viewCount", "*.resourceSource", "*.course" };
	@Autowired
	private SuggestV3Service suggestService;

	@Autowired
	private RequestService requestService;

	@RequestMapping(method = RequestMethod.POST , value = "/{type}", headers = "Content-Type=application/json")
	public ModelAndView suggest(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false) String sessionToken,
			@RequestParam(defaultValue = "10", value = "limit") Integer pageSize, @RequestParam(defaultValue = "0") String pretty, 
			@RequestParam(required = false, defaultValue = "false") Boolean isInternalSuggest, @PathVariable String type, @RequestBody String contextPayload,
			@RequestParam(required = false, defaultValue= "true") boolean isCrosswalk)
			throws Exception {
		long start = System.currentTimeMillis();
		JSONObject requestContext = null;
		LOG.info("Suggest Request Payload : {}", contextPayload);
		if (!contextPayload.isEmpty())
			requestContext = new JSONObject(contextPayload);
		if (requestContext == null || requestContext.keys() == null) {
			throw new BadRequestException("Request Payload missing!");
		}
		SuggestData suggestData = new SuggestData();
		suggestData.setCrosswalk(isCrosswalk);
		suggestData = suggestRequest(suggestData, request, type, pageSize, requestContext, pretty, sessionToken);
		try {
			if (!(type.equalsIgnoreCase(RESOURCE) || type.equalsIgnoreCase(COLLECTION))) {
				throw new BadRequestException("Invalid Type is passed. For now, the support types are resource and collection.");
			}
			suggestData.setIsInternalSuggest(isInternalSuggest);
			SuggestResponse<Object> suggestResults = suggestService.suggest(suggestData).get(0);
			String result = serialize(suggestResults, JSON, SINGLE_EXCLUDES, true);
			LOG.info("Total latency of suggest " + (System.currentTimeMillis() - start));
			return toModelAndView(result);
		} catch (SearchException searchException) {
			response.setStatus(searchException.getStatus().value());
			return toModelAndView(searchException.getMessage());
		}
	}
	
	@RequestMapping(method = RequestMethod.POST , value = "taxonomy/{type}", headers = "Content-Type=application/json")
	public ModelAndView suggestForCode(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false) String sessionToken,
			@RequestParam(defaultValue = "10", value = "limit") Integer pageSize, @RequestParam(defaultValue = "0") String pretty, @RequestParam(required = false, defaultValue = "false") Boolean inputTypeInternalCode, @RequestParam(required = false, defaultValue = "false") Boolean isInternalSuggest, @PathVariable String type, @RequestBody String contextPayload,
			@RequestParam(required = false, defaultValue= "true") boolean isCrosswalk)
			throws Exception {
		long start = System.currentTimeMillis();
		JSONObject requestContext = null;
		if (!contextPayload.isEmpty())
			requestContext = new JSONObject(contextPayload);
		if (requestContext == null || requestContext.keys() == null) {
			throw new BadRequestException("Request Payload missing!");
		}
		SuggestData suggestData = new SuggestData();
		suggestData.setSuggestInputType("code");
		suggestRequest(suggestData, request, type, pageSize, requestContext, pretty, sessionToken);
		try {
			if (!(type.equalsIgnoreCase(RESOURCE) || type.equalsIgnoreCase(COLLECTION))) {
				throw new BadRequestException("Invalid Type is passed. For now, the support types are resource and collection.");
			}
			if (type.equalsIgnoreCase(RESOURCE)) {
				type = TAXONOMY_RESOURCE;
			} else if (type.equalsIgnoreCase(COLLECTION)) {
				type = TAXONOMY_COLLECTION;
			}
			suggestData.setType(type);
			suggestData.setIsInternalSuggest(isInternalSuggest);
			suggestData.setCrosswalk(isCrosswalk);
			SuggestResponse<Object> suggestResults = suggestService.suggest(suggestData).get(0);
			String result = serialize(suggestResults, JSON, SINGLE_EXCLUDES, true);
			LOG.info("Total latency of suggest " + (System.currentTimeMillis() - start));
			return toModelAndView(result);
		} catch (SearchException searchException) {
			response.setStatus(searchException.getStatus().value());
			return toModelAndView(searchException.getMessage());
		}
	}

	private SuggestData suggestRequest(SuggestData suggestData, HttpServletRequest request, String type, Integer pageSize, JSONObject requestContext, String pretty, String sessionToken) throws Exception {
		if (sessionToken == null) {
			sessionToken = getSessionToken(request);
		}
		User apiCaller = (User) request.getAttribute(Constants.USER);
		suggestData.setType(type);
		suggestData.setUserTaxonomyPreference((JSONObject) request.getAttribute(Constants.USER_PREFERENCES));

		@SuppressWarnings("unchecked")
		MapWrapper<Object> suggestParamMap = new MapWrapper<Object>(request.getParameterMap());
		suggestData.setParameters(suggestParamMap);
		if (suggestParamMap != null) {
			if (suggestParamMap.containsKey("inputTypeInternalCode")) suggestData.setInputTypeInternalCode(suggestParamMap.getBoolean("inputTypeInternalCode"));

			SuggestContextData suggestContext = getStudyPlayerContextData(suggestParamMap, requestContext, suggestData);
			if (apiCaller != null) {
				suggestContext.setUserId(apiCaller.getGooruUId());
			}
			suggestData.setSuggestContextData(suggestContext);
		}

		// Set user permits
		UserGroupSupport userGroup = (UserGroupSupport) request.getAttribute(TENANT);
		String userTenantId = userGroup.getTenantId();
		String userTenantRootId = userGroup.getTenantRoot();
		suggestData.setUserTenantId(userTenantId);
		suggestData.setUserTenantRootId(userTenantRootId);
		
		suggestData.setFrom(0);
		suggestData.setSize(pageSize > 0 ? pageSize : 10);
		suggestData.setPretty(pretty);
		suggestData.setSessionToken(sessionToken);
		suggestData.setRemoteAddress(request.getRemoteAddr());
		suggestData.setUser(apiCaller);
		return suggestData;
	}

	private SuggestContextData getStudyPlayerContextData(MapWrapper<Object> suggestParamMap, JSONObject requestContext, SuggestData suggestData) throws Exception {
		SuggestContextData suggestContext = new SuggestContextData();
		if (requestContext != null) {
			if (suggestData.getSuggestInputType() != null && suggestData.getSuggestInputType().equalsIgnoreCase("code")) {
				getRequestService().processCodeContextPayload(requestContext, suggestContext, suggestData);
			} else {
				getRequestService().processContextPayload(requestContext, suggestContext, suggestData);
			}
		} else {
			throw new BadRequestException("Please refer the document to pass correct parameters");
		}
		return suggestContext;
	}
	
	private RequestService getRequestService() {
		return requestService;
	}
	
	private void setEventLogObject(HttpServletRequest request, SuggestData suggestData, SearchResponse<Object> searchResponse) throws JSONException {
		if (suggestData.getType() != null && suggestData.getType().equalsIgnoreCase(TYPE_SCOLLECTION)) {
			request.setAttribute(SEARCH_TYPE, COLLECTION);
		} else {
			request.setAttribute(SEARCH_TYPE, suggestData.getType());
		}
		JSONObject payloadObject = new JSONObject();
		JSONObject session = new JSONObject();
		SessionContextSupport.putLogParameter(EventConstants.EVENT_NAME, EventConstants.ITEM_DOT_SUGGEST);
		payloadObject.put(EventConstants.TEXT, suggestData.getOriginalQuery());
		if (suggestData.getSessionToken() != null) {
			session.put(EventConstants.SESSION_TOKEN, suggestData.getSessionToken());
			session.put(EventConstants.PARTNER_ID, request.getAttribute(EventConstants.PARTNER_ID));
			session.put(EventConstants.APP_ID, request.getAttribute(EventConstants.APP_ID));
			session.put(EventConstants.TENANT_ID, suggestData.getUserTenantId());
			session.put(EventConstants.TENANT_ROOT, suggestData.getUserTenantRootId());
		}
		SessionContextSupport.putLogParameter(EventConstants.SESSION, session);
		payloadObject.put(EventConstants.PAGE_SIZE, suggestData.getSize());
		payloadObject.put(EventConstants.PAGE_NUM, suggestData.getPageNum());
		payloadObject.put(EventConstants.START_AT, suggestData.getFrom());
		payloadObject.put(EventConstants.RESULT_SIZE, searchResponse.getResultCount());
		payloadObject.put(EventConstants.HIT_COUNT, searchResponse.getTotalHitCount());
		payloadObject.put(EventConstants.SEARCH_EXECUTION_TIME, searchResponse.getExecutionTime());
		SessionContextSupport.putLogParameter(EventConstants.PAYLOAD_OBJECT, payloadObject);
		request.setAttribute(EventConstants.ACTION, SUGGEST);
	}
}
