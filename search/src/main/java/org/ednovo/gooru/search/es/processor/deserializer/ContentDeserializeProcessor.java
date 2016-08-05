package org.ednovo.gooru.search.es.processor.deserializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.search.domain.service.SearchResult;
import org.ednovo.gooru.search.es.constant.IndexFields;
import org.ednovo.gooru.search.es.model.Code;
import org.ednovo.gooru.search.es.model.SearchData;

public abstract class ContentDeserializeProcessor<O extends List<?>, S extends SearchResult>
		extends DeserializeProcessor<O, S> {

	@Override
	S collect(Map<String, Object> model, SearchData searchData, S output) {
		if (output == null) {
			output = (S) new SearchResult();
		}
		if (model.isEmpty()) {
			return output;
		}
		output.setId((String) model.get(IndexFields.ID));
		output.setSharing((String) model.get(IndexFields.PUBLISH_STATUS));
		output.setDescription((String) model.get(IndexFields.DESCRIPTION));
		output.setCategory(null);

		if (model.containsKey(IndexFields.TITLE)) {
			output.setTitle((String) model.get(IndexFields.TITLE));
		}

		List<String> collaboratorIds = (List<String>) model.get(IndexFields.COLLABORATOR);
		if(collaboratorIds != null) {
			output.setCollaborators(collaboratorIds.toString());
		}
		output.setThumbnail((String) model.get(IndexFields.THUMBNAIL));
		output.setType((String) model.get(IndexFields.CONTENT_FORMAT));

		output.setLastModified((String) model.get(IndexFields.UPDATED_AT));
		if (model.get(IndexFields.CREATED_AT) != null) {
			output.setAddDate((String) model.get(IndexFields.CREATED_AT));
		}
		Map<String, Object> creator = (Map<String, Object>) model.get(IndexFields.ORIGINAL_CREATOR);
		if (creator != null) {
			output.setCreatorId((String) creator.get(IndexFields.USER_ID));
			output.setCreatorFirstname((String) creator.get(IndexFields.FIRST_NAME));
			output.setCreatorLastname((String) creator.get(IndexFields.LAST_NAME));
			output.setCreatornameDisplay((String) creator.getOrDefault(IndexFields.USERNAME, null));
		}
		Map<String, Object> owner = (Map<String, Object>) model.get(IndexFields.CREATOR);
		if (owner != null) {
			output.setUsernameDisplay((String) owner.getOrDefault(IndexFields.USERNAME, null));
			output.setUserFirstName((String) owner.get(IndexFields.FIRST_NAME));
			output.setUserLastName((String) owner.get(IndexFields.LAST_NAME));
			output.setGooruUId((String) owner.get(IndexFields.USER_ID));
		}
		Map<String, Object> statisticsMap = (Map<String, Object>) model.get(IndexFields.STATISTICS);
		if (statisticsMap.get(IndexFields.VIEWS_COUNT) != null) {
			output.setViewCount((Integer)statisticsMap.get(IndexFields.VIEWS_COUNT));
		}
		if (statisticsMap.get(IndexFields.COLLABORATOR_COUNT) != null) {
			output.setCollaboratorCount((Integer)statisticsMap.get(IndexFields.COLLABORATOR_COUNT));
		}
		output.setResultUId(java.util.UUID.randomUUID().toString());
		Map<String, Object> taxonomyMap = new HashMap<String, Object>();
		if (model.containsKey(IndexFields.TAXONOMY)) {
			taxonomyMap = (Map<String, Object>) model.get(IndexFields.TAXONOMY);
		}
		if (taxonomyMap != null) {
			output.setTaxonomyDataSet((String) taxonomyMap.get(IndexFields.TAXONOMY_DATA_SET));
			output.setTaxonomySet((Map<String, Object>) taxonomyMap.get(IndexFields.TAXONOMY_SET));
			output.setTaxonomySkills((String) taxonomyMap.get(IndexFields.SKILLS));
			List<Code> taxonomySubject = (List<Code>) taxonomyMap.get(IndexFields.SUBJECT);
			output.setSubject(getTaxonomyMetadataLabel(taxonomySubject));
		}
		return output;
	}
	
}
