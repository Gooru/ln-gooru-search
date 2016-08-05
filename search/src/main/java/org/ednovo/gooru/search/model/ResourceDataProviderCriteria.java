package org.ednovo.gooru.search.model;


public class ResourceDataProviderCriteria extends ContextDataProviderCriteria {

	private String resourceId;
	
	private String parentId;

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getParentId() {
		return parentId;
	}
}
