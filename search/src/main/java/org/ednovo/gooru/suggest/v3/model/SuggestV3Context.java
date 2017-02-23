package org.ednovo.gooru.suggest.v3.model;

import java.io.Serializable;

import com.google.gson.JsonObject;

public class SuggestV3Context implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String userId;

	private String id;

	private String context;

	private String contextPath;

	private String contextSubPath;

	private String containerId;

	private String courseId;

	private String unitId;

	private String lessonId;
	
	private JsonObject metrics;

	private Integer score;
	
	private Long timeSpent;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public String getContextSubPath() {
		return contextSubPath;
	}

	public void setContextSubPath(String contextSubPath) {
		this.contextSubPath = contextSubPath;
	}

	public String getContainerId() {
		return containerId;
	}

	public void setContainerId(String containerId) {
		this.containerId = containerId;
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

	public JsonObject getMetrics() {
		return metrics;
	}

	public void setMetrics(JsonObject metrics) {
		this.metrics = metrics;
	}
	
	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		if (score == null) {
			this.score = 0;
		}
		this.score = score;
	}
	
	public Long getTimeSpent() {
		return timeSpent;
	}

	public void setTimeSpent(Long timeSpent) {
		if (timeSpent == null) {
			this.timeSpent = 0L;
		}
		this.timeSpent = timeSpent;
	}

}
