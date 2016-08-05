/**
 * 
 */
package org.ednovo.gooru.search.es.service;



/**
 * @author SearchTeam
 *
 */
public interface SearchService {
	
	SearchFilters getSearchFilters(Integer codeId, String type);
		
}
