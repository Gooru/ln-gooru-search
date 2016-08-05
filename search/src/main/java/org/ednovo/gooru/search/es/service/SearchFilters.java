/**
 * 
 */
package org.ednovo.gooru.search.es.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Search Team
 * 
 */
public class SearchFilters implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -3932032047012758170L;

	private Map<String, String> categories;

	private List<String> subjects;

	private Map<String, String> gradeLevels;
	
	private Set<Map<String, String>>  resourceFormat;

	public Map<String, String> getCategories() {
		return categories;
	}

	public void setCategories(Map<String, String> categories) {
		this.categories = categories;
	}

	public void setSubjects(List<String> subjects) {
		this.subjects = subjects;
	}

	public List<String> getSubjects() {
		return subjects;
	}

	public Map<String, String> getGradeLevels() {
		return gradeLevels;
	}

	public void setGradeLevels(Map<String, String> gradeLevels) {
		this.gradeLevels = gradeLevels;
	}

	public Set<Map<String, String>> getResourceFormat() {
		return resourceFormat;
	}

	public void setResourceFormat(Set<Map<String, String>> resourceFormat) {
		this.resourceFormat = resourceFormat;
	}


}
