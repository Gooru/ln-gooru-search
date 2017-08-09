package org.ednovo.gooru.search.es.model;

import java.util.List;

public class UserGroupSupport {

	List<String> partyPermits;
	
	String tenantId;
	
	String tenantRoot;

	public List<String> getPartyPermits() {
		return partyPermits;
	}

	public void setPartyPermits(List<String> partyPermits) {
		this.partyPermits = partyPermits;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getTenantRoot() {
		return tenantRoot;
	}

	public void setTenantRoot(String tenantRoot) {
		this.tenantRoot = tenantRoot;
	}
	
}
