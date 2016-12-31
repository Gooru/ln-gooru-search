package org.ednovo.gooru.controllers.api.v3;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.ednovo.gooru.controllers.api.BaseController;
import org.ednovo.gooru.search.es.constant.Constants;
import org.ednovo.gooru.search.es.exception.BadRequestException;
import org.ednovo.gooru.search.es.exception.SearchException;
import org.ednovo.gooru.search.es.model.MapWrapper;
import org.ednovo.gooru.search.es.model.SuggestResponse;
import org.ednovo.gooru.search.es.model.User;
import org.ednovo.gooru.suggest.v3.model.SuggestData;
import org.ednovo.gooru.suggest.v3.model.SuggestV3Context;
import org.ednovo.gooru.suggest.v3.service.SuggestV3Service;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = { "/v3/suggest" })
public class SuggestV3RestController extends BaseController {

	protected static final Logger LOG = LoggerFactory.getLogger(SuggestV3RestController.class);

	public static final String SINGLE_EXCLUDES[] = { "*.text", "*.class", "*.answer", "*.isCorrect", "*.question", "*.sequence", "*.unit", "*.hintId",
			"*.assets", "*.codes","*.importCode", "*.queryUId", "*.resource.indexType","*.resource.recordSource", "*.brokenStatus","*.assetURI","*.category","*.contentId","*.contentPermissions",
			"creator.isDeleted","*.emailId","*.organizationName","*.partyUid","*.userRoleSetString","*.distinguish","*.entryId","*.folder","*.indexId","*.isDeleted",
			"*.isOer","*.libraryNames","*.ratings.*","*.*.recordSource","*.resourceAddedCount","*.resourceTags","*.resourceType","*.resourceUsedUserCount","*.resultUId","*.s3UploadFlag","*.sharing","*.taxonomyDataSet",
			"*.thumbnail","*.thumbnails","*.viewCount","*.resourceSource","*.course"};
	@Autowired
	private SuggestV3Service suggestService;

	@RequestMapping(method = { RequestMethod.GET }, value = "/{type}")
	public ModelAndView suggest(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam(required = false) String sessionToken, 
			@RequestParam(defaultValue = "0") Integer startAt,
			@RequestParam(defaultValue = "10", value = "limit") Integer pageSize, 
			@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(required = true) String context,
			@RequestParam(required = true) String id, @RequestParam(required = true) String userId, 
			@RequestParam(required = false) String containerId, @RequestParam(required = false) String courseId,
			@RequestParam(required = false) String unitId, @RequestParam(required = false) String lessonId, 
			@RequestParam(defaultValue = "0") String pretty, @PathVariable String type)
			throws Exception {

		if(sessionToken == null){
			sessionToken = getSessionToken(request);
		}
		User apiCaller = (User) request.getAttribute(Constants.USER);
		SuggestData suggestData = new SuggestData();
		SuggestV3RequestData suggestRequestData = new SuggestV3RequestData();

		@SuppressWarnings("unchecked")
		MapWrapper<Object> suggestParamMap = new MapWrapper<Object>(request.getParameterMap());
		suggestData.setParameters(suggestParamMap);
		if (suggestParamMap != null) {
			SuggestV3Context suggestContext = getContextData(suggestParamMap, suggestRequestData);
			if (apiCaller != null) {
				suggestContext.setUserId(apiCaller.getGooruUId());
			}
			if (suggestParamMap.containsKey("userId") && StringUtils.trimToNull(suggestParamMap.getString("userId")) != null) {
				suggestContext.setUserId(suggestParamMap.getString("userId"));
			}
			suggestData.setSuggestV3Context(suggestContext);
		}

		suggestData.setType(type);
		suggestData.setFrom(0);
		suggestData.setSize(pageSize);
		suggestData.setPretty(pretty);
		suggestData.setSessionToken(sessionToken);
		suggestData.setRemoteAddress(request.getRemoteAddr());
		suggestData.setUser(apiCaller);

		try{
			//String result = getSuggestCannedResponse().toJSONString();
			List<SuggestResponse<Object>> suggestResults = suggestService.suggest(suggestData);
			String result = serialize(suggestResults, JSON, SINGLE_EXCLUDES, true);
			//LOG.info("Total latency suggest " , System.currentTimeMillis() - start);
			return toModelAndView(result);
		} catch (SearchException searchException) {
			response.setStatus(searchException.getStatus().value());
			return toModelAndView(searchException.getMessage());
		}
	}

  private SuggestV3Context getContextData(MapWrapper<Object> suggestParamMap, SuggestV3RequestData suggestData) {
    SuggestV3Context suggestContext = new SuggestV3Context();
    Map<String, Object> inputParameters = suggestParamMap.getValues();
    if (inputParameters.containsKey("id") && inputParameters.containsKey("context") && inputParameters.containsKey("userId")) {
      for (String key : inputParameters.keySet()) {
        switch (key) {
        case "id":
          suggestContext.setId(((String[]) inputParameters.get(key))[0]);
          break;
        case "context":
          suggestContext.setContext(((String[]) inputParameters.get(key))[0]);
          break;
        case "userId":
          suggestContext.setUserId(((String[]) inputParameters.get(key))[0]);
          break;
        case "containerId":
          suggestContext.setContainerId(((String[]) inputParameters.get(key))[0]);
          break;
        case "courseId":
          suggestContext.setCourseId(((String[]) inputParameters.get(key))[0]);
          break;
        case "unitId":
          suggestContext.setUnitId(((String[]) inputParameters.get(key))[0]);
          break;
        case "lessonId":
          suggestContext.setLessonId(((String[]) inputParameters.get(key))[0]);
          break;
        case "limit":
            Integer limit = Integer.parseInt(((String[]) inputParameters.get(key))[0]);
            if (limit > 50)
              throw new BadRequestException("Requested limit exceeds the allowed limit. Please refer document for maximum allowed limit");
          break;
        }
      }
    } else {
      throw new BadRequestException("Please refer the document to pass correct parameters");
    }
    return suggestContext;
  }

  private JSONArray getSuggestCannedResponse() {
    JSONArray json = new JSONArray();
    JSONParser parser = new JSONParser();
    try {
      json = (JSONArray) parser.parse(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("suggest_canned_response.txt")));
      return json;
    } catch (ParseException e) {
      LOG.error("Unable to Parse Canned response");
    } catch (FileNotFoundException e) {
      LOG.error("Canned response file not found");
    } catch (IOException e) {
      LOG.error("IOException on getting canned response file");
    }
    return json;
  }

}
