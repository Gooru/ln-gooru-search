/**
 * 
 */
package org.ednovo.gooru.search.es.constant;

/**
 * @author SearchTeam
 *
 */
public enum EsIndexType {
	
	  COLLECTION("collection")
	, SCOLLECTION("scollection")
	, RESOURCE("resource")
	, QUIZ("quiz")
	, USER("user")
	, SEARCH_QUERY("searchquery")
	, TAXONOMY("taxonomy")
	, LIBRARY("library")
	, DICTIONARY("dictionary")
	, QUESTION("question"),
	  PUBLISHER("publisher"),
	  AGGREGATOR("aggregator"),
	  SCHOOLDISTRICT("schooldistrict");
	
	String type;
	
	EsIndexType(String type){
		this.type = type;
	}
	
	public String getType() {
		return this.type;
	}
}
