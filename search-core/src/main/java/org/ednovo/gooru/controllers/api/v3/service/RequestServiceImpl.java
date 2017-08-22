package org.ednovo.gooru.controllers.api.v3.service;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.ednovo.gooru.search.es.constant.Constants;
import org.ednovo.gooru.search.es.constant.RequestContextFields;
import org.ednovo.gooru.search.es.exception.BadRequestException;
import org.ednovo.gooru.suggest.v3.model.SuggestContextData;
import org.ednovo.gooru.suggest.v3.model.SuggestData;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class RequestServiceImpl implements RequestService, Constants{

	@Override
	public void processContextPayload(JSONObject requestContext, SuggestContextData suggestContext, SuggestData suggestData) throws Exception {
		Boolean isValidRequest = false;
		if (requestContext.has(RequestContextFields.CONTEXT)) {
			JSONObject context = requestContext.getJSONObject(RequestContextFields.CONTEXT);
			if (context != null && context.has(RequestContextFields.CONTEXT_TYPE) && context.has(RequestContextFields.USER_ID)) {
				suggestContext.setContextType(context.getString(RequestContextFields.CONTEXT_TYPE));
				suggestContext.setUserId(context.getString(RequestContextFields.USER_ID));
				if (context.has(RequestContextFields.COLLECTION_ID)) suggestContext.setCollectionId(context.getString(RequestContextFields.COLLECTION_ID));
				if (context.has(RequestContextFields.COLLECTION_TYPE)) suggestContext.setCollectionType(context.getString(RequestContextFields.COLLECTION_TYPE));
				if (context.has(RequestContextFields.COLLECTION_SUBTYPE)) suggestContext.setCollectionSubType(context.getString(RequestContextFields.COLLECTION_SUBTYPE));
				if (context.has(RequestContextFields.COURSE_ID)) suggestContext.setCourseId(context.getString(RequestContextFields.COURSE_ID));
				if (context.has(RequestContextFields.UNIT_ID)) suggestContext.setUnitId(context.getString(RequestContextFields.UNIT_ID));
				if (context.has(RequestContextFields.LESSON_ID)) suggestContext.setLessonId(context.getString(RequestContextFields.LESSON_ID));
				if (context.has(RequestContextFields.CURRENT_ITEM_ID)) suggestContext.setCurrentItemId(context.getString(RequestContextFields.CURRENT_ITEM_ID));
				if (context.has(RequestContextFields.CURRENT_ITEM_TYPE)) suggestContext.setCurrentItemType(context.getString(RequestContextFields.CURRENT_ITEM_TYPE));
				if (context.has(RequestContextFields.RESOURCE_ID)) suggestContext.setResourceId(context.getString(RequestContextFields.RESOURCE_ID));

				if (context.has(RequestContextFields.CONTEXT_AREA) && context.getString(RequestContextFields.CONTEXT_AREA).equalsIgnoreCase(STUDY_PLAYER)) {
					suggestContext.setContextArea(context.getString(RequestContextFields.CONTEXT_AREA));
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
		} else {
			isValidRequest = false;
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
	
	@Override
	public void processCodeContextPayload(JSONObject requestContext, SuggestContextData suggestContext, SuggestData suggestData) throws Exception {
		Boolean isValidRequest = false;
		if (requestContext.has(RequestContextFields.CONTEXT)) {
			JSONObject context = requestContext.getJSONObject(RequestContextFields.CONTEXT);
			if (context != null ) {
				suggestContext.setUserId(context.getString(RequestContextFields.USER_ID));
				if (context.has(RequestContextFields.CODES)) { 
					suggestContext.setCodes(Arrays.asList(context.getString(RequestContextFields.CODES).split(",")));
					isValidRequest = true;
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
