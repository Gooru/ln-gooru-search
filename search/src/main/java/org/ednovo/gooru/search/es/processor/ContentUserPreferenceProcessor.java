/**
 * 
 */
package org.ednovo.gooru.search.es.processor;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.springframework.stereotype.Component;

/**
 * @author SearchTeam
 * 
 */
@Component
public class ContentUserPreferenceProcessor extends SearchProcessor<SearchData, Object> {

/*	@Autowired
	private OrganizationSettingRepository organizationSettingRepository;

	@Autowired
	private PreferredSearchCassandraFactory preferredSearchCassandraFactory;

	@Autowired
	private ApiCassandraFactory apiCassandraFactory;

	@Autowired
	private UserRepository userRepository;*/

	private static final ArrayList<String> USER_CATEGORY_FIELDS = new ArrayList<String>();
	
	private static final ArrayList<String> USER_RESOURCE_FORMAT_FIELDS = new ArrayList<String>();

	private static final ArrayList<String> USER_GRADE_FIELDS = new ArrayList<String>();

	static {
		USER_CATEGORY_FIELDS.add("preferredCategory.video");
		USER_CATEGORY_FIELDS.add("preferredCategory.website");
		USER_CATEGORY_FIELDS.add("preferredCategory.exam");
		USER_CATEGORY_FIELDS.add("preferredCategory.handout");
		USER_CATEGORY_FIELDS.add("preferredCategory.interactive");
		USER_CATEGORY_FIELDS.add("preferredCategory.lesson");
		USER_CATEGORY_FIELDS.add("preferredCategory.question");
		USER_CATEGORY_FIELDS.add("preferredCategory.slide");
		USER_CATEGORY_FIELDS.add("preferredCategory.textbook");
		USER_CATEGORY_FIELDS.add("preferredCategory.image");
		USER_RESOURCE_FORMAT_FIELDS.add("preferredResourceFormat.audio");
		USER_RESOURCE_FORMAT_FIELDS.add("preferredResourceFormat.image");
		USER_RESOURCE_FORMAT_FIELDS.add("preferredResourceFormat.interactive");
		USER_RESOURCE_FORMAT_FIELDS.add("preferredResourceFormat.question");
		USER_RESOURCE_FORMAT_FIELDS.add("preferredResourceFormat.ext");
		USER_RESOURCE_FORMAT_FIELDS.add("preferredResourceFormat.video");
		USER_RESOURCE_FORMAT_FIELDS.add("preferredResourceFormat.webpage");
		for (int grade = 1; grade <= 12; grade++) {
			USER_GRADE_FIELDS.add("preferredGrade." + grade);
		}
		USER_GRADE_FIELDS.add("preferredGrade.higher education");
		USER_GRADE_FIELDS.add("preferredGrade.kindergarten");
	}

	public ContentUserPreferenceProcessor() {
		super();
		setTransactional(true);
	}

	@Override
	public void process(SearchData searchData, SearchResponse<Object> response) {/*
		User user = searchData.getUser();
		String anonymousUid = organizationSettingRepository.getOrganizationSetting(Constants.ANONYMOUS, user.getOrganization().getPartyUid());
		if (anonymousUid == null) {
			anonymousUid = "ANONYMOUS";
		}
		if (!user.getGooruUId().contains(anonymousUid)) {
			Profile profile = userRepository.getProfile(user, false);
			if (profile == null) {
				return;
			}

			String searchDataType = searchData.getType();
			if (searchDataType.equalsIgnoreCase(MULTI_RESOURCE)) {
				searchDataType = searchData.getIndexType().getName();
			}

			String profileSubject = profile.getSubject();
			String profileGrade = profile.getGrade();
			if ((!StringUtils.isEmpty(profileSubject)) || (!StringUtils.isEmpty(profileGrade))) {
				if (!StringUtils.isEmpty(profileSubject)) {
					Float profileBoost = SearchSettingService.getSettingAsFloat("S_" + searchDataType.toUpperCase() + "_USER_PROFILE_SUBJECT_BOOST");
					searchData.getCustomFilters().add("taxonomyV2.subject.label^" + profileBoost + ":" + profileSubject.toLowerCase());
				}
				if (!StringUtils.isEmpty(profileGrade)) {
					Float profileBoost = SearchSettingService.getSettingAsFloat("S_" + searchDataType.toUpperCase() + "_USER_PROFILE_GRADE_BOOST");
					searchData.getCustomFilters().add("grade^" + profileBoost + ":" + getSearchUserGrades(profileGrade));
				}
			}
			if (searchDataType.equalsIgnoreCase(SearchType.RESOURCE.getType())) {
				Float preferredBoost = SearchSettingService.getSettingAsFloat(SearchSettingType.S_RESOURCE_USER_PREFERRED_CATEGORY_BOOST);
				ColumnList<String> columnList = null;
				try{
					columnList = preferredSearchCassandraFactory.get(ColumnFamilyConstant.USER_PREFERENCE).getColumns(user.getPartyUid(), USER_CATEGORY_FIELDS);
				}
				catch(Exception e){
					logger.error("Insights Cassandra throws Error : " + e.getMessage());
					return;
				}
				
				if (columnList != null && columnList.size() > 0) {
					for (Column<String> column : columnList) {
						if (column.hasValue()) {
							String boostValue = column.getStringValue();
							if (boostValue != null && NumberUtils.isNumber(boostValue)) {
								Float typeBoost = Float.valueOf(boostValue);
								typeBoost = preferredBoost + typeBoost;
								String[] columnName = column.getName().trim().split("\\.");
								searchData.getCustomFilters().add(columnName[0].replace("preferred", "").toLowerCase() + "^" + typeBoost + ":" + columnName[1].toLowerCase());
							}
						}
					}
				}
				Float preferredBoost = SearchSettingService.getSettingAsFloat(SearchSettingType.S_RESOURCE_USER_PREFERRED_RESOURCE_FORMAT_BOOST);
				ColumnList<String> columnList = preferredSearchCassandraFactory.get(ColumnFamilyConstant.USER_PREFERENCE).getColumns(user.getPartyUid(), USER_RESOURCE_FORMAT_FIELDS);
				if (columnList != null && columnList.size() > 0) {
					for (Column<String> column : columnList) {
						if (column.hasValue()) {
							String boostValue = column.getStringValue();
							if (boostValue != null && NumberUtils.isNumber(boostValue)) {
								Float typeBoost = Float.valueOf(boostValue);
								typeBoost = preferredBoost + typeBoost;
								String[] columnName = column.getName().trim().split("\\.");
								searchData.getCustomFilters().add(columnName[0].replace("preferred", "").toLowerCase() + "^" + typeBoost + ":" + columnNKindergartename[1].toLowerCase());
							}
						}
					}
				}
			
			}
		    Float gradeProfileBoost = SearchSettingService.getSettingAsFloat("S_" + searchDataType.toUpperCase() + "_USER_PROFILE_GRADE_BOOST");
			ColumnList<String> gradeColumnList = null;
			try{
				gradeColumnList = preferredSearchCassandraFactory.get(ColumnFamilyConstant.USER_PREFERENCE).getColumns(user.getPartyUid(), USER_GRADE_FIELDS);
			}
			catch(Exception e){
				logger.error("Insights Cassandra throws Error : " + e.getMessage());
				return;
			}

			if (gradeColumnList != null && gradeColumnList.size() > 0) {
				for (Column<String> column : gradeColumnList) {
					if (column.hasValue()) {
						String boostValue = column.getStringValue();
						if (boostValue != null && NumberUtils.isNumber(boostValue)) {
							Float typeBoost = Float.valueOf(boostValue);
							typeBoost = gradeProfileBoost + typeBoost;
							String[] columnName = column.getName().trim().split("\\.");
							searchData.getCustomFilters().add(columnName[0].replace("preferred", "").toLowerCase() + "^" + typeBoost + ":" + WordUtils.capitalize(columnName[1].toLowerCase()));
						}
					}
				}
			}
			Float subjectProfileBoost = SearchSettingService.getSettingAsFloat("S_" + searchDataType.toUpperCase() + "_USER_PROFILE_SUBJECT_BOOST");
			ColumnList<String> subjectColumnList = null;
			try{
				subjectColumnList = preferredSearchCassandraFactory.get(ColumnFamilyConstant.USER_PREFERENCE).getColumns(user.getPartyUid());
			}
			catch(Exception e){
				logger.error("Insights Cassandra throws Error : " + e.getMessage());
				return;
			}

			if (subjectColumnList != null && subjectColumnList.size() > 0) {
				for (Column<String> column : subjectColumnList) {
					if (column.getName().contains("preferredSubject") && column.hasValue()) {
						String boostValue = column.getStringValue();
						if (boostValue != null && NumberUtils.isNumber(boostValue)) {
							Float typeBoost = Float.valueOf(boostValue);
							typeBoost = subjectProfileBoost + typeBoost;
							String[] columnName = column.getName().trim().split("\\.");
							searchData.getCustomFilters().add("taxonomyV2.subject.label^" + typeBoost + ":" + columnName[1].toLowerCase());
						}
					}
				}
			}
		}
	*/}

	protected String getSearchUserGrades(String profileGrades) {
		String[] grades = profileGrades.split(",");
		String gradeString = "0";
		for (String grade : grades) {
			grade = grade.trim();
			if (grade.length() == 0 || grade.equals("null") || grade.equals("-")) {
				continue;
			}
			gradeString += "," + grade;
			if (StringUtils.isNumeric(grade)) {
				int gradeInt = Integer.parseInt(grade);
				if (gradeInt - 1 > 0) {
					gradeString += "," + (gradeInt - 1);
				}
				if (gradeInt - 2 > 0) {
					gradeString += "," + (gradeInt - 2);
				}
				if (gradeInt - 3 > 0) {
					gradeString += "," + (gradeInt - 3);
				}
			}
		}
		return gradeString;
	}

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.ContentUserPreference;
	}

}
