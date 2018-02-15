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

	DICTIONARY(),

    SPELLCHECKER(),

	MULTI_RESOURCE(),
	
	USER(),
	
	SEARCHQUERY(),
	
	TAXONOMY(),
	
	SOURCE(),
	
	AUTOCOMPLETE(),
	
	PUBLISHER(),
	
	SUBJECT_FACET_FILTER(),
	
	COURSE(),
	
	UNIT(),
	
	LESSON(),
	
	RUBRIC(),
	
	CROSSWALK(),
	
	KEYWORDCOMPETENCY(),
	
	AUTOCOMPLETE_KEYWORD(),
	
	TENANT(),
	
	PEDAGOGY_RESOURCE(),

	PEDAGOGY_SCOLLECTION(),
	
	PEDAGOGY_UNIT(),

	PEDAGOGY_LESSON(),

	PEDAGOGY_COURSE(),

	PEDAGOGY_RUBRIC();

	private SearchHandlerType() {
	}

}
