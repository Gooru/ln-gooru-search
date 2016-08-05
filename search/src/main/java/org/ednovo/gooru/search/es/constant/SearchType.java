/**
 * 
 */
package org.ednovo.gooru.search.es.constant;

import java.util.HashMap;
import java.util.Map;

public enum SearchType{
	COLLECTION("collection")
	, RESOURCE("resource")
	, QUESTION("question")
	, QUIZ("quiz")
	, USER("user")
	, NETWORK("network")
	, QUESTION_BOARD("qb")
	, SEARCH_QUERY("searchquery")
	, LIBRARY("library")
	, TAXONOMY ("taxonomy")
	, ALL("all")
	,SIMPLE_COLLECTION("scollection")
	,RESOURCES(new String[]{SearchType.RESOURCE.getType(),SearchType.COLLECTION.getType(),SearchType.QUIZ.getType(),SearchType.SIMPLE_COLLECTION.getType()}),
	RESOURCE_BY_STATUS("resource_by_status"),
	STANDARD_CODE("standardCode"),
	STANDARD("standard"),
	ATTRIBUTION("attribution"),
	COLLECTION_QUIZ("collection-quiz"),
	QUIZ_SUGGEST("quiz-suggest"),
	COLLECTION_SUGGEST("collection-suggest"),
    COMMONWORDS("commonwords")
	
	;
	
	String type;
	String[] types;
	private static final Map<String,SearchType> typeMap=new HashMap<String, SearchType>();
	static{
		for(SearchType type:SearchType.values()){
			if(type.getType() != null){
				typeMap.put(type.getType(),type);
			}
		}
	}
	SearchType(String type){
		this.type = type;
	}
	SearchType(String[] types){
		this.types = types;
	}
	public String getType() {
		return this.type;
	}
	public String[] getTypes() {
		return this.types;
	}
	public static SearchType getTypeByValue(String type){
		return typeMap.get(type);
	}
}