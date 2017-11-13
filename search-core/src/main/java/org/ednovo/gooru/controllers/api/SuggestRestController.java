package org.ednovo.gooru.controllers.api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.ednovo.gooru.search.es.constant.Constants;
import org.ednovo.gooru.search.es.constant.SearchInputType;
import org.ednovo.gooru.search.es.exception.BadRequestException;
import org.ednovo.gooru.search.es.exception.SearchException;
import org.ednovo.gooru.search.es.handler.SearchHandler;
import org.ednovo.gooru.search.es.model.MapWrapper;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.ednovo.gooru.search.es.model.SuggestContext;
import org.ednovo.gooru.search.es.model.SuggestData;
import org.ednovo.gooru.search.es.model.SuggestResponse;
import org.ednovo.gooru.search.es.model.User;
import org.ednovo.gooru.search.es.service.ActivityStreamDataProviderService;
import org.ednovo.gooru.search.es.service.SuggestService;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = { "/suggest" })
public class SuggestRestController extends BaseController {

	protected static final Logger LOG = LoggerFactory.getLogger(SuggestRestController.class);
	
	@Autowired
	private ActivityStreamDataProviderService activityStreamDataProviderService;

	private static final String RESOURCE = "resource";

	private static final String SINGLE_EXCLUDES[] = { "*.organization", "fileData", "fileHash", "fromQa", "isFeaturedBoolean", "isLive", "isNew", "lessonsString", "new", "parentUrl",
			"resourceInstances", "resourceLearnguides", "resourceMetaData", "resourceOid", "resourceSegments", "responses", "score", "siteName", "sourceReference", "subscriptions", "taxonomySet.*",
			"text", "userPermSet", "userUploadedImage", "vocaularyString", "*.class", "*.answerId", "*.answerType", "*.isCorrect", "*.matchingAnswer", "*.question", "*.sequence", "*.unit",
			"*.hintId", "*.assessmentCode", "*.assessmentGooruId", "*.assets", "*.codes", "*.creator", "*.difficultyLevel", "*.helpContentLink", "*.importCode", "*.instruction", "*.isFolderAbsent",
			"*.quizNetwork", "*.scorePoints", "*.sourceContentInfo", "*.user", "*.userVote", "*.queryUId" };

	@Autowired
	private SuggestService suggestService;
	
	@RequestMapping(method = { RequestMethod.GET }, value = "/{type}")
	public ModelAndView suggest(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam(required = false) String sessionToken,
			@RequestParam(required = false) String context,
			@RequestParam(defaultValue = "10") Integer pageSize, @RequestParam(defaultValue = "0") String pretty, 
			@RequestParam(defaultValue = "0") Integer startAt, @RequestParam(defaultValue = "1") Integer pageNum, 
			@RequestParam(required = false) String excludeAttributes, @PathVariable String type,
			@RequestParam(required = false, defaultValue = "false") boolean includeCollectionItem, 
			@RequestParam(required = false, defaultValue = "*") String query) {

		long start = System.currentTimeMillis();
		if(sessionToken == null){
			sessionToken = getSessionToken(request);
		}
		User apiCaller = (User) request.getAttribute(Constants.USER);
		SuggestData searchData = new SuggestData();
		MapWrapper<Object> searchDataMap = new MapWrapper<Object>(request.getParameterMap());
		searchDataMap.put("allowDuplicates", true);
		searchDataMap.put("includeCollectionItem", includeCollectionItem);
		searchData.setQueryString(query);
		searchData.setContext(context);
		searchData.setParameters(searchDataMap);
		searchData.setPretty(pretty);
		searchData.setType(type);
		searchData.setFrom(startAt);
		searchData.setSize(pageSize);
		if (searchData.getFrom() < 1) {
			searchData.setFrom((pageNum - 1) * searchData.getSize());
		}
		searchData.setSessionToken(sessionToken);
		searchData.setRemoteAddress(request.getRemoteAddr());
		searchData.setUser(apiCaller);
		//Disabled while removing api-jar dependency
		//searchData.setRestricted(hasUnrestrictedContentAccess());

		// Set Default Values
		for (SearchInputType searchInputType : SearchInputType.values()) {
			if (searchData.getParameters().getObject(searchInputType.getName()) == null) {
				searchData.getParameters().put(searchInputType.getName(), searchInputType.getDefaultValue());
			}
		}

		try {
			SearchResponse<Object> searchResponse = SearchHandler.getSuggester(type.toUpperCase()).search(searchData);
			String excludeAttributeArray[] = {};
			if (excludeAttributes != null) {
				excludeAttributeArray = excludeAttributes.split("\\s*,\\s*");
			}
			searchResponse.setExecutionTime(System.currentTimeMillis() - start);
			if (type.equalsIgnoreCase(RESOURCE)) {
				excludeAttributeArray = (String[]) ArrayUtils.addAll(SINGLE_EXCLUDES, excludeAttributeArray);
			}
			return toModelAndView(serialize(searchResponse, JSON, excludeAttributeArray, true));
		} catch (SearchException searchException) {
			response.setStatus(searchException.getStatus().value());
			return toModelAndView(searchException.getMessage());
		} 
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	@RequestMapping(method = { RequestMethod.GET }, value = "/v2/{type}")
	public ModelAndView suggestNew(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam(required = false) String sessionToken,
			@RequestParam(defaultValue = "10") Integer pageSize,
			@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "0") Integer startAt, 
			@RequestParam(required = false) String excludeAttributes, @RequestParam(defaultValue = "0") String pretty, 
			@RequestParam(required = false, defaultValue = "false") boolean includeCollectionItem, 
			@PathVariable String type) throws Exception {

		if(sessionToken == null){
			sessionToken = getSessionToken(request);
		}
		User apiCaller = (User) request.getAttribute(Constants.USER);
		SuggestData suggestData = new SuggestData();

		MapWrapper<Object> suggestParamMap = new MapWrapper<Object>(request.getParameterMap());
		suggestParamMap.put("includeCollectionItem", includeCollectionItem);

		suggestData.setParameters(suggestParamMap);
		if (suggestParamMap != null) {
			SuggestContext suggestContext = getContextData(suggestParamMap);
			if (apiCaller != null) {
				suggestContext.setUserUid(apiCaller.getGooruUId());
			}
			if (suggestParamMap.containsKey("userUid") && StringUtils.trimToNull(suggestParamMap.getString("userUid")) != null) {
				suggestContext.setUserUid(suggestParamMap.getString("userUid"));
			}
			suggestData.setQueryString(suggestContext.getSearchTerm());
			suggestData.setSuggestContext(suggestContext);
		}
		//Disabled while removing core-api jar
		/*PartyCustomField partyCustomField = partyService.getPartyCustomeField(suggestData.getSuggestContext().getUserUid(), USER_TAXONOMY_ROOT_CODE, null);
		if(partyCustomField != null){
			suggestData.setUserTaxonomyPreference(partyCustomField.getOptionalValue());
		}*/
		suggestData.setType(type);
		suggestData.setFrom(startAt);
		suggestData.setSize(pageSize);
		suggestData.setPretty(pretty);
		if (suggestData.getFrom() < 1) {
			suggestData.setFrom((pageNum - 1) * suggestData.getSize());
		}
		suggestData.setSessionToken(sessionToken);
		suggestData.setRemoteAddress(request.getRemoteAddr());
		suggestData.setUser(apiCaller);
		//Disabled while removing api-jar dependency
		//suggestData.setRestricted(hasUnrestrictedContentAccess());

		try {
			List<SuggestResponse<Object>> suggestResults = suggestService.suggest(suggestData);

			String excludeAttributeArray[] = {};
			if (excludeAttributes != null) {
				excludeAttributeArray = excludeAttributes.split("\\s*,\\s*");
			}
			if (type.equalsIgnoreCase(RESOURCE)) {
				excludeAttributeArray = (String[]) ArrayUtils.addAll(SINGLE_EXCLUDES, excludeAttributeArray);
			}
			return toModelAndView(serialize(suggestResults, JSON, excludeAttributeArray, true));
		} catch (SearchException searchException) {
			response.setStatus(searchException.getStatus().value());
			return toModelAndView(searchException.getMessage());
		}
	}

	private SuggestContext getContextData(MapWrapper<Object> suggestParamMap) {
		SuggestContext suggestContext = new SuggestContext();
		if (suggestParamMap.containsKey("contentGooruOid") && suggestParamMap.containsKey("event")) {
			suggestContext.setContentGooruOid(suggestParamMap.getString("contentGooruOid"));
			suggestContext.setEvent(suggestParamMap.getString("event"));
			suggestContext.setSearchTerm(suggestParamMap.getString("searchTerm"));
			suggestContext.setCategory(suggestParamMap.getString("resourceFormat"));
			if (suggestParamMap.containsKey("quizSessionId")) {
				suggestContext.setQuizSessionId(suggestParamMap.getString("quizSessionId"));
			}
			if (suggestParamMap.containsKey("parentGooruOid")) {
				suggestContext.setParentGooruOid(suggestParamMap.getString("parentGooruOid"));
			}
			if (suggestParamMap.getString("event").equalsIgnoreCase("student-suggestion")) {
				if (suggestParamMap.containsKey("classId") && suggestParamMap.containsKey("contentGooruOid") && suggestParamMap.containsKey("studentId")) {
					suggestContext.setClassId(suggestParamMap.getString("classId"));
					suggestContext.setContentGooruOid(suggestParamMap.getString("contentGooruOid"));
					suggestContext.setStudentId(suggestParamMap.getString("studentId"));
				} else {
					throw new BadRequestException("Please refer the document to pass correct parameters");
				}
			}

			return suggestContext;
		} else {
			throw new BadRequestException("Please refer the document to pass correct parameters");
		}
	}

	@RequestMapping(value = "/recent/activity", method = RequestMethod.GET)
	public void getUserActivity(@RequestParam(value = "userUid", required = false) String userUid,
			@RequestParam(required = false) String sessionToken,
			@RequestParam(value = "apiKey", required = false) String apiKey,
			@RequestParam(value = "eventName", required = false) String eventName, 
			@RequestParam(value = "minutesToRead", required = false, defaultValue = "30") Integer minutesToRead,
			@RequestParam(value = "eventsToRead", required = false, defaultValue = "30") Integer eventsToRead, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		User apiCaller = (User) request.getAttribute(Constants.USER);
		userUid = apiCaller.getGooruUId();
		List<Map<String, Object>> activity = activityStreamDataProviderService.getUserActivityStream(userUid, eventName, minutesToRead, eventsToRead);
		JSONObject resultJson = new JSONObject();
		resultJson.put("activity", activity);
		try {
			response.getWriter().write(resultJson.toJSONString());
		} catch (IOException e) {
			LOG.error("OOPS! Something went wrong when trying to retrieve user's activity stream", e);
		}
		return;			
	}

}
