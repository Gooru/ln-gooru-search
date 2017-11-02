package org.ednovo.gooru.search.es.repository;

import java.util.List;
import java.util.Map;

public interface TaxonomyRepository {

	String getParentTaxonomyCode(String code);

	List<String> getConceptNeighbours(String code, String parentCode);

	Map<String, String> getTaxonomyCodeByInternalCode(String code);

	Map<String, String> getTaxonomyCodeByDisplayCode(String code);

}
