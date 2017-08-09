package org.ednovo.gooru.search.es.repository;

import java.util.List;

public interface TaxonomyRepository {

	String getParentTaxonomyCode(String code);

	List<String> getConceptNeighbours(String code, String parentCode);

}
