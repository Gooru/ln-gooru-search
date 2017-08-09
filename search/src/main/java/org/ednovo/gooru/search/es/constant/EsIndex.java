/**
 * 
 */
package org.ednovo.gooru.search.es.constant;

/**
 * @author SearchTeam
 *
 */
public enum EsIndex {
	
	RESOURCE("resource" , new String[] {"resource"}),
	COLLECTION("collection" , new String[] {"collection"}),
	TAXONOMY("taxonomy",new String [] {"taxonomy","skills"} ),
	LIBRARY("library"),
	SEARCH_QUERY("searchquery"),
	DICTIONARY("dictionary"),
	USER("user"),
    CONTENT_PROVIDER("content_provider", new String[] {"publisher","aggregator"}),
    SCHOOL_DISTRICT("schooldistrict"),
	ORGANIZATION("organization"),
	COURSE("course"),
	UNIT("unit"),
	LESSON("lesson"),
	CROSSWALK("crosswalk"),
	RUBRIC("rubric");

	private String name;
	
	private String[] types;
	
	/**
	 * 
	 */
	private EsIndex(String name, String[] types) {
		this.name = name;
		this.types = types;
	}
	
	private EsIndex(String name) {
		this.name = name;
		this.types = new String[]{name};
	}

	public String getName() {
		return name;
	}

	public String[] getTypes() {
		return types;
	}
}
