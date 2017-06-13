package org.ednovo.gooru.search.es.repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.search.model.Concept;
import org.ednovo.gooru.search.model.ConceptNodeBadge;
import org.ednovo.gooru.search.model.ConceptNodeBenchmark;
import org.ednovo.gooru.search.model.ConceptNodeDTO;
import org.ednovo.gooru.search.model.ConceptNodePostTest;
import org.ednovo.gooru.search.model.ConceptNodePreTest;

public interface ConceptNodeRepository {

	Map<String, Object> getConceptNode(String id);

	List<BigInteger> getConceptNodeProgression(BigInteger conceptNodeId);

	List<Concept> getConceptNodes(List<BigInteger> progressions);

	List<ConceptNodePreTest> getConceptNodePreTest(BigInteger id);

	List<ConceptNodePostTest> getConceptNodePostTest(BigInteger id);

	List<ConceptNodeBenchmark> getConceptNodeBenchmark(BigInteger id);

	List<ConceptNodeBadge> getConceptNodeBadges(BigInteger id);

}
