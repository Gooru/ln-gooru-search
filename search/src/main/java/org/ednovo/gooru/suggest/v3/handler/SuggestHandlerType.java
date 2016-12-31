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

	SCOLLECTION(),

	DICTIONARY(),

	USER(),

	SEARCHQUERY(),

	TAXONOMY(),

	COURSE();

	private SuggestHandlerType() {
	}

}
