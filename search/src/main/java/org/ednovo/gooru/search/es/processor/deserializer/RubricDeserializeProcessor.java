package org.ednovo.gooru.search.es.processor.deserializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.search.domain.service.RubricSearchResult;
import org.ednovo.gooru.search.es.constant.IndexFields;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.UserV2;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RubricDeserializeProcessor extends DeserializeProcessor<List<RubricSearchResult>, RubricSearchResult> {

	protected static final Logger logger = LoggerFactory.getLogger(RubricDeserializeProcessor.class);

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.RubricDeserializeProcessor;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	List<RubricSearchResult> deserialize(Map<String, Object> model, SearchData input, List<RubricSearchResult> output) {
		Map<String, Object> hitsMap = (Map<String, Object>) model.get(SEARCH_HITS);
		List<Map<String, Object>> hits = (List<Map<String, Object>>) (hitsMap).get(SEARCH_HITS);
		output = new ArrayList<RubricSearchResult>();
		for (Map<String, Object> hit : hits) {
			Map<String, Object> fields = (Map<String, Object>) hit.get(SEARCH_SOURCE);
			output.add(collect(fields, input, null));
		}
		return output;
	}

	@SuppressWarnings("unchecked")
	@Override
	RubricSearchResult collect(Map<String, Object> model, SearchData input, RubricSearchResult output) {
		if (output == null) {
			output = new RubricSearchResult();
		}
		output.setId((String) model.get(IndexFields.ID));
		output.setTitle((String) model.get(IndexFields.TITLE));
		output.setPublishStatus((String) model.get(IndexFields.PUBLISH_STATUS));
		output.setUpdatedAt((String) model.get(IndexFields.UPDATED_AT));
		output.setCreatedAt((String) model.get(IndexFields.CREATED_AT));
		output.setLastModifiedBy((String) model.get(IndexFields.MODIFIER_ID));
		output.setContentFormat((String) model.get(IndexFields.CONTENT_FORMAT));

		// set counts
		if (model.get(IndexFields.STATISTICS) != null) {
			Map<String, Object> statistics = (Map<String, Object>) model.get(IndexFields.STATISTICS);
			output.setQuestionCount(statistics.get("questionCount") != null ? (Integer) statistics.get("questionCount") : 0);
			long viewsCount = 0L;
			if (statistics.get(IndexFields.VIEWS_COUNT) != null) {
				viewsCount = ((Number) statistics.get(IndexFields.VIEWS_COUNT)).longValue();
				output.setViews(viewsCount);
			}
		}

		// set course
		if (model.get(IndexFields.COURSE) != null) {
			output.setCourse((Map<String, Object>) model.get(IndexFields.COURSE));
		}

		// set unit
		if (model.get(IndexFields.UNIT) != null) {
			output.setUnit((Map<String, Object>) model.get(IndexFields.UNIT));
		}

		// set lesson
		if (model.get(IndexFields.LESSON) != null) {
			output.setLesson((Map<String, Object>) model.get(IndexFields.LESSON));
		}

		// set collection
		if (model.get(IndexFields.COLLECTION) != null) {
			output.setCollection((Map<String, Object>) model.get(IndexFields.COLLECTION));
		}

		// set course
		if (model.get(IndexFields.CONTENT) != null) {
			output.setContent((Map<String, Object>) model.get(IndexFields.CONTENT));
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
		if (model.get(IndexFields.TAXONOMY) != null) {
			Map<String, Object> tax = (Map<String, Object>) model.get(IndexFields.TAXONOMY);
			output.setTaxonomy((Map<String, Object>) tax.get(IndexFields.TAXONOMY_SET));
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

	private UserV2 setUser(Map<String, Object> userData) {
		UserV2 user = new UserV2();
		user.setFirstname((String) userData.get(IndexFields.FIRST_NAME));
		user.setLastname((String) userData.get(IndexFields.LAST_NAME));
		user.setUsernameDisplay((String) userData.get(IndexFields.USERNAME));
		user.setId((String) userData.get(IndexFields.USER_ID));
		user.setProfileImage((String) userData.get(IndexFields.PROFILE_IMAGE));
		return user;
	}

}
