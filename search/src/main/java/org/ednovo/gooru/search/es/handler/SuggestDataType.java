/**
 * 
 */
package org.ednovo.gooru.search.es.handler;

/**
 * @author SearchTeam
 * 
 */

public enum SuggestDataType {

	RESOURCE(),

	SCOLLECTION(),

	COLLECTION(),

	QUIZ(),
	
	USER(),
	
	QUESTION();

	private SuggestDataType() {
	}

}
