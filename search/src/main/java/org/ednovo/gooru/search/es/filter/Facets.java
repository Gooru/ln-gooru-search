package org.ednovo.gooru.search.es.filter;

import java.util.List;

public class Facets {

	private List<Object> facets;
	
	public Facets(List<Object> facets) {
		this.facets = facets;
	}
	
	public List<Object> getFacets() {
		return facets;
	}

	public void setFacets(List<Object> facets) {
		this.facets = facets;
	}
	
}
