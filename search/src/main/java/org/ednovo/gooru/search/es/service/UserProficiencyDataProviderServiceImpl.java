package org.ednovo.gooru.search.es.service;

import org.ednovo.gooru.search.model.UserDataProviderCriteria;
import org.ednovo.gooru.search.model.UserProficiencyData;
import org.springframework.stereotype.Service;


@Service
public class UserProficiencyDataProviderServiceImpl implements UserProficiencyDataProviderService {

/*	@Autowired
	private OrganizationSettingRepository organizationSettingRepository;

	@Autowired
	private PreferredSearchCassandraFactory insightsSearchCassandraFactory;

	@Autowired
	private UserRepository userRepository;*/
	
	@Override
	public UserProficiencyData getUserProficiencyData(UserDataProviderCriteria userDataProviderCriteria) {/*
		UserProficiencyData userProficiencyData = new UserProficiencyData();
		String anonymousUid = organizationSettingRepository.getOrganizationSetting(Constants.ANONYMOUS, userDataProviderCriteria.getUserUid());
		if (anonymousUid == null) {
			anonymousUid = "ANONYMOUS";
		}
		if (!userDataProviderCriteria.getUserUid().contains(anonymousUid)) {
			User user = userRepository.findByGooruId(userDataProviderCriteria.getUserUid());		
			
			Float subjectBoost = SearchSettingService.getSettingAsFloat("S_" +userDataProviderCriteria.getSuggestDataType().toUpperCase() + "_USER_SUBJECT_PROFICIENCY_BOOST");
			HashSet<Object> subjectDataFilter = new HashSet<Object>();
			HashSet<Object> subjectDataBoost = new HashSet<Object>();
			Rows<String, String> subjectRows = insightsSearchCassandraFactory.get(ColumnFamilyConstant.USER_SUBJECT_PROFICIENCY).getRows("user_uid", user.getPartyUid());
			if (subjectRows != null && subjectRows.size() > 0) {
				for (Row<String, String> subjectRow : subjectRows) {
					for (Column<String> column : subjectRow.getColumns()) {
						String[] columnName = column.getName().trim().split("\\.");
						if (column.getName().contains("subject.")) {
							subjectDataFilter.add(columnName[1]);
							if (column.hasValue()) {
								String boostValue = column.getStringValue();
								if (boostValue != null && NumberUtils.isNumber(boostValue)) {
									Float typeBoost = Float.valueOf(boostValue);
									typeBoost = subjectBoost + typeBoost;
									subjectDataBoost.add("taxonomyV2.subject.codeId^" + typeBoost + ":" + columnName[1].toLowerCase());
								}
							}
						}
					}
				}
			}
			if(subjectDataFilter.size() > 0) {
				userProficiencyData.putUserProficiencySubjectFilter("&^taxonomyV2.subject.codeId", subjectDataFilter);
			}
			if(subjectDataBoost.size() > 0){
				userProficiencyData.setUserProficiencySubjectBoost(new ArrayList(subjectDataBoost));
			}
			Float courseBoost = SearchSettingService.getSettingAsFloat("S_" +userDataProviderCriteria.getSuggestDataType().toUpperCase() + "_USER_COURSE_PROFICIENCY_BOOST");
			HashSet<Object> courseDataFilter = new HashSet<Object>();
			HashSet<Object> courseDataBoost = new HashSet<Object>();
			Rows<String, String> courseRows = insightsSearchCassandraFactory.get(ColumnFamilyConstant.USER_COURSE_PROFICIENCY).getRows("user_uid", user.getPartyUid());
			if (courseRows != null && courseRows.size() > 0) {
				for (Row<String, String> courseRow : courseRows) {
					for (Column<String> column : courseRow.getColumns()) {
						String[] columnName = column.getName().trim().split("\\.");
						if (column.getName().contains("course.")) {
							courseDataFilter.add(columnName[1]);
							if (column.hasValue()) {
								String boostValue = column.getStringValue();
								if (boostValue != null && NumberUtils.isNumber(boostValue)) {
									Float typeBoost = Float.valueOf(boostValue);
									typeBoost = courseBoost + typeBoost;
									courseDataBoost.add("taxonomyV2.course.codeId^" + typeBoost + ":" + columnName[1].toLowerCase());
								}
							}
						}
					}
				}
			}
			if((courseDataFilter.size() > 0)) {
				userProficiencyData.putUserProficiencyCourseFilter("&^taxonomyV2.course.codeId", courseDataFilter);
			}
			if(courseDataBoost.size() > 0){
				userProficiencyData.setUserProficiencyCourseBoost(new ArrayList(courseDataBoost));
			}
		}
		return userProficiencyData;
	*/return null;}	
}
