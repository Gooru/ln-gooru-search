package org.ednovo.gooru.search.es.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.ednovo.gooru.search.domain.service.CollectionSearchResult;
import org.ednovo.gooru.search.es.constant.EsIndex;
import org.ednovo.gooru.search.es.exception.SearchException;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.ednovo.gooru.search.es.model.SuggestData;
import org.ednovo.gooru.search.es.model.SuggestResponse;
import org.ednovo.gooru.search.es.processor.ElasticsearchProcessor;
import org.ednovo.gooru.search.es.processor.deserializer.SCollectionDeserializeProcessor;
import org.ednovo.gooru.search.es.processor.query_builder.EsDslQueryBuildProcessor;
import org.ednovo.gooru.search.model.ActivityStreamRawData;
import org.ednovo.gooru.search.model.CollectionContextData;
import org.ednovo.gooru.search.model.ResourceContextData;
import org.ednovo.gooru.search.model.ResourceUsageData;
import org.ednovo.gooru.search.model.UserPreferenceData;
import org.ednovo.gooru.search.model.UserProficiencyData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SCollectionSuggestHandler extends SuggestV2Handler<Map<String, Object>> {
	private static Integer threadPoolLength = 3;

	private ExecutorService doerService;

	@Autowired
	private ElasticsearchProcessor elasticSearchProcessor;

	@Autowired
	private EsDslQueryBuildProcessor esDslQueryBuildProcessor;

	@Autowired
	private SCollectionDeserializeProcessor scollectionDeserializeProcessor;

	private static final String QUIZ_SUMMARY = "quiz-summary";

	private static final String COLLECTION_PLAY = "collection-play";

	private static final String SCOLLECTION_PLAY = "scollection-play";
	
	private static final String SCOLLECTIONPLAY = "collection.play";
	
	private static final String SCOLLECTION_RESOURCE_PLAY = "collection-resource-play-dots";
	
	private static final String SCOLLECTIONRESOURCEPLAY = "collection.resource.play";
	
	private static final String  RESOURCE_PLAY = "resource-play";

	private static final String WRONG_STATUS = "wrong";

	private static final String CORRECT_STATUS = "correct";
	
	private static final String SIMILAR_CONTENT = "similar-content";
	
	private static final String STUDENT_SUGGESTION = "student-suggestion";	

	private static final Long MIN_LONG = Long.MIN_VALUE;
	private static final Long MAX_LONG = Long.MAX_VALUE;
	
	private final String NO_QUESTION = "noQuestion";
	private final String ONLY_QUESTION = "onlyQuestion";
	private final String SOME_QUESTION = "someQuestion";

	private SuggestDataProviderType[] suggestDataProviders = { SuggestDataProviderType.RESOURCE, SuggestDataProviderType.SCOLLECTION, SuggestDataProviderType.USER_PREFERENCE, SuggestDataProviderType.USER_PROFICIENCY, SuggestDataProviderType.USER_ACTIVITY, SuggestDataProviderType.USER_RESOURCE_PERFORMANCE };

	@Override
	public SuggestResponse<Object> suggest(final SuggestData suggestData, final Map<SuggestDataProviderType, Object> dataProviderInput) {
		final SearchResponse<Object> searchResponse = new SearchResponse<Object>();
		final SuggestResponse<Object> suggestResponse = new SuggestResponse<Object>();
		final SearchResponse<List<CollectionSearchResult>> searchCollectionResult = new SearchResponse<List<CollectionSearchResult>>();
		final String context = suggestData.getSuggestContext().getEvent();
		final String searchTerm = suggestData.getSuggestContext().getSearchTerm();
		final StringBuilder conceptsBuilder = new StringBuilder();

		if (doerService == null) {
			doerService = Executors.newFixedThreadPool(threadPoolLength);
		}
		List<Callable<SuggestResponse<Object>>> tasks = new ArrayList<Callable<SuggestResponse<Object>>>();
		tasks.add(new Callable<SuggestResponse<Object>>() {
			@Override
			public SuggestResponse<Object> call() throws Exception {
				String queryString = "*";
				for (SuggestDataProviderType suggestDataProviderType : suggestDataProviderTypes()) {
					if (suggestDataProviderType != null && suggestDataProviderType == SuggestDataProviderType.SCOLLECTION && dataProviderInput.get(SuggestDataProviderType.SCOLLECTION) != null) {
						final CollectionContextData scollectionData = (CollectionContextData) dataProviderInput.get(SuggestDataProviderType.SCOLLECTION);
						final Map<String, ResourceUsageData>  resourceUsageData =   (Map<String, ResourceUsageData>) dataProviderInput.get(SuggestDataProviderType.USER_RESOURCE_PERFORMANCE);
						final List<ActivityStreamRawData> activityList = (List<ActivityStreamRawData>) dataProviderInput.get(SuggestDataProviderType.USER_ACTIVITY);
						Map<String, String> resourceAnswerStatus = new HashMap<String, String>();
						Map<String, Map<String, String>> quizAnswerStatus = new HashMap<String, Map<String, String>>();
						
						if (context.equalsIgnoreCase(COLLECTION_PLAY)) {
							if (searchTerm != null && StringUtils.trimToNull(searchTerm) != null) {
								queryString = searchTerm.trim();
							} else if (scollectionData != null && StringUtils.trimToNull(scollectionData.getCollectionTitle()) != null) {
								queryString = scollectionData.getCollectionTitle().trim();
							}
							if (StringUtils.trimToNull(suggestData.getSuggestContext().getUserUid()) != null) {
								suggestData.putFilter("!^owner.gooruUId", suggestData.getSuggestContext().getUserUid());
								suggestData.putFilter("!^creator.gooruUId", suggestData.getSuggestContext().getUserUid());
							}
							suggestData.putFilter("^category", "noQuestion");
							//addTaxonomyFilters(suggestData, scollectionData, quizAnswerStatus);
							if (scollectionData.getCollectionTaxonomyCourseId() != null && scollectionData.getCollectionTaxonomyCourseId().size() > 0) {
								suggestData.putFilter("&^taxonomyV2.course.codeId", StringUtils.join(scollectionData.getCollectionTaxonomyCourseId(), ","));
							} else if (scollectionData.getCollectionTaxonomySubjectId() != null && scollectionData.getCollectionTaxonomySubjectId().size() > 0) {
								suggestData.putFilter("&^taxonomyV2.subject.codeId", StringUtils.join(scollectionData.getCollectionTaxonomySubjectId(), ","));
							} else if (scollectionData.getCollectionStandards() != null && scollectionData.getCollectionStandards().size() > 0) {
								suggestData.putFilter("&taxonomyV2.standards", StringUtils.join(scollectionData.getCollectionStandards(), ","));
							}
						}
						
						
						if (context.equalsIgnoreCase(QUIZ_SUMMARY)) {
								if (scollectionData != null) {
									if((activityList != null && activityList.size() > 0)){
										for(ActivityStreamRawData activityData : activityList){
											if(StringUtils.trimToNull(activityData.getCollectionGooruOid()) != null && StringUtils.trimToNull(scollectionData.getCollectionGooruOid()) != null && activityData.getCollectionGooruOid().equals(scollectionData.getCollectionGooruOid()) && (activityData.getEventName().equalsIgnoreCase(SCOLLECTIONPLAY))){
												if(activityData.getSessionId() != null && StringUtils.trimToNull(activityData.getSessionId().toString()) != null){
													suggestData.getSuggestContext().setQuizSessionId(activityData.getSessionId());
												}
											}
											for (String resourceId : scollectionData.getCollectionResourceIds()){
												if(StringUtils.trimToNull(activityData.getResourceGooruOid()) != null && StringUtils.trimToNull(resourceId) != null && activityData.getResourceGooruOid().equals(resourceId) && (activityData.getEventName().equalsIgnoreCase(SCOLLECTIONRESOURCEPLAY))){
													resourceAnswerStatus.put(resourceId, activityData.getAnswerStatus());
												}
											}
										}
										quizAnswerStatus.put(scollectionData.getCollectionGooruOid(), resourceAnswerStatus);
									}
									if (scollectionData.getCollectionQuestionConcepts() != null){
										for (String questionConcept : scollectionData.getCollectionQuestionConcepts()) {
											if (conceptsBuilder.length() > 0 && questionConcept.trim() != null && !questionConcept.trim().equalsIgnoreCase("")) {
												conceptsBuilder.append(" OR " + questionConcept);
											}
											if (conceptsBuilder.length() == 0 && questionConcept.trim() != null && !questionConcept.trim().equalsIgnoreCase("")) {
												conceptsBuilder.append(questionConcept);
											}
										}
									}
									addTaxonomyFilters(suggestData, scollectionData, quizAnswerStatus);
							}
							if (conceptsBuilder.toString().trim() != null && !conceptsBuilder.toString().equalsIgnoreCase("")) {
								queryString = conceptsBuilder.toString();
							} else if (searchTerm != null && searchTerm.replaceAll("(?i)quiz:", "").trim() != null && !searchTerm.replaceAll("(?i)quiz:", "").trim().equalsIgnoreCase("")) {
								queryString = searchTerm.replaceAll("(?i)quiz:", "").trim();
							}
							suggestData.putFilter("&^collectionType", "collection");
							suggestData.putFilter("!^category", "onlyQuestion");
							suggestData.putFilter("!^gooruOId", suggestData.getSuggestContext().getContentGooruOid());
							
						}
						
						if (context.equalsIgnoreCase(SIMILAR_CONTENT)) {
							if(scollectionData != null){
								suggestData.putFilter("!^gooruOId", scollectionData.getCollectionGooruOid());
							}
							suggestData.putFilter("&^statistics.invalidResource", "0");
							suggestData.putFilter("&^statistics.statusIsBroken", "0");
							suggestData.putFilter("&^sharing", "public");
							
							if (scollectionData != null) {
								
								if(suggestData.getSuggestContext() != null && StringUtils.trimToNull(suggestData.getSuggestContext().getSearchTerm()) != null){
									queryString = suggestData.getSuggestContext().getSearchTerm().trim();
								} else if (StringUtils.trimToNull(scollectionData.getCollectionTitle()) != null) {
									queryString = scollectionData.getCollectionTitle().trim();
								}

								if(suggestData.getSuggestContext() != null && StringUtils.trimToNull(suggestData.getSuggestContext().getCategory()) != null){
									suggestData.putFilter("&resourceFormat", suggestData.getSuggestContext().getCategory().trim());
								}

								if (scollectionData.getCollectionTaxonomyCourseId() != null && scollectionData.getCollectionTaxonomyCourseId().size() > 0) {
									suggestData.putFilter("&^taxonomyV2.course.codeId", StringUtils.join(scollectionData.getCollectionTaxonomyCourseId(), ","));
								} else if (scollectionData.getCollectionTaxonomySubjectId() != null && scollectionData.getCollectionTaxonomySubjectId().size() > 0) {
									suggestData.putFilter("&^taxonomyV2.subject.codeId", StringUtils.join(scollectionData.getCollectionTaxonomySubjectId(), ","));
								} else if (scollectionData.getCollectionStandards() != null && scollectionData.getCollectionStandards().size() > 0) {
									suggestData.putFilter("&taxonomyV2.standards", StringUtils.join(scollectionData.getCollectionStandards(), ","));
								}

							}
						}
						
						if (context.equalsIgnoreCase(STUDENT_SUGGESTION)) {
							if (scollectionData != null) {
								queryString = scollectionData.getCollectionTitle();
								suggestData.putFilter("!^gooruOId", scollectionData.getCollectionGooruOid());
								if (resourceUsageData != null && scollectionData.getCollectionResourceIds().size() > 0) {
									if (scollectionData.getCollectionCategory().equalsIgnoreCase(ONLY_QUESTION) || scollectionData.getCollectionCategory().equalsIgnoreCase(SOME_QUESTION)) {
										try {
											Long minTS = MAX_LONG;
											Long maxTS = MIN_LONG;
											Long lowReacMaxTS = MIN_LONG;
											String resourceMaximumTS = null;
											String resourceMinimumTS = null;
											String incorrectAnsweredQuestionResource = null;
											String questionSkippedLowReaction = null;
											String resourceLowReactionMaxTS = null;
											List<String> lowScoredQuestion = new ArrayList<String>();
											Map<String, ResourceUsageData> resourceUsageDataMap = new HashMap<String, ResourceUsageData>();
											
											for (String resourceGooruOid : scollectionData.getCollectionResourceIds()) {
												if (resourceUsageData.get(resourceGooruOid) != null) {
													ResourceUsageData resourceData = resourceUsageData.get(resourceGooruOid);
													Long avgTs = resourceData.getAvgTimeSpent();
													if (avgTs > maxTS) {
														maxTS = avgTs;
														resourceMaximumTS = resourceGooruOid + "~" + maxTS;
														resourceUsageDataMap.put(resourceMaximumTS, resourceData);
													}
													if (avgTs < minTS) {
														minTS = avgTs;
														resourceMinimumTS = resourceGooruOid + "~" + minTS;
														if (resourceMinimumTS != resourceMaximumTS) {
															resourceUsageDataMap.put(resourceMinimumTS, resourceData);
														}
													}
													if (resourceData.getQuestionType() != null) {
														if (resourceData.getAttempts() > 0) {
															if (resourceData.getAttemptStatus() == 0 && resourceData.getTotalInCorrectCount() > 0) {
																incorrectAnsweredQuestionResource = resourceGooruOid + "~" + resourceData.getScore();
																lowScoredQuestion.add(incorrectAnsweredQuestionResource);
															}
														} else if (resourceData.getAttempts() == 0 && resourceData.getSkip() != null && resourceData.getSkip() > 0) {
															if (resourceData.getAvgReaction() <= 3) {
																questionSkippedLowReaction = resourceGooruOid + "~" + resourceData.getAvgReaction();
																resourceUsageDataMap.put(questionSkippedLowReaction, resourceData);
															}
														}
													}
													if(resourceData.getAvgReaction() != null && resourceData.getAvgReaction() <= 3) {
														if(resourceData.getAvgTimeSpent() > lowReacMaxTS) {
															lowReacMaxTS = resourceData.getAvgTimeSpent();
															resourceLowReactionMaxTS = resourceGooruOid + "~" + lowReacMaxTS;
															resourceUsageDataMap.put(resourceLowReactionMaxTS, resourceData);
														}
													}

												}

												//disabled while removing core-api jar
												/*if(lowScoredQuestion != null && lowScoredQuestion.size() > 0) {
													String[] minScoreRes = lowScoredQuestion.get(0).split("~");
													Resource itemData = scollectionData.getCollectionItemData().get(minScoreRes[0]);
													queryString = itemData.getTitle();
												} else if (questionSkippedLowReaction != null && resourceUsageDataMap.get(questionSkippedLowReaction) != null) {
													String[] skippedRes = questionSkippedLowReaction.split("~");
													Resource itemData = scollectionData.getCollectionItemData().get(skippedRes[0]);
													queryString = itemData.getTitle();
												} else if (resourceMinimumTS != null && resourceUsageDataMap.get(resourceMinimumTS) != null) {
													String[] minTSRes = resourceMinimumTS.split("~");
													Resource itemData = scollectionData.getCollectionItemData().get(minTSRes[0]);
													queryString = itemData.getTitle();
												} else if (resourceLowReactionMaxTS != null) {
													String[] lowReactedResource = resourceLowReactionMaxTS.split("~");
													Resource itemData = scollectionData.getCollectionItemData().get(lowReactedResource[0]);
													queryString = itemData.getTitle();
												}*/
											}
										} catch (Exception e) {
											e.printStackTrace();
										}
									} else if (scollectionData.getCollectionCategory().equalsIgnoreCase(NO_QUESTION)) {
										Long minTS = MAX_LONG;
										Long maxTS = MIN_LONG;
										Long lowReacMaxTS = MIN_LONG;
										String resourceMaximumTS = null;
										String resourceMinimumTS = null;
										String resourceLowReactionMaxTS = null;
										Map<String, ResourceUsageData> resourceUsageDataMap = new HashMap<String, ResourceUsageData>();
										for (String resourceGooruOid : scollectionData.getCollectionResourceIds()) {
											if (resourceUsageData.get(resourceGooruOid) != null) {
												ResourceUsageData resourceData = resourceUsageData.get(resourceGooruOid);
												Long avgTs = resourceData.getAvgTimeSpent();
												if (avgTs > maxTS) {
													maxTS = avgTs;
													resourceMaximumTS = resourceGooruOid + "~" + maxTS;
													resourceUsageDataMap.put(resourceMaximumTS, resourceData);
												}
												if (avgTs < minTS) {
													minTS = avgTs;
													resourceMinimumTS = resourceGooruOid + "~" + minTS;
													if (resourceMinimumTS != resourceMaximumTS) {
														resourceUsageDataMap.put(resourceMinimumTS, resourceData);
													}
												}
												if(resourceData.getAvgReaction() != null && resourceData.getAvgReaction() <= 3) {
													if(resourceData.getAvgTimeSpent() > lowReacMaxTS) {
														lowReacMaxTS = resourceData.getAvgTimeSpent();
														resourceLowReactionMaxTS = resourceGooruOid + "~" + lowReacMaxTS;
														resourceUsageDataMap.put(resourceLowReactionMaxTS, resourceData);
													}
												}
											}
										}
										
									/*	
									  //Compute MAX and MIN after Midpoint of time spent
									  
									  	long midPoint = Math.round((maxTS + minTS)/2);
										int count = 0;
										List<Long> resourceLowTimeSpent = new ArrayList<Long>();
										List<String> resourcesTS = new ArrayList<String>();
										String nearMidPointResource = null;
										long MaxnearMidPointResource = MIN_LONG;
										long MinNearMidPointResource = midPoint;
										for (String resourceId : scollectionData.getCollectionResourceIds()) {
											ResourceUsageData resourceUsageForMidPoint = resourceUsageDataMap.get(resourceId);
											Long avgTs = resourceUsageForMidPoint.getAvgTimeSpent();
											if (avgTs > MaxnearMidPointResource) {
												MaxnearMidPointResource = avgTs;
												nearMidPointResource = resourceId + "~" + MaxnearMidPointResource;
												resourceUsageDataMap.put(nearMidPointResource, resourceUsageForMidPoint);
											}
											if (avgTs < MinNearMidPointResource) {
												MinNearMidPointResource = avgTs;
												resourceMinimumTS = resourceId + "~" + MinNearMidPointResource;
												if (resourceMinimumTS != resourceMaximumTS) {
													resourceUsageDataMap.put(resourceMinimumTS, resourceUsageForMidPoint);
												}
											}
											if (resourceUsageForMidPoint.getAvgTimeSpent() < midPoint) {
												count++;
												resourceLowTimeSpent.add(resourceUsageForMidPoint.getAvgTimeSpent());
											}
										}
										/*
										 * Minimum TimeSpent and Low Reacted Resource in average
										 
										ResourceUsageData computedMinResourceUsageData = resourceUsageDataMap.get(resourceMinimumTS);
										String[] minTSRes = resourceMinimumTS.split("~");
										if (computedMinResourceUsageData.getReaction() != null) {
											Resource itemData = scollectionData.getCollectionItemData().get(minTSRes[0]);
											if (computedMinResourceUsageData.getReaction() <= 3) {
												queryString = itemData.getTitle();
											}
										}
										*/
										
										/* 
										 * Maximum TimeSpent resource in low reacted resources in average
										 */
										//disabled while removing core-api jar
										/*if (resourceLowReactionMaxTS != null) {
											String[] lowReactedResource = resourceLowReactionMaxTS.split("~");
											Resource itemData = scollectionData.getCollectionItemData().get(lowReactedResource[0]);
											queryString = itemData.getTitle();
										} else if (resourceMinimumTS != null && resourceUsageDataMap.get(resourceMinimumTS) != null) {
											ResourceUsageData computedMinResourceUsageData = resourceUsageDataMap.get(resourceMinimumTS);
											String[] minTSRes = resourceMinimumTS.split("~");
											Resource itemData = scollectionData.getCollectionItemData().get(minTSRes[0]);
											queryString = itemData.getTitle();
										}	*/	
									}		
									
									if (scollectionData.getCollectionTaxonomyCourseId() != null && scollectionData.getCollectionTaxonomyCourseId().size() > 0) {
										suggestData.putFilter("&^taxonomyV2.course.codeId", StringUtils.join(scollectionData.getCollectionTaxonomyCourseId(), ","));
									} else if (scollectionData.getCollectionStandards() != null && scollectionData.getCollectionStandards().size() > 0) {
										suggestData.putFilter("&taxonomyV2.standards", StringUtils.join(scollectionData.getCollectionStandards(), ","));
									} else if (scollectionData.getCollectionTaxonomySubjectId() != null && scollectionData.getCollectionTaxonomySubjectId().size() > 0) {
										suggestData.putFilter("&^taxonomyV2.subject.codeId", StringUtils.join(scollectionData.getCollectionTaxonomySubjectId(), ","));
									}
								}								
							}
						}
						
						String[] queryWords = queryString.toLowerCase().split("[^a-zA-Z0-9\\']");
						StringBuilder queryStringBuilder = new StringBuilder();
						for (String word : queryWords) {
							StringBuilder wordsBuilder = new StringBuilder();
							if (wordsBuilder.length() == 0) {
								wordsBuilder.append(word);
							}
							if (queryStringBuilder.length() > 0  && word.trim().length() > 0) {
								queryStringBuilder.append(" AND ");
							}
							queryStringBuilder.append(wordsBuilder.toString());								
						}
						if(StringUtils.trimToNull(queryStringBuilder.toString()) != null){
							queryString = queryStringBuilder.toString();
						}
						suggestData.setQueryString(queryString);
					}

					if (suggestDataProviderType != null && suggestDataProviderType == SuggestDataProviderType.RESOURCE && dataProviderInput.get(SuggestDataProviderType.RESOURCE) != null) {
						if(context.equalsIgnoreCase(RESOURCE_PLAY)){
							suggestData.setQueryString(queryString);
							final ResourceContextData resourceContextData = (ResourceContextData) dataProviderInput.get(SuggestDataProviderType.RESOURCE);
							suggestData.putFilter("&^resourceGooruOIds.public", resourceContextData.getResourceGooruOid());
						}
					}
					
					if (suggestDataProviderType != null && suggestDataProviderType == SuggestDataProviderType.USER_PREFERENCE && dataProviderInput.get(SuggestDataProviderType.USER_PREFERENCE) != null) {
						final UserPreferenceData preferenceData = (UserPreferenceData) dataProviderInput.get(SuggestDataProviderType.USER_PREFERENCE);
						if (preferenceData != null) {
							if (preferenceData.getProfileGradeBoost() != null) {
								suggestData.getCustomFilters().add(preferenceData.getProfileGradeBoost());
							}
							if (preferenceData.getProfileSubjectBoost() != null) {
								suggestData.getCustomFilters().add(preferenceData.getProfileSubjectBoost());
							}
							if (preferenceData.getUserPreferredGradeBoost() != null && preferenceData.getUserPreferredGradeBoost().size() > 0) {
								suggestData.getCustomFilters().addAll(preferenceData.getUserPreferredGradeBoost());
							}
							if (preferenceData.getUserPreferredResourceCategoryBoost() != null && preferenceData.getUserPreferredResourceCategoryBoost().size() > 0) {
								suggestData.getCustomFilters().addAll(preferenceData.getUserPreferredResourceCategoryBoost());
							}
							if (preferenceData.getUserPreferredSubjectBoost() != null && preferenceData.getUserPreferredSubjectBoost().size() > 0) {
								suggestData.getCustomFilters().addAll(preferenceData.getUserPreferredSubjectBoost());
							}
						}
					}
					if (suggestDataProviderType != null && suggestDataProviderType == SuggestDataProviderType.USER_PROFICIENCY
							&& dataProviderInput.get(SuggestDataProviderType.USER_PROFICIENCY) != null && !context.equalsIgnoreCase(QUIZ_SUMMARY) && !context.equalsIgnoreCase(SIMILAR_CONTENT)) {
						final UserProficiencyData proficiencyData = (UserProficiencyData) dataProviderInput.get(SuggestDataProviderType.USER_PROFICIENCY);
						if (proficiencyData != null) {
							if (proficiencyData.getUserProficiencySubjectBoost() != null && proficiencyData.getUserProficiencySubjectBoost().size() > 0) {
								suggestData.getCustomFilters().addAll(proficiencyData.getUserProficiencySubjectBoost());
							}
							if (proficiencyData.getUserProficiencyCourseBoost() != null && proficiencyData.getUserProficiencyCourseBoost().size() > 0) {
								suggestData.getCustomFilters().addAll(proficiencyData.getUserProficiencyCourseBoost());
							}
							if (proficiencyData.getUserProficiencyUnitBoost() != null && proficiencyData.getUserProficiencyUnitBoost().size() > 0) {
								suggestData.getCustomFilters().addAll(proficiencyData.getUserProficiencyUnitBoost());
							}
							if (proficiencyData.getUserProficiencyConceptBoost() != null && proficiencyData.getUserProficiencyConceptBoost().size() > 0) {
								suggestData.getCustomFilters().addAll(proficiencyData.getUserProficiencyConceptBoost());
							}
							if (proficiencyData.getUserProficiencyLessonBoost() != null && proficiencyData.getUserProficiencyLessonBoost().size() > 0) {
								suggestData.getCustomFilters().addAll(proficiencyData.getUserProficiencyLessonBoost());
							}
							if (proficiencyData.getUserProficiencyTopicBoost() != null && proficiencyData.getUserProficiencyTopicBoost().size() > 0) {
								suggestData.getCustomFilters().addAll(proficiencyData.getUserProficiencyTopicBoost());
							}
						}
					}
					
				}
				suggestData.setIndexType(getIndexType());
				suggestData.setType(getName());
				esDslQueryBuildProcessor.process(suggestData, searchResponse);
				elasticSearchProcessor.process(suggestData, searchResponse);
				scollectionDeserializeProcessor.process(suggestData, searchCollectionResult);
				suggestResponse.setSuggestResults(searchCollectionResult.getSearchResults());
				return suggestResponse;
			}

			//disabled while removing core-api jar
			private void addTaxonomyFilters(SuggestData suggestData, CollectionContextData scollectionData, Map<String, Map<String, String>> questionAnswerStatus) {/*
				if (scollectionData != null) {
					String subjectCodeLevel = "taxonomy.subject.codeId";
					String courseCodeLevel = "taxonomy.course.codeId";
					String unitCodeLevel = "taxonomy.unit.codeId";
					String topicCodeLevel = "taxonomy.topic.codeId";
					String lessonCodeLevel = "taxonomy.lesson.codeId";
					String standardLevel = "taxonomy.standards";

					Set<Object> collectionSubjectCodeIds = new HashSet<Object>();
					Set<Object> collectionCourseCodeIds = new HashSet<Object>();
					Set<Object> collectionUnitCodeIds = new HashSet<Object>();
					Set<Object> collectionTopicCodeIds = new HashSet<Object>();
					Set<Object> collectionLessonCodeIds = new HashSet<Object>();

					Set<Object> resourceSubjectCodeIds = new HashSet<Object>();
					Set<Object> resourceCourseCodeIds = new HashSet<Object>();
					Set<Object> resourceUnitCodeIds = new HashSet<Object>();
					Set<Object> resourceTopicCodeIds = new HashSet<Object>();
					Set<Object> resourceLessonCodeIds = new HashSet<Object>();
					Set<Object> resourceStandards = new HashSet<Object>();

					Map<String, String> taxonomyIndexLevels = new HashMap<String, String>();
					Map<String, Set<Object>> collectionCodeIds = new HashMap<String, Set<Object>>();
					Map<String, Set<Object>> resourceCodeIds = new HashMap<String, Set<Object>>();
					Map<String, Set<Object>> resourceTaxonomy = new HashMap<String, Set<Object>>();
					Map<String, Set<Object>> collectionTaxonomy = new HashMap<String, Set<Object>>();
					Set<Object> customFilters = new HashSet<Object>();

					collectionCodeIds.put("subject", collectionSubjectCodeIds);
					collectionCodeIds.put("course", collectionCourseCodeIds);
					collectionCodeIds.put("unit", collectionUnitCodeIds);
					collectionCodeIds.put("topic", collectionTopicCodeIds);
					collectionCodeIds.put("lesson", collectionLessonCodeIds);

					resourceCodeIds.put("subject", resourceSubjectCodeIds);
					resourceCodeIds.put("course", resourceCourseCodeIds);
					resourceCodeIds.put("unit", resourceUnitCodeIds);
					resourceCodeIds.put("topic", resourceTopicCodeIds);
					resourceCodeIds.put("lesson", resourceLessonCodeIds);
					resourceCodeIds.put("standards", resourceStandards);

					taxonomyIndexLevels.put("subject", subjectCodeLevel);
					taxonomyIndexLevels.put("course", courseCodeLevel);
					taxonomyIndexLevels.put("unit", unitCodeLevel);
					taxonomyIndexLevels.put("topic", topicCodeLevel);
					taxonomyIndexLevels.put("lesson", lessonCodeLevel);
					taxonomyIndexLevels.put("standard", standardLevel);

					Boolean addStandardFilter = false;
					Boolean hasStandard = false;
					Boolean boosterIsRequired = false;
					double taxonomyBoost = 0;

					String context = suggestData.getSuggestContext().getEvent();
					Long collectionId = ((scollectionData.getCollectionContentId() != null && scollectionData.getCollectionContentId() != 0) ? scollectionData.getCollectionContentId() : null);
					if (scollectionData.getCollectionTaxonomy() != null && scollectionData.getCollectionTaxonomy().size() > 0) {
						Set<Code> collectionTaxonomySet = scollectionData.getCollectionTaxonomy();
						for (Code collectionCode : collectionTaxonomySet) {
							if (collectionCode.getActiveFlag() == 1) {
								Code code = taxonomyRepository.findTaxonomyCodeById(collectionCode.getCodeId());
								if (code.getCodeId() != null && code.getRootNodeId() == 20000) {
									addTaxonomy(code, collectionTaxonomy, collectionCodeIds, taxonomyIndexLevels, customFilters, hasStandard, boosterIsRequired, taxonomyBoost);
								}
							}
						}
						if (context.equalsIgnoreCase(COLLECTION_PLAY)) {
							addTaxonomyFilter(collectionTaxonomy, taxonomyIndexLevels, false, addStandardFilter);
						}

						if (context.equalsIgnoreCase(QUIZ_SUMMARY)) {
							Map<String, Object> quizSummary = new HashMap<String, Object>();
							String questionStatus = null;
							//quizSummary = sessionRepository.getQuizSummary(suggestData.getSuggestContext().getQuizSessionId(), 1, "1, 2", scollectionData.getCollectionContentId());
							double quizAverageScore = Double.parseDouble(quizSummary.get("attemptedQuestionCount").toString()) != 0 ? Double.parseDouble(new DecimalFormat("##.#").format(Double
									.parseDouble(quizSummary.get("correctAnswerCount").toString()) / Double.parseDouble(quizSummary.get("attemptedQuestionCount").toString()))) : 0.0;
							
							 * if (quizAverageScore <= 0.8) {
							 * addTaxonomyFilter(collectionTaxonomy); }
							 
							Set<Entry<String, Set<Code>>> resourcesTaxonomySet = scollectionData.getResourcesTaxonomy().entrySet();
							for (Entry<String, Set<Code>> entry : resourcesTaxonomySet) {
								String resourceGooruOid = entry.getKey();
								Set<Code> resourceTaxonomySet = entry.getValue();
								if(questionAnswerStatus != null && scollectionData.getCollectionResourceIds().size() == questionAnswerStatus.size()){
									Map<String, String> answerStatus = questionAnswerStatus.get(suggestData.getSuggestContext().getContentGooruOid());
									questionStatus = answerStatus.get(resourceGooruOid);
								} else if (suggestData.getSuggestContext().getQuizSessionId() != null && resourceGooruOid != null) {

									//questionStatus = sessionRepository.getQuestionStatus(suggestData.getSuggestContext().getQuizSessionId(), 1, "1, 2", collectionId, resourceGooruOid);
		}
								for (Code resourceCode : resourceTaxonomySet) {
									if (questionStatus != null && questionStatus.equalsIgnoreCase(WRONG_STATUS)) {
										if (resourceCode.getActiveFlag() == 1) {
											taxonomyBoost = 2.3;
											Code codeDetail = taxonomyRepository.findTaxonomyCodeById(resourceCode.getCodeId());
											if (codeDetail.getCodeId() != null && codeDetail.getRootNodeId() == 20000) {
												addTaxonomy(codeDetail, resourceTaxonomy, resourceCodeIds, taxonomyIndexLevels, customFilters, hasStandard, boosterIsRequired, taxonomyBoost);
											} else if (codeDetail.getCodeId() != null
													&& (codeDetail.getRootNodeId() != null && codeDetail.getRootNodeId() != 0 && codeDetail.getRootNodeId() != 20000)) {
												hasStandard = true;
												addStandardFilter = true;
												addTaxonomy(codeDetail, resourceTaxonomy, resourceCodeIds, taxonomyIndexLevels, customFilters, hasStandard, boosterIsRequired, taxonomyBoost);
											}
										}
									} else if (questionStatus != null && questionStatus.equalsIgnoreCase(CORRECT_STATUS)) {
										boosterIsRequired = true;
										taxonomyBoost = 1.7;
										if (resourceCode.getActiveFlag() == 1) {
											List<Code> childCodes = new ArrayList<Code>();
											Integer childCodeDepth = resourceCode.getDepth() + 1;
											childCodes = taxonomyRepository.findChildTaxonomyCodeByDepth(resourceCode.getCodeId(), childCodeDepth);
											if (childCodes != null && childCodes.size() > 0) {
												for (Code childCode : childCodes) {
													if (childCode != null && childCode.getActiveFlag() == 1 && childCode.getRootNodeId() == 20000 && childCode.getDepth() < 5) {
														addTaxonomy(childCode, resourceTaxonomy, resourceCodeIds, taxonomyIndexLevels, customFilters, hasStandard, boosterIsRequired, taxonomyBoost);
														break;
													} else if (childCode != null && childCode.getActiveFlag() == 1
															&& (childCode.getRootNodeId() != null && childCode.getRootNodeId() != 0 && childCode.getRootNodeId() != 20000)) {
														hasStandard = true;
														addStandardFilter = true;
														addTaxonomy(childCode, resourceTaxonomy, resourceCodeIds, taxonomyIndexLevels, customFilters, hasStandard, boosterIsRequired, taxonomyBoost);
													}
												}
											} else if (resourceCode.getDepth() == taxonomyRepository.findMaxDepthInTaxonomy(resourceCode, resourceCode.getOrganization().getPartyUid())
													|| resourceCode.getDepth() >= 5) {
												Code codeDetail = taxonomyRepository.findTaxonomyCodeById(resourceCode.getCodeId());
												if (codeDetail.getCodeId() != null && codeDetail.getRootNodeId() == 20000) {
													addTaxonomy(codeDetail, resourceTaxonomy, resourceCodeIds, taxonomyIndexLevels, customFilters, hasStandard, boosterIsRequired, taxonomyBoost);
												} else if (codeDetail.getCodeId() != null
														&& (codeDetail.getRootNodeId() != null && codeDetail.getRootNodeId() != 0 && codeDetail.getRootNodeId() != 20000)) {
													hasStandard = true;
													addStandardFilter = true;
													addTaxonomy(codeDetail, resourceTaxonomy, resourceCodeIds, taxonomyIndexLevels, customFilters, hasStandard, boosterIsRequired, taxonomyBoost);
												}
											}
										}
									} 
									//To be Revoked after session_id can be retrieved from api/activity stream
									else {
										if (resourceCode.getActiveFlag() == 1) {
											taxonomyBoost = 1.3;
											Code codeDetail = taxonomyRepository.findTaxonomyCodeById(resourceCode.getCodeId());
											if (codeDetail.getCodeId() != null && codeDetail.getRootNodeId() == 20000) {
												addTaxonomy(codeDetail, resourceTaxonomy, resourceCodeIds, taxonomyIndexLevels, customFilters, hasStandard, boosterIsRequired, taxonomyBoost);
											} else if (codeDetail.getCodeId() != null
													&& (codeDetail.getRootNodeId() != null && codeDetail.getRootNodeId() != 0 && codeDetail.getRootNodeId() != 20000)) {
												hasStandard = true;
												addStandardFilter = true;
												addTaxonomy(codeDetail, resourceTaxonomy, resourceCodeIds, taxonomyIndexLevels, customFilters, hasStandard, boosterIsRequired, taxonomyBoost);
											}
										}
									}
								}
							}
							// addTaxonomyFilter(resourceTaxonomy, taxonomyIndexLevels, true, addStandardFilter);
						}
					}
				}

			*/}
			
		});

		try {
			doerService.invokeAll(tasks, 60L, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
			suggestData.setException(e);
		}

		if (suggestData.getException() != null) {
			if (suggestData.getException() instanceof SearchException) {
				throw (SearchException) suggestData.getException();
			} else {
				throw new RuntimeException(suggestData.getException());
			}
		}
		suggestResponse.setSuggestedType(SearchHandlerType.SCOLLECTION.name().toLowerCase());
		return suggestResponse;
	}

	@Override
	protected SearchHandlerType getType() {
		return SearchHandlerType.SCOLLECTION;
	}

	@Override
	protected String getName() {
		return getType().name();
	}

	@Override
	public List<SuggestDataProviderType> suggestDataProviderTypes() {
		return new ArrayList<SuggestDataProviderType>(Arrays.asList(this.suggestDataProviders));
	}

	protected EsIndex getIndexType() {
		return EsIndex.COLLECTION;
	}

}
