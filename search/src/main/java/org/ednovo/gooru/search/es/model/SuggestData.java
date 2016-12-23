/**
 * 
 */
package org.ednovo.gooru.search.es.model;

/**
 * @author SearchTeam
 * 
 */

public class SuggestData extends SearchData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9127315652940617808L;

	private String context;
	
	private SuggestContext suggestContext;

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public void setSuggestContext(SuggestContext suggestContext) {
		this.suggestContext = suggestContext;
	}

	public SuggestContext getSuggestContext() {
		return suggestContext;
	}

}
