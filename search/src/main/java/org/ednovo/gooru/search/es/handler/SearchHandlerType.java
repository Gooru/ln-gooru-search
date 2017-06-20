/**
 * 
 */
package org.ednovo.gooru.search.es.handler;

/**
 * @author SearchTeam
 * 
 */
public enum SearchHandlerType {

	RESOURCE(),

	SCOLLECTION(),

	QUIZ(),

	DICTIONARY(),

    SPELLCHECKER(),

	MULTI_RESOURCE(),
	
	USER(),
	
	QUESTION(),

	LIBRARY(),
	
	SEARCHQUERY(),
	
	TAXONOMY(),
	
	ATTRIBUTION(),
	
	SOURCE(),
	
	AUTOCOMPLETE(), 
	
	MULTI_FORMAT(),
	
	PUBLISHER(),
	
	AGGREGATOR(),
	
	SUBJECT_FACET_FILTER(),
	
	SCHOOLDISTRICT(),
	
	SKILLS(),
	
	CONTRIBUTOR(),
	
	COURSE(),
	
	UNIT(),
	
	LESSON(),
	
	RUBRIC(),
	
	CROSSWALK();

	private SearchHandlerType() {
	}

}
