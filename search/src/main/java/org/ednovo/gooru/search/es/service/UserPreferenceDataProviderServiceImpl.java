package org.ednovo.gooru.search.es.service;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.ednovo.gooru.search.model.UserDataProviderCriteria;
import org.ednovo.gooru.search.model.UserPreferenceData;
import org.springframework.stereotype.Service;

@Service
public class UserPreferenceDataProviderServiceImpl implements UserPreferenceDataProviderService {
	
/*	@Autowired
	private OrganizationSettingRepository organizationSettingRepository;

	@Autowired
	private PreferredSearchCassandraFactory preferredSearchCassandraFactory;

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

	
	@Override
	public UserPreferenceData getUserPreferenceData(UserDataProviderCriteria userDataProviderCriteria) {/*
		UserPreferenceData userPreferenceData = new UserPreferenceData();
		String anonymousUid = organizationSettingRepository.getOrganizationSetting(Constants.ANONYMOUS, userDataProviderCriteria.getUserUid());
		if (anonymousUid == null) {
			anonymousUid = "ANONYMOUS";
		}
		if (!userDataProviderCriteria.getUserUid().contains(anonymousUid)) {
			String userUid = userDataProviderCriteria.getUserUid();
			//User user = userRepository.findByGooruId(userDataProviderCriteria.getUserUid());
			//Disabled temporarily
			Profile profile = userRepository.getProfile(user, false);
			if (profile == null) {
				return null;
			}
			
			userPreferenceData.setUserSubject(profile.getSubject());
			userPreferenceData.setUserGrade(profile.getGrade());
			
			if ((!StringUtils.isEmpty(userPreferenceData.getUserSubject())) || (!StringUtils.isEmpty(userPreferenceData.getUserGrade()))) {
				if (!StringUtils.isEmpty(userPreferenceData.getUserSubject())) {
					Float profileBoost = SearchSettingService.getSettingAsFloat("S_" + userDataProviderCriteria.getSuggestDataType().toUpperCase() + "_USER_PROFILE_SUBJECT_BOOST");
					userPreferenceData.setProfileSubjectBoost("taxonomyV2.subject.label^" + profileBoost + ":" + userPreferenceData.getUserSubject());
				}
				if (!StringUtils.isEmpty(userPreferenceData.getUserGrade())) {
					Float profileBoost = SearchSettingService.getSettingAsFloat("S_" + userDataProviderCriteria.getSuggestDataType().toUpperCase() + "_USER_PROFILE_GRADE_BOOST");
					userPreferenceData.setProfileGradeBoost("grade^" + profileBoost + ":" + getSearchUserGrades(userPreferenceData.getUserGrade()));
				}
			}
			if (userDataProviderCriteria.getSuggestDataType().equalsIgnoreCase(SuggestDataType.RESOURCE.name())) {
				Float preferredBoost = SearchSettingService.getSettingAsFloat(SearchSettingType.S_RESOURCE_USER_PREFERRED_CATEGORY_BOOST);
				ColumnList<String> columnList = preferredSearchCassandraFactory.get(ColumnFamilyConstant.USER_PREFERENCE).getColumns(userUid, USER_CATEGORY_FIELDS);
				List<String> userPreferredResourceCategoryBoost = new ArrayList<String>();
				if (columnList != null && columnList.size() > 0) {
					for (Column<String> column : columnList) {
						if (column.hasValue()) {
							String boostValue = column.getStringValue();
							if (boostValue != null && NumberUtils.isNumber(boostValue)) {
								Float typeBoost = Float.valueOf(boostValue);
								typeBoost = preferredBoost + typeBoost;
								String[] columnName = column.getName().trim().split("\\.");
								userPreferredResourceCategoryBoost.add(columnName[0].replace("preferred", "").toLowerCase() + "^" + typeBoost + ":" + columnName[1].toLowerCase());
							}
						}
					}
				}
				userPreferenceData.setUserPreferredResourceCategoryBoost(userPreferredResourceCategoryBoost);
			}
			Float gradeProfileBoost = SearchSettingService.getSettingAsFloat("S_" + userDataProviderCriteria.getSuggestDataType().toUpperCase() + "_USER_PROFILE_GRADE_BOOST");
			ColumnList<String> gradeColumnList = preferredSearchCassandraFactory.get(ColumnFamilyConstant.USER_PREFERENCE).getColumns(userUid, USER_GRADE_FIELDS);
			List<String> userPreferredGradeBoost = new ArrayList<String>();
			if (gradeColumnList != null && gradeColumnList.size() > 0) {
				for (Column<String> column : gradeColumnList) {
					if (column.hasValue()) {
						String boostValue = column.getStringValue();
						if (boostValue != null && NumberUtils.isNumber(boostValue)) {
							Float typeBoost = Float.valueOf(boostValue);
							typeBoost = gradeProfileBoost + typeBoost;
							String[] columnName = column.getName().trim().split("\\.");
							userPreferredGradeBoost.add(columnName[0].replace("preferred", "").toLowerCase() + "^" + typeBoost + ":" + columnName[1].toLowerCase());
						}
					}
				}
			}
			userPreferenceData.setUserPreferredGradeBoost(userPreferredGradeBoost);
			Float subjectProfileBoost = SearchSettingService.getSettingAsFloat("S_" + userDataProviderCriteria.getSuggestDataType().toUpperCase() + "_USER_PROFILE_SUBJECT_BOOST");
			ColumnList<String> subjectColumnList = preferredSearchCassandraFactory.get(ColumnFamilyConstant.USER_PREFERENCE).getColumns(userUid);
			List<String> userPreferredSubjectBoost = new ArrayList<String>();
			if (subjectColumnList != null && subjectColumnList.size() > 0) {
				for (Column<String> column : subjectColumnList) {
					if (column.getName().contains("preferredSubject") && column.hasValue()) {
						String boostValue = column.getStringValue();
						if (boostValue != null && NumberUtils.isNumber(boostValue)) {
							Float typeBoost = Float.valueOf(boostValue);
							typeBoost = subjectProfileBoost + typeBoost;
							String[] columnName = column.getName().trim().split("\\.");
							userPreferredSubjectBoost.add("taxonomyV2.subject.label^" + typeBoost + ":" + columnName[1].toLowerCase());
						}
					}
				}
			}
			userPreferenceData.setUserPreferredSubjectBoost(userPreferredSubjectBoost);
		}
		return userPreferenceData;
	*/
		return null;
	}
	
	private String getSearchUserGrades(String profileGrades) {
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


}
