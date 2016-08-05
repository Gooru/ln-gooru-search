package org.ednovo.gooru.search.es.model;

public class SearchRequestData {

	private Integer pageSize = 8;
	
	private Integer pageNum = 1;
	
	private Integer startAt = 0;
	
	private Integer pretty = 0;
	
	private String excludeAttributes;
	
	private String facet;
	
	private boolean userDetails = false;
	
	private boolean includeCollectionItem = false;
	
	private boolean IncludeCIMetaData = false;
	
	private boolean bsSearch = false;
	
	private String protocolSupported;
	
	private boolean allowDuplicates = true;

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getStartAt() {
		return startAt;
	}

	public void setStartAt(Integer startAt) {
		this.startAt = startAt;
	}

	public Integer getPretty() {
		return pretty;
	}

	public void setPretty(Integer pretty) {
		this.pretty = pretty;
	}

	public String getExcludeAttributes() {
		return excludeAttributes;
	}

	public void setExcludeAttributes(String excludeAttributes) {
		this.excludeAttributes = excludeAttributes;
	}

	public String getFacet() {
		return facet;
	}

	public void setFacet(String facet) {
		this.facet = facet;
	}

	public boolean isUserDetails() {
		return userDetails;
	}

	public void setUserDetails(boolean userDetails) {
		this.userDetails = userDetails;
	}

	public boolean isIncludeCollectionItem() {
		return includeCollectionItem;
	}

	public void setIncludeCollectionItem(boolean includeCollectionItem) {
		this.includeCollectionItem = includeCollectionItem;
	}

	public boolean isBsSearch() {
		return bsSearch;
	}

	public void setBsSearch(boolean bsSearch) {
		this.bsSearch = bsSearch;
	}

	public String getProtocolSupported() {
		return protocolSupported;
	}

	public void setProtocolSupported(String protocolSupported) {
		this.protocolSupported = protocolSupported;
	}

	public boolean isAllowDuplicates() {
		return allowDuplicates;
	}

	public void setAllowDuplicates(boolean allowDuplicates) {
		this.allowDuplicates = allowDuplicates;
	}

	public boolean isIncludeCIMetaData() {
		return IncludeCIMetaData;
	}

	public void setIncludeCIMetaData(boolean IncludeCIMetaData) {
		this.IncludeCIMetaData = IncludeCIMetaData;
	}

}
