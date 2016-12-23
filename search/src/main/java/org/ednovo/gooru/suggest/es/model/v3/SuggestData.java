/**
 * 
 */
package org.ednovo.gooru.suggest.es.model.v3;

import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.SuggestContext;

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

	private SuggestV3Context suggestV3Context;

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
	
	 public void setSuggestV3Context(SuggestV3Context suggestV3Context) {
	    this.suggestV3Context = suggestV3Context;
	  }

	  public SuggestV3Context getSuggestV3Context() {
	    return suggestV3Context;
	  }

}
