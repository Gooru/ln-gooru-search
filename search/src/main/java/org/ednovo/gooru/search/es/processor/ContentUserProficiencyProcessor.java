/**
 * 
 */
package org.ednovo.gooru.search.es.processor;

import org.ednovo.gooru.search.es.model.SuggestData;
import org.ednovo.gooru.search.responses.SearchResponse;
import org.springframework.stereotype.Component;

/**
 * @author SearchTeam
 * 
 */
@Component
public class ContentUserProficiencyProcessor extends SearchProcessor<SuggestData, Object> {

/*	@Autowired
	private OrganizationSettingRepository organizationSettingRepository;

	@Autowired
	private PreferredSearchCassandraFactory preferredSearchCassandraFactory;

	@Autowired
	private UserRepository userRepository;*/

	public ContentUserProficiencyProcessor() {
		super();
		setTransactional(true);
	}

	@Override
	public void process(SuggestData suggestData, SearchResponse<Object> response) {/*
		User user = suggestData.getUser();
		String anonymousUid = organizationSettingRepository.getOrganizationSetting(SEARCH_ANONYMOUS, user.getOrganization().getPartyUid());
		if (anonymousUid == null) {
			anonymousUid = "ANONYMOUS";
		}
		if (!user.getGooruUId().contains(anonymousUid)) {
			Profile profile = userRepository.getProfile(user, false);
			if (profile == null) {
				return;
			}

			String searchDataType = suggestData.getType();
			if (searchDataType.equalsIgnoreCase(MULTI_RESOURCE)) {
				searchDataType = suggestData.getIndexType().getName();
			}

			String activeFlag = getCassandraSetting("search." + suggestData.getType().toLowerCase() + ".taxonomy.proficiency.suggest.active");
			if (Boolean.parseBoolean(activeFlag)) {
				
				Float topicBoost = SearchSettingService.getSettingAsFloat(SearchSettingType.S_RESOURCE_USER_TOPIC_PROFICIENCY_BOOST);
				Float lessonBoost = SearchSettingService.getSettingAsFloat(SearchSettingType.S_RESOURCE_USER_LESSON_PROFICIENCY_BOOST);
				Float conceptBoost = SearchSettingService.getSettingAsFloat(SearchSettingType.S_RESOURCE_USER_CONCEPT_PROFICIENCY_BOOST);
				
				HashSet<Object> subjectData = new HashSet<Object>();
				HashSet<Object> courseData = new HashSet<Object>();
				HashSet<Object> unitData = new HashSet<Object>();
	
				Rows<String, String> subjectRows = preferredSearchCassandraFactory.get(ColumnFamilyConstant.USER_SUBJECT_PROFICIENCY).getRows(SEARCH_USER_ID, user.getPartyUid());
				if (subjectRows != null && subjectRows.size() > 0) {
					for (Row<String, String> subjectRow : subjectRows) {
						for (Column<String> column : subjectRow.getColumns()) {
							String[] columnName = column.getName().trim().split("\\.");
							if (column.getName().contains("subject.")) {
								subjectData.add(columnName[1]);
							}
						}
						if(subjectData.size() > 0) {
							suggestData.putFilter("&^taxonomy.subject.codeId", subjectData);
						}
					}
				}
				Rows<String, String> courseRows = preferredSearchCassandraFactory.get(ColumnFamilyConstant.USER_COURSE_PROFICIENCY).getRows(SEARCH_USER_ID, user.getPartyUid());
				if (courseRows != null && courseRows.size() > 0) {
					for (Row<String, String> courseRow : courseRows) {
						for (Column<String> column : courseRow.getColumns()) {
							String[] columnName = column.getName().trim().split("\\.");
							if (column.getName().contains("course.")) {
								courseData.add(columnName[1]);
							}
						}
						if((courseData.size() > 0)) {
							suggestData.putFilter("&^taxonomy.course.codeId", courseData);
						}
					}
				}
				Rows<String, String> unitRows = preferredSearchCassandraFactory.get(ColumnFamilyConstant.USER_UNIT_PROFICIENCY).getRows(SEARCH_USER_ID, user.getPartyUid());
				if (unitRows != null && unitRows.size() > 0) {
					for (Row<String, String> unitRow : unitRows) {
						for (Column<String> column : unitRow.getColumns()) {
							String[] columnName = column.getName().trim().split("\\.");
							if (column.getName().contains("unit.")) {
								unitData.add(columnName[1]);
							}
						}
						if(unitData.size() > 0) {
							suggestData.putFilter("&^taxonomy.unit.codeId", unitData);
						}
					}
				}
				Rows<String, String> topicRows = preferredSearchCassandraFactory.get(ColumnFamilyConstant.USER_TOPIC_PROFICIENCY).getRows(SEARCH_USER_ID, user.getPartyUid());
				if (topicRows != null && topicRows.size() > 0) {
					for (Row<String, String> topicRow : topicRows) {
						for (Column<String> column : topicRow.getColumns()) {
							String[] columnName = column.getName().trim().split("\\.");
							if (column.getName().contains("topic.") && column.hasValue()) {
								String boostValue = column.getStringValue();
								if (boostValue != null && NumberUtils.isNumber(boostValue)) {
									Float typeBoost = Float.valueOf(boostValue);
									typeBoost = topicBoost + typeBoost;
									suggestData.getCustomFilters().add("taxonomy.topic.codeId^" + typeBoost + ":" + columnName[1].toLowerCase());
								}
							}
						}
					}
				}
				Rows<String, String> lessonRows = preferredSearchCassandraFactory.get(ColumnFamilyConstant.USER_LESSON_PROFICIENCY).getRows(SEARCH_USER_ID, user.getPartyUid());
				if (lessonRows != null && lessonRows.size() > 0) {
					for (Row<String, String> lessonRow : lessonRows) {
						for (Column<String> column : lessonRow.getColumns()) {
							String[] columnName = column.getName().trim().split("\\.");
							if (column.getName().contains("lesson.") && column.hasValue()) {
								String boostValue = column.getStringValue();
								if (boostValue != null && NumberUtils.isNumber(boostValue)) {
									Float typeBoost = Float.valueOf(boostValue);
									typeBoost = lessonBoost + typeBoost;
									suggestData.getCustomFilters().add("taxonomy.lesson.codeId^" + typeBoost + ":" + columnName[1].toLowerCase());
								}
							}
						}
					}
				}
				Rows<String, String> conceptRows = preferredSearchCassandraFactory.get(ColumnFamilyConstant.USER_CONCEPT_PROFICIENCY).getRows(SEARCH_USER_ID, user.getPartyUid());
				if (conceptRows != null && conceptRows.size() > 0) {
					for (Row<String, String> conceptRow : conceptRows) {
						for (Column<String> column : conceptRow.getColumns()) {
							String[] columnName = column.getName().trim().split("\\.");
							if (column.getName().contains("concept.") && column.hasValue()) {
								String boostValue = column.getStringValue();
								if (boostValue != null && NumberUtils.isNumber(boostValue)) {
									Float typeBoost = Float.valueOf(boostValue);
									typeBoost = conceptBoost + typeBoost;
									suggestData.getCustomFilters().add("taxonomy.concept.codeId^" + typeBoost + ":" + columnName[1].toLowerCase());
								}
							}
						}
					}
				}
			}
		}			
	*/}

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.ContentUserProficiency;
	}
}
