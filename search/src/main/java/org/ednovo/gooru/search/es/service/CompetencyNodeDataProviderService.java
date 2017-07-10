package org.ednovo.gooru.search.es.service;

import java.util.List;

import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.model.CompetencyNodeDTO;

public interface CompetencyNodeDataProviderService {

	List<CompetencyNodeDTO> getCompetencyNode(List<String> gutCodes, SearchData searchData, List<CompetencyNodeDTO> output);

}
