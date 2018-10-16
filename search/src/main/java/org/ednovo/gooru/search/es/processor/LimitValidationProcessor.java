/**
 * 
 */
package org.ednovo.gooru.search.es.processor;


import java.util.HashMap;
import java.util.Map;

import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.SessionContextSupport;
import org.ednovo.gooru.search.es.processor.util.JsonDeserializer;
import org.ednovo.gooru.search.es.processor.util.JsonSerializer;
import org.ednovo.gooru.search.es.service.RedisClient;
import org.ednovo.gooru.search.responses.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * @author SearchTeam
 * 
 */
@Component
public class LimitValidationProcessor extends SearchProcessor<SearchData, Object> {

	@Autowired
	private RedisClient redisService;
	
	private static final String FLT_COURSE = "flt.courseType";

	@Override
	public void process(SearchData searchData,
			SearchResponse<Object> response) {
		int maxResultSize = 20;
		if (searchData.getType().equalsIgnoreCase(TYPE_COURSE) && searchData.getParameters() != null && searchData.getParameters().containsKey(FLT_COURSE)) {
			maxResultSize = 70;
		}

		Integer apikeySearchLimit = SessionContextSupport.getUserCredential().getApiKeySearchLimit();
		if (apikeySearchLimit != null && apikeySearchLimit != -1 && searchData.getSize() > apikeySearchLimit) {
			throw new AccessDeniedException(SEARCH_LIMIT_MESSAGE);
		}

		if (!searchData.isAdmin()) {
		    if (searchData.getFrom() > 10000) {
		        throw new AccessDeniedException("Offset reached maximum limit. You are allowed only to search within 10K contents. Contact developer for further details!");
		    }
			if (searchData.getSize() != null && searchData.getSize() > maxResultSize) {
				throw new AccessDeniedException("Maximum pageSize exceeded. Please try with maximum size of " + maxResultSize);
			}
			String cacheKeyIP = "limitSearchAny:" + searchData.getRemoteAddress() + " - type:" + searchData.getType();

			Map<String, String> limitSearchCallCache = getRedisService().get(cacheKeyIP) != null
					? JsonDeserializer.deserialize(getRedisService().get(cacheKeyIP), new TypeReference<Map<String, String>>() {
					})
					: null;

			if (limitSearchCallCache != null) {
				int totalSearchCount = Integer.parseInt(limitSearchCallCache.get(SEARCH_SEARCH_COUNT));
				long lastAccessTime = Long.parseLong(limitSearchCallCache.get(SEARCH_LAST_ACCESSTIME));

				if ((System.currentTimeMillis() - lastAccessTime < 900000) && totalSearchCount < 25000) {
					limitSearchCallCache.put("searchCount", (totalSearchCount + searchData.getSize()) + "");
					limitSearchCallCache.put("lastAccessTime", System.currentTimeMillis() + "");
					getRedisService().set(cacheKeyIP, JsonSerializer.serializeToJson(limitSearchCallCache, true), 15);
				} else {
					throw new AccessDeniedException("You have reached the search limit of 25000. Wait for 15 minutes");
				}
			} else {
				Map<String, String> userCallerInfo = new HashMap<String, String>();
				userCallerInfo.put("searchCount", searchData.getSize() + "");
				userCallerInfo.put("lastAccessTime", System.currentTimeMillis() + "");
				getRedisService().set(cacheKeyIP, JsonSerializer.serializeToJson(userCallerInfo, true), 15);
			}
		}
	}

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.LimitValidation;
	}

	public void setRedisService(RedisClient redisService) {
		this.redisService = redisService;
	}

	public RedisClient getRedisService() {
		return redisService;
	}

}
