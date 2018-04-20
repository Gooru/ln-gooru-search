package org.ednovo.gooru.controllers.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.ednovo.gooru.search.es.constant.Constants;
import org.ednovo.gooru.search.es.constant.SearchInputType;
import org.ednovo.gooru.search.es.exception.BadRequestException;
import org.ednovo.gooru.search.es.exception.NotFoundException;
import org.ednovo.gooru.search.es.exception.SearchException;
import org.ednovo.gooru.search.es.handler.SearchHandler;
import org.ednovo.gooru.search.es.model.MapWrapper;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.ednovo.gooru.search.es.model.User;
import org.ednovo.gooru.search.es.model.UserGroupSupport;
import org.ednovo.gooru.search.es.processor.util.SerializerUtil;
import org.ednovo.gooru.search.es.service.PedagogyNavigatorService;
import org.ednovo.gooru.search.es.service.SearchSettingService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
/**
 * @author Renuka
 * 
 */
@Controller
@RequestMapping(value = { "/v1/pedagogy-search" })
public class PedagogyNavigatorSearchRestController extends SerializerUtil implements Constants {

	protected static final Logger logger = LoggerFactory.getLogger(PedagogyNavigatorSearchRestController.class);
	
	@Autowired
	private PedagogyNavigatorService pedagogySearchService;
		
	@SuppressWarnings("unchecked")
	@RequestMapping(method = { RequestMethod.GET }, value = "/{type}")
	public ModelAndView search(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false) String sessionToken,
			@RequestParam(defaultValue = "5", value = "length") Integer limit, @RequestParam(defaultValue = "0") Integer startAt, @RequestParam(defaultValue = "1", value = "start") Integer pageNum,
			@RequestParam(defaultValue = "0") String pretty, @RequestParam(value = "q", defaultValue = "*") String query, @PathVariable String type,
			@RequestParam(required = false, defaultValue = "true") boolean isCrosswalk) throws Exception {
		long start = System.currentTimeMillis();

		SearchData searchData = new SearchData();
		User apiCaller = (User) request.getAttribute(USER);

		/**
		 * Here, when no filter is chosen, * search and keyword request with length less than 3 without * are skipped.
		 **/
		if (type.equalsIgnoreCase(TYPE_COMPETENCY_GRAPH) || type.equalsIgnoreCase(LEARNING_MAPS)) {
			query = checkQueryValidity(query, (Map<String, Object>) request.getParameterMap());
		}

		// Set user permits
		UserGroupSupport userGroup = (UserGroupSupport) request.getAttribute(Constants.TENANT);
		List<String> userPermits = new ArrayList<>();
		String userTenantId = userGroup.getTenantId();
		String userTenantRootId = userGroup.getTenantRoot();
		searchData.setUserTenantId(userTenantId);
		searchData.setUserTenantRootId(userTenantRootId);
		userPermits.add(userTenantId);
		List<String> discoverableTenantIds = SearchSettingService.getAllDiscoverableTenantIds(Constants.ALL_DISCOVERABLE_TENANT_IDS);
		if (discoverableTenantIds != null && !discoverableTenantIds.isEmpty())
			userPermits.addAll(discoverableTenantIds);
		searchData.setUserPermits(userPermits.stream().distinct().collect(Collectors.toList()));

		searchData.setUserTaxonomyPreference((JSONObject) request.getAttribute(Constants.USER_PREFERENCES));
		MapWrapper<Object> searchDataMap = new MapWrapper<Object>(request.getParameterMap());

		if (searchDataMap.containsKey("flt.standard") || searchDataMap.containsKey("flt.standardDisplay") ) {
			searchData.setTaxFilterType("standard");
		}
		if (searchDataMap.containsKey("flt.course")) {
			searchData.setTaxFilterType("course");
		}
		if (searchDataMap.containsKey("flt.domain")) {
			searchData.setTaxFilterType("domain");
		}
		if (searchDataMap.containsKey("flt.gutCode")) {
			searchData.setTaxFilterType("standard");
			searchDataMap.put(FLT_TAXONOMY_GUT_CODE, searchDataMap.getString("flt.gutCode"));
			searchDataMap.remove("flt.gutCode");
		}
		searchData.setParameters(searchDataMap);

		if (query.contains("!")) {
			query = query.replace("!", "");
		}
		searchData.setOriginalQuery(query);
		searchData.setQueryString(query);
		searchData.setPretty(pretty);
		searchData.setSessionToken(sessionToken);
		searchData.setCrosswalk(isCrosswalk);

		if (type.equalsIgnoreCase(TYPE_COMPETENCY_GRAPH)) {
			type = KEYWORD_COMPETENCY;
		}

		searchData.setType(type);
		searchData.setFrom(startAt > 0 ? startAt : 0);
		searchData.setPageNum(pageNum > 0 ? pageNum : 1);
		searchData.setSize(limit >= 0 ? limit : 10);
		if (searchData.getFrom() < 1) {
			searchData.setFrom((searchData.getPageNum() - 1) * searchData.getSize());
		}
		searchData.setRemoteAddress(request.getRemoteAddr());
		searchData.setUser(apiCaller);

		// Set Default Values
		for (SearchInputType searchInputType : SearchInputType.values()) {
			if (searchData.getParameters().getObject(searchInputType.getName()) == null) {
				searchData.getParameters().put(searchInputType.getName(), searchInputType.getDefaultValue());
			}
		}

		String excludeAttributeArray[] = {};
		try {
			SearchResponse<Object> searchResponse = new SearchResponse<>();
			if (type.equalsIgnoreCase(LEARNING_MAPS)) {
				searchResponse = pedagogySearchService.searchPedagogy(searchData);
			} else {
				searchResponse = SearchHandler.getSearcher(type.toUpperCase()).search(searchData);
			}
			logger.info("Elapsed time to complete search process :" + (System.currentTimeMillis() - start) + " ms");
			if (type.equalsIgnoreCase(KEYWORD_COMPETENCY)) searchResponse.setExecutionTime(System.currentTimeMillis() - start);
			if (type.equalsIgnoreCase(KEYWORD_COMPETENCY) || type.equalsIgnoreCase(LEARNING_MAPS)) {
				return toModelAndView(serialize(searchResponse.getSearchResults(), JSON, excludeAttributeArray, true, true));
			}
			return toModelAndView(serialize(searchResponse, JSON, excludeAttributeArray, true, false));
		} catch (SearchException searchException) {
			response.setStatus(searchException.getStatus().value());
			return toModelAndView(searchException.getMessage());
		}
	}
	
	@RequestMapping(method = { RequestMethod.GET }, value = "/learning-maps/subject/{subjectCode:.+}")
	public ModelAndView searchLearningMapsBySubject(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String subjectCode,
			@RequestParam(required = false) String fwCode,
			@RequestParam(required = false) String sessionToken,
			@RequestParam(defaultValue = "5", value = "length") Integer limit, 
			@RequestParam(defaultValue = "0") Integer startAt, 
			@RequestParam(defaultValue = "1", value = "start") Integer pageNum,
			@RequestParam(defaultValue = "0") String pretty, 
			@RequestParam(required = false, defaultValue = "*", value = "q") String query,
			@RequestParam(defaultValue = "true") boolean isCrosswalk,
			@RequestParam(defaultValue = "true") boolean isDisplayCode) throws Exception {
		long start = System.currentTimeMillis();
		int dotCount = StringUtils.countMatches(subjectCode, DOT);
		int hyphenCount = StringUtils.countMatches(subjectCode, HYPHEN);
		if (dotCount < 1 || dotCount > 3 || hyphenCount > 0) throw new BadRequestException("Invalid code! Please pass valid subject.");
		
		SearchData searchData = new SearchData();
		searchData = generateLMSearchData(request, searchData, subjectCode, fwCode, sessionToken, limit, startAt, pageNum, pretty, query, isCrosswalk, SUBJECT, isDisplayCode);
		
		String excludeAttributeArray[] = {};
		try {
			SearchResponse<Object> searchResponse = pedagogySearchService.searchPedagogy(searchData);
			logger.info("Elapsed time to complete search process :" + (System.currentTimeMillis() - start) + " ms");
			return toModelAndView(serialize(searchResponse.getSearchResults(), JSON, excludeAttributeArray, true, false));
		} catch (SearchException searchException) {
			response.setStatus(searchException.getStatus().value());
			return toModelAndView(searchException.getMessage());
		}
	}
	
	@RequestMapping(method = { RequestMethod.GET }, value = "/learning-maps/course/{courseCode:.+}")
	public ModelAndView searchLearningMapsByCourse(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String courseCode,
			@RequestParam(required = false) String fwCode,
			@RequestParam(required = false) String sessionToken,
			@RequestParam(defaultValue = "5", value = "length") Integer limit, 
			@RequestParam(defaultValue = "0") Integer startAt, 
			@RequestParam(defaultValue = "1", value = "start") Integer pageNum,
			@RequestParam(defaultValue = "0") String pretty, 
			@RequestParam(required = false, defaultValue = "*", value = "q") String query,
			@RequestParam(defaultValue = "true") boolean isCrosswalk,
			@RequestParam(defaultValue = "true") boolean isDisplayCode) throws Exception {
		long start = System.currentTimeMillis();
		int hyphenCount = StringUtils.countMatches(courseCode, HYPHEN);
		if (!(hyphenCount == 1)) throw new BadRequestException("Invalid code! Please pass valid course.");
		
		SearchData searchData = new SearchData();
		searchData = generateLMSearchData(request, searchData, courseCode, fwCode, sessionToken, limit, startAt, pageNum, pretty, query, isCrosswalk, TYPE_COURSE, isDisplayCode);
		
		String excludeAttributeArray[] = {};
		try {
			SearchResponse<Object> searchResponse = pedagogySearchService.searchPedagogy(searchData);
			logger.info("Elapsed time to complete search process :" + (System.currentTimeMillis() - start) + " ms");
			return toModelAndView(serialize(searchResponse.getSearchResults(), JSON, excludeAttributeArray, true, false));
		} catch (SearchException searchException) {
			response.setStatus(searchException.getStatus().value());
			return toModelAndView(searchException.getMessage());
		}
	}
	
	@RequestMapping(method = { RequestMethod.GET }, value = "/learning-maps/domain/{domainCode:.+}")
	public ModelAndView searchLearningMapsByDomain(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String domainCode,
			@RequestParam(required = false) String fwCode,
			@RequestParam(required = false) String sessionToken,
			@RequestParam(defaultValue = "5", value = "length") Integer limit, 
			@RequestParam(defaultValue = "0") Integer startAt, 
			@RequestParam(defaultValue = "1", value = "start") Integer pageNum,
			@RequestParam(defaultValue = "0") String pretty, 
			@RequestParam(required = false, defaultValue = "*", value = "q") String query,
			@RequestParam(defaultValue = "true") boolean isCrosswalk,
			@RequestParam(defaultValue = "true") boolean isDisplayCode) throws Exception {
		long start = System.currentTimeMillis();
		int hyphenCount = StringUtils.countMatches(domainCode, HYPHEN);
		if (!(hyphenCount == 2)) throw new BadRequestException("Invalid code! Please pass valid domain.");
		
		SearchData searchData = new SearchData();
		searchData = generateLMSearchData(request, searchData, domainCode, fwCode, sessionToken, limit, startAt, pageNum, pretty, query, isCrosswalk, DOMAIN, isDisplayCode);
		
		String excludeAttributeArray[] = {};
		try {
			SearchResponse<Object> searchResponse = pedagogySearchService.searchPedagogy(searchData);
			logger.info("Elapsed time to complete search process :" + (System.currentTimeMillis() - start) + " ms");
			return toModelAndView(serialize(searchResponse.getSearchResults(), JSON, excludeAttributeArray, true, false));
		} catch (SearchException searchException) {
			response.setStatus(searchException.getStatus().value());
			return toModelAndView(searchException.getMessage());
		}
	}
	
	@RequestMapping(method = { RequestMethod.GET }, value = "/learning-maps/standard/{standardCode:.+}")
	public ModelAndView searchLearningMapsByCompetency(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String standardCode,
			@RequestParam(required = false) String fwCode,
			@RequestParam(required = false) String sessionToken,
			@RequestParam(defaultValue = "5", value = "length") Integer limit, 
			@RequestParam(defaultValue = "0") Integer startAt, 
			@RequestParam(defaultValue = "1", value = "start") Integer pageNum,
			@RequestParam(defaultValue = "0") String pretty, 
			@RequestParam(required = false, defaultValue = "*", value = "q") String query,
			@RequestParam(defaultValue = "true") boolean isCrosswalk,
			@RequestParam(defaultValue = "true") boolean isDisplayCode) throws Exception {
		long start = System.currentTimeMillis();
		
		if (!(isDisplayCode && fwCode != null) && StringUtils.countMatches(standardCode, HYPHEN) < 3) throw new BadRequestException("Invalid code! Please pass valid competency/micro-competency.");

		SearchData searchData = new SearchData();
		searchData = generateLMSearchData(request, searchData, standardCode, fwCode, sessionToken, limit, startAt, pageNum, pretty, query, isCrosswalk, TYPE_STANDARD, isDisplayCode);
		String excludeAttributeArray[] = {};
		try {
			SearchResponse<Object> searchResponse = pedagogySearchService.searchPedagogy(searchData);
			logger.info("Elapsed time to complete search process :" + (System.currentTimeMillis() - start) + " ms");
			return toModelAndView(serialize(searchResponse.getSearchResults(), JSON, excludeAttributeArray, true, false));
		} catch (SearchException searchException) {
			response.setStatus(searchException.getStatus().value());
			return toModelAndView(searchException.getMessage());
		}
	}
	
	@RequestMapping(method = { RequestMethod.GET }, value = "/learning-maps/stats")
	public ModelAndView fetchLearningMapStats(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = true) String subjectClassification,
			@RequestParam(required = false) String subjectCode,
			@RequestParam(required = false) String courseCode,
			@RequestParam(required = false) String domainCode,
			@RequestParam(required = false) String codeType,
			@RequestParam(required = false) String sessionToken,
			@RequestParam(defaultValue = "10", value = "length") Integer limit, 
			@RequestParam(defaultValue = "0") Integer startAt, 
			@RequestParam(defaultValue = "1", value = "start") Integer pageNum,
			@RequestParam(defaultValue = "0") String pretty) throws Exception {
		long start = System.currentTimeMillis();
		if (StringUtils.isNotBlank(subjectCode)) {
			String[] subjectList = subjectCode.split(COMMA);
			for (String subject : subjectList) {
				int dotCount = StringUtils.countMatches(subject, DOT);
				int hyphenCount = StringUtils.countMatches(subject, HYPHEN);
				if (dotCount < 1 || dotCount > 3 || hyphenCount > 0)
					throw new BadRequestException("Invalid code! Please pass valid subject.");
			}
		}
		if (StringUtils.isNotBlank(courseCode)) {
			String[] courseList = courseCode.split(COMMA);
			for (String course : courseList) {
				int hyphenCount = StringUtils.countMatches(course, HYPHEN);
				if (!(hyphenCount == 1))
					throw new BadRequestException("Invalid code! Please pass valid course.");
			}
		}
		if (StringUtils.isNotBlank(domainCode)) {
			String[] domainList = domainCode.split(COMMA);
			for (String domain : domainList) {
				int hyphenCount = StringUtils.countMatches(domain, HYPHEN);
				if (!(hyphenCount == 2))
					throw new BadRequestException("Invalid code! Please pass valid domain.");
			}
		}		

		SearchData searchData = new SearchData();
		searchData.setPretty(pretty);
		searchData.setFrom(startAt > 0 ? startAt : 0);
		searchData.setPageNum(pageNum > 0 ? pageNum : 1);
		searchData.setSize(limit >= 0 ? limit : 10);
		if (searchData.getFrom() < 1) {
			searchData.setFrom((searchData.getPageNum() - 1) * searchData.getSize());
		}
		String excludeAttributeArray[] = {};
		try {
			SearchResponse<Object> searchResponse = pedagogySearchService.fetchLearningMapStats(searchData, subjectClassification, subjectCode, courseCode, domainCode, null, codeType);
			logger.info("Elapsed time to fetch LM Stats :" + (System.currentTimeMillis() - start) + " ms");
			return toModelAndView(serialize(searchResponse.getSearchResults(), JSON, excludeAttributeArray, true, false));
		} catch (SearchException searchException) {
			response.setStatus(searchException.getStatus().value());
			return toModelAndView(searchException.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = { RequestMethod.GET }, value = "/learning-maps/stats/search")
	public ModelAndView fetchLearningMapStats(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "q", defaultValue = "*") String query,
			@RequestParam(required = false) String codeType,
			@RequestParam(required = false) String sessionToken,
			@RequestParam(defaultValue = "10", value = "length") Integer limit, 
			@RequestParam(defaultValue = "0") Integer startAt, 
			@RequestParam(defaultValue = "1", value = "start") Integer pageNum,
			@RequestParam(defaultValue = "0") String pretty) throws Exception {
		long start = System.currentTimeMillis();
		SearchResponse<Object> searchResponse = new SearchResponse<Object>();
		try {
			SearchData searchData = new SearchData();
			searchData.setPretty(pretty);
			searchData.setFrom(startAt > 0 ? startAt : 0);
			searchData.setPageNum(pageNum > 0 ? pageNum : 1);
			searchData.setSize(limit >= 0 ? limit : 10);
			if (searchData.getFrom() < 1) {
				searchData.setFrom((searchData.getPageNum() - 1) * searchData.getSize());
			}
			/**
			 * Here, when no filter is chosen, * search and keyword request with length less than 3 without * are skipped.
			 **/
			query = checkQueryValidity(query, (Map<String, Object>) request.getParameterMap());
			String standardCode = pedagogySearchService.fetchKwToCompetency(query, pretty, 0, 100);
			if (StringUtils.isNotBlank(standardCode)) {
				searchResponse = pedagogySearchService.fetchLearningMapStats(searchData, null, null, null, null, standardCode, codeType);
			} else {
				throw new NotFoundException("No matching GUT found for requested search term : " + query);
			}
			String excludeAttributeArray[] = {};
			logger.info("Elapsed time to fetch LM Stats Search :" + (System.currentTimeMillis() - start) + " ms");
			return toModelAndView(serialize(searchResponse.getSearchResults(), JSON, excludeAttributeArray, true, false));
		} catch (SearchException searchException) {
			response.setStatus(searchException.getStatus().value());
			return toModelAndView(searchException.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = { RequestMethod.GET }, value = "/learning-maps/search")
	public ModelAndView learningMapSearch(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam(required = false) String sessionToken,
			@RequestParam(defaultValue = "10", value = "length") Integer limit, 
			@RequestParam(defaultValue = "0") Integer startAt, 
			@RequestParam(defaultValue = "1", value = "start") Integer pageNum,
			@RequestParam(defaultValue = "0") String pretty, 
			@RequestParam(value = "q", defaultValue = "*") String query,
			@RequestParam(required = false, defaultValue = "true") boolean isCrosswalk) throws Exception {
		long start = System.currentTimeMillis();
		SearchResponse<Object> searchResponse = new SearchResponse<Object>();
		try {
			/**
			 * Here, when no filter is chosen, * search and keyword request with length less than 3 without * are skipped.
			 **/
			query = checkQueryValidity(query, (Map<String, Object>) request.getParameterMap());
			String standardCode = pedagogySearchService.fetchKwToCompetency(query, pretty, 0, 40);
			SearchData searchData = new SearchData();
			if (StringUtils.isNotBlank(standardCode)) {
				searchData = generateLMSearchData(request, searchData, standardCode, null, sessionToken, limit, startAt, pageNum, pretty, query, isCrosswalk, KEYWORD_COMPETENCY, false);
				searchResponse = pedagogySearchService.searchPedagogy(searchData);
			} else {
				throw new NotFoundException("No matching GUT found for requested search term : "+ query);
			}
			String excludeAttributeArray[] = {};
			logger.info("Elapsed time to complete KwToComp LM search process :" + (System.currentTimeMillis() - start) + " ms");
			return toModelAndView(serialize(searchResponse.getSearchResults(), JSON, excludeAttributeArray, true, false));
		} catch (SearchException searchException) {
			response.setStatus(searchException.getStatus().value());
			return toModelAndView(searchException.getMessage());
		}
	}
	
	private SearchData generateLMSearchData(HttpServletRequest request, SearchData searchData, String code, String fwCode, String sessionToken, Integer limit, Integer startAt, Integer pageNum, String pretty,
			String query, boolean isCrosswalk, String codeType, boolean isDisplayCode) {
		User apiCaller = (User) request.getAttribute(USER);

		// Set user permits
		UserGroupSupport userGroup = (UserGroupSupport) request.getAttribute(Constants.TENANT);
		List<String> userPermits = new ArrayList<>();
		String userTenantId = userGroup.getTenantId();
		String userTenantRootId = userGroup.getTenantRoot();
		searchData.setUserTenantId(userTenantId);
		searchData.setUserTenantRootId(userTenantRootId);
		userPermits.add(userTenantId);
		List<String> discoverableTenantIds = SearchSettingService.getAllDiscoverableTenantIds(Constants.ALL_DISCOVERABLE_TENANT_IDS);
		if (discoverableTenantIds != null && !discoverableTenantIds.isEmpty())
			userPermits.addAll(discoverableTenantIds);
		searchData.setUserPermits(userPermits.stream().distinct().collect(Collectors.toList()));

		searchData.setUserTaxonomyPreference((JSONObject) request.getAttribute(Constants.USER_PREFERENCES));

		searchData.setOriginalQuery(query);
		searchData.setQueryString(query);
		searchData.setPretty(pretty);
		searchData.setSessionToken(sessionToken);
		searchData.setCrosswalk(isCrosswalk);

		searchData.setType(LEARNING_MAPS);
		searchData.setFrom(startAt > 0 ? startAt : 0);
		searchData.setPageNum(pageNum > 0 ? pageNum : 1);
		searchData.setSize(limit >= 0 ? limit : 10);
		if (searchData.getFrom() < 1) {
			searchData.setFrom((searchData.getPageNum() - 1) * searchData.getSize());
		}
		searchData.setRemoteAddress(request.getRemoteAddr());
		searchData.setUser(apiCaller);

		processParameters(request, searchData, code, fwCode, codeType, isDisplayCode);
		// Set Default Values
		for (SearchInputType searchInputType : SearchInputType.values()) {
			if (searchData.getParameters().getObject(searchInputType.getName()) == null) {
				searchData.getParameters().put(searchInputType.getName(), searchInputType.getDefaultValue());
			}
		}
		return searchData;
	}

	@SuppressWarnings("unchecked")
	private void processParameters(HttpServletRequest request, SearchData searchData, String code, String fwCode, String codeType, boolean isDisplayCode) {
		MapWrapper<Object> searchDataMap = new MapWrapper<Object>(request.getParameterMap());

		if (codeType.equalsIgnoreCase(TYPE_STANDARD) || codeType.equalsIgnoreCase(KEYWORD_COMPETENCY) || (searchDataMap.containsKey(FLT_STANDARD) || searchDataMap.containsKey(FLT_STANDARD_DISPLAY) || searchDataMap.containsKey(FLT_TAXONOMY_GUT_CODE) )) {
			if (StringUtils.isNotBlank(code)) {
				if (StringUtils.isBlank(fwCode) && StringUtils.isNotBlank(code)) {
					searchDataMap.put(FLT_TAXONOMY_GUT_CODE, code);
				} else if (StringUtils.isNotBlank(fwCode)) {
					if (isDisplayCode) {
						searchDataMap.put(FLT_STANDARD_DISPLAY, code);
					} else {
						searchDataMap.put(FLT_STANDARD, code);
					}
					searchDataMap.put(FLT_FWCODE, fwCode);
				}
			}
			searchData.setTaxFilterType(TYPE_STANDARD);
			if (codeType.equalsIgnoreCase(KEYWORD_COMPETENCY)) searchData.setTaxFilterType(KEYWORD_COMPETENCY);
		} else if (codeType.equalsIgnoreCase(SUBJECT) || searchDataMap.containsKey(FLT_SUBJECT)) {
			searchDataMap.put(FLT_SUBJECT, code);
			searchData.setTaxFilterType(SUBJECT);
		} else if (codeType.equalsIgnoreCase(TYPE_COURSE) || searchDataMap.containsKey(FLT_COURSE)) {
			searchDataMap.put(FLT_COURSE, code);
			searchData.setTaxFilterType(TYPE_COURSE);
		} else if (codeType.equalsIgnoreCase(DOMAIN) || searchDataMap.containsKey(FLT_DOMAIN)) {
			searchDataMap.put(FLT_DOMAIN, code);
			searchData.setTaxFilterType(DOMAIN);
		}
		searchData.setParameters(searchDataMap);
	}
	
	private String checkQueryValidity(String query, Map<String, Object> parameterMap) {
		if (!parameterMap.isEmpty()) {
			Set<String> parameterSet = parameterMap.keySet();
			boolean hasFilter = false;
			for (String parameterKey : parameterSet) {
				parameterKey = parameterKey.trim();
				if (parameterKey.contains(FLT) && (!parameterKey.equalsIgnoreCase(FLT_COLLECTION_TYPE) && !(parameterKey.equalsIgnoreCase(FLT_RESOURCE_FORMAT) && parameterMap.get(FLT_RESOURCE_FORMAT).toString().equalsIgnoreCase(TYPE_QUESTION)) && !(parameterKey.equalsIgnoreCase(FLT_RATING) && parameterMap.get(FLT_RATING).toString().contains(STR_ZERO)))) {
					hasFilter = true;
					break;
				}
			}
			query = query.replaceAll(ESCAPED_STAR, EMPTY_STRING);
			if (!hasFilter && query.length() < 3) {
				throw new BadRequestException(INVALID_KEYWORD_ERROR_MESSAGE);
			} else if (hasFilter && StringUtils.isBlank(query)) {
				query = STAR;
			}
		}
		return query;
	}

}
