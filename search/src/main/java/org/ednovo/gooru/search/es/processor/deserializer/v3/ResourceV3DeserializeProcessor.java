
package org.ednovo.gooru.search.es.processor.deserializer.v3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.ednovo.gooru.search.es.constant.IndexFields;
import org.ednovo.gooru.search.es.model.Hint;
import org.ednovo.gooru.search.es.model.MapWrapper;
import org.ednovo.gooru.search.es.model.QuestionV3;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.ednovo.gooru.search.es.service.SearchSettingService;
import org.ednovo.gooru.search.responses.Metadata;
import org.ednovo.gooru.search.responses.v3.ContentSearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author SearchTeam
 *
 */
@Component
public class ResourceV3DeserializeProcessor extends DeserializeV3Processor<List<ContentSearchResult>, ContentSearchResult> {

	protected static final Logger logger = LoggerFactory.getLogger(ResourceV3DeserializeProcessor.class);
	protected static final String UN_RESTRICTED_SEARCH = "unrestrictedSearch";
	protected static final String ALLOW_DUPLICATES = "allowDuplicates";

	@SuppressWarnings("unchecked")
	@Override
	public List<ContentSearchResult> deserialize(Map<String, Object> model, SearchData searchData, List<ContentSearchResult> output) {
		output = new ArrayList<ContentSearchResult>();
		Set<String> contentUrls = new HashSet<String>();
		Set<String> contentTitles = new HashSet<String>();
		if (model != null && model.get(SEARCH_HITS) != null && !((List<Map<String, Object>>) ((Map<String, Object>) model.get(SEARCH_HITS)).get(SEARCH_HITS)).isEmpty()) {
			List<Map<String, Object>> hits = (List<Map<String, Object>>) ((Map<String, Object>) model.get(SEARCH_HITS)).get(SEARCH_HITS);
			List<String> resourceIds = new ArrayList<String>();
			float minScore = 0;
			int lessScoreResourceCount = 0;
			StringBuilder lessScoreResourceGooruOid = new StringBuilder();

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
							url = url.split(AMPERSAND)[0];
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
					if (!parameters.getBoolean(UN_RESTRICTED_SEARCH) && (Double) searchHit.get(IndexFields._SCORE) < minScore) {
						lessScoreResourceCount++;
						lessScoreResourceGooruOid.append(fields.get(IndexFields.ID) + " , ");
						if (output.size() > 4) {
							continue;
						}
					}
				}
				ContentSearchResult resource = collect(fields, searchData);
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
		return SearchProcessorType.ResourceV3Deserializer;
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	ContentSearchResult collect(Map<String, Object> source, SearchData searchData) {
		String contentFormat = (String) source.get(IndexFields.CONTENT_FORMAT);
		String contentSubFormat = (String) source.get(IndexFields.CONTENT_SUB_FORMAT);

		ContentSearchResult output = null;
		if (contentFormat != null && contentFormat.equalsIgnoreCase(ContentFormat.QUESTION.getContentFormat())) {
			QuestionV3 question = convertToQuestion(source, searchData);
			output = question;
		} else {
			output = new ContentSearchResult();
		}
		
		output.setFormat(contentFormat);
		contentSubFormat = contentSubFormat.replaceAll(UNDERSCORE + TYPE_RESOURCE, EMPTY_STRING);
		contentSubFormat = contentSubFormat.replaceAll(UNDERSCORE + TYPE_QUESTION, EMPTY_STRING);
		output.setSubFormat(contentSubFormat);
		output.setTitle(StringUtils.defaultString((String) source.get(IndexFields.TITLE), EMPTY_STRING));
		if (source.containsKey(IndexFields.URL)) {
			String url = (String) source.get(IndexFields.URL);
			if (!url.startsWith(HTTP)) url = HTTP + COLON + searchData.getContentCdnUrl() + url;
			output.setUrl(url);
		}
		if (source.containsKey(IndexFields.THUMBNAIL)) {
			String thumbnail = (String) source.get(IndexFields.THUMBNAIL);
			if (!thumbnail.startsWith(HTTP)) thumbnail = HTTP + COLON + searchData.getContentCdnUrl() + thumbnail;
			output.setThumbnail(thumbnail);
		}
		output.setId((String) source.get(IndexFields.ID));
		output.setDescription((String) source.get(IndexFields.DESCRIPTION));
		output.setPlayerUrl(SearchSettingService.getByName(DNS_ENV) + "/content/" + output.getFormat() + "s/play/" + output.getId());
		
		// Set metadata
		Metadata metadata = new Metadata();
		String publishStatus = (String) source.get(IndexFields.PUBLISH_STATUS);
		metadata.setCurated(publishStatus.equalsIgnoreCase(PublishedStatus.PUBLISHED.name()) ? true : false);

		if (source.get(IndexFields.METADATA) != null) {
			Map<String, Object> contentMeta = (Map<String, Object>) source.get(IndexFields.METADATA);
			if (contentMeta != null) {
				// depth of knowledge
				if (contentFormat != null && contentFormat.equalsIgnoreCase(SEARCH_QUESTION)) {
					List<String> depthOfKnowledge = new ArrayList<>();
					depthOfKnowledge.add("Level 0");
					if (contentMeta.get(IndexFields.DEPTH_OF_KNOWLEDGE) != null) depthOfKnowledge = (List<String>) contentMeta.get(IndexFields.DEPTH_OF_KNOWLEDGE);
					Collections.sort(depthOfKnowledge);
					metadata.setDok(depthOfKnowledge.get(depthOfKnowledge.size() - 1));
				}

				// 21st century skill
				List<Map<String, String>> twentyOneCenturySkills = (List<Map<String, String>>) contentMeta.get(IndexFields.TWENTY_ONE_CENTURY_SKILL);
				if (twentyOneCenturySkills != null && !twentyOneCenturySkills.isEmpty()) {
					metadata.setTwentyOneCenturySkills(twentyOneCenturySkills);
				}

				List<String> grade = (List<String>) contentMeta.get(IndexFields.GRADE);
				if (grade != null && grade.size() > 0) {
					metadata.setGrade(String.join(COMMA, grade));
				}
			}
		}

		// set creator
		if (source.get(IndexFields.CREATOR) != null) {
			output.setCreator(setUser((Map<String, Object>) source.get(IndexFields.CREATOR), searchData));
		}

		Date date = null;
		try {
			date = SIMPLE_DATE_FORMAT.parse((String) source.get(IndexFields.UPDATED_AT) + EMPTY_STRING);
			String s = SIMPLE_DATE_FORMAT.format(date);
		} catch (Exception e) {
			logger.error("modifiedAt field error: {}" , e);
		}
		output.setModifiedAt(date);
		
		Date createdDate = null;
		try {
			createdDate = SIMPLE_DATE_FORMAT.parse((String) source.get(IndexFields.CREATED_AT) + EMPTY_STRING);
			String s = SIMPLE_DATE_FORMAT.format(createdDate);
		} catch (Exception e) {
			logger.error("createdAt field error: {}" , e);
		}
		output.setCreatedAt(createdDate);
		
		Map<String, Object> accessibilyMap = new HashMap<>();
		Map<String, Object> statisticsMap = (Map<String, Object>) source.get(IndexFields.STATISTICS);
		boolean hasFrameBreaker = false;
		if (statisticsMap != null) {
			try {
				if (statisticsMap.containsKey(IndexFields.HAS_FRAMEBREAKER) && ((Boolean) statisticsMap.get(IndexFields.HAS_FRAMEBREAKER))) {
					hasFrameBreaker = true;
				}
			} catch (Exception e) {
				if (((Integer) statisticsMap.get(IndexFields.HAS_FRAMEBREAKER)) != null && ((Integer) statisticsMap.get(IndexFields.HAS_FRAMEBREAKER)) == 1) {
					hasFrameBreaker = true;
				}
			}
			accessibilyMap.put("framebreaker", hasFrameBreaker);
		}

		Map<String, Object> taxonomyMap = (Map<String, Object>) source.get(IndexFields.TAXONOMY);
		if (taxonomyMap != null) {
			if (searchData.isCrosswalk() && searchData.getUserTaxonomyPreference() != null) {
				long start = System.currentTimeMillis();
				transformTaxonomy(taxonomyMap, searchData, metadata);
				logger.debug("Latency of Taxonomy Transformation : {} ms", (System.currentTimeMillis() - start));
			}
		}
		
		Map<String, Object> infoMap = (Map<String, Object>) source.get(IndexFields.INFO);
		if (infoMap != null) {
			if (infoMap.containsKey(IndexFields.PUBLISHER) && infoMap.get(IndexFields.PUBLISHER) != null) {
				List<String> infoPublisher = (List<String>) infoMap.get(IndexFields.PUBLISHER);
				metadata.setPublisher(infoPublisher);
			}
			if (infoMap.containsKey(IndexFields.LANGUAGE) && infoMap.get(IndexFields.LANGUAGE) != null) {
				String language = (String) infoMap.get(IndexFields.LANGUAGE);
				metadata.setLanguage(language);
			}
		}

		Boolean isCopyrightOwner = source.get(IndexFields.IS_COPYRIGHT_OWNER) == null ? false : (Boolean)source.get(IndexFields.IS_COPYRIGHT_OWNER);
		if (metadata.getPublisher() == null || metadata.getPublisher().isEmpty()) {
			List<String> publisher = new ArrayList<>();
			if (isCopyrightOwner) {
				if (output.getCreator() != null && output.getCreator().getUsername() != null) publisher.add(output.getCreator().getUsername());
			} else if (source.containsKey(IndexFields.COPYRIGHT_OWNER_LIST) && ((List<String>) source.get(IndexFields.COPYRIGHT_OWNER_LIST)).size() > 0) {
				publisher = (List<String>) source.get(IndexFields.COPYRIGHT_OWNER_LIST);
			}
			metadata.setPublisher((publisher != null && publisher.size() > 0) ? publisher : null);
		}
		output.getCreator().setUsername(null);
		
		Map<String, Object> licenseMap = (Map<String, Object>) source.get(IndexFields.LICENSE);
		if (licenseMap != null && !licenseMap.isEmpty()) {
			Map<String, Object> licenseAsMap = new HashMap<>();
			licenseAsMap.put(IndexFields.CODE, licenseMap.get(IndexFields.CODE));
			licenseAsMap.put(IndexFields.URL, licenseMap.get(IndexFields.URL));
			if (!licenseAsMap.isEmpty()) metadata.setLicense(licenseAsMap);
		}

		output.setMetadata(metadata);
		output.setAccessibility(accessibilyMap);
		return output;
	}
	
	@SuppressWarnings("unchecked")
	private QuestionV3 convertToQuestion(Map<String, Object> source, SearchData searchData) {
		QuestionV3 question = new QuestionV3();
		Map<String, Object> questionMap = (Map<String, Object>) (source.get(IndexFields.QUESTION));
		if (questionMap != null) {
			question.setExplanation((String) questionMap.get(IndexFields.EXPLANATION));

			Set<String> answers = new HashSet<String>();
			Map<String, Object> answerMap = (Map<String, Object>) (questionMap.get(IndexFields.ANSWER));
			if ((answerMap != null) && (answerMap.get(IndexFields.ANSWER_TEXT) != null)) {
				String[] answersText = ((String) answerMap.get(IndexFields.ANSWER_TEXT)).split(" ~~ ");
				for (String text : answersText) {
					String subFormat = (String) source.get(IndexFields.CONTENT_SUB_FORMAT);
					if (subFormat.equalsIgnoreCase(HOT_SPOT_IMAGE_QUESTION) && !text.startsWith(HTTP)) text = HTTP + COLON + searchData.getContentCdnUrl() + text;
					answers.add(text);
				}
			}
			Map<String, Object> hintMap = (Map<String, Object>) (source.get(IndexFields.HINTS));
			Set<Hint> hints = new HashSet<Hint>();
			if ((hintMap != null) && (hintMap.get(IndexFields.HINT_TEXT) != null)) {
				String[] hintsText = ((String) hintMap.get(IndexFields.HINT_TEXT)).split(" ~~ ");
				for (String text : hintsText) {
					Hint hint = new Hint();
					hint.setHintText(text);
					hints.add(hint);
				}
			}
			question.setAnswer(answers);
			question.setHints(hints);
		}
		return question;
	}

}
