package org.ednovo.gooru.suggest.v3.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class SuggestContextData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String userId;

	private String resourceId;

	private String contextArea;

	private String contextType;

	private String collectionType;

	private String collectionSubType;

	private String collectionId;

	private String courseId;

	private String unitId;

	private String lessonId;
	
	private String requestedSubType;
	
	private String currentItemId;

	private String currentItemType;

	private Integer score;
	
	private Long timeSpent;

	private List<String> codes;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getContextArea() {
		return contextArea;
	}

	public void setContextArea(String contextArea) {
		this.contextArea = contextArea;
	}

	public String getCollectionType() {
		return collectionType;
	}

	public void setCollectionType(String collectionType) {
		this.collectionType = collectionType;
	}

	public String getCollectionSubType() {
		return collectionSubType;
	}

	public void setCollectionSubType(String collectionSubType) {
		this.collectionSubType = collectionSubType;
	}

	public String getCollectionId() {
		return collectionId;
	}

	public void setCollectionId(String collectionId) {
		if(StringUtils.isBlank(collectionId)) {
			this.collectionId = null;
		}
		this.collectionId = collectionId;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getLessonId() {
		return lessonId;
	}

	public void setLessonId(String lessonId) {
		this.lessonId = lessonId;
	}

	public String getRequestedSubType() {
		return requestedSubType;
	}

	public void setRequestedSubType(String requestedSubType) {
		this.requestedSubType = requestedSubType;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Long getTimeSpent() {
		return timeSpent;
	}

	public void setTimeSpent(Long timeSpent) {
		this.timeSpent = timeSpent;
	}

	public String getContextType() {
		return contextType;
	}

	public void setContextType(String contextType) {
		this.contextType = contextType;
	}

	public String getCurrentItemId() {
		return currentItemId;
	}

	public void setCurrentItemId(String currentItemId) {
		this.currentItemId = currentItemId;
	}

	public String getCurrentItemType() {
		return currentItemType;
	}

	public void setCurrentItemType(String currentItemType) {
		this.currentItemType = currentItemType;
	}

	public List<String> getCodes() {
		return codes;
	}

	public void setCodes(List<String> codes) {
		this.codes = codes;
	}
}
