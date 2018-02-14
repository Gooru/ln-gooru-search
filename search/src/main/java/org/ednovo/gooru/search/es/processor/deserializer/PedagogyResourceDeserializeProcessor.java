
package org.ednovo.gooru.search.es.processor.deserializer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.ednovo.gooru.search.domain.service.PedagogyContentSearchResult;
import org.ednovo.gooru.search.domain.service.PedagogyQuestion;
import org.ednovo.gooru.search.es.constant.IndexFields;
import org.ednovo.gooru.search.es.model.Answer;
import org.ednovo.gooru.search.es.model.Hint;
import org.ednovo.gooru.search.es.model.MapWrapper;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.UserV2;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author SearchTeam
 *
 */
@Component
public class PedagogyResourceDeserializeProcessor extends PedagogyDeserializeProcessor<List<PedagogyContentSearchResult>, PedagogyContentSearchResult> {

	protected static final Logger logger = LoggerFactory.getLogger(PedagogyResourceDeserializeProcessor.class);
	protected static final String UN_RESTRICTED_SEARCH = "unrestrictedSearch";
	protected static final String ALLOW_DUPLICATES = "allowDuplicates";

	@SuppressWarnings("unchecked")
	@Override
	public List<PedagogyContentSearchResult> deserialize(Map<String, Object> model, SearchData searchData, List<PedagogyContentSearchResult> output) {
		output = new ArrayList<PedagogyContentSearchResult>();
		Set<String> contentUrls = new HashSet<String>();
		Set<String> contentTitles = new HashSet<String>();
		if (model != null && model.get(SEARCH_HITS) != null) {
			List<Map<String, Object>> hits = (List<Map<String, Object>>) ((Map<String, Object>) model.get(SEARCH_HITS)).get(SEARCH_HITS);
			List<String> resourceIds = new ArrayList<String>();
			float minScore = 0;
			int lessScoreResourceCount = 0;
			StringBuilder lessScoreResourceGooruOid = new StringBuilder();
			if (hits.size() > 0) {
				String configName = "S_RESOURCE_QUERY_MINSCORE_CATEGORY_" + (((Map<String, Object>) hits.get(0).get(SEARCH_SOURCE)).get(SEARCH_CATEGORY) + "").toUpperCase();
				minScore = getCategorySettingAsFloat(configName);
			}

			for (Map<String, Object> searchHit : hits) {
				if (searchHit.get(IndexFields._SOURCE) == null) {
					// FIXME, Can this happen?
					continue;
				}
				Map<String, Object> fields = (Map<String, Object>) searchHit.get(IndexFields._SOURCE);
				// Check for Duplicates
				String url = (String) fields.get(IndexFields.URL);
				String title = (String) fields.get(IndexFields.TITLE);
				if (((String) fields.get(IndexFields.CONTENT_FORMAT)) != null && fields.get(IndexFields.CONTENT_FORMAT).equals(ContentFormat.QUESTION.getContentFormat())) {
					Map<String, Object> questionMap = (Map<String, Object>) fields.get(IndexFields.QUESTION);
					if (questionMap != null) {
						title = (String) (questionMap.get(IndexFields.QUESTION));
					}
				}
				MapWrapper<Object> parameters = searchData.getParameters();
				if (parameters != null && parameters.getBoolean(ALLOW_DUPLICATES) != null && !parameters.getBoolean(ALLOW_DUPLICATES)) {
					if (StringUtils.isNotBlank(url)) {
						if (StringUtils.contains(url, "http://www.youtube.com")) {
							url = url.split("&")[0];
						}
						if (contentUrls.contains(url)) {
							continue;
						}
					}
					if (StringUtils.isBlank(title) || contentTitles.contains(title.toLowerCase())) {
						continue;
					}
					contentUrls.add(url);
					contentTitles.add(title.toLowerCase());
					// check the resource has less than the minScore640
					if (!parameters.getBoolean(UN_RESTRICTED_SEARCH) && (Double) searchHit.get(SEARCH_SOURCE) < minScore) {
						lessScoreResourceCount++;
						lessScoreResourceGooruOid.append(fields.get(IndexFields.ID) + " , ");
						if (output.size() > 4) {
							continue;
						}
					}
				}
				PedagogyContentSearchResult resource = collect(fields, searchData, null);
				resourceIds.add(resource.getId());
				output.add(resource);
			}
			searchData.setResourceGooruOIds(resourceIds);
			if (lessScoreResourceCount > 0) {
				logger.debug("Minimum score resources: total = " + lessScoreResourceCount + " - keyword = " + searchData.getQueryString() + " -  gooruOId = " + lessScoreResourceGooruOid + "");
			}
		}
		return output;
	}

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.PedagogyResourceDeserializer;
	}

	@Override
	PedagogyContentSearchResult collect(Map<String, Object> model, SearchData input, PedagogyContentSearchResult resource) {
		return deserializeToResource(model, input);
	}

	@SuppressWarnings("unchecked")
	private PedagogyContentSearchResult deserializeToResource(Map<String, Object> dataMap, SearchData input) {
		String contentFormat = (String) dataMap.get(IndexFields.CONTENT_FORMAT);
		String contentSubFormat = (String) dataMap.get(IndexFields.CONTENT_SUB_FORMAT);

		PedagogyContentSearchResult resource = null;

		if (contentFormat != null && contentFormat.equalsIgnoreCase(ContentFormat.QUESTION.getContentFormat())) {
			PedagogyQuestion question = convertToQuestion(dataMap);
			resource = question;
		} else {
			resource = new PedagogyContentSearchResult();
		}
		
		resource.setContentFormat(contentFormat);
		resource.setContentSubFormat(contentSubFormat);
		resource.setTitle(StringUtils.defaultString((String) dataMap.get(IndexFields.TITLE), ""));
		resource.setUrl((String) dataMap.get(IndexFields.URL));
		resource.setThumbnail((String) dataMap.get(IndexFields.THUMBNAIL));
		resource.setId((String) dataMap.get(IndexFields.ID));
		resource.setDescription((String) dataMap.get(IndexFields.DESCRIPTION));
		resource.setPublishStatus((String) dataMap.get(IndexFields.PUBLISH_STATUS));

		// Set metadata
		if (dataMap.get(IndexFields.METADATA) != null) {
			Map<String, List<String>> metadata = (Map<String, List<String>>) dataMap.get(IndexFields.METADATA);
			if (metadata != null) {
				List<String> grade = metadata.get(IndexFields.GRADE);
				if(grade != null && grade.size() > 0){
					resource.setGrade(String.join(COMMA, grade));
				}
			}
		}

		Map<String, Object> creatorMap = (Map<String, Object>) dataMap.get(IndexFields.ORIGINAL_CREATOR);
		UserV2 creator = new UserV2();
		if (creatorMap != null) {
			creator = setUser(creatorMap);
		}
		resource.setCreator(creator);

		Map<String, Object> ownerMap = (Map<String, Object>) dataMap.get(IndexFields.CREATOR);
		UserV2 user = new UserV2();
		if (ownerMap != null) {
			user = setUser(ownerMap);
		}
		resource.setUser(user);
		if (creatorMap == null && ownerMap != null) {
			resource.setCreator(user);
		}

		Map<String, Object> statisticsMap = (Map<String, Object>) dataMap.get(IndexFields.STATISTICS);
		boolean hasFrameBreaker = false;
		if (statisticsMap != null) {
			long viewsCount = 0L;
			if (statisticsMap.get(IndexFields.VIEWS_COUNT) != null) {
				viewsCount = ((Number) statisticsMap.get(IndexFields.VIEWS_COUNT)).longValue();
				resource.setViewCount(viewsCount);
			}

			if (statisticsMap.containsKey(IndexFields.HAS_FRAMEBREAKER) && ((Boolean) statisticsMap.get(IndexFields.HAS_FRAMEBREAKER))) {
				hasFrameBreaker = true;
			}
			resource.setHasFrameBreaker(hasFrameBreaker);
			resource.setRemixedInCollectionCount((statisticsMap.get(IndexFields.COLLECTION_COUNT) != null) ? ((Number) statisticsMap.get(IndexFields.COLLECTION_COUNT)).longValue() : 0);
			resource.setRemixedInAssessmentCount((statisticsMap.get(IndexFields.ASSESSMENT_COUNT) != null) ? ((Number) statisticsMap.get(IndexFields.ASSESSMENT_COUNT)).longValue() : 0);
			resource.setEfficacy((statisticsMap.get(IndexFields.EFFICACY) != null) ? ((Number) statisticsMap.get(IndexFields.EFFICACY)).doubleValue() : 0.5);
			resource.setEngagement((statisticsMap.get(IndexFields.ENGAGEMENT) != null) ? ((Number) statisticsMap.get(IndexFields.ENGAGEMENT)).doubleValue() : 0.5);
			resource.setRelevance((statisticsMap.get(IndexFields.RELEVANCE) != null) ? ((Number) statisticsMap.get(IndexFields.RELEVANCE)).doubleValue() : 0.5);
		}

		Map<String, Object> taxonomyMap = (Map<String, Object>) dataMap.get(IndexFields.TAXONOMY);
		if (taxonomyMap != null) {
			long start = System.currentTimeMillis();
			setTaxonomy(taxonomyMap, input, resource);
			logger.debug("Latency of Taxonomy Transformation : {} ms", (System.currentTimeMillis() - start));
		}
		return resource;
	}

	@SuppressWarnings("unchecked")
	private PedagogyQuestion convertToQuestion(Map<String, Object> source) {
		PedagogyQuestion question = new PedagogyQuestion();
		Map<String, Object> questionMap = (Map<String, Object>) (source.get(IndexFields.QUESTION));
		question.setId((String) source.get(IndexFields.ID));
		question.setQuestionText((String) source.get(IndexFields.TITLE));
		question.setTypeName((String) source.get(IndexFields.CONTENT_SUB_FORMAT));
		if (questionMap != null) {
			question.setExplanation((String) questionMap.get(IndexFields.EXPLANATION));

			Set<Answer> answers = null;
			Map<String, Object> answerMap = (Map<String, Object>) (questionMap.get(IndexFields.ANSWER));
			if ((answerMap != null) && (answerMap.get(IndexFields.ANSWER_TEXT) != null)) {
				answers = new HashSet<Answer>();
				String[] answersText = ((String) answerMap.get(IndexFields.ANSWER_TEXT)).split(" ~~ ");
				for (String text : answersText) {
					Answer answer = new Answer();
					answer.setAnswerText(text);
					answers.add(answer);
				}
			}
			Map<String, Object> hintMap = (Map<String, Object>) (source.get(IndexFields.HINTS));
			Set<Hint> hints = null;
			if ((hintMap != null) && (hintMap.get(IndexFields.HINT_TEXT) != null)) {
				hints = new HashSet<Hint>();
				String[] hintsText = ((String) hintMap.get(IndexFields.HINT_TEXT)).split(" ~~ ");
				for (String text : hintsText) {
					Hint hint = new Hint();
					hint.setHintText(text);
					hints.add(hint);
				}
			}
			question.setAnswers(answers);
			question.setHints(hints);
		}
		return question;
	}

}
