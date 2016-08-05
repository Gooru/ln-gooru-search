package org.ednovo.gooru.search.model;

import java.util.List;


public class UserPreferenceData {

	private String userSubject;
	
	private String userGrade;
	
	private Float subjectBoost;
	
	private Float gradeBoost;
	
	private String profileSubjectBoost;
	
	private String profileGradeBoost;
	
	private List<String> userPreferredSubjectBoost;
	
	private List<String> userPreferredGradeBoost;
	
	private List<String> userPreferredResourceCategoryBoost;
	
	public String getProfileSubjectBoost() {
		return profileSubjectBoost;
	}

	public void setProfileSubjectBoost(String profileSubjectBoost) {
		this.profileSubjectBoost = profileSubjectBoost;
	}

	public String getProfileGradeBoost() {
		return profileGradeBoost;
	}

	public void setProfileGradeBoost(String profileGradeBoost) {
		this.profileGradeBoost = profileGradeBoost;
	}

	public List<String> getUserPreferredSubjectBoost() {
		return userPreferredSubjectBoost;
	}

	public void setUserPreferredSubjectBoost(List<String> userPreferredSubjectBoost) {
		this.userPreferredSubjectBoost = userPreferredSubjectBoost;
	}

	public List<String> getUserPreferredGradeBoost() {
		return userPreferredGradeBoost;
	}

	public void setUserPreferredGradeBoost(List<String> userPreferredGradeBoost) {
		this.userPreferredGradeBoost = userPreferredGradeBoost;
	}

	public List<String> getUserPreferredResourceCategoryBoost() {
		return userPreferredResourceCategoryBoost;
	}

	public void setUserPreferredResourceCategoryBoost(List<String> userPreferredResourceCategoryBoost) {
		this.userPreferredResourceCategoryBoost = userPreferredResourceCategoryBoost;
	}

	public String getUserGrade() {
		return userGrade;
	}

	public void setUserGrade(String userGrade) {
		this.userGrade = userGrade;
	}

	public Float getSubjectBoost() {
		return subjectBoost;
	}

	public void setSubjectBoost(Float subjectBoost) {
		this.subjectBoost = subjectBoost;
	}

	public Float getGradeBoost() {
		return gradeBoost;
	}

	public void setGradeBoost(Float gradeBoost) {
		this.gradeBoost = gradeBoost;
	}
	
	public void setUserSubject(String userSubject) {
		this.userSubject = userSubject;
	}

	public String getUserSubject() {
		return userSubject;
	}
}
