/**
 * 
 */
package org.ednovo.gooru.search.es.handler.v3;

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
