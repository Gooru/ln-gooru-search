package org.ednovo.gooru.search.model;

import java.util.List;
import java.util.Set;

public class AssignmentUsageDTO {
	
	private String resourceGooruOId;
	private String category;
	private String title;
	private String type;
	private Long status;
	private List<ResourceUsageData> userData;
	
	public String getResourceGooruOId() {
		return resourceGooruOId;
	}

	public void setResourceGooruOId(String resourceGooruOId) {
		this.resourceGooruOId = resourceGooruOId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public void setStatus(Long status) {
		this.status = status;
	}

	public Long getStatus() {
		return status;
	}

	public void setUserData(List<ResourceUsageData> userData) {
		this.userData = userData;
	}

	public List<ResourceUsageData> getUserData() {
		return userData;
	}
	

}
