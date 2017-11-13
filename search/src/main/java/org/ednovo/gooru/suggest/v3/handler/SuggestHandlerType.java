/**
 * 
 */
package org.ednovo.gooru.suggest.v3.handler;

/**
 * @author SearchTeam
 * 
 */
public enum SuggestHandlerType {

	RESOURCE(),

	QUESTION(),

	COLLECTION(),

	DICTIONARY(),

	USER(),

	SEARCHQUERY(),

	TAXONOMY_RESOURCE(),

	TAXONOMY_COLLECTION(),

	COURSE();

	private SuggestHandlerType() {
	}

}
