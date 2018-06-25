package org.ednovo.gooru.search.responses;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.search.es.model.UserV2;

public class RubricSearchResult extends SearchResult implements Serializable {

	private static final long serialVersionUID = 5673214015280665128L;

	private static final String INDEX_TYPE = "rubric";

	private String url;

	private String thumbnail;

	private String description;

	private String createdAt;

	private String updatedAt;

	private String lastModifiedBy;

	private UserV2 originalCreator;

	private UserV2 creator;

	private Long views = 0L;

	private Integer questionCount;

	private String resultUId;

	private String contentFormat;
	
	private List<Map<String, Object>> categories;
	
	private Map<String, Object> course;
	
	private Map<String, Object> unit;

	private Map<String, Object> lesson;

	private Map<String, Object> collection;
	
	private Map<String, Object> content;

	private List<String> audience;

	public static String getIndexType() {
		return INDEX_TYPE;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public UserV2 getOriginalCreator() {
		return originalCreator;
	}

	public void setOriginalCreator(UserV2 originalCreator) {
		this.originalCreator = originalCreator;
	}

	public UserV2 getCreator() {
		return creator;
	}

	public void setCreator(UserV2 creator) {
		this.creator = creator;
	}

	public Long getViews() {
		return views;
	}

	public void setViews(Long views) {
		this.views = views;
	}

	public Integer getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(Integer questionCount) {
		this.questionCount = questionCount;
	}

	public String getResultUId() {
		return resultUId;
	}

	public void setResultUId(String resultUId) {
		this.resultUId = resultUId;
	}

	public String getContentFormat() {
		return contentFormat;
	}

	public void setContentFormat(String contentFormat) {
		this.contentFormat = contentFormat;
	}

	public List<Map<String, Object>> getCategories() {
		return categories;
	}

	public void setCategories(List<Map<String, Object>> categories) {
		this.categories = categories;
	}

	public Map<String, Object> getCourse() {
		return course;
	}

	public void setCourse(Map<String, Object> course) {
		this.course = course;
	}

	public Map<String, Object> getUnit() {
		return unit;
	}

	public void setUnit(Map<String, Object> unit) {
		this.unit = unit;
	}

	public Map<String, Object> getLesson() {
		return lesson;
	}

	public void setLesson(Map<String, Object> lesson) {
		this.lesson = lesson;
	}

	public Map<String, Object> getCollection() {
		return collection;
	}

	public void setCollection(Map<String, Object> collection) {
		this.collection = collection;
	}

	public Map<String, Object> getContent() {
		return content;
	}

	public void setContent(Map<String, Object> content) {
		this.content = content;
	}

	public List<String> getAudience() {
		return audience;
	}

	public void setAudience(List<String> audience) {
		this.audience = audience;
	}


}
