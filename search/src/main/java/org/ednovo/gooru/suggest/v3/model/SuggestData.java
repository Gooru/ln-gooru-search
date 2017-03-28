/**
 * 
 */
package org.ednovo.gooru.suggest.v3.model;

import org.ednovo.gooru.search.es.model.SearchData;

/**
 * @author SearchTeam
 * 
 */

public class SuggestData extends SearchData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9127315652940617808L;

	private SuggestContextData suggestContextData;

	public SuggestContextData getSuggestContextData() {
		return suggestContextData;
	}

	public void setSuggestContextData(SuggestContextData suggestContextData) {
		this.suggestContextData = suggestContextData;
	}

}
