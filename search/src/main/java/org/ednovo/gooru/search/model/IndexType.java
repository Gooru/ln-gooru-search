/**
 * 
 */
package org.ednovo.gooru.search.model;

/**
 * @author SearchTeam
 *
 */
public enum IndexType {
	
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
	  SCHOOLDISTRICT("schooldistrict"),
	  SKILLS("skills");
	
	String type;
	
	IndexType(String type){
		this.type = type;
	}
	
	public String getType() {
		return this.type;
	}
}
