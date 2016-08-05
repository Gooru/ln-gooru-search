package org.ednovo.gooru.search.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserProficiencyData {

	private Map<String, Object> userProficiencySubjectFilter;
	
	private Map<String, Object> userProficiencyCourseFilter;
	
	private Map<String, Object> userProficiencyUnitFilter;
	
	private Map<String, Object> userProficiencyTopicFilter;
	
	private Map<String, Object> userProficiencyLessonFilter;
	
	private Map<String, Object> userProficiencyConceptFilter;
	
	private List<String> userProficiencySubjectBoost;

	private List<String> userProficiencyCourseBoost;

	private List<String> userProficiencyUnitBoost;

	private List<String> userProficiencyTopicBoost;
	
	private List<String> userProficiencyLessonBoost;
	
	private List<String> userProficiencyConceptBoost;

	public Map<String, Object> getUserProficiencySubjectFilter() {
		return userProficiencySubjectFilter;
	}

	public void putUserProficiencySubjectFilter(String key, Object value) {
		if (this.userProficiencySubjectFilter == null) {
			this.userProficiencySubjectFilter = new HashMap<String, Object>(1);
		}
		this.userProficiencySubjectFilter.put(key, value);
	}
	
	public Map<String, Object> getUserProficiencyCourseFilter() {
		return userProficiencyCourseFilter;
	}

	public void putUserProficiencyCourseFilter(String key, Object value) {
		if (this.userProficiencyCourseFilter == null) {
			this.userProficiencyCourseFilter = new HashMap<String, Object>(1);
		}
		this.userProficiencyCourseFilter.put(key, value);
	}
	
	public Map<String, Object> getUserProficiencyUnitFilter() {
		return userProficiencyUnitFilter;
	}

	public void putUserProficiencyUnitFilter(String key, Object value) {
		if (this.userProficiencyUnitFilter == null) {
			this.userProficiencyUnitFilter = new HashMap<String, Object>(1);
		}
		this.userProficiencyUnitFilter.put(key, value);
	}

	public Map<String, Object> getUserProficiencyTopicFilter() {
		return userProficiencyTopicFilter;
	}
	
	public void putUserProficiencyTopicFilter(String key, Object value) {
		if (this.userProficiencyTopicFilter == null) {
			this.userProficiencyTopicFilter = new HashMap<String, Object>(1);
		}
		this.userProficiencyTopicFilter.put(key, value);	
	}

	public Map<String, Object> getUserProficiencyLessonFilter() {
		return userProficiencyLessonFilter;
	}

	public void putUserProficiencyLessonFilter(String key, Object value) {
		if (this.userProficiencyLessonFilter == null) {
			this.userProficiencyLessonFilter = new HashMap<String, Object>(1);
		}
		this.userProficiencyLessonFilter.put(key, value);	
	}

	public Map<String, Object> getUserProficiencyConceptFilter() {
		return userProficiencyConceptFilter;
	}

	public void putUserProficiencyConceptFilter(String key, Object value) {
		if (this.userProficiencyConceptFilter == null) {
			this.userProficiencyConceptFilter = new HashMap<String, Object>(1);
		}
		this.userProficiencyConceptFilter.put(key, value);	
	}

	public void setUserProficiencySubjectBoost(List<String> userProficiencySubjectBoost) {
		this.userProficiencySubjectBoost = userProficiencySubjectBoost;
	}

	public List<String> getUserProficiencySubjectBoost() {
		return userProficiencySubjectBoost;
	}

	public void setUserProficiencyCourseBoost(List<String> userProficiencyCourseBoost) {
		this.userProficiencyCourseBoost = userProficiencyCourseBoost;
	}

	public List<String> getUserProficiencyCourseBoost() {
		return userProficiencyCourseBoost;
	}

	public void setUserProficiencyUnitBoost(List<String> userProficiencyUnitBoost) {
		this.userProficiencyUnitBoost = userProficiencyUnitBoost;
	}

	public List<String> getUserProficiencyUnitBoost() {
		return userProficiencyUnitBoost;
	}

	public List<String> getUserProficiencyTopicBoost() {
		return userProficiencyTopicBoost;
	}

	public void setUserProficiencyTopicBoost(List<String> userProficiencyTopicBoost) {
		this.userProficiencyTopicBoost = userProficiencyTopicBoost;
	}

	public List<String> getUserProficiencyLessonBoost() {
		return userProficiencyLessonBoost;
	}

	public void setUserProficiencyLessonBoost(List<String> userProficiencyLessonBoost) {
		this.userProficiencyLessonBoost = userProficiencyLessonBoost;
	}

	public List<String> getUserProficiencyConceptBoost() {
		return userProficiencyConceptBoost;
	}

	public void setUserProficiencyConceptBoost(List<String> userProficiencyConceptBoost) {
		this.userProficiencyConceptBoost = userProficiencyConceptBoost;
	}
	
}
