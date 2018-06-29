
package org.ednovo.gooru.search.es.processor.deserializer.v3;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
	protected static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

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
				ContentSearchResult resource = collect(fields, searchData, null);
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

	@Override
	ContentSearchResult collect(Map<String, Object> model, SearchData input, ContentSearchResult resource) {
		return deserializeToResource(model, input);
	}

	@SuppressWarnings({ "unchecked", "unused" })
	private ContentSearchResult deserializeToResource(Map<String, Object> dataMap, SearchData input) {
		String contentFormat = (String) dataMap.get(IndexFields.CONTENT_FORMAT);
		String contentSubFormat = (String) dataMap.get(IndexFields.CONTENT_SUB_FORMAT);

		ContentSearchResult resource = null;
		if (contentFormat != null && contentFormat.equalsIgnoreCase(ContentFormat.QUESTION.getContentFormat())) {
			QuestionV3 question = convertToQuestion(dataMap, input);
			resource = question;
		} else {
			resource = new ContentSearchResult();
		}
		
		resource.setFormat(contentFormat);
		resource.setSubFormat(contentSubFormat);
		resource.setTitle(StringUtils.defaultString((String) dataMap.get(IndexFields.TITLE), EMPTY_STRING));
		if (dataMap.containsKey(IndexFields.URL)) {
			String url = (String) dataMap.get(IndexFields.URL);
			if (!url.startsWith(HTTP)) url = HTTP + COLON + input.getContentCdnUrl() + url;
			resource.setUrl(url);
		}
		if (dataMap.containsKey(IndexFields.THUMBNAIL)) {
			String thumbnail = (String) dataMap.get(IndexFields.THUMBNAIL);
			if (!thumbnail.startsWith(HTTP)) thumbnail = HTTP + COLON + input.getContentCdnUrl() + thumbnail;
			resource.setThumbnail(thumbnail);
		}
		resource.setId((String) dataMap.get(IndexFields.ID));
		resource.setDescription((String) dataMap.get(IndexFields.DESCRIPTION));
		resource.setPlayerUrl(SearchSettingService.getByName(DNS_ENV) + "/content/" + resource.getFormat() + "s/play/" + resource.getId());
		
		// Set metadata
		Metadata metadata = new Metadata();
		String publishStatus = (String) dataMap.get(IndexFields.PUBLISH_STATUS);
		metadata.setCurated(publishStatus.equalsIgnoreCase(PublishedStatus.PUBLISHED.name()) ? true : false);

		if (dataMap.get(IndexFields.METADATA) != null) {
			Map<String, List<String>> contentMeta = (Map<String, List<String>>) dataMap.get(IndexFields.METADATA);
			if (contentMeta != null) {
				// depth of knowledge
				if (contentFormat != null && contentFormat.equalsIgnoreCase(SEARCH_QUESTION)) {
					List<String> depthOfKnowledge = new ArrayList<>();
					depthOfKnowledge.add("Level 0");
					if (contentMeta.get(IndexFields.DEPTH_OF_KNOWLEDGE) != null) depthOfKnowledge = contentMeta.get(IndexFields.DEPTH_OF_KNOWLEDGE);
					metadata.setDok(depthOfKnowledge);
				}

				// 21st century skill
				List<String> twentyOneCenturySkills = contentMeta.get(IndexFields.TWENTY_ONE_CENTURY_SKILL);
				if (twentyOneCenturySkills != null && !twentyOneCenturySkills.isEmpty()) {
					metadata.setTwentyOneCenturySkills(twentyOneCenturySkills);
				}

				List<String> grade = contentMeta.get(IndexFields.GRADE);
				if (grade != null && grade.size() > 0) {
					metadata.setGrade(String.join(COMMA, grade));
				}
			}
		}

		// set creator
		if (dataMap.get(IndexFields.CREATOR) != null) {
			resource.setCreator(setUser((Map<String, Object>) dataMap.get(IndexFields.CREATOR), input));
		}

		Date date = null;
		try {
			date = SIMPLE_DATE_FORMAT.parse((String) dataMap.get(IndexFields.UPDATED_AT) + EMPTY_STRING);
			String s = SIMPLE_DATE_FORMAT.format(date);
		} catch (Exception e) {
			logger.error("modifiedDate field error: " + e);
		}
		resource.setModifiedAt(date);
		
		Date createdDate = null;
		try {
			createdDate = SIMPLE_DATE_FORMAT.parse((String) dataMap.get(IndexFields.CREATED_AT) + EMPTY_STRING);
			String s = SIMPLE_DATE_FORMAT.format(createdDate);
		} catch (Exception e) {
			logger.error("createdDate field error: " + e);
		}
		resource.setCreatedAt(createdDate);
		
		Map<String, Object> accessibilyMap = new HashMap<>();
		Map<String, Object> statisticsMap = (Map<String, Object>) dataMap.get(IndexFields.STATISTICS);
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

		Map<String, Object> taxonomyMap = (Map<String, Object>) dataMap.get(IndexFields.TAXONOMY);
		if (taxonomyMap != null) {
			Map<String, Object> taxonomySetAsMap = (Map<String, Object>) taxonomyMap.get(IndexFields.TAXONOMY_SET);
			if (input.isCrosswalk()) {
				if (input.getUserTaxonomyPreference() != null) {
					long start = System.currentTimeMillis();
					taxonomySetAsMap = transformTaxonomy(taxonomyMap, input);
					logger.debug("Latency of Taxonomy Transformation : {} ms", (System.currentTimeMillis() - start));
				}
			}
			if (taxonomySetAsMap.get(IndexFields.TAXONOMY_SET) != null && !((Map<String, Object>) taxonomySetAsMap.get(IndexFields.TAXONOMY_SET)).isEmpty()) metadata.setStandards((Map<String, Object>) taxonomySetAsMap.get(IndexFields.TAXONOMY_SET));	
			if (taxonomySetAsMap.get(IndexFields.SUBJECT) != null && !((List<String>) taxonomySetAsMap.get(IndexFields.SUBJECT)).isEmpty()) metadata.setSubject((List<String>) taxonomySetAsMap.get(IndexFields.SUBJECT));		
			if (taxonomySetAsMap.get(IndexFields.COURSE) != null && !((List<String>) taxonomySetAsMap.get(IndexFields.COURSE)).isEmpty()) metadata.setCourse((List<String>) taxonomySetAsMap.get(IndexFields.COURSE));		
			if (taxonomySetAsMap.get(IndexFields.DOMAIN) != null && !((List<String>) taxonomySetAsMap.get(IndexFields.DOMAIN)).isEmpty()) metadata.setDomain((List<String>) taxonomySetAsMap.get(IndexFields.DOMAIN));		

		}
		
		Map<String, Object> infoMap = (Map<String, Object>) dataMap.get(IndexFields.INFO);
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

		Boolean isCopyrightOwner = dataMap.get(IndexFields.IS_COPYRIGHT_OWNER) == null ? false : (Boolean)dataMap.get(IndexFields.IS_COPYRIGHT_OWNER);
		if (metadata.getPublisher() == null || metadata.getPublisher().isEmpty()) {
			List<String> publisher = new ArrayList<>();
			if (isCopyrightOwner) {
				if (resource.getCreator() != null && resource.getCreator().getUsername() != null) publisher.add(resource.getCreator().getUsername());
			} else if (dataMap.containsKey(IndexFields.COPYRIGHT_OWNER_LIST) && ((List<String>) dataMap.get(IndexFields.COPYRIGHT_OWNER_LIST)).size() > 0) {
				publisher = (List<String>) dataMap.get(IndexFields.COPYRIGHT_OWNER_LIST);
			}
			metadata.setPublisher((publisher != null && publisher.size() > 0) ? publisher : null);
		}
		resource.getCreator().setUsername(null);
		
		Map<String, Object> licenseMap = (Map<String, Object>) dataMap.get(IndexFields.LICENSE);
		if (licenseMap != null && !licenseMap.isEmpty()) {
			Map<String, Object> licenseAsMap = new HashMap<>();
			licenseAsMap.put(IndexFields.CODE, licenseMap.get(IndexFields.CODE));
			licenseAsMap.put(IndexFields.URL, licenseMap.get(IndexFields.URL));
			metadata.setLicense(licenseAsMap);
		}

		resource.setMetadata(metadata);
		resource.setAccessibility(accessibilyMap);
		return resource;
	}
	
	@SuppressWarnings("unchecked")
	private QuestionV3 convertToQuestion(Map<String, Object> source, SearchData input) {
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
					if (subFormat.equalsIgnoreCase(HOT_SPOT_IMAGE_QUESTION) && !text.startsWith(HTTP)) text = HTTP + COLON + input.getContentCdnUrl() + text;
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
