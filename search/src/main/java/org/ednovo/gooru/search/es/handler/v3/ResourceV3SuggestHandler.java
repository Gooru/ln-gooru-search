/**
 * 
 */
package org.ednovo.gooru.search.es.handler.v3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.ednovo.gooru.search.es.constant.EsIndex;
import org.ednovo.gooru.search.es.exception.SearchException;
import org.ednovo.gooru.search.es.handler.SuggestDataProviderType;
import org.ednovo.gooru.search.es.model.ContentSearchResult;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.ednovo.gooru.search.es.model.SuggestData;
import org.ednovo.gooru.search.es.model.SuggestResponse;
import org.ednovo.gooru.search.es.processor.ElasticsearchProcessor;
import org.ednovo.gooru.search.es.processor.deserializer.ResourceDeserializeProcessor;
import org.ednovo.gooru.search.es.processor.filter.constructor.FacetFilterConstructionProcessor;
import org.ednovo.gooru.search.es.processor.query_builder.ResourceEsDslQueryBuildProcessor;
import org.ednovo.gooru.search.model.ActivityStreamRawData;
import org.ednovo.gooru.search.model.CollectionContextData;
import org.ednovo.gooru.search.model.ResourceContextData;
import org.ednovo.gooru.search.model.UserPreferenceData;
import org.ednovo.gooru.search.model.UserProficiencyData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResourceV3SuggestHandler extends SuggestHandler<Map<String, Object>> {
	
  private static final String RESOURCE_REMOVE = "item.delete";

	private static Integer threadPoolLength = 3;
    
  private ExecutorService doerService;
    
	private static final String COLLECTION_EDIT = "collection-edit";
	
	private static final String DISLIKE = "dislike";
	
	private static final String RESOURCE_PLAY_ACTIVITY = "resource.play";

	private static final String COLLECTION_ITEM_PLAY_ACTIVITY = "collection.resource.play";
	
	private static final String RESOURCE_SUGGEST = "resource-suggest";
	
	private static final String NEGATIVE_REACTION = "negative-reaction";
	
	private static final String SIMILAR_CONTENT = "similar-content";
	
  private static final String OR_DELIMETER = "~~@@";
    
	@Autowired
	private ElasticsearchProcessor elasticSearchProcessor;
	
	@Autowired
	private ResourceEsDslQueryBuildProcessor resourceEsDslQueryProcessor;
	
	@Autowired
	private ResourceDeserializeProcessor resourceDeserializeProcessor;
	
	@Autowired
	private FacetFilterConstructionProcessor facetFilterConsProcessor;
	
	private SuggestDataProviderType [] suggestDataProviders = {SuggestDataProviderType.RESOURCE, SuggestDataProviderType.SCOLLECTION, SuggestDataProviderType.USER_PREFERENCE, SuggestDataProviderType.USER_PROFICIENCY, SuggestDataProviderType.USER_ACTIVITY};
	
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
    final List<String> recentlyRemovedResIds = new ArrayList<String>();
    final List<String> recentlyPlayedResIds = new ArrayList<String>();
    final CollectionContextData collectionData = (CollectionContextData) dataProviderInput.get(SuggestDataProviderType.SCOLLECTION);
    final UserPreferenceData preferenceData = (UserPreferenceData) dataProviderInput.get(SuggestDataProviderType.USER_PREFERENCE);
    final UserProficiencyData proficiencyData = (UserProficiencyData) dataProviderInput.get(SuggestDataProviderType.USER_PROFICIENCY);
    final ResourceContextData resourceData = (ResourceContextData) dataProviderInput.get(SuggestDataProviderType.RESOURCE);
    final List<ActivityStreamRawData> activityList = (List<ActivityStreamRawData>) dataProviderInput.get(SuggestDataProviderType.USER_ACTIVITY);

    if ((activityList != null && activityList.size() > 0)) {
      for (ActivityStreamRawData activityData : activityList) {
        if (collectionData != null && activityData.getCollectionGooruOid().equals(collectionData.getCollectionGooruOid())
                && activityData.getEventName().equalsIgnoreCase(RESOURCE_REMOVE)) {
          recentlyRemovedResIds.add(activityData.getResourceGooruOid());
        }
        if (resourceData != null && (activityData.getEventName().equalsIgnoreCase(COLLECTION_ITEM_PLAY_ACTIVITY)
                || activityData.getEventName().equalsIgnoreCase(RESOURCE_PLAY_ACTIVITY)) && activityData.getResourceGooruOid() != null) {
          recentlyPlayedResIds.add(activityData.getResourceGooruOid());
        }
      }
    }

    final SearchResponse<List<ContentSearchResult>> searchResponseResource = new SearchResponse<List<ContentSearchResult>>();
    final SearchResponse<Object> searchRes = new SearchResponse<Object>();
    final String eventName = suggestData.getSuggestV3Context().getContext();

    tasks.add(new Callable<SuggestResponse<Object>>() {

      @Override
      public SuggestResponse<Object> call() throws Exception {
        try {
          boolean addPreference = false;
          boolean addProficiency = false;
          String queryString = "*";
          if (eventName.equalsIgnoreCase(COLLECTION_EDIT)) {
            if (collectionData != null) {

              suggestData.putFilter("&^publishStatus", "published");

              if (recentlyRemovedResIds.size() > 0) {
                collectionData.getCollectionResourceIds().addAll(recentlyRemovedResIds);
              }
              if (collectionData.getCollectionResourceIds() != null && collectionData.getCollectionResourceIds().size() > 0) {
                suggestData.putFilter("!^id", StringUtils.join(collectionData.getCollectionResourceIds(), ","));
              }
              // suggestData.putFilter("gte-usedinscollectioncount","1" ) ;

              // Search query formation: If query string is null in the request,
              // build the search query with collection title and course title
              // of the given collection info.
              if (StringUtils.isNotBlank(suggestData.getQueryString())) {
                queryString = suggestData.getQueryString().trim();
              } else {
                // Fetch results either matched with collection title OR course
                // title.
                String suggestQuery = null;
                if (StringUtils.isNotBlank(collectionData.getCollectionTitle())) {
                  suggestQuery = collectionData.getCollectionTitle().trim();
                }
                if (StringUtils.isNotBlank(collectionData.getCollectionCourseTitle())) {
                  suggestQuery += OR_DELIMETER + collectionData.getCollectionCourseTitle().trim();
                }

                if (suggestQuery != null) {
                  queryString = suggestQuery;
                } else {
                  queryString = "*";
                }
              }
              if (collectionData.getResourceTitles() != null && collectionData.getResourceTitles().size() > 0) {
                suggestData.putFilter("!^title", collectionData.getResourceTitles());
              }
              /*
               * if(collectionData.getCollectionCategory().equalsIgnoreCase(
               * ONLY_QUESTION)){ suggestData.putFilter("&^resourceFormat",
               * "question"); } else
               * if(suggestData.getSuggestContext().getCategory() != null){
               * suggestData.putFilter("&^resourceFormat",
               * suggestData.getSuggestContext().getCategory().toLowerCase()); }
               */

              /*
               * if (collectionData.getCollectionCourseTitle() != null) {
               * if(queryString != null && !queryString.equalsIgnoreCase("*"))
               * queryString += " "+collectionData.getCollectionCourseTitle(); }
               */

              if (collectionData.getCollectionCourseId() != null) {
                suggestData.putFilter("!^course.id", collectionData.getCollectionCourseId());
              }
              StringBuilder orFilterData = new StringBuilder();
              if (collectionData.getCollectionTaxonomyCourseId() != null && collectionData.getCollectionTaxonomyCourseId().size() > 0) {
                orFilterData.append("taxonomy.course.codeId:" + StringUtils.join(collectionData.getCollectionTaxonomyCourseId(), ","));
              } else if (collectionData.getCollectionTaxonomySubjectId() != null && collectionData.getCollectionTaxonomySubjectId().size() > 0) {
                orFilterData.append("taxonomy.subject.codeId:" + StringUtils.join(collectionData.getCollectionTaxonomySubjectId(), ","));
              }

              if (collectionData.getCollectionStandards() != null && collectionData.getCollectionStandards().size() > 0) {
                if (orFilterData.length() > 0) {
                  orFilterData.append("|");
                }
                orFilterData.append("taxonomy.leafInternalCodes:" + StringUtils.join(collectionData.getCollectionStandards(), ","));
              }

              if (orFilterData.length() > 0) {
                suggestData.putFilter("&^orFilters", orFilterData.toString());
              }
              addPreference = false;
              addProficiency = false;
            }

          }

          if (eventName.equalsIgnoreCase(DISLIKE)) {
            suggestData.putFilter("", "");
          }
          if (eventName.equalsIgnoreCase(NEGATIVE_REACTION) && resourceData != null) {

            if (StringUtils.trimToNull(resourceData.getResourceTitle()) != null) {
              queryString = resourceData.getResourceTitle().trim();
            }
            List<String> notIncludeResourceIds = new ArrayList<String>();
            notIncludeResourceIds.addAll(recentlyPlayedResIds);
            notIncludeResourceIds.add(resourceData.getResourceGooruOid());
            suggestData.putFilter("gte-usedinscollectioncount", "1");
            suggestData.putFilter("&^statistics.invalidResource", "0");
            suggestData.putFilter("&^statistics.statusIsBroken", "0");
            suggestData.putFilter("&^sharing", "public");
            if (notIncludeResourceIds != null && notIncludeResourceIds.size() > 0) {
              suggestData.putFilter("!^gooruOId", notIncludeResourceIds);
            }
            if (StringUtils.trimToNull(resourceData.getResourceFormat()) != null) {
              suggestData.getCustomFilters().add("resourceFormat^2.5:" + resourceData.getResourceFormat());
            } else if (StringUtils.trimToNull(resourceData.getResourceCategory()) != null) {
              suggestData.getCustomFilters().add("category^2.5:" + resourceData.getResourceCategory());
            }

            if (resourceData.getResourceCourseId() != null && resourceData.getResourceCourseId().size() > 0) {
              suggestData.putFilter("&taxonomyV2.course.codeId", StringUtils.join(resourceData.getResourceCourseId(), ","));
            } else if (resourceData.getResourceSubjectId() != null && resourceData.getResourceSubjectId().size() > 0) {
              suggestData.putFilter("&taxonomyV2.subject.codeId", StringUtils.join(resourceData.getResourceSubjectId(), ","));
            } else if (resourceData.getResourceStandards() != null && resourceData.getResourceStandards().size() > 0) {
              suggestData.putFilter("&taxonomyV2.standards", StringUtils.join(resourceData.getResourceStandards(), ","));
            }
            if (resourceData.getResourceGrade() != null) {
              suggestData.putFilter("&^grade", resourceData.getResourceGrade());
            }
          }

          if (eventName.equalsIgnoreCase(RESOURCE_SUGGEST)) {
            suggestData.putFilter("gte-usedinscollectioncount", "1");
            suggestData.putFilter("&^statistics.invalidResource", "0");
            suggestData.putFilter("&^statistics.statusIsBroken", "0");
            suggestData.putFilter("&^sharing", "public");
            if (resourceData != null) {
              suggestData.putFilter("!^gooruOId", resourceData.getResourceGooruOid());
            }
            if (resourceData != null) {
              /*
               * if (StringUtils.trimToNull(resourceData.getResourceTitle()) !=
               * null) { queryString = resourceData.getResourceTitle().trim(); }
               */
              if (StringUtils.trimToNull(resourceData.getResourceFormat()) != null) {
                suggestData.getCustomFilters().add("resourceFormat^2.5:" + resourceData.getResourceFormat());
              } else if (StringUtils.trimToNull(resourceData.getResourceCategory()) != null) {
                suggestData.getCustomFilters().add("category^2.5:" + resourceData.getResourceCategory());
              }

              if (resourceData.getResourceCourseId() != null && resourceData.getResourceCourseId().size() > 0) {
                suggestData.putFilter("&taxonomyV2.course.codeId", StringUtils.join(resourceData.getResourceCourseId(), ","));
              } else if (resourceData.getResourceSubjectId() != null && resourceData.getResourceSubjectId().size() > 0) {
                suggestData.putFilter("&taxonomyV2.subject.codeId", StringUtils.join(resourceData.getResourceSubjectId(), ","));
              } else if (resourceData.getResourceStandards() != null && resourceData.getResourceStandards().size() > 0) {
                suggestData.putFilter("&taxonomyV2.standards", StringUtils.join(resourceData.getResourceStandards(), ","));
              }

            }
            addPreference = true;
          }
          if (eventName.equalsIgnoreCase(SIMILAR_CONTENT)) {

            suggestData.putFilter("gte-usedinscollectioncount", "1");
            suggestData.putFilter("&^statistics.invalidResource", "0");
            suggestData.putFilter("&^statistics.statusIsBroken", "0");
            suggestData.putFilter("&^sharing", "public");

            if (resourceData != null) {
              if (resourceData != null) {
                suggestData.putFilter("!^gooruOId", resourceData.getResourceGooruOid());
              }
              /*
               * if(suggestData.getSuggestV3Context() != null &&
               * StringUtils.trimToNull(suggestData.getSuggestV3Context().
               * getSearchTerm()) != null){ queryString =
               * suggestData.getSuggestV3Context().getSearchTerm().trim(); }
               * else if
               * (StringUtils.trimToNull(resourceData.getResourceTitle()) !=
               * null) { queryString = resourceData.getResourceTitle().trim(); }
               * 
               * if(suggestData.getSuggestV3Context() != null &&
               * StringUtils.trimToNull(suggestData.getSuggestV3Context().
               * getCategory()) != null){
               * suggestData.putFilter("&resourceFormat",
               * suggestData.getSuggestV3Context().getCategory().trim()); }
               */

              if (resourceData.getResourceCourseId() != null && resourceData.getResourceCourseId().size() > 0) {
                suggestData.putFilter("&taxonomyV2.course.codeId", StringUtils.join(resourceData.getResourceCourseId(), ","));
              } else if (resourceData.getResourceSubjectId() != null && resourceData.getResourceSubjectId().size() > 0) {
                suggestData.putFilter("&taxonomyV2.subject.codeId", StringUtils.join(resourceData.getResourceSubjectId(), ","));
              } else if (resourceData.getResourceStandards() != null && resourceData.getResourceStandards().size() > 0) {
                suggestData.putFilter("&taxonomyV2.standards", StringUtils.join(resourceData.getResourceStandards(), ","));
              }

            }
            addPreference = true;
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

          if (preferenceData != null && addPreference) {
            if (preferenceData.getUserPreferredGradeBoost() != null && preferenceData.getUserPreferredGradeBoost().size() > 0) {
              suggestData.getCustomFilters().addAll(preferenceData.getUserPreferredGradeBoost());
            }
            if (preferenceData.getUserPreferredResourceCategoryBoost() != null && preferenceData.getUserPreferredResourceCategoryBoost().size() > 0) {
              suggestData.getCustomFilters().addAll(preferenceData.getUserPreferredResourceCategoryBoost());
            }
            if (preferenceData.getUserPreferredSubjectBoost() != null && preferenceData.getUserPreferredSubjectBoost().size() > 0) {
              suggestData.getCustomFilters().addAll(preferenceData.getUserPreferredSubjectBoost());
            }
            if (preferenceData.getProfileGradeBoost() != null) {
              suggestData.getCustomFilters().add(preferenceData.getProfileGradeBoost());
            }
            if (preferenceData.getProfileSubjectBoost() != null) {
              suggestData.getCustomFilters().add(preferenceData.getProfileSubjectBoost());
            }
          }

          if (proficiencyData != null && addProficiency) {
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
          resourceEsDslQueryProcessor.process(suggestData, searchRes);
          facetFilterConsProcessor.process(suggestData, searchRes);
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
	
	void addTaxonomyFilter(Map<String, Set<Object>> taxonomy, Map<String, String> taxonomyIndexLevels, Boolean boosterIsRequired, Boolean addStandardFilter, SuggestData suggestData) {
		if (taxonomy.get("lesson") != null && !taxonomy.get("lesson").isEmpty()) {
			suggestData.putFilter("&^" + taxonomyIndexLevels.get("lesson"), taxonomy.get("lesson"));
		} else if (taxonomy.get("topic") != null && !taxonomy.get("topic").isEmpty()) {
			suggestData.putFilter("&^" + taxonomyIndexLevels.get("topic"), taxonomy.get("topic"));
		} else if (taxonomy.get("unit") != null && !taxonomy.get("unit").isEmpty()) {
			suggestData.putFilter("&^" + taxonomyIndexLevels.get("unit"), taxonomy.get("unit"));
		} else if (taxonomy.get("course") != null && !taxonomy.get("course").isEmpty()) {
			suggestData.putFilter("&^" + taxonomyIndexLevels.get("course"), taxonomy.get("course"));
		} else if (taxonomy.get("subject") != null && !taxonomy.get("subject").isEmpty()) {
			suggestData.putFilter("&^" + taxonomyIndexLevels.get("subject"), taxonomy.get("subject"));
		}
		if (addStandardFilter) {
			suggestData.putFilter("&^" + taxonomyIndexLevels.get("standard"), taxonomy.get("standards"));
		}
	}

	protected EsIndex getIndexType() {
		return EsIndex.RESOURCE;
	}
	
	private void buildQuery(String queryString, StringBuilder queryStringBuilder){
		String[] queryWords = queryString.toLowerCase().split("[^a-zA-Z0-9\\']");
		for (String word : queryWords) {
			StringBuilder wordsBuilder = new StringBuilder();
			if (wordsBuilder.length() == 0) {
				wordsBuilder.append(word);
			}
			if (queryStringBuilder.length() > 0 && word.trim().length() > 0) {
				queryStringBuilder.append(" AND ");
			}
			queryStringBuilder.append(wordsBuilder.toString());								
		}
	}

}
