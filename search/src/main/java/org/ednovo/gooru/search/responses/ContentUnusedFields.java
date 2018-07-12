package org.ednovo.gooru.search.responses;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public class ContentUnusedFields extends SearchResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6586694154253184566L;

	private String assetURI;

	private Set<String> contentPermissions;

	private Map<String, Object> customFields;

	private Integer distinguish;

	private String entryId;

	private String folder;

	private String grade;

	private String indexId;

	private Map<String, String> instructional;

	private Set<String> libraryNames;

	private Map<String, Object> ratings;

	private String recordSource;

	private Integer resourceAddedCount;

	private Map<String, Object> resourceSource;

	private Set<Map<String, Object>> resourceTags;

	private Integer resourceUsedUserCount;

	private Integer s3UploadFlag;

	private Map<String, Object> thumbnails;

	public String getAssetURI() {
		return assetURI;
	}

	public void setAssetURI(String assetURI) {
		this.assetURI = assetURI;
	}

	public Set<String> getContentPermissions() {
		return contentPermissions;
	}

	public void setContentPermissions(Set<String> contentPermissions) {
		this.contentPermissions = contentPermissions;
	}

	public Map<String, Object> getCustomFields() {
		return customFields;
	}

	public void setCustomFields(Map<String, Object> customFields) {
		this.customFields = customFields;
	}

	public Integer getDistinguish() {
		if (distinguish == null)
			return 0;
		return distinguish;
	}

	public void setDistinguish(Integer distinguish) {
		if (distinguish == null) {
			distinguish = 0;
		}
		this.distinguish = distinguish;
	}

	public String getEntryId() {
		return entryId;
	}

	public void setEntryId(String entryId) {
		this.entryId = entryId;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getIndexId() {
		return indexId;
	}

	public void setIndexId(String indexId) {
		this.indexId = indexId;
	}

	public Map<String, String> getInstructional() {
		return instructional;
	}

	public void setInstructional(Map<String, String> instructional) {
		this.instructional = instructional;
	}

	public Set<String> getLibraryNames() {
		return libraryNames;
	}

	public void setLibraryNames(Set<String> libraryNames) {
		this.libraryNames = libraryNames;
	}

	public Map<String, Object> getRatings() {
		return ratings;
	}

	public void setRatings(Map<String, Object> ratings) {
		this.ratings = ratings;
	}

	public String getRecordSource() {
		return recordSource;
	}

	public void setRecordSource(String recordSource) {
		this.recordSource = recordSource;
	}

	public Integer getResourceAddedCount() {
		return resourceAddedCount;
	}

	public void setResourceAddedCount(Integer resourceAddedCount) {
		this.resourceAddedCount = resourceAddedCount;
	}

	public Map<String, Object> getResourceSource() {
		return resourceSource;
	}

	public void setResourceSource(Map<String, Object> resourceSource) {
		this.resourceSource = resourceSource;
	}

	public Set<Map<String, Object>> getResourceTags() {
		return resourceTags;
	}

	public void setResourceTags(Set<Map<String, Object>> resourceTags) {
		this.resourceTags = resourceTags;
	}

	public Integer getResourceUsedUserCount() {
		if (resourceUsedUserCount == null)
			return 0;
		return resourceUsedUserCount;
	}

	public void setResourceUsedUserCount(Integer resourceUsedUserCount) {
		if (resourceUsedUserCount == null) {
			this.resourceUsedUserCount = 0;
		}
		this.resourceUsedUserCount = resourceUsedUserCount;
	}

	public Integer getS3UploadFlag() {
		if (s3UploadFlag == null)
			return 0;
		return s3UploadFlag;
	}

	public void setS3UploadFlag(Integer s3UploadFlag) {
		if (s3UploadFlag == null) {
			s3UploadFlag = 0;
		}
		this.s3UploadFlag = s3UploadFlag;
	}

	public Map<String, Object> getThumbnails() {
		return thumbnails;
	}

	public void setThumbnails(Map<String, Object> thumbnails) {
		this.thumbnails = thumbnails;
	}

}
