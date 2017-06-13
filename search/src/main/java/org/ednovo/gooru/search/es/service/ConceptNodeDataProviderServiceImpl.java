package org.ednovo.gooru.search.es.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.repository.ConceptNodeRepository;
import org.ednovo.gooru.search.model.ConceptNodeBadge;
import org.ednovo.gooru.search.model.ConceptNodeBenchmark;
import org.ednovo.gooru.search.model.ConceptNodeDTO;
import org.ednovo.gooru.search.model.ConceptNodePerformanceDTO;
import org.ednovo.gooru.search.model.ConceptNodePostTest;
import org.ednovo.gooru.search.model.ConceptNodePreTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConceptNodeDataProviderServiceImpl implements ConceptNodeDataProviderService {

	private static final Logger LOG = LoggerFactory.getLogger(ConceptNodeDataProviderServiceImpl.class);

	@Autowired
	private ConceptNodeRepository conceptNodeRepository;
	
	@Override
	public List<ConceptNodeDTO> getConceptNodes(List<String> concepts , SearchData searchData, List<ConceptNodeDTO> output) {
		if (concepts != null && concepts.size() > 0) {
			ConceptNodeDTO conceptNode = new ConceptNodeDTO();
			for (String id : concepts) {
				Map<String, Object> cnMap = conceptNodeRepository.getConceptNode(id);
				if (cnMap != null && !cnMap.isEmpty()) {
					conceptNode.setTarget_entity_id((String) cnMap.get("target_entity_id"));
					conceptNode.setDescription((String) cnMap.get("description"));
					conceptNode.setTarget_entity_type((String) cnMap.get("target_entity_type"));
					BigInteger conceptNodeId = (BigInteger) cnMap.get("id");
					try {
						List<BigInteger> progressions = conceptNodeRepository.getConceptNodeProgression(conceptNodeId);
						if (progressions != null && progressions.size() > 0) {
							conceptNode.setDependencies(conceptNodeRepository.getConceptNodes(progressions));
						}

						ConceptNodePerformanceDTO conceptNodePerformanceDTO = new ConceptNodePerformanceDTO();
						List<ConceptNodePreTest> preTest = conceptNodeRepository.getConceptNodePreTest(conceptNodeId);
						if (preTest != null && !preTest.isEmpty()) conceptNodePerformanceDTO.setPre_test(preTest);
						List<ConceptNodePostTest> postTest = conceptNodeRepository.getConceptNodePostTest(conceptNodeId);
						if (postTest != null && !postTest.isEmpty()) conceptNodePerformanceDTO.setPost_test(postTest);
						List<ConceptNodeBenchmark> benchmark = conceptNodeRepository.getConceptNodeBenchmark(conceptNodeId);
						if (benchmark != null && !benchmark.isEmpty()) conceptNodePerformanceDTO.setBenchmark(benchmark);
						List<ConceptNodeBadge> badges = conceptNodeRepository.getConceptNodeBadges(conceptNodeId);
						if (badges != null && !badges.isEmpty()) conceptNodePerformanceDTO.setBadges(badges);
						conceptNode.setPerformance(conceptNodePerformanceDTO);
					} catch (Exception e) {
						LOG.error("Error while fetching concept node", e.getMessage(), e.fillInStackTrace());
					}
					output.add(conceptNode);
				}
			}
		}
		return output;
	}
	
}
