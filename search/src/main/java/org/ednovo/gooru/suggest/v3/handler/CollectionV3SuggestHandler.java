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
import org.ednovo.gooru.search.domain.service.CollectionSearchResult;
import org.ednovo.gooru.search.es.constant.EsIndex;
import org.ednovo.gooru.search.es.exception.SearchException;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.ednovo.gooru.search.es.model.SuggestResponse;
import org.ednovo.gooru.search.es.processor.ElasticsearchProcessor;
import org.ednovo.gooru.search.es.processor.deserializer.SCollectionDeserializeProcessor;
import org.ednovo.gooru.search.es.processor.query_builder.EsDslQueryBuildProcessor;
import org.ednovo.gooru.search.es.repository.ConceptSuggestionRepository;
import org.ednovo.gooru.search.es.service.SearchSettingService;
import org.ednovo.gooru.suggest.v3.data.provider.model.SuggestDataProviderType;
import org.ednovo.gooru.suggest.v3.model.CollectionContextData;
import org.ednovo.gooru.suggest.v3.model.SuggestData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
	private ConceptSuggestionRepository conceptSuggestionRepository;
	
	private static final String COLLECTION_STUDY = "collection-study";	

	private static final String OR_DELIMETER = "~~@@";

	private SuggestDataProviderType[] suggestDataProviders = { SuggestDataProviderType.RESOURCE,SuggestDataProviderType.COLLECTION };

	@Override
	public SuggestResponse<Object> suggest(SuggestData suggestData, Map<SuggestDataProviderType, Object> dataProviderInput) {
		final SearchResponse<Object> searchResponse = new SearchResponse<Object>();
		final SuggestResponse<Object> suggestResponse = new SuggestResponse<Object>();
		final SearchResponse<List<CollectionSearchResult>> searchCollectionResult = new SearchResponse<List<CollectionSearchResult>>();
		final String contextPath = suggestData.getSuggestV3Context().getContext();
		final Integer score = suggestData.getSuggestV3Context().getScore();
		final Long timespent = suggestData.getSuggestV3Context().getTimeSpent();

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
						final CollectionContextData scollectionData = (CollectionContextData) dataProviderInput.get(SuggestDataProviderType.COLLECTION);
						if (contextPath.equalsIgnoreCase(COLLECTION_STUDY)) {
							if (StringUtils.isNotBlank(suggestData.getSuggestV3Context().getContainerId()))
								suggestData.putFilter("!^id", suggestData.getSuggestV3Context().getContainerId());
							List<String> ids = null;
							String range = "M";
							if (score != null) {
								range = getScoreRange(score);
							} else if (timespent != null) {
								range = getTimespentRange(timespent);
							}
							if (scollectionData != null && scollectionData.getTaxonomyLeafSLInternalCodes() != null && scollectionData.getTaxonomyLeafSLInternalCodes().size() > 0) {
								ids = conceptSuggestionRepository.getSuggestionByCompetency(scollectionData.getTaxonomyLeafSLInternalCodes(), contextPath, range,
										SuggestHandlerType.COLLECTION.name().toLowerCase());
							}
							if (ids != null && !ids.isEmpty()) {
								suggestData.putFilter("&id", StringUtils.join(ids, ","));
							} else {
								LOG.info("Suggestions unavailable for concept nodes : " + scollectionData.getTaxonomyLeafSLInternalCodes());
								suggestData.setSize(0);
							}

							/*if (StringUtils.isNotBlank(scollectionData.getTitle())) {
							if (suggestQuery.length() > 0) {
								suggestQuery.append(OR_DELIMETER);
							}
							suggestQuery.append(scollectionData.getTitle().trim());
						}
						if (StringUtils.isNotBlank(scollectionData.getLearningObjective())) {
							if (suggestQuery.length() > 0) {
								suggestQuery.append(OR_DELIMETER);
							}
							suggestQuery.append(scollectionData.getLearningObjective().trim());
						}
*/
							if (suggestQuery.length() == 0) {
								queryString = "*";
							} else {
								queryString = suggestQuery.toString();
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

	private String getScoreRange(Integer score) {
		String scoreRange = "L";
		if (score >= 80) {
			scoreRange = "H";
		} else if (score >= 50 && score < 80) {
			scoreRange = "M";
		}
		return scoreRange;
	}

	private String getTimespentRange(Long timespent) {
		String timespentRange = "L";
		if (timespent >= 900000) {
			timespentRange = "H";
		} else if (timespent >= 120000 && timespent < 900000) {
			timespentRange = "M";
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
