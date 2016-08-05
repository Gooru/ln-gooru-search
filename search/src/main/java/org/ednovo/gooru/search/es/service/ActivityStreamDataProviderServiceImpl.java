package org.ednovo.gooru.search.es.service;

import java.util.List;
import java.util.Map;

import org.ednovo.gooru.search.es.model.SuggestData;
import org.ednovo.gooru.search.model.ActivityStreamRawData;
import org.ednovo.gooru.search.model.UserActivityDataProviderCriteria;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ActivityStreamDataProviderServiceImpl implements ActivityStreamDataProviderService{
	
	private static final Logger logger = LoggerFactory.getLogger(ActivityStreamDataProviderServiceImpl.class);
		
	private ClientResource clientResource = null;

	private Representation representation = null;
	
	private JsonRepresentation jsonRepresentation;
		
	
	@Override
	public List<ActivityStreamRawData> getActivityStreamData(UserActivityDataProviderCriteria userActivityDataProviderCriteria , SuggestData suggestData) {/*
		List<ActivityStreamRawData> activityDataList = new ArrayList<ActivityStreamRawData>();
		String uri = settingService.getConfigSetting(Constants.INSIGHT_QUERY_URI, TaxonomyUtil.GOORU_ORG_UID) +suggestData.getSessionToken()+"&data=" ;
		String parameter = settingService.getConfigSetting(Constants.INSIGHT_QUERY_URI_RAWDATA, TaxonomyUtil.GOORU_ORG_UID);
		if(userActivityDataProviderCriteria.getEventName()!= null){
			parameter = parameter.replace(Constants.EVENT,userActivityDataProviderCriteria.getEventName());
		}
		if(userActivityDataProviderCriteria.getUserUid() != null){
			parameter = parameter.replace(Constants.USERID,userActivityDataProviderCriteria.getUserUid());
		}
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
		Calendar cal = Calendar.getInstance();
		parameter = parameter.replace(Constants.DATE1,dateFormat.format(cal.getTime()).toString());
		cal.add(Calendar.HOUR, -1);
		parameter = parameter.replace(Constants.DATE2,dateFormat.format(cal.getTime()).toString());
		try {
			parameter = URLEncoder.encode(parameter, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			
			e1.printStackTrace();
		}
	
		try {
			clientResource = new ClientResource(uri+parameter);
			if (clientResource.getStatus().isSuccess()) {
				representation = clientResource.get();
				jsonRepresentation = new JsonRepresentation(representation.getText());
				if(jsonRepresentation != null && jsonRepresentation.getJsonObject() != null && jsonRepresentation.getJsonObject().get("content") instanceof JSONArray){
					try {
							JSONArray activityArray = jsonRepresentation.getJsonObject().getJSONArray("content");
						    for(int index = 0; index < activityArray.length(); index++){
						    	ActivityStreamRawData activityStreamDo = new ActivityStreamRawData();
                                JSONObject activityJsonObject = activityArray.getJSONObject(index);	 
                                if(activityJsonObject != null){
	                                String eventName = activityJsonObject.get("eventName").toString();
	                                
	                                //FIXME add required validations when classpage events required
	                                if(eventName.equalsIgnoreCase("collection-play-dots")){
                                		activityStreamDo.setCollectionGooruOid(activityJsonObject.get("gooruOid").toString());
                                	} else if (eventName.equalsIgnoreCase("collection.play")){
                                		activityStreamDo.setCollectionGooruOid(activityJsonObject.get("gooruOid").toString());
                                	} else if (StringUtils.trimToNull(activityJsonObject.get("gooruOid").toString()) == null && StringUtils.trimToNull(activityJsonObject.get("parentGooruId").toString()) == null){
                                		continue;                                	
                                	} else {
                                		activityStreamDo.setCollectionGooruOid(activityJsonObject.get("parentGooruId").toString());
                                		activityStreamDo.setResourceGooruOid(activityJsonObject.get("parentGooruId").toString());
                                	}
	                                
	                                activityStreamDo.setUserUid(activityJsonObject.get("gooruUId").toString());
	                                activityStreamDo.setUsername(activityJsonObject.get("userName").toString());
	                                activityStreamDo.setEventName(eventName);
	                                
	                                if(!activityJsonObject.isNull("score") && activityJsonObject.get("score") != null && StringUtils.trimToNull(activityJsonObject.get("score").toString()) != null){
	                                	activityStreamDo.setFirstAttemptStatus(activityJsonObject.get("score").toString());
	                                }
	                                if(!activityJsonObject.isNull("sessionId") && activityJsonObject.get("sessionId") != null && StringUtils.trimToNull(activityJsonObject.get("sessionId").toString()) != null){
	                                	activityStreamDo.setSessionId(activityJsonObject.get("sessionId").toString());
	                                }
	                                if(!activityJsonObject.isNull("attemptStatus") && activityJsonObject.get("attemptStatus") != null && StringUtils.trimToNull(activityJsonObject.get("answerStatus").toString()) != null){
	                                	String answerStatus = activityJsonObject.get("attemptStatus").toString();
	                                	String firstAttemptstatus = activityJsonObject.getJSONArray("attemptStatus").toString(0);
	                                	activityStreamDo.setFirstAttemptStatus(firstAttemptstatus);

                              	activityStreamDo.setAnswerStatus(answerStatus);
	                                }
	                                
                                }

                                activityDataList.add(activityStreamDo);
                            }						    
					} catch (JSONException e) {
						logger.error("JSON Exception in User's Activity Stream : ", e);
					}
				}			 
			}
		}
		 catch (Exception exception) {
				logger.error("Error in getting User's Activity Stream: ", exception);
		} finally {
			try {
				if (clientResource != null) {
					clientResource.release();
				}
				if (representation != null) {
					representation.release();
				}
				clientResource = null;
				representation = null;
			} catch (Exception e) {
				logger.error(e.getMessage());
			}			
		}
		return activityDataList;	
	*/
		return null;}

	public List<Map<String, Object>> getUserActivityStream(String userUid, String eventName, Integer minutesToRead, Integer eventsToRead){/*
		
		List<Map<String, Object>> activity = new ArrayList<Map<String, Object>>();
		String uri = settingService.getConfigSetting(ConfigConstants.INSIGHTS_ACTIVITYSTREAM_URL, TaxonomyUtil.GOORU_ORG_UID) + "?userUid=" + userUid;
		if (eventName != null) {
			uri += "&eventName=" + eventName;
		}
		if (minutesToRead != null) {
			uri += "&minutesToRead=" + minutesToRead;
		}
		if (eventsToRead != null) {
			uri += "&eventsToRead=" + eventsToRead;
		}
		try {
			clientResource = new ClientResource(uri);
			if (clientResource.getStatus().isSuccess()) {
				representation = clientResource.get();
				jsonRepresentation = new JsonRepresentation(representation.getText());
				if (jsonRepresentation != null) {
					try {
						JSONArray activityArray = jsonRepresentation.getJsonObject().getJSONArray("activity");
						for (int index = 0; index < activityArray.length(); index++) {
							org.json.JSONObject activityJsonObject = activityArray.getJSONObject(index);
							if (activityJsonObject != null) {
								Map<String, Object> activityMap = new HashMap<String, Object>();
								String userId = activityJsonObject.get("userUid").toString();
								String username = activityJsonObject.get("username").toString();
								String profileImageUrl = settingService.getConfigSetting(ConfigConstants.PROFILE_IMAGE_URL, 0, TaxonomyUtil.GOORU_ORG_UID) + '/' + settingService.getConfigSetting(ConfigConstants.PROFILE_BUCKET, 0, TaxonomyUtil.GOORU_ORG_UID).toString() + activityJsonObject.get("userUid").toString() + DOT_PNG;
								String event = activityJsonObject.get("eventName").toString();
								String parentId = null;
								String resourceId = null;
								String collectionTitle = null;
								String collectionThumbnailUrl = null;
								String resourceTitle = null;
								String score = null;
								String sessionId = null;
								String firstAttemptStatus = null;
								String answerStatus = null;
								
								String collectionCourse = null;
								String collectionUnit = null;
								String collectionTopic = null;
								String collectionLesson = null;
								String collectionSubject = null;

								String resourceCourse = null;
								String resourceUnit = null;
								String resourceTopic = null;
								String resourceLesson = null;
								String resourceSubject = null;

								
                                //FIXME add required validations when classpage events required
								if(event.equalsIgnoreCase("collection-play-dots")){
                            		parentId = activityJsonObject.get("parentId").toString();
                            	} else if (event.equalsIgnoreCase("collection.play")){
                            		parentId = activityJsonObject.get("resourceId").toString();
                            	} else if (StringUtils.trimToNull(activityJsonObject.get("resourceId").toString()) == null && StringUtils.trimToNull(activityJsonObject.get("parentId").toString()) == null){
                            		continue;                                	
                            	} else {
                            		parentId = StringUtils.trimToNull((String) activityJsonObject.get("parentId")) != null ? activityJsonObject.get("parentId").toString() : null;
                            		resourceId = StringUtils.trimToNull((String) activityJsonObject.get("resourceId")) != null ? activityJsonObject.get("resourceId").toString() : null;
                            	}
								if(parentId != null){
									List<String> collectionIds = new ArrayList<String>();
									collectionIds.add(parentId);
									List<Collection> collectionList = collectionRepository.getCollectionListByIds(collectionIds);
									if (collectionList != null) {
										for (Collection collection : collectionList) {
											collectionTitle = collection.getTitle();
										 //   collectionThumbnailUrl = collection.getThumbnails().get("url");
											collectionCourse = !this.getLabel(collection.getTaxonomySet(), 2).isEmpty() ? this.getLabel(collection.getTaxonomySet(), 2).toString() : null;
											collectionUnit = !this.getLabel(collection.getTaxonomySet(), 3).isEmpty() ? this.getLabel(collection.getTaxonomySet(), 3).toString() : null;
											collectionTopic = !this.getLabel(collection.getTaxonomySet(), 4).isEmpty() ? this.getLabel(collection.getTaxonomySet(), 4).toString() : null;
											collectionLesson = !this.getLabel(collection.getTaxonomySet(), 5).isEmpty() ? this.getLabel(collection.getTaxonomySet(), 5).toString() : null;
											collectionSubject = !this.getLabel(collection.getTaxonomySet(), 1).isEmpty() ? this.getLabel(collection.getTaxonomySet(), 1).toString() : null;
											
											for(CollectionItem collectionItem : collection.getCollectionItems()){
												if(resourceId != null && collectionItem.getResource().getGooruOid().equalsIgnoreCase(resourceId)){
													resourceTitle = collectionItem.getResource().getTitle();
													resourceCourse = !this.getLabel(collectionItem.getResource().getTaxonomySet(), 2).isEmpty() ? this.getLabel(collectionItem.getResource().getTaxonomySet(), 2).toString() : null;
													resourceUnit = !this.getLabel(collectionItem.getResource().getTaxonomySet(), 3).isEmpty() ? this.getLabel(collectionItem.getResource().getTaxonomySet(), 3).toString() : null;
													resourceTopic = !this.getLabel(collectionItem.getResource().getTaxonomySet(), 4).isEmpty() ? this.getLabel(collectionItem.getResource().getTaxonomySet(), 4).toString() : null;
													resourceLesson = !this.getLabel(collectionItem.getResource().getTaxonomySet(), 5).isEmpty() ? this.getLabel(collectionItem.getResource().getTaxonomySet(), 5).toString() : null;
													resourceSubject = !this.getLabel(collectionItem.getResource().getTaxonomySet(), 1).isEmpty() ? this.getLabel(collectionItem.getResource().getTaxonomySet(), 1).toString() : null;
												}
											}
										}
									}
									
									if (!activityJsonObject.isNull("score") && activityJsonObject.get("score") != null && StringUtils.trimToNull(activityJsonObject.get("score").toString()) != null) {
										score = activityJsonObject.get("score").toString();
									}
									if (!activityJsonObject.isNull("sessionId") && activityJsonObject.get("sessionId") != null && StringUtils.trimToNull(activityJsonObject.get("sessionId").toString()) != null) {
										sessionId = StringUtils.trimToNull(activityJsonObject.get("sessionId").toString()) != null ? activityJsonObject.get("sessionId").toString() : "NA";
									}
									if (!activityJsonObject.isNull("firstAttemptStatus") && activityJsonObject.get("firstAttemptStatus") != null
											&& StringUtils.trimToNull(activityJsonObject.get("firstAttemptStatus").toString()) != null) {
										firstAttemptStatus = activityJsonObject.get("firstAttemptStatus").toString();
									}
									if (!activityJsonObject.isNull("answerStatus") && activityJsonObject.get("answerStatus") != null && StringUtils.trimToNull(activityJsonObject.get("answerStatus").toString()) != null) {
										answerStatus = activityJsonObject.get("answerStatus").toString();
									}
								}

								activityMap.put("userUid", userId);
								activityMap.put("username", username);
								activityMap.put("eventName", event);
								
								if(event.contains("play") && event.contains("resource")){
									activityMap.put("target", "resource");
									if((score != null || answerStatus != null)){
										activityMap.put("action", "attempted");
										activityMap.put("target", "question");
									} else {
										activityMap.put("action", "played");
									}
								} else if(event.contains("play") && !event.contains("resource")){
									activityMap.put("target", "collection");
									if((score != null || answerStatus != null)){
										activityMap.put("action", "attempted");
										activityMap.put("target", "quiz");
									} else {
										activityMap.put("action", "played");
									}									
								} else if (event.contains("update") || event.contains("edit")) {
									activityMap.put("action", "edited");
								} else if (event.contains("delete")) {
									activityMap.put("action", "deleted");
								} 
								if (profileImageUrl != null){
									activityMap.put("userProfileImage", profileImageUrl);
								}
								activityMap.put("collectionGooruOid", parentId);
								activityMap.put("resourceGooruOid", resourceId);
								activityMap.put("collectionTitle", collectionTitle);
								activityMap.put("collectionThumbnailUrl", collectionThumbnailUrl);
								activityMap.put("resourceTitle", resourceTitle);
								activityMap.put("score", score);
								activityMap.put("sessionId", sessionId);
								activityMap.put("firstAttemptStatus", firstAttemptStatus);
								activityMap.put("answerStatus", answerStatus);

								activityMap.put("collectionCourse", collectionCourse);
								activityMap.put("collectionUnit", collectionUnit);
								activityMap.put("collectionTopic", collectionTopic);
								activityMap.put("collectionLesson", collectionLesson);
								activityMap.put("collectionSubject", collectionSubject);

								activityMap.put("resourceCourse", resourceCourse);
								activityMap.put("resourceUnit", resourceUnit);
								activityMap.put("resourceTopic", resourceTopic);
								activityMap.put("resourceLesson", resourceLesson);
								activityMap.put("resourceSubject", resourceSubject);
	
								activity.add(activityMap);		
							}
						}
						return activity;										
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			try {
				if (clientResource != null) {
					clientResource.release();
				}
				if (representation != null) {
					representation.release();
				}
				clientResource = null;
				representation = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return activity;
	*/
		return null;}
	
	/*private Set<String> getLabel(Set<Code> taxonomySet, int depth) {
		Set<String> labels = null;
		
		if (taxonomySet != null) {
			labels = new HashSet<String>();
			for (Code code : taxonomySet) {
				if (code.getDepth() == depth) {
					labels.add(code.getLabel().trim());
				}
			}
		}
		return labels;
	}*/
	
}
