package org.ednovo.gooru.search.domain.service;


import java.util.ArrayList;
import java.util.List;

public class IndexData {
	private List<IndexDataTerm> indexDataTerms = new ArrayList<IndexDataTerm>();
	private boolean deleted = false;
	
	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	public List<IndexDataTerm> getIndexDataTerms() {
		return indexDataTerms;
	}

	public void setIndexDataTerms(List<IndexDataTerm> indexDataTerms) {
		this.indexDataTerms = indexDataTerms;
	}

	public void addIndexDataTerm(IndexDataTerm indexDataTerm){
		this.indexDataTerms.add(indexDataTerm);
	}
}




