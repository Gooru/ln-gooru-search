/**
 * 
 */
package org.ednovo.gooru.search.es.handler;


import org.ednovo.gooru.search.es.model.SuggestData;

/**
 * Each suggest handler must extend this abstract class.
 * The registration is done automatically as a post bean creation process.
 * Call the search method to perform the search operation
 * @author SearchTeam
 * 
 */
public abstract class SuggestHandler<I extends SuggestData, O extends Object> extends SearchHandler<I, O> {

	@Override
	protected String getName() {
		return SUGGEST_PREFIX + getType().name();
	}
	
}
