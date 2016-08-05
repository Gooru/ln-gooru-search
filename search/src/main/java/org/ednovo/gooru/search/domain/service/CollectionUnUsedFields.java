package org.ednovo.gooru.search.domain.service;

import java.util.List;
import java.util.Map;

public class CollectionUnUsedFields {
	
	private String assetURI;
	private long contentId;
	private String contentOrganizationUid;
	private String contentOrganizationName;
	private String contentOrganizationCode;
	private Short distinguish;
	private String folder;
	private List<String> libraryNames;
	private Map<String, Object> ratings;
	private String sharing;
	private List<Map<String, Object>> skills;
	private List<Map<String, Object>> tags;
	private String taxonomySkills;
	private Integer numberOfResources;
	private Map<String, Object> thumbnails;
  private String description;
	private String languageObjective;
	private Integer isFeatured;
	
	public String getAssetURI() {
		return assetURI;
	}
	public void setAssetURI(String assetURI) {
		this.assetURI = assetURI;
	}
	public long getContentId() {
		return contentId;
	}
	public void setContentId(long contentId) {
		this.contentId = contentId;
	}
	public String getContentOrganizationUid() {
		return contentOrganizationUid;
	}
	public void setContentOrganizationUid(String contentOrganizationUid) {
		this.contentOrganizationUid = contentOrganizationUid;
	}
	public String getContentOrganizationName() {
		return contentOrganizationName;
	}
	public void setContentOrganizationName(String contentOrganizationName) {
		this.contentOrganizationName = contentOrganizationName;
	}
	public String getContentOrganizationCode() {
		return contentOrganizationCode;
	}
	public void setContentOrganizationCode(String contentOrganizationCode) {
		this.contentOrganizationCode = contentOrganizationCode;
	}
	public Short getDistinguish() {
		return distinguish;
	}
	public void setDistinguish(Short distinguish) {
		this.distinguish = distinguish;
	}
	public String getFolder() {
		return folder;
	}
	public void setFolder(String folder) {
		this.folder = folder;
	}
	public List<String> getLibraryNames() {
		return libraryNames;
	}
	public void setLibraryNames(List<String> libraryNames) {
		this.libraryNames = libraryNames;
	}
	public Map<String, Object> getRatings() {
		return ratings;
	}
	public void setRatings(Map<String, Object> ratings) {
		this.ratings = ratings;
	}
	public String getSharing() {
		return sharing;
	}
	public void setSharing(String sharing) {
		this.sharing = sharing;
	}
	public List<Map<String, Object>> getSkills() {
		return skills;
	}
	public void setSkills(List<Map<String, Object>> skills) {
		this.skills = skills;
	}
	public List<Map<String, Object>> getTags() {
		return tags;
	}
	public void setTags(List<Map<String, Object>> tags) {
		this.tags = tags;
	}
	public String getTaxonomySkills() {
		return taxonomySkills;
	}
	public void setTaxonomySkills(String taxonomySkills) {
		this.taxonomySkills = taxonomySkills;
	}
	public Integer getNumberOfResources() {
		return numberOfResources;
	}
	public void setNumberOfResources(Integer numberOfResources) {
		this.numberOfResources = numberOfResources;
	}
	public Map<String, Object> getThumbnails() {
		return thumbnails;
	}
	public void setThumbnails(Map<String, Object> thumbnails) {
		this.thumbnails = thumbnails;
	}
  public String getLanguageObjective() {
    return languageObjective;
  }
  public void setLanguageObjective(String languageObjective) {
    this.languageObjective = languageObjective;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public Integer getIsFeatured() {
    return isFeatured;
  }
  public void setIsFeatured(Integer isFeatured) {
    this.isFeatured = isFeatured;
  }



}
