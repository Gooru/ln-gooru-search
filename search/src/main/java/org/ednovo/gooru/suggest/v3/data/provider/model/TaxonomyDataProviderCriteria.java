package org.ednovo.gooru.suggest.v3.data.provider.model;

import java.util.List;

public class TaxonomyDataProviderCriteria extends ContextDataProviderCriteria {

	private Boolean isInternalCode;

	private List<String> codeIds;

	public Boolean getIsInternalCode() {
		return isInternalCode;
	}

	public void setIsInternalCode(Boolean isInternalCode) {
		this.isInternalCode = isInternalCode;
	}

	public void setCodeIds(List<String> codeIds) {
		this.codeIds = codeIds;
	}

	public List<String> getCodeIds() {
		return codeIds;
	}
}
