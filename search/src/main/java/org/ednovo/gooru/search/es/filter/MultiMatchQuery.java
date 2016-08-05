/**
 * 
 */
package org.ednovo.gooru.search.es.filter;

/**
 * @author SearchTeam
 * 
 */
public class MultiMatchQuery {

	private Query multi_match;

	public MultiMatchQuery(String query,
			String[] fields,
			Float boost) {
		setMulti_match(new Query(query, fields, boost));
	}
	
	public MultiMatchQuery(String query,
			String[] fields,
			Float boost, String minimumShouldMatch, Float cutoffFrequency) {
		if(cutoffFrequency == null){
			setMulti_match(new Query(query, fields, boost, minimumShouldMatch));
		} else {
			setMulti_match(new Query(query, fields, boost, minimumShouldMatch, cutoffFrequency));
		}
	}

	public Query getMulti_match() {
		return multi_match;
	}

	public void setMulti_match(Query multi_match) {
		this.multi_match = multi_match;
	}

}
