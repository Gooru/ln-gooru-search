/**
 * 
 */
package org.ednovo.gooru.search.es.handler;


public enum SuggestDataProviderType {

	RESOURCE(),

	COLLECTION(),

	SCOLLECTION(),

	QUIZ(),

	USER(),
	
	QUESTION(),
	
	USER_PREFERENCE(), 
	
	USER_PROFICIENCY(), 
	
	USER_ACTIVITY(),

	USER_RESOURCE_PERFORMANCE();
	
	private SuggestDataProviderType() {
	}

}
