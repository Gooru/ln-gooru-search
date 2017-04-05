package org.ednovo.gooru.controllers.api.v3;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.ednovo.gooru.controllers.api.BaseController;
import org.ednovo.gooru.search.es.constant.Constants;
import org.ednovo.gooru.search.es.constant.RequestContextFields;
import org.ednovo.gooru.search.es.exception.BadRequestException;
import org.ednovo.gooru.search.es.exception.SearchException;
import org.ednovo.gooru.search.es.model.MapWrapper;
import org.ednovo.gooru.search.es.model.SuggestResponse;
import org.ednovo.gooru.search.es.model.User;
import org.ednovo.gooru.search.es.model.UserGroupSupport;
import org.ednovo.gooru.search.es.service.SearchSettingService;
import org.ednovo.gooru.suggest.v3.model.SuggestContextData;
import org.ednovo.gooru.suggest.v3.model.SuggestData;
import org.ednovo.gooru.suggest.v3.service.SuggestV3Service;
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

	@RequestMapping(method = { RequestMethod.POST }, value = "/{type}")
	public ModelAndView suggest(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false) String sessionToken,
			@RequestParam(defaultValue = "10", value = "limit") Integer pageSize, @RequestParam(defaultValue = "0") String pretty, @PathVariable String type, @RequestBody String contextPayload)
			throws Exception {
		long start = System.currentTimeMillis();
		JSONObject requestContext = null;
		if (!contextPayload.isEmpty())
			requestContext = new JSONObject(contextPayload);
		if (requestContext == null || requestContext.keys() == null) {
			throw new BadRequestException("Request Payload missing!");
		}
		SuggestData suggestData = suggestRequest(request, type, pageSize, requestContext, pretty, sessionToken);
		try {
			if (!(type.equalsIgnoreCase(RESOURCE) || type.equalsIgnoreCase(COLLECTION))) {
				throw new BadRequestException("Invalid Type is passed. For now, the support types are resource and collection.");
			}
			SuggestResponse<Object> suggestResults = suggestService.suggest(suggestData).get(0);
			String result = serialize(suggestResults, JSON, SINGLE_EXCLUDES, true);
			LOG.info("Total latency of suggest " + (System.currentTimeMillis() - start));
			return toModelAndView(result);
		} catch (SearchException searchException) {
			response.setStatus(searchException.getStatus().value());
			return toModelAndView(searchException.getMessage());
		}
	}

	private SuggestData suggestRequest(HttpServletRequest request, String type, Integer pageSize, JSONObject requestContext, String pretty, String sessionToken) throws Exception {
		if (sessionToken == null) {
			sessionToken = getSessionToken(request);
		}
		User apiCaller = (User) request.getAttribute(Constants.USER);
		SuggestData suggestData = new SuggestData();
		suggestData.setType(type);

		@SuppressWarnings("unchecked")
		MapWrapper<Object> suggestParamMap = new MapWrapper<Object>(request.getParameterMap());
		suggestData.setParameters(suggestParamMap);
		if (suggestParamMap != null) {
			SuggestContextData suggestContext = getStudyPlayerContextData(suggestParamMap, requestContext, suggestData);
			if (apiCaller != null) {
				suggestContext.setUserId(apiCaller.getGooruUId());
			}
			suggestData.setSuggestContextData(suggestContext);
		}

		// Set user permits
		UserGroupSupport userGroup = (UserGroupSupport) request.getAttribute(TENANT);
		List<String> userPermits = new ArrayList<>();
		String userTenantId = userGroup.getTenantId();
		userPermits.add(userTenantId);
		List<String> discoverableTenantIds = SearchSettingService.getDiscoverableTenantIds(DISCOVERABLE_TENANT_IDS);
		if (discoverableTenantIds != null && !discoverableTenantIds.isEmpty())
			userPermits.addAll(discoverableTenantIds);
		suggestData.setUserPermits(userPermits);

		suggestData.setFrom(0);
		suggestData.setSize(pageSize);
		suggestData.setPretty(pretty);
		suggestData.setSessionToken(sessionToken);
		suggestData.setRemoteAddress(request.getRemoteAddr());
		suggestData.setUser(apiCaller);
		return suggestData;
	}

	private SuggestContextData getStudyPlayerContextData(MapWrapper<Object> suggestParamMap, JSONObject requestContext, SuggestData suggestData) throws Exception {
		SuggestContextData suggestContext = new SuggestContextData();
		if (requestContext != null) {
			processContextPayload(requestContext, suggestContext, suggestData);
		} else {
			throw new BadRequestException("Please refer the document to pass correct parameters");
		}
		return suggestContext;
	}

	private void processContextPayload(JSONObject requestContext, SuggestContextData suggestContext, SuggestData suggestData) throws Exception {
		Boolean isValidRequest = false;
		if (requestContext.has(RequestContextFields.CONTEXT)) {
			JSONObject context = requestContext.getJSONObject(RequestContextFields.CONTEXT);
			if (context != null && context.has(RequestContextFields.CONTEXT_AREA) && context.has(RequestContextFields.USER_ID)) {
				suggestContext.setContextArea(context.getString(RequestContextFields.CONTEXT_AREA));
				suggestContext.setUserId(context.getString(RequestContextFields.USER_ID));
				if (context.has(RequestContextFields.CONTEXT_TYPE)) suggestContext.setContextType(context.getString(RequestContextFields.CONTEXT_TYPE));
				if (context.has(RequestContextFields.COLLECTION_ID)) suggestContext.setCollectionId(context.getString(RequestContextFields.COLLECTION_ID));
				if (context.has(RequestContextFields.COLLECTION_TYPE)) suggestContext.setCollectionType(context.getString(RequestContextFields.COLLECTION_TYPE));
				if (context.has(RequestContextFields.COLLECTION_SUBTYPE)) suggestContext.setCollectionSubType(context.getString(RequestContextFields.COLLECTION_SUBTYPE));
				if (context.has(RequestContextFields.COURSE_ID)) suggestContext.setCourseId(context.getString(RequestContextFields.COURSE_ID));
				if (context.has(RequestContextFields.UNIT_ID)) suggestContext.setUnitId(context.getString(RequestContextFields.UNIT_ID));
				if (context.has(RequestContextFields.LESSON_ID)) suggestContext.setLessonId(context.getString(RequestContextFields.LESSON_ID));
				if (context.has(RequestContextFields.CURRENT_ITEM_ID)) suggestContext.setCurrentItemId(context.getString(RequestContextFields.CURRENT_ITEM_ID));
				if (context.has(RequestContextFields.CURRENT_ITEM_TYPE)) suggestContext.setCurrentItemType(context.getString(RequestContextFields.CURRENT_ITEM_TYPE));
				if (context.has(RequestContextFields.RESOURCE_ID)) suggestContext.setResourceId(context.getString(RequestContextFields.RESOURCE_ID));

				if (context.getString(RequestContextFields.CONTEXT_AREA).equalsIgnoreCase(STUDY_PLAYER)) {
					if (suggestData.getType().equalsIgnoreCase(COLLECTION) && (context.has(RequestContextFields.REQUESTED_SUBTYPE) && StringUtils.isNotBlank(context.getString(RequestContextFields.REQUESTED_SUBTYPE)))) {
						suggestContext.setRequestedSubType(context.getString(RequestContextFields.REQUESTED_SUBTYPE));
						if ((suggestContext.getRequestedSubType().equalsIgnoreCase(PRE_TEST) && (StringUtils.isNotBlank(suggestContext.getLessonId()))) 
								|| (POST_TEST_OR_BENCHMARK.matcher(suggestContext.getRequestedSubType()).matches() && (StringUtils.isNotBlank(suggestContext.getCollectionId()))) 
								|| (suggestContext.getRequestedSubType().equalsIgnoreCase(BACKFILL) && StringUtils.isNotBlank(suggestContext.getCollectionId()) && (suggestContext.getCollectionSubType() != null && suggestContext.getCollectionSubType().equalsIgnoreCase(PRE_TEST)))) {
							isValidRequest = true;
						}
					} else if (suggestData.getType().equalsIgnoreCase(RESOURCE)) {
						if (StringUtils.isNotBlank(suggestContext.getCollectionId())) {
							isValidRequest = true;
						}
					}
				} else if (suggestData.getType().equalsIgnoreCase(RESOURCE)) {
					if ((context.getString(RequestContextFields.CONTEXT_TYPE).equalsIgnoreCase(RESOURCE_STUDY) && StringUtils.isNotBlank(suggestContext.getResourceId()))
							|| ((context.getString(RequestContextFields.CONTEXT_TYPE).equalsIgnoreCase(COLLECTION_STUDY) && StringUtils.isNotBlank(suggestContext.getCollectionId())))) {
						isValidRequest = true;
					}
				}
			}
		}
		if (requestContext.has(RequestContextFields.METRICS)) {
			JSONObject metrics = requestContext.getJSONObject(RequestContextFields.METRICS);
			if (metrics != null) {
				if (metrics.has(RequestContextFields.SCORE))
					suggestContext.setScore(metrics.getInt(RequestContextFields.SCORE));
				if (metrics.has(RequestContextFields.TIMESPENT))
					suggestContext.setTimeSpent(metrics.getLong(RequestContextFields.TIMESPENT));
			}
		}
		if (!isValidRequest) {
			throw new BadRequestException("Please refer the document to pass correct parameters");
		}
	}

}
