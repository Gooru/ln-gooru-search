/**
 * 
 */
package org.ednovo.gooru.suggest.v3.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.ednovo.gooru.search.es.constant.Constants;
import org.ednovo.gooru.search.es.constant.EsIndex;
import org.ednovo.gooru.search.es.exception.SearchException;
import org.ednovo.gooru.search.es.model.ContentSearchResult;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.ednovo.gooru.search.es.model.SuggestResponse;
import org.ednovo.gooru.search.es.model.SimpleSuggestResponse;
import org.ednovo.gooru.search.es.processor.ElasticsearchProcessor;
import org.ednovo.gooru.search.es.processor.deserializer.ResourceDeserializeProcessor;
import org.ednovo.gooru.search.es.processor.deserializer.ResourceSuggestDeserializeProcessor;
import org.ednovo.gooru.search.es.processor.filter.constructor.TenantFilterConstructionProcessor;
import org.ednovo.gooru.search.es.processor.query_builder.ResourceEsDslQueryBuildProcessor;
import org.ednovo.gooru.search.es.repository.GutBasedResourceSuggestRepository;
import org.ednovo.gooru.search.es.service.SearchSettingService;
import org.ednovo.gooru.suggest.v3.data.provider.model.SuggestDataProviderType;
import org.ednovo.gooru.suggest.v3.model.SuggestData;
import org.ednovo.gooru.suggest.v3.model.TaxonomyContextData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResourceV3SuggestHandlerForTaxonomy extends SuggestHandler<Map<String, Object>> implements Constants {

	protected static final Logger LOG = LoggerFactory.getLogger(ResourceV3SuggestHandlerForTaxonomy.class);

	@Autowired
	private ElasticsearchProcessor elasticSearchProcessor;

	@Autowired
	private ResourceEsDslQueryBuildProcessor resourceEsDslQueryProcessor;

	@Autowired
	private ResourceDeserializeProcessor resourceDeserializeProcessor;

	@Autowired
	private ResourceSuggestDeserializeProcessor resourceSuggestDeserializeProcessor;

	@Autowired
	private GutBasedResourceSuggestRepository gutSuggestionRepository;

	@Autowired
	private TenantFilterConstructionProcessor tenantFilterConstructionProcessor;
	
	private SuggestDataProviderType[] suggestDataProviders = { SuggestDataProviderType.TAXONOMY };

	@Override
	protected SuggestHandlerType getType() {
		return SuggestHandlerType.TAXONOMY_RESOURCE;
	}

	@Override
	protected String getName() {
		return getType().name();
	}

	@Override
	public List<SuggestDataProviderType> suggestDataProviderTypes() {
		return new ArrayList<SuggestDataProviderType>(Arrays.asList(this.suggestDataProviders));
	}

	@Override
	public SuggestResponse<Object> suggest(final SuggestData suggestData, Map<SuggestDataProviderType, Object> dataProviderInput) {
		final SuggestResponse<Object> suggestResponse = new SuggestResponse<Object>();
		long start = System.currentTimeMillis();
		suggestData.setIndexType(getIndexType());
		suggestData.setType(TYPE_RESOURCE);

		final TaxonomyContextData taxonomyData = (TaxonomyContextData) dataProviderInput.get(SuggestDataProviderType.TAXONOMY);

		final SearchResponse<List<SimpleSuggestResponse>> suggestResponseResource = new SearchResponse<>();
		final SearchResponse<List<ContentSearchResult>> searchResponseResource = new SearchResponse<>();
		final SearchResponse<Object> searchRes = new SearchResponse<Object>();
		final Integer score = suggestData.getSuggestContextData().getScore();
		final Long timespent = suggestData.getSuggestContextData().getTimeSpent();
		try {
			String queryString = STAR;
			suggestData.putFilter("&^statistics.statusIsBroken", 0);

			if (taxonomyData != null) {

				String range = null;
				if (score != null) {
					range = getScoreRange(score);
				} else if (timespent != null) {
					range = getTimespentRange(timespent);
				}
				List<String> ids = null;
				if (taxonomyData.getGutLtCodes() != null && taxonomyData.getGutLtCodes().size() > 0) {
					ids = gutSuggestionRepository.getSuggestionByMicroCompetency(taxonomyData.getGutLtCodes(), range);
				}

				if (ids == null && taxonomyData.getGutStdCodes() != null && taxonomyData.getGutStdCodes().size() > 0) {
					ids = gutSuggestionRepository.getSuggestionByCompetency(taxonomyData.getGutStdCodes(), range);
				}

				if (ids != null && !ids.isEmpty()) {
					suggestData.putFilter("&^id", StringUtils.join(ids, COMMA));
				} else {
					LOG.info("Checking other published resources as quality suggestions are unavailable for concept nodes : " + taxonomyData.getSlInternalCodes());
					suggestData.putFilter(FLT_PUBLISH_STATUS, PublishedStatus.PUBLISHED.getStatus());

					if (taxonomyData.getSlInternalCodes() != null && taxonomyData.getSlInternalCodes().size() > 0) {
						suggestData.putFilter("&^taxonomy.leafInternalCodes", StringUtils.join(taxonomyData.getSlInternalCodes(), ","));
					}
				}
			}

			suggestData.setQueryString(queryString);

			tenantFilterConstructionProcessor.process(suggestData, searchRes);
			resourceEsDslQueryProcessor.process(suggestData, searchRes);
			elasticSearchProcessor.process(suggestData, searchRes);
			if (suggestData.getIsIntenralSuggest()) {
				resourceSuggestDeserializeProcessor.process(suggestData, suggestResponseResource);
				suggestResponse.setSuggestResults(suggestResponseResource.getSearchResults());
			} else {
				resourceDeserializeProcessor.process(suggestData, searchResponseResource);
				suggestResponse.setSuggestResults(searchResponseResource.getSearchResults());
			}
			suggestResponse.setExecutionTime(System.currentTimeMillis() - start);
		} catch (Exception e) {
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
			scoreRange = SearchSettingService.getByName(BELOW_AVERAGE);
			if (score >= maxScore) {
				scoreRange = SearchSettingService.getByName(ABOVE_AVERAGE);
			} else if (score >= minScore && score < maxScore) {
				scoreRange = SearchSettingService.getByName(AVERAGE);
			}
		}
		return scoreRange;
	}

	private String getTimespentRange(Long timespent) {
		String timespentRange = null;
		if (timespent != null) {
			Integer minTS = SearchSettingService.getSettingAsInteger(Constants.TIMESPENT_AVERAGE_MIN, 120000);
			Integer maxTS = SearchSettingService.getSettingAsInteger(Constants.TIMESPENT_AVERAGE_MAX, 900000);
			timespentRange = SearchSettingService.getByName(BELOW_AVERAGE);
			if (timespent >= maxTS) {
				timespentRange = SearchSettingService.getByName(ABOVE_AVERAGE);
			} else if (timespent >= minTS && timespent < maxTS) {
				timespentRange = SearchSettingService.getByName(AVERAGE);
			}
		}
		return timespentRange;
	}

	protected EsIndex getIndexType() {
		return EsIndex.RESOURCE;
	}

}
