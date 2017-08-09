package org.ednovo.gooru.search.es.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.ednovo.gooru.search.es.constant.Constants;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.model.CompetencyNodeDTO;
import org.ednovo.gooru.search.model.CompetencyNodesDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CompetencyNodeDataProviderServiceImpl implements CompetencyNodeDataProviderService {
	
	private static final Logger LOG = LoggerFactory.getLogger(CompetencyNodeDataProviderServiceImpl.class);
	
	@Override
	public List<CompetencyNodeDTO> getCompetencyNode(List<String> gutCodes , SearchData searchData, List<CompetencyNodeDTO> output) {
		if (gutCodes != null && gutCodes.size() > 0) {
			for (String gutCode : gutCodes) {
				try {
					final String uri = SearchSettingService.getCompetencyNodeURI();

					Map<String, String> params = new HashMap<String, String>();
					params.put(Constants.GUT_COMPETENCY_ID, gutCode);

					HttpHeaders headers = new HttpHeaders();
					headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
					headers.set(Constants.AUTHORIZATION, Constants.TOKEN_SPACE + searchData.getSessionToken());

					CompetencyNodesDTO competencyNode = getCompetencyNode(uri, headers, params);

					if (competencyNode != null && competencyNode.getCompetency_nodes() != null) {
						output.add(competencyNode.getCompetency_nodes());
					}
				} catch (Exception e) {
					LOG.error("Error while fetching competency node", e.getMessage());
				}
			}
			if (output != null && output.size() > 0) {
				LOG.info("Related competencies : {}", output.get(0).getId());
			} else {
				LOG.info("No related competencies for keyword : {}", searchData.getOriginalQuery());
			}
		}
		return output;
	}
	
	private CompetencyNodesDTO getCompetencyNode(String uri, HttpHeaders headers, Map<String, String> params) {
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		ResponseEntity<CompetencyNodesDTO> result = restTemplate.exchange(uri, HttpMethod.GET, entity, CompetencyNodesDTO.class, params);
		if (result.getStatusCode().equals(HttpStatus.SC_NOT_FOUND)) {
			return null;
		}
		return result.getBody();
	}
}
