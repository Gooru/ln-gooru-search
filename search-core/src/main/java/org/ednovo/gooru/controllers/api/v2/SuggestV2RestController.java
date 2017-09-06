package org.ednovo.gooru.controllers.api.v2;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.ednovo.gooru.controllers.api.BaseController;
import org.ednovo.gooru.controllers.api.SuggestRequestData;
import org.ednovo.gooru.search.es.constant.Constants;
import org.ednovo.gooru.search.es.exception.BadRequestException;
import org.ednovo.gooru.search.es.exception.SearchException;
import org.ednovo.gooru.search.es.model.MapWrapper;
import org.ednovo.gooru.search.es.model.SuggestContext;
import org.ednovo.gooru.search.es.model.SuggestData;
import org.ednovo.gooru.search.es.model.SuggestResponse;
import org.ednovo.gooru.search.es.model.User;
import org.ednovo.gooru.search.es.processor.util.JsonDeserializer;
import org.ednovo.gooru.search.es.service.SuggestService;
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
@RequestMapping(value = { "/v2/suggest" })
public class SuggestV2RestController extends BaseController {

	protected static final Logger LOG = LoggerFactory.getLogger(SuggestV2RestController.class);

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
			@RequestParam(defaultValue = "10") Integer pageSize,
			@RequestParam(defaultValue = "0") String pretty, 
			@RequestBody String data, 
			@PathVariable String type) throws Exception {
		if(sessionToken == null){
			sessionToken = getSessionToken(request);
		}
		User apiCaller = (User) request.getAttribute(Constants.USER);
		SuggestData suggestData = new SuggestData();
		SuggestRequestData suggestRequestData = new SuggestRequestData();
		if(!data.isEmpty()){
			suggestRequestData = JsonDeserializer.deserialize(data, SuggestRequestData.class);
		}
		
		MapWrapper<Object> suggestParamMap = new MapWrapper<Object>(request.getParameterMap());
		suggestData.setParameters(suggestParamMap);
		if (suggestParamMap != null) {
			SuggestContext suggestContext = getContextData(suggestParamMap, suggestRequestData);
			if (apiCaller != null) {
				suggestContext.setUserUid(apiCaller.getGooruUId());
			}
			if (suggestParamMap.containsKey("userUid") && StringUtils.trimToNull(suggestParamMap.getString("userUid")) != null) {
				suggestContext.setUserUid(suggestParamMap.getString("userUid"));
			}
			suggestData.setQueryString(suggestContext.getSearchTerm());
			suggestData.setSuggestContext(suggestContext);
		}
		//Disabled while removing api-jar dependency
		/*PartyCustomField partyCustomField = partyService.getPartyCustomeField(suggestData.getSuggestContext().getUserUid(), USER_TAXONOMY_ROOT_CODE, null);
		if(partyCustomField != null){
			suggestData.setUserTaxonomyPreference(partyCustomField.getOptionalValue());
		}*/
		suggestData.setType(type);
		suggestData.setFrom(0);
		suggestData.setSize(pageSize);
		suggestData.setPretty(pretty);
		suggestData.setSessionToken(sessionToken);
		suggestData.setRemoteAddress(request.getRemoteAddr());
		suggestData.setUser(apiCaller);
		//suggestData.setRestricted(hasUnrestrictedContentAccess());

		try {
			List<SuggestResponse<Object>> suggestResults = suggestService.suggest(suggestData);
			String resutls = serialize(suggestResults, JSON, SINGLE_EXCLUDES, true);
			//LOG.info("Total latency suggest " , System.currentTimeMillis() - start);
			return toModelAndView(resutls);
		} catch (SearchException searchException) {
			response.setStatus(searchException.getStatus().value());
			return toModelAndView(searchException.getMessage());
		}
	}

	private SuggestContext getContextData(MapWrapper<Object> suggestParamMap, SuggestRequestData suggestData) {
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
			return suggestContext;
		} else {
			throw new BadRequestException("Please refer the document to pass correct parameters");
		}
	}

}
