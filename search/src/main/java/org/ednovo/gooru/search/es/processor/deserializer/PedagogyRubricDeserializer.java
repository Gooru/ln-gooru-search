package org.ednovo.gooru.search.es.processor.deserializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.search.es.constant.IndexFields;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.ednovo.gooru.search.responses.PedagogyRubricSearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author SearchTeam
 *
 */
@Component
public class PedagogyRubricDeserializer extends PedagogyDeserializeProcessor<List<PedagogyRubricSearchResult>, PedagogyRubricSearchResult> {

	protected static final Logger logger = LoggerFactory.getLogger(PedagogyRubricDeserializer.class);

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.PedagogyRubricDeserializer;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	List<PedagogyRubricSearchResult> deserialize(Map<String, Object> model, SearchData input, List<PedagogyRubricSearchResult> output) {
		Map<String, Object> hitsMap = (Map<String, Object>) model.get(SEARCH_HITS);
		List<Map<String, Object>> hits = (List<Map<String, Object>>) (hitsMap).get(SEARCH_HITS);
		output = new ArrayList<PedagogyRubricSearchResult>();
		for (Map<String, Object> hit : hits) {
			Map<String, Object> fields = (Map<String, Object>) hit.get(SEARCH_SOURCE);
			output.add(collect(fields, input, null));
		}
		return output;
	}

	@SuppressWarnings("unchecked")
	@Override
	PedagogyRubricSearchResult collect(Map<String, Object> model, SearchData input, PedagogyRubricSearchResult output) {
		if (output == null) {
			output = new PedagogyRubricSearchResult();
		}
		output.setId((String) model.get(IndexFields.ID));
		output.setTitle((String) model.get(IndexFields.TITLE));
		output.setPublishStatus((String) model.get(IndexFields.PUBLISH_STATUS));
		output.setContentFormat((String) model.get(IndexFields.CONTENT_FORMAT));

		// set counts
		if (model.get(IndexFields.STATISTICS) != null) {
			Map<String, Object> statistics = (Map<String, Object>) model.get(IndexFields.STATISTICS);
			output.setQuestionCount(statistics.get(IndexFields.QUESTION_COUNT) != null ? (Integer) statistics.get(IndexFields.QUESTION_COUNT) : 0);
			long viewsCount = 0L;
			if (statistics.get(IndexFields.VIEWS_COUNT) != null) {
				viewsCount = ((Number) statistics.get(IndexFields.VIEWS_COUNT)).longValue();
				output.setViews(viewsCount);
			}
			output.setIsFeatured(statistics.get(IndexFields.IS_FEATURED) != null ? (Boolean) statistics.get(IndexFields.IS_FEATURED) : false);
		}

		// set creator
		if (model.get(IndexFields.CREATOR) != null) {
			output.setCreator(setUser((Map<String, Object>) model.get(IndexFields.CREATOR)));
		}

		// set original creator
		if (model.get(IndexFields.ORIGINAL_CREATOR) != null) {
			output.setOriginalCreator(setUser((Map<String, Object>) model.get(IndexFields.ORIGINAL_CREATOR)));
		}

		// set taxonomy
		Map<String, Object> taxonomyMap = (Map<String, Object>) model.get(IndexFields.TAXONOMY);
		if (taxonomyMap != null) {
			long start = System.currentTimeMillis();
			setTaxonomy(taxonomyMap, input, output);
			logger.debug("Latency of Taxonomy Transformation : {} ms", (System.currentTimeMillis() - start));
		}
		
		// Set metadata
		if (model.get(IndexFields.METADATA) != null) {
			Map<String, List<String>> metadata = (Map<String, List<String>>) model.get(IndexFields.METADATA);
			if (metadata != null) {
				// audience
				List<String> audience = metadata.get(IndexFields.AUDIENCE);
				if (audience != null) {
					output.setAudience(audience);
				}
			}
		}
		return output;
	}

}
