package org.ednovo.gooru.suggest.v3.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.ednovo.gooru.search.domain.service.CollectionSearchResult;
import org.ednovo.gooru.search.es.constant.Constants;
import org.ednovo.gooru.search.es.constant.EsIndex;
import org.ednovo.gooru.search.es.exception.SearchException;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.ednovo.gooru.search.es.model.SuggestResponse;
import org.ednovo.gooru.search.es.processor.ElasticsearchProcessor;
import org.ednovo.gooru.search.es.processor.deserializer.SCollectionDeserializeProcessor;
import org.ednovo.gooru.search.es.processor.filter.constructor.TenantFilterConstructionProcessor;
import org.ednovo.gooru.search.es.processor.query_builder.EsDslQueryBuildProcessor;
import org.ednovo.gooru.search.es.repository.ConceptBasedCollectionSuggestRepository;
import org.ednovo.gooru.search.es.service.SearchSettingService;
import org.ednovo.gooru.suggest.v3.data.provider.model.SuggestDataProviderType;
import org.ednovo.gooru.suggest.v3.model.CollectionContextData;
import org.ednovo.gooru.suggest.v3.model.LessonContextData;
import org.ednovo.gooru.suggest.v3.model.SuggestData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class CollectionV3SuggestHandler extends SuggestHandler<Map<String, Object>> {

	protected static final Logger LOG = LoggerFactory.getLogger(CollectionV3SuggestHandler.class);

	private static Integer threadPoolLength = 3;

	private ExecutorService doerService;

	@Autowired
	private ElasticsearchProcessor elasticSearchProcessor;

	@Autowired
	private EsDslQueryBuildProcessor esDslQueryBuildProcessor;

	@Autowired
	private SCollectionDeserializeProcessor scollectionDeserializeProcessor;
	
	@Autowired
	private ConceptBasedCollectionSuggestRepository conceptSuggestionRepository;

	@Autowired
	private TenantFilterConstructionProcessor tenantFilterConstructionProcessor;
	
	private static final String COLLECTION_STUDY = "collection-study";	

	private static final String OR_DELIMETER = "~~@@";

	protected static final String STUDY_PLAYER = "study-player";

	private SuggestDataProviderType[] suggestDataProviders = { SuggestDataProviderType.RESOURCE,SuggestDataProviderType.COLLECTION,SuggestDataProviderType.LESSON };

	@Override
	public SuggestResponse<Object> suggest(SuggestData suggestData, Map<SuggestDataProviderType, Object> dataProviderInput) {
		final SearchResponse<Object> searchResponse = new SearchResponse<Object>();
		final SuggestResponse<Object> suggestResponse = new SuggestResponse<Object>();
		final SearchResponse<List<CollectionSearchResult>> searchCollectionResult = new SearchResponse<List<CollectionSearchResult>>();
		final String contextArea = suggestData.getSuggestContextData().getContextArea();
		final String contextType = suggestData.getSuggestContextData().getContextType();
		final Integer score = suggestData.getSuggestContextData().getScore();
		final Long timespent = suggestData.getSuggestContextData().getTimeSpent();

		StringBuilder suggestQuery = new StringBuilder();

		if (doerService == null) {
			doerService = Executors.newFixedThreadPool(threadPoolLength);
		}
		List<Callable<SuggestResponse<Object>>> tasks = new ArrayList<Callable<SuggestResponse<Object>>>();
		tasks.add(new Callable<SuggestResponse<Object>>() {
			@Override
			public SuggestResponse<Object> call() throws Exception {
				
				String queryString = "*";
				for (SuggestDataProviderType suggestDataProviderType : suggestDataProviderTypes()) {
					if (suggestDataProviderType != null && suggestDataProviderType == SuggestDataProviderType.COLLECTION && dataProviderInput.get(SuggestDataProviderType.COLLECTION) != null) {
						final CollectionContextData collectionData = (CollectionContextData) dataProviderInput.get(SuggestDataProviderType.COLLECTION);
						final LessonContextData lessonData = (LessonContextData) dataProviderInput.get(SuggestDataProviderType.LESSON);
						if (contextType.equalsIgnoreCase(COLLECTION_STUDY)) {
							if (contextArea.equalsIgnoreCase(STUDY_PLAYER)) {
								if (StringUtils.isNotBlank(suggestData.getSuggestContextData().getCollectionId()))
									suggestData.putFilter("!^id", suggestData.getSuggestContextData().getCollectionId());
								List<String> ids = null;
								String range = SearchSettingService.getByName(Constants.AVERAGE);
								if (score != null) {
									range = getScoreRange(score);
								} else if (timespent != null) {
									range = getTimespentRange(timespent);
								}
								
								ids = fetchPrePopulatedSuggestions(suggestData, contextType, collectionData, lessonData, ids, range);

								if (ids != null && !ids.isEmpty()) {
									suggestData.putFilter("&id", StringUtils.join(ids, ","));
								} else {
									LOG.info("Suggestions unavailable for concept nodes : " + collectionData.getTaxonomyLeafSLInternalCodes());
									suggestData.setSize(0);
								}

								if (suggestQuery.length() == 0) {
									queryString = "*";
								} else {
									queryString = suggestQuery.toString();
								}
							}
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
					}

				}
				suggestData.setIndexType(getIndexType());
				suggestData.setType(getName());
				tenantFilterConstructionProcessor.process(suggestData, searchResponse);
				esDslQueryBuildProcessor.process(suggestData, searchResponse);
				elasticSearchProcessor.process(suggestData, searchResponse);
				scollectionDeserializeProcessor.process(suggestData, searchCollectionResult);
				suggestResponse.setSuggestResults(searchCollectionResult.getSearchResults());
				return suggestResponse;
			}

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
		suggestResponse.setSuggestedType(SuggestHandlerType.COLLECTION.name().toLowerCase());
		return suggestResponse;
	}
	
	private List<String> fetchPrePopulatedSuggestions(SuggestData suggestData, final String contextType, final CollectionContextData collectionData, final LessonContextData lessonData,
			List<String> ids, String range) {
		switch (suggestData.getSuggestContextData().getRequestedSubType()) {
		case Constants.PRE_TEST:
			if (lessonData != null && lessonData.getStandards() != null && lessonData.getStandards().size() > 0) {
				ids = conceptSuggestionRepository.getSuggestionByCompetency(lessonData.getStandards(), contextType, range,
						suggestData.getSuggestContextData().getRequestedSubType());
			}
			break;
		case Constants.POST_TEST:
		case Constants.BACKFILL:
			if (collectionData != null && collectionData.getStandards() != null && collectionData.getStandards().size() > 0) {
				ids = conceptSuggestionRepository.getSuggestionByCompetency(collectionData.getStandards(), contextType, range,
						suggestData.getSuggestContextData().getRequestedSubType());
			}
			break;
		case Constants.BENCHMARK:
			if (collectionData != null && collectionData.getTaxonomyLearningTargets() != null && collectionData.getTaxonomyLearningTargets().size() > 0) {
				ids = conceptSuggestionRepository.getSuggestionByMicroCompetency(collectionData.getTaxonomyLearningTargets(), contextType, range,
						suggestData.getSuggestContextData().getRequestedSubType());
			}
			break;
		default:
			LOG.info("Invalid Collection Subtype requested for Study player suggestion" );
			throw new SearchException(HttpStatus.BAD_REQUEST, "Invalid Collection Subtype requested for Study player suggestion");
		}
		return ids;
	}

	private String getScoreRange(Integer score) {
		String scoreRange = null;
		if (score != null) {
			Integer minScore = SearchSettingService.getSettingAsInteger(Constants.SCORE_AVERAGE_MIN, 50);
			Integer maxScore = SearchSettingService.getSettingAsInteger(Constants.SCORE_AVERAGE_MAX, 80);
			scoreRange = SearchSettingService.getByName(Constants.BELOW_AVERAGE);
			if (score >= maxScore) {
				scoreRange = SearchSettingService.getByName(Constants.ABOVE_AVERAGE);
			} else if (score >= minScore && score < maxScore) {
				scoreRange = SearchSettingService.getByName(Constants.AVERAGE);
			}
		}
		return scoreRange;
	}

	private String getTimespentRange(Long timespent) {
		String timespentRange = null;
		if (timespent != null) {
			Integer minTS = SearchSettingService.getSettingAsInteger(Constants.TIMESPENT_AVERAGE_MIN, 120000);
			Integer maxTS = SearchSettingService.getSettingAsInteger(Constants.TIMESPENT_AVERAGE_MAX, 900000);
			timespentRange = SearchSettingService.getByName(Constants.BELOW_AVERAGE);
			if (timespent >= maxTS) {
				timespentRange = SearchSettingService.getByName(Constants.ABOVE_AVERAGE);
			} else if (timespent >= minTS && timespent < maxTS) {
				timespentRange = SearchSettingService.getByName(Constants.AVERAGE);
			}
		}
		return timespentRange;
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

	@Override
	protected SuggestHandlerType getType() {
		return SuggestHandlerType.COLLECTION;
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
