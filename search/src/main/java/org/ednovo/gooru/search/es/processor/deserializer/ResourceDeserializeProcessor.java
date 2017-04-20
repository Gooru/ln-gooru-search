
package org.ednovo.gooru.search.es.processor.deserializer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.ednovo.gooru.search.es.constant.IndexFields;
import org.ednovo.gooru.search.es.constant.SearchFilterConstants;
import org.ednovo.gooru.search.es.model.Answer;
import org.ednovo.gooru.search.es.model.ContentFormat;
import org.ednovo.gooru.search.es.model.ContentSearchResult;
import org.ednovo.gooru.search.es.model.Hint;
import org.ednovo.gooru.search.es.model.MapWrapper;
import org.ednovo.gooru.search.es.model.Question;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.User;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author SearchTeam
 *
 */
@Component
public class ResourceDeserializeProcessor extends DeserializeProcessor<List<ContentSearchResult>, ContentSearchResult> {

	protected static final Logger logger = LoggerFactory.getLogger(ResourceDeserializeProcessor.class);
	protected static final String UN_RESTRICTED_SEARCH = "unrestrictedSearch";
	protected static final String ALLOW_DUPLICATES = "allowDuplicates";

	@SuppressWarnings("unchecked")
	@Override
	public List<ContentSearchResult> deserialize(Map<String, Object> model, SearchData searchData, List<ContentSearchResult> output) {
		output = new ArrayList<ContentSearchResult>();
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
			// FIX ME enable for resource format
			/*
			 * if (hits.size() > 0) { String configName = "S_RESOURCE_QUERY_MINSCORE_RESOURCE_FORMAT_" + (((Map<String,Object>) hits.get(0).get("fields")).get("resourceFormat") +"").toUpperCase();
			 * minScore = getResourceFormatSettingAsFloat(configName); }
			 */

			for (Map<String, Object> searchHit : hits) {
				if (searchHit.get(IndexFields._SOURCE) == null) {
					// FIXME, Can this happen?
					continue;
				}
				Map<String, Object> fields = (Map<String, Object>) searchHit.get(IndexFields._SOURCE);
				String resultUId = java.util.UUID.randomUUID().toString();
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
				ContentSearchResult resource = collect(fields, searchData, null);
				resourceIds.add(resource.getGooruOid());
				resource.setResultUId(resultUId);
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
		return SearchProcessorType.ResourceDeserializer;
	}

	@Override
	ContentSearchResult collect(Map<String, Object> model, SearchData input, ContentSearchResult resource) {
		return deserializeToResource(model, input);
	}

	@SuppressWarnings("unchecked")
	private ContentSearchResult deserializeToResource(Map<String, Object> dataMap, SearchData input) {
		String contentFormat = (String) dataMap.get(IndexFields.CONTENT_FORMAT);
		String contentSubFormat = (String) dataMap.get(IndexFields.CONTENT_SUB_FORMAT);

		ContentSearchResult resource = null;

		if (contentFormat != null && contentFormat.equalsIgnoreCase(ContentFormat.QUESTION.getContentFormat())) {
			Question question = convertToQuestion(dataMap);
			resource = question;
			Map<String, String> resourceFormatValueAsMap = new HashMap<>(1);
			resourceFormatValueAsMap.put(VALUE, contentFormat);
			resource.setResourceFormat(resourceFormatValueAsMap);
			resource.setCategory(ContentFormat.QUESTION.getContentFormat());
		} else {
			resource = new ContentSearchResult();
			Map<String, String> resourceFormatValueAsMap = new HashMap<>(1);
			 String resFormat = SearchFilterConstants.isInSubFormatResponseKeySet(contentSubFormat) ? SearchFilterConstants.getSubFormatResponseValue(contentSubFormat) : contentSubFormat;
			resourceFormatValueAsMap.put(VALUE, resFormat);
			resource.setResourceFormat(resourceFormatValueAsMap);
			resource.setCategory(resFormat);
		}
		
		resource.setContentFormat(contentFormat);
		resource.setContentSubFormat(contentSubFormat);
		resource.setTitle(StringUtils.defaultString((String) dataMap.get(IndexFields.TITLE), ""));
		resource.setUrl((String) dataMap.get(IndexFields.URL));
		resource.setThumbnail((String) dataMap.get(IndexFields.THUMBNAIL));
		resource.setGooruOid((String) dataMap.get(IndexFields.ID));
		resource.setResultUId(java.util.UUID.randomUUID().toString());
		resource.setDescription((String) dataMap.get(IndexFields.DESCRIPTION));
		String publishStatus = (String) dataMap.get(IndexFields.PUBLISH_STATUS);
		resource.setSharing((publishStatus != null && publishStatus.equalsIgnoreCase("published")) ? "public" : "private");
		resource.setPublishStatus(publishStatus);

		// Set assest uri to handle 2.0 request 
		if(input.getContentCdnUrl() != null){
			resource.setAssetURI(input.getContentCdnUrl());
			resource.setFolder("");
			
			Map<String,Object> thumbnails = new HashMap<>();
			thumbnails.put(THUMBNAIL_URL, resource.getAssetURI() + resource.getThumbnail());
			resource.setThumbnails(thumbnails);
		}

		// Set metadata
		if (dataMap.get(IndexFields.METADATA) != null) {
			Map<String, List<String>> metadata = (Map<String, List<String>>) dataMap.get(IndexFields.METADATA);
			if (metadata != null) {
				// depth of knowledge
				if (contentFormat != null && contentFormat.equalsIgnoreCase(SEARCH_QUESTION)) {
					List<String> depthofknwoledge = metadata.get(IndexFields.DEPTH_OF_KNOWLEDGE);
					if (depthofknwoledge != null) {
						resource.setDepthOfKnowledges(depthofknwoledge);
					}
				}
				// educationl use
				List<String> educationalUse = metadata.get(IndexFields.EDUCATIONAL_USE);
				if (educationalUse != null) {
					resource.setEducationalUse(educationalUse);
				}
				// moments of learning
				if (contentFormat != null && !contentFormat.equalsIgnoreCase(SEARCH_QUESTION)) {
					List<String> momentsOflearning = metadata.get(IndexFields.MOMENTS_OF_LEARNING);
					if (momentsOflearning != null) {
						resource.setMomentsOfLearning(momentsOflearning);
					}
				}
				
				// 21st century skill
				List<String> twentyOneCenturySkills = metadata.get(IndexFields.TWENTY_ONE_CENTURY_SKILL);
				if (twentyOneCenturySkills != null && !twentyOneCenturySkills.isEmpty()) {
					resource.setTwentyOneCenturySkills(twentyOneCenturySkills);
				}

				List<String> grade = metadata.get(IndexFields.GRADE);
				if(grade != null && grade.size() > 0){
					resource.setGrade(String.join(SEARCH_COMMA_SEPERTOR, grade));
				}
			}
		}

		Map<String, Object> creatorMap = (Map<String, Object>) dataMap.get(IndexFields.ORIGINAL_CREATOR);
		User creator = new User();
		if (creatorMap != null) {
			creator.setFirstName((String) creatorMap.get(IndexFields.FIRST_NAME));
			creator.setLastName((String) creatorMap.get(IndexFields.LAST_NAME));
			creator.setUsername((String) creatorMap.get(IndexFields.USERNAME));
			creator.setPartyUid((String) creatorMap.get(IndexFields.USER_ID));
			creator.setGooruUId((String) creatorMap.get(IndexFields.USER_ID));
			creator.setProfileImageUrl((String) creatorMap.get(IndexFields.PROFILE_IMAGE));
		}
		resource.setCreator(creator);

		Map<String, Object> ownerMap = (Map<String, Object>) dataMap.get(IndexFields.CREATOR);
		User user = new User();
		if (ownerMap != null) {
			user.setFirstName((String) ownerMap.get(IndexFields.FIRST_NAME));
			user.setLastName((String) ownerMap.get(IndexFields.LAST_NAME));
			user.setUsername((String) ownerMap.get(IndexFields.USERNAME));
			user.setPartyUid((String) ownerMap.get(IndexFields.USER_ID));
			user.setGooruUId((String) ownerMap.get(IndexFields.USER_ID));
			user.setProfileImageUrl((String) ownerMap.get(IndexFields.PROFILE_IMAGE));
		}
		resource.setUser(user);
		if (creatorMap == null && ownerMap != null) {
			resource.setCreator(user);
		}

		Date date = null;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		try {
			date = simpleDateFormat.parse((String) dataMap.get(IndexFields.UPDATED_AT) + "");
			String s = simpleDateFormat.format(date);
		} catch (Exception e) {
			logger.error("lastModified field error: " + e);
		}
		resource.setLastModified(date);
		if (dataMap.get(IndexFields.UPDATED_AT) instanceof Long) {
			Long lastModified = (Long) dataMap.get(IndexFields.UPDATED_AT);
			resource.setLastModifiedString(lastModified.toString());
		} else {
			resource.setLastModifiedString((String) dataMap.get(IndexFields.UPDATED_AT));
		}
		try {
			resource.setAddDate(simpleDateFormat.parse((String) dataMap.get(IndexFields.CREATED_AT)));
		} catch (Exception e) {
			LOG.debug("Error while parsing date", (String) dataMap.get(IndexFields.CREATED_AT));
		}

		// resource.setMediaType((String) dataMap.get(SEARCH_MEDIA_TYPE));

		Map<String, Object> statisticsMap = (Map<String, Object>) dataMap.get(IndexFields.STATISTICS);
		boolean hasFrameBreaker = false;
		if (statisticsMap != null) {
			long viewsCount = 0L;
			if (statisticsMap.get(IndexFields.VIEWS_COUNT) != null) {
				viewsCount = ((Number) statisticsMap.get(IndexFields.VIEWS_COUNT)).longValue();
				resource.setViewCount(viewsCount);
				resource.setViews(viewsCount);
			}

			if (((Integer) statisticsMap.get(SEARCH_HAS_FRAMEBREAKER)) != null && ((Integer) statisticsMap.get(SEARCH_HAS_FRAMEBREAKER)) == 1) {
				hasFrameBreaker = true;
			}
			resource.setBrokenStatus((statisticsMap.get(SEARCH_STATUS_BROKEN) != null) ? (Integer) statisticsMap.get(SEARCH_STATUS_BROKEN) : 0);
			resource.setHasFrameBreaker(hasFrameBreaker);
			Integer collectionCount = 0;
			if (statisticsMap.get(IndexFields.USED_IN_COLLECTION_COUNT) != null) {
				collectionCount = (Integer) statisticsMap.get(IndexFields.USED_IN_COLLECTION_COUNT);
			}
			resource.setCollectionCount(collectionCount);
			resource.setResourceAddedCount(collectionCount);
			if (statisticsMap.get(IndexFields.COLLABORATOR_COUNT) != null) {
				resource.setCollaboratorCount(0);
			}
		}

		Map<String, Object> taxonomyMap = (Map<String, Object>) dataMap.get(IndexFields.TAXONOMY);
		if (taxonomyMap != null) {
			Map<String, Object> taxonomySetAsMap = (Map<String, Object>) taxonomyMap.get(IndexFields.TAXONOMY_SET);
			if (input.isStandardsSearch() && input.isCrosswalk()) {
				setCrosswalkData(input, resource, taxonomyMap);
			} else if (input.getUserTaxonomyPreference() != null) {
				long start = System.currentTimeMillis();
				taxonomySetAsMap = transformTaxonomy(taxonomyMap, input);
				logger.debug("Latency of Taxonomy Transformation : {} ms", (System.currentTimeMillis() - start));
			}
			resource.setTaxonomySet(taxonomySetAsMap);		
			resource.setTaxonomyDataSet((String) taxonomyMap.get(IndexFields.TAXONOMY_DATA_SET));
		}

		if (dataMap.get(IndexFields.COLLECTION_TITLES) != null) {
			resource.setScollectionTitles((List<String>) dataMap.get(IndexFields.COLLECTION_TITLES));
		}
		if (dataMap.get(IndexFields.COLLECTION_IDS) != null) {
			resource.setScollectionIds((List<String>) dataMap.get(IndexFields.COLLECTION_IDS));
		}
		
		Map<String, Object> courseMap = (Map<String, Object>) dataMap.get(IndexFields.COURSE);
		if (courseMap != null && !courseMap.isEmpty()) {
			resource.setCourse(courseMap);
		}

		Map<String, Object> licenseMap = (Map<String, Object>) dataMap.get(IndexFields.LICENSE);
		if (licenseMap != null && !licenseMap.isEmpty()) {
			Map<String, Object> licenseAsMap = new HashMap<>();
			licenseAsMap.put(IndexFields.CODE, licenseMap.get(IndexFields.CODE));
			licenseAsMap.put(IndexFields.ICON, licenseMap.get(IndexFields.ICON));
			licenseAsMap.put(IndexFields.NAME, licenseMap.get(IndexFields.NAME));
			licenseAsMap.put(IndexFields.URL, licenseMap.get(IndexFields.URL));
			resource.setLicense(licenseAsMap);
		}
	
		Map<String, Object> infoMap = (Map<String, Object>) dataMap.get(IndexFields.INFO);
		Map<String, Object> resourceSource = new HashMap<>();
		Map<String, Object> domain = null;
		if (infoMap != null) {
			if (infoMap.containsKey(IndexFields.DOMAIN) && infoMap.get(IndexFields.DOMAIN) != null) {
				domain = (Map<String, Object>) infoMap.get(IndexFields.DOMAIN);
			}
			if (infoMap.containsKey(IndexFields.PUBLISHER) && infoMap.get(IndexFields.PUBLISHER) != null) {
				List<String> infoPublisher = (List<String>) infoMap.get(IndexFields.PUBLISHER);
				resource.setPublisher(infoPublisher);
			}
			if (infoMap.containsKey(IndexFields.AGGREGATOR) && infoMap.get(IndexFields.AGGREGATOR) != null) {
				List<String> aggregator = (List<String>) infoMap.get(IndexFields.AGGREGATOR);
				resource.setAggregator(aggregator);
			}
			if (infoMap.containsKey(IndexFields.OER) && infoMap.get(IndexFields.OER) != null) {
				resource.setIsOer((Integer) infoMap.get(IndexFields.OER));
			}
			if (infoMap.containsKey(IndexFields.MOBILE_FRIENDLINESS) && infoMap.get(IndexFields.MOBILE_FRIENDLINESS) != null) {
				resource.setMediaType((String) infoMap.get(IndexFields.MOBILE_FRIENDLINESS));
			}
		}
		resource.setCustomFields(infoMap);

		Boolean isCopyrightOwner = dataMap.get(IndexFields.IS_COPYRIGHT_OWNER) == null ? false : (Boolean)dataMap.get(IndexFields.IS_COPYRIGHT_OWNER);
		if (resource.getPublisher() == null || resource.getPublisher().isEmpty()) {
			List<String> publisher = new ArrayList<>();
			if (isCopyrightOwner) {
				if (user != null && user.getUsername() != null) publisher.add(user.getUsername());
			} else if (dataMap.containsKey(IndexFields.COPYRIGHT_OWNER_LIST) && ((List<String>) dataMap.get(IndexFields.COPYRIGHT_OWNER_LIST)).size() > 0) {
				publisher = (List<String>) dataMap.get(IndexFields.COPYRIGHT_OWNER_LIST);
			}
			resource.setPublisher((publisher != null && publisher.size() > 0) ? publisher : null);
		}

		resourceSource.put("attribution", domain != null ? domain.get(IndexFields.ATTRIBUTION) : null);
		resourceSource.put("domainName", domain != null ? domain.get(IndexFields.NAME) : null);
		resourceSource.put("url", domain != null ? domain.get(IndexFields.URL) : null);
		resourceSource.put("sourceName", (infoMap != null && infoMap.containsKey(IndexFields.RESOURCE_SOURCE_NAME)) ? infoMap.get(IndexFields.RESOURCE_SOURCE_NAME) : null);
		resourceSource.put("activeStatus", 0);
		resourceSource.put("frameBreaker", hasFrameBreaker);
		resourceSource.put("resourceSourceId", 0);
		resourceSource.put("type", null);
		resource.setResourceSource(resourceSource);

		Map<String, Object> ratingsAsMap = new HashMap<>();
		ratingsAsMap.put("count", 0);
		ratingsAsMap.put("reviewCount", 0);
		ratingsAsMap.put("average", 0);
		resource.setRatings(ratingsAsMap);

		return resource;
	}
	
	@SuppressWarnings("unchecked")
	private void setCrosswalkData(SearchData input, ContentSearchResult resource, Map<String, Object> taxonomyMap) {
		String fltStandard = null;
		String fltStandardDisplay = null;
		if(input.getFilters().containsKey(AMPERSAND_EQ_INTERNAL_CODE)) fltStandard = input.getFilters().get(AMPERSAND_EQ_INTERNAL_CODE).toString();
		if(input.getFilters().containsKey(AMPERSAND_EQ_DISPLAY_CODE)) fltStandardDisplay = input.getFilters().get(AMPERSAND_EQ_DISPLAY_CODE).toString();
		Boolean isCrosswalked = false;
		List<String> leafInternalCodes = (List<String>) taxonomyMap.get(IndexFields.LEAF_INTERNAL_CODES);
		List<String> leafDisplayCodes = (List<String>) taxonomyMap.get(IndexFields.LEAF_DISPLAY_CODES);
		List<Map<String, Object>> equivalentCompetencies = (List<Map<String, Object>>) taxonomyMap.get(IndexFields.EQUIVALENT_COMPETENCIES);

		if (!(leafInternalCodes != null && leafInternalCodes.size() > 0 && fltStandard != null && leafInternalCodes.contains(fltStandard.toUpperCase()))
				&& !(leafDisplayCodes != null && leafDisplayCodes.size() > 0 && fltStandardDisplay != null && leafDisplayCodes.contains(fltStandardDisplay.toUpperCase()))) {
			isCrosswalked = true;
		}
		resource.setIsCrosswalked(isCrosswalked);
		resource.setTaxonomyEquivalentCompetencies(equivalentCompetencies);
	}

	@SuppressWarnings("unchecked")
	private Question convertToQuestion(Map<String, Object> source) {
		Question question = new Question();
		Map<String, Object> questionMap = (Map<String, Object>) (source.get(IndexFields.QUESTION));
		question.setGooruOid((String) source.get(IndexFields.ID));
		question.setQuestionText((String) source.get(IndexFields.TITLE));
		question.setTypeName((String) source.get(IndexFields.CONTENT_SUB_FORMAT));
		if (questionMap != null) {
			question.setExplanation((String) questionMap.get(IndexFields.EXPLANATION));

			Set<Answer> answers = new HashSet<Answer>();
			Map<String, Object> answerMap = (Map<String, Object>) (questionMap.get(IndexFields.ANSWER));
			if ((answerMap != null) && (answerMap.get(IndexFields.ANSWER_TEXT) != null)) {
				String[] answersText = ((String) answerMap.get(IndexFields.ANSWER_TEXT)).split(" ~~ ");
				for (String text : answersText) {
					Answer answer = new Answer();
					answer.setAnswerText(text);
					answers.add(answer);
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
			question.setAnswers(answers);
			question.setHints(hints);
		}
		return question;
	}
}
