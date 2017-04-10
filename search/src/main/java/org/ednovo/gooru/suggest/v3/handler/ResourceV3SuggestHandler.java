/**
 * 
 */
package org.ednovo.gooru.suggest.v3.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.ednovo.gooru.search.es.constant.Constants;
import org.ednovo.gooru.search.es.constant.EsIndex;
import org.ednovo.gooru.search.es.exception.SearchException;
import org.ednovo.gooru.search.es.model.ContentSearchResult;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.ednovo.gooru.search.es.model.SuggestResponse;
import org.ednovo.gooru.search.es.processor.ElasticsearchProcessor;
import org.ednovo.gooru.search.es.processor.deserializer.ResourceDeserializeProcessor;
import org.ednovo.gooru.search.es.processor.query_builder.ResourceEsDslQueryBuildProcessor;
import org.ednovo.gooru.search.es.repository.ConceptBasedResourceSuggestRepository;
import org.ednovo.gooru.search.es.service.SearchSettingService;
import org.ednovo.gooru.search.model.ActivityStreamRawData;
import org.ednovo.gooru.suggest.v3.data.provider.model.SuggestDataProviderType;
import org.ednovo.gooru.suggest.v3.model.CollectionContextData;
import org.ednovo.gooru.suggest.v3.model.ResourceContextData;
import org.ednovo.gooru.suggest.v3.model.SuggestData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResourceV3SuggestHandler extends SuggestHandler<Map<String, Object>> implements Constants {

	protected static final Logger LOG = LoggerFactory.getLogger(ResourceV3SuggestHandler.class);

	private static Integer threadPoolLength = 3;

	private ExecutorService doerService;

	private static final String RESOURCE_STUDY = "resource-study";

	private static final String COLLECTION_STUDY = "collection-study";

	private static final String RESOURCE_PLAY_ACTIVITY = "resource.play";

	private static final String COLLECTION_ITEM_PLAY_ACTIVITY = "collection.resource.play";

	private static final String OR_DELIMETER = "~~@@";

	@Autowired
	private ElasticsearchProcessor elasticSearchProcessor;

	@Autowired
	private ResourceEsDslQueryBuildProcessor resourceEsDslQueryProcessor;

	@Autowired
	private ResourceDeserializeProcessor resourceDeserializeProcessor;
	
	@Autowired
	private ConceptBasedResourceSuggestRepository conceptSuggestionRepository;
	
	private SuggestDataProviderType[] suggestDataProviders = { SuggestDataProviderType.RESOURCE,SuggestDataProviderType.COLLECTION };

	@Override
	protected SuggestHandlerType getType() {
		return SuggestHandlerType.RESOURCE;
	}

	@Override
	protected String getName() {
		return getType().name();
	}

	@Override
	public List<SuggestDataProviderType> suggestDataProviderTypes() {
		return new ArrayList<SuggestDataProviderType>(Arrays.asList(this.suggestDataProviders));
	}

	@SuppressWarnings("unchecked")
	@Override
	public SuggestResponse<Object> suggest(final SuggestData suggestData, Map<SuggestDataProviderType, Object> dataProviderInput) {
		final SuggestResponse<Object> suggestResponse = new SuggestResponse<Object>();
		long start = System.currentTimeMillis();
		suggestData.setIndexType(getIndexType());
		suggestData.setType(getName());
		if (doerService == null) {
			doerService = Executors.newFixedThreadPool(threadPoolLength);
		}

		List<Callable<SuggestResponse<Object>>> tasks = new ArrayList<Callable<SuggestResponse<Object>>>();
		final List<String> recentlyPlayedResIds = new ArrayList<String>();
		final ResourceContextData resourceData = (ResourceContextData) dataProviderInput.get(SuggestDataProviderType.RESOURCE);
		final CollectionContextData collectionData = (CollectionContextData) dataProviderInput.get(SuggestDataProviderType.COLLECTION);
		final List<ActivityStreamRawData> activityList = (List<ActivityStreamRawData>) dataProviderInput.get(SuggestDataProviderType.USER_ACTIVITY);

		if ((activityList != null && activityList.size() > 0)) {
			for (ActivityStreamRawData activityData : activityList) {
				if (resourceData != null && (activityData.getEventName().equalsIgnoreCase(COLLECTION_ITEM_PLAY_ACTIVITY) || activityData.getEventName().equalsIgnoreCase(RESOURCE_PLAY_ACTIVITY))
						&& activityData.getResourceGooruOid() != null) {
					recentlyPlayedResIds.add(activityData.getResourceGooruOid());
				}
			}
		}

		final SearchResponse<List<ContentSearchResult>> searchResponseResource = new SearchResponse<List<ContentSearchResult>>();
		final SearchResponse<Object> searchRes = new SearchResponse<Object>();
		final String contextType = suggestData.getSuggestContextData().getContextType();
		final Integer score = suggestData.getSuggestContextData().getScore();
		final Long timespent = suggestData.getSuggestContextData().getTimeSpent();

		tasks.add(new Callable<SuggestResponse<Object>>() {

			@Override
			public SuggestResponse<Object> call() throws Exception {
				try {
					String queryString = "*";
					suggestData.putFilter("&^statistics.statusIsBroken", 0);
					suggestData.putFilter(FLT_TENANT_ID, StringUtils.join(suggestData.getUserPermits(), ","));
					if (contextType.equalsIgnoreCase(RESOURCE_STUDY)) {
						if (resourceData != null) {

							suggestData.putFilter("!^id", suggestData.getSuggestContextData().getResourceId());

							// Search query formation: If query string is null in the request,
							// build the search query with resource keywords OR resource title
							// of the participating resource.

							StringBuilder suggestQuery = new StringBuilder();
							if (StringUtils.isNotBlank(resourceData.getKeywords())) {
								for (String keyword : resourceData.getKeywords().trim().split(",")) {
									if (suggestQuery.length() > 0) {
										suggestQuery.append(OR_DELIMETER);
									}
									suggestQuery.append(keyword);
								}
							}
							if (StringUtils.isNotBlank(resourceData.getTitle())) {
								if (suggestQuery.length() > 0) {
									suggestQuery.append(OR_DELIMETER);
								}
								suggestQuery.append(resourceData.getTitle().trim());
							}
							if (StringUtils.isNotBlank(resourceData.getDescription())) {
								if (suggestQuery.length() > 0) {
									suggestQuery.append(OR_DELIMETER);
								}
								suggestQuery.append(resourceData.getDescription().trim());
							}

							if (suggestQuery.length() == 0) {
								queryString = "*";
							} else {
								queryString = suggestQuery.toString();
							}

							if (collectionData != null) {
								if (collectionData.getTaxonomyDomains() != null && collectionData.getTaxonomyDomains().size() > 0) {
									suggestData.putFilter("&^domain", StringUtils.join(collectionData.getTaxonomyDomains(), ","));
								} else if (collectionData.getTaxonomyLeafSLInternalCodes() != null && collectionData.getTaxonomyLeafSLInternalCodes().size() > 0) {
									suggestData.putFilter("&^taxonomy.leafInternalCodes", StringUtils.join(collectionData.getTaxonomyLeafSLInternalCodes(), ","));
								}
							}
						}
					} else if (contextType.equalsIgnoreCase(COLLECTION_STUDY) && collectionData != null) {
						StringBuilder suggestQuery = new StringBuilder();

						/*if (StringUtils.isNotBlank(collectionData.getTitle())) {
							if (suggestQuery.length() > 0) {
								suggestQuery.append(OR_DELIMETER);
							}
							suggestQuery.append(collectionData.getTitle().trim());
						}
						if (StringUtils.isNotBlank(collectionData.getLearningObjective())) {
							if (suggestQuery.length() > 0) {
								suggestQuery.append(OR_DELIMETER);
							}
							suggestQuery.append(collectionData.getLearningObjective().trim());
						}*/

						if (suggestQuery.length() == 0) {
							queryString = "*";
						} else {
							queryString = suggestQuery.toString();
						}

						String range = null;
						if (score != null) {
							range = getScoreRange(score);
						} else if (timespent != null) {
							range = getTimespentRange(timespent);
						}
						List<String> ids = null;
						if (collectionData != null) {
							if (collectionData.getTaxonomyLearningTargets() != null && collectionData.getTaxonomyLearningTargets().size() > 0) {
								ids = conceptSuggestionRepository.getSuggestionByMicroCompetency(collectionData.getTaxonomyLearningTargets(), contextType, range,
										SuggestHandlerType.RESOURCE.name().toLowerCase());
							} 
							
							if (ids == null && collectionData.getStandards() != null && collectionData.getStandards().size() > 0) {
								ids = conceptSuggestionRepository.getSuggestionByCompetency(collectionData.getStandards(), contextType, range, SuggestHandlerType.RESOURCE.name().toLowerCase());
							}
						}
						if (ids != null && !ids.isEmpty()) {
							suggestData.putFilter("&id", StringUtils.join(ids, ","));
						} else {
							LOG.info("Suggestions unavailable for concept nodes : " + collectionData.getTaxonomyLeafSLInternalCodes());
							suggestData.setSize(0);
						}
					}

					if (queryString.contains(OR_DELIMETER)) {
						String[] queryArr = queryString.split(OR_DELIMETER);
						StringBuilder searchQuery = new StringBuilder();
						for (int index = 0; index < queryArr.length; index++) {
							StringBuilder queryBuilder = new StringBuilder();
							buildQuery(queryArr[index], queryBuilder);
							if (index > 0) {
								searchQuery.append(" OR ");
							}
							searchQuery.append("(");
							searchQuery.append(queryBuilder.toString());
							searchQuery.append(")");
						}
						if (StringUtils.trimToNull(searchQuery.toString()) != null) {
							queryString = searchQuery.toString();
						}
					} else {
						StringBuilder queryStringBuilder = new StringBuilder();
						buildQuery(queryString, queryStringBuilder);
						if (StringUtils.trimToNull(queryStringBuilder.toString()) != null) {
							queryString = queryStringBuilder.toString();
						}
					}
					suggestData.setQueryString(queryString);

					resourceEsDslQueryProcessor.process(suggestData, searchRes);
					elasticSearchProcessor.process(suggestData, searchRes);
					resourceDeserializeProcessor.process(suggestData, searchResponseResource);
					suggestResponse.setSuggestResults(searchResponseResource.getSearchResults());
					suggestResponse.setExecutionTime(System.currentTimeMillis() - start);
				} catch (Exception e) {
					suggestData.setException(e);
				}
				return suggestResponse;
			}

		});

		try {
			doerService.invokeAll(tasks, 60L, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			suggestData.setException(e);
		}

		if (suggestData.getException() != null) {
			if (suggestData.getException() instanceof SearchException) {
				throw (SearchException) suggestData.getException();
			} else {
				throw new RuntimeException(suggestData.getException());
			}
		}
		suggestResponse.setSuggestedType(SuggestHandlerType.RESOURCE.name().toLowerCase());
		return suggestResponse;
	}

	private String getScoreRange(Integer score) {
		String scoreRange = null;
		if (score != null) {
			Integer minScore = SearchSettingService.getSettingAsInteger(Constants.SCORE_AVERAGE_MIN, 50);
			Integer maxScore = SearchSettingService.getSettingAsInteger(Constants.SCORE_AVERAGE_MAX, 80);
			scoreRange = "L";
			if (score >= maxScore) {
				scoreRange = "H";
			} else if (score >= minScore && score < maxScore) {
				scoreRange = "M";
			}
		}
		return scoreRange;
	}

	private String getTimespentRange(Long timespent) {
		String timespentRange = null;
		if (timespent != null) {
			Integer minTS = SearchSettingService.getSettingAsInteger(Constants.TIMESPENT_AVERAGE_MIN, 120000);
			Integer maxTS = SearchSettingService.getSettingAsInteger(Constants.TIMESPENT_AVERAGE_MAX, 900000);
			timespentRange = "L";
			if (timespent >= maxTS) {
				timespentRange = "H";
			} else if (timespent >= minTS && timespent < maxTS) {
				timespentRange = "M";
			}
		}
		return timespentRange;
	}
	
	protected EsIndex getIndexType() {
		return EsIndex.RESOURCE;
	}

	private void buildQuery(String queryString, StringBuilder queryStringBuilder) {
		String[] queryWords = queryString.toLowerCase().split("[^a-zA-Z0-9\\']");
		String stopWords = SearchSettingService.getByName("search.query.stopwords"); 
		for (String word : queryWords) {
			if (!(Arrays.asList(stopWords.split(",")).contains(word))) {
				StringBuilder wordsBuilder = new StringBuilder();
				if (wordsBuilder.length() == 0) {
					wordsBuilder.append(word);
				}
				if (queryStringBuilder.length() > 0 && word.trim().length() > 0) {
					queryStringBuilder.append(" OR ");
				}
				queryStringBuilder.append(wordsBuilder.toString());
			}
		}
	}

}
