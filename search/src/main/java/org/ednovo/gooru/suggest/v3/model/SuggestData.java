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

	private SuggestV3Context suggestV3Context;

	public void setSuggestV3Context(SuggestV3Context suggestV3Context) {
		this.suggestV3Context = suggestV3Context;
	}

	public SuggestV3Context getSuggestV3Context() {
		return suggestV3Context;
	}

}
