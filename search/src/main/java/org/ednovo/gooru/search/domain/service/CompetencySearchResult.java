package org.ednovo.gooru.search.domain.service;

import java.io.Serializable;
import java.util.List;

public class CompetencySearchResult implements Serializable {

	private static final long serialVersionUID = 5673214015280665103L;

	private List<String> gutCodes;
	
	public CompetencySearchResult() {
	}

	public List<String> getGutCodes() {
		return gutCodes;
	}

	public void setGutCodes(List<String> gutCodes) {
		this.gutCodes = gutCodes;
	}
	
}
