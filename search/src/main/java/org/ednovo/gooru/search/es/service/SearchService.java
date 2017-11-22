/**
 * 
 */
package org.ednovo.gooru.search.es.service;

import org.ednovo.gooru.search.es.model.SearchData;

/**
 * @author SearchTeam
 *
 */
public interface SearchService {
	
	SearchFilters getSearchFilters(Integer codeId, String type);

	void refreshGlobalTenantsInCache();

	void trimInvalidExpression(SearchData searchData);
		
}
