/**
 * 
 */
package org.ednovo.gooru.search.es.processor.deserializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.search.es.constant.IndexFields;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.Taxonomy;
import org.ednovo.gooru.search.es.model.UserV2;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.ednovo.gooru.search.model.GutPrerequisites;
import org.ednovo.gooru.search.model.SignatureItems;
import org.ednovo.gooru.search.model.SignatureResources;
import org.springframework.stereotype.Component;

/**
 * @author SearchTeam
 * 
 */
@Component
public class TaxonomyDeserializeProcessor extends DeserializeProcessor<List<Taxonomy>, Taxonomy> {

	@SuppressWarnings("unchecked")
	@Override
	List<Taxonomy> deserialize(Map<String, Object> model, SearchData searchData, List<Taxonomy> output) {
		Map<String, Object> hitsMap = (Map<String, Object>) model.get(SEARCH_HITS);
		List<Map<String, Object>> hits = (List<Map<String, Object>>) (hitsMap).get(SEARCH_HITS);
		output = new ArrayList<Taxonomy>();
		for (Map<String, Object> hit : hits) {
			Map<String, Object> fields = (Map<String, Object>) hit.get(SEARCH_SOURCE);
			output.add((Taxonomy) collect(fields, searchData, null));
		}
		return output;
	}

	@SuppressWarnings("unchecked")
	@Override
	Taxonomy collect(Map<String, Object> model, SearchData input, Taxonomy code) {
		if(code == null){
			code = new Taxonomy();
		}
		//TODO disabled while removing coreAPI jar. Need to re-enable
		code.setId((String) model.get(IndexFields.ID));
		code.setDisplayCode((String) model.get(IndexFields.CODE));
		code.setTitle((String) model.get(IndexFields.TITLE));
		code.setDescription((String) model.get(IndexFields.DESCRIPTION));
		code.setCodeType((String) model.get(IndexFields.CODE_TYPE));
		code.setKeywords((List<String>) model.get(IndexFields.KEYWORDS));
		
		List<Map<String, Object>> gutDataAsList = (List<Map<String, Object>>) model.get(IndexFields.GUT_DATA);
		if (gutDataAsList != null && !gutDataAsList.isEmpty()) {
			Map<String, Object> outputGutData = new HashMap<>();
			gutDataAsList.forEach(a -> {
				Map<String, Object> gutData = (Map<String, Object>) a;
				Map<String, Object> gutDataAsMap = new HashMap<>();
				List<GutPrerequisites> gutPrerequisites = new ArrayList<>();
				List<Map<String, String>> prerequisites = (List<Map<String, String>>) gutData.get(IndexFields.PREREQUISITES);
				if (prerequisites != null) {
					prerequisites.forEach(p -> {
						GutPrerequisites gutPrerequisite = new GutPrerequisites();
						gutPrerequisite.setId(p.get(IndexFields.ID));
						gutPrerequisite.setCode(p.get(IndexFields.CODE));
						gutPrerequisite.setTitle(p.get(IndexFields.TITLE));
						gutPrerequisites.add(gutPrerequisite);
					});
				}
				gutDataAsMap.put(IndexFields.PREREQUISITES, gutPrerequisites);

				Map<String, Object> signatureContents = new HashMap<>();
				signatureContents.put(COLLECTIONS, generateSignatureCollections(model, (List<Map<String, Object>>) gutData.get(IndexFields.SIGNATURE_COLLECTIONS)));
				signatureContents.put(ASSESSMENTS, generateSignatureCollections(model, (List<Map<String, Object>>) gutData.get(IndexFields.SIGNATURE_ASSESSMENTS)));
				signatureContents.put(RESOURCES, generateSignatureResources(model, (List<Map<String, Object>>) gutData.get(IndexFields.SIGNATURE_RESOURCES)));
				gutDataAsMap.put(SIGNATURE_CONTENTS, signatureContents);
				gutDataAsMap.put(IndexFields.CODE, (String) gutData.get(IndexFields.CODE));
				gutDataAsMap.put(IndexFields.CODE_TYPE, (String) gutData.get(IndexFields.CODE_TYPE));
				gutDataAsMap.put(IndexFields.TITLE, (String) gutData.get(IndexFields.TITLE));
				gutDataAsMap.put(IndexFields.ID, (String) gutData.get(IndexFields.ID));
				gutDataAsMap.put(IndexFields.SUBJECT, (String) gutData.get(IndexFields.SUBJECT));
				gutDataAsMap.put(IndexFields.COURSE, (String) gutData.get(IndexFields.COURSE));
				gutDataAsMap.put(IndexFields.DOMAIN, (String) gutData.get(IndexFields.DOMAIN));
				outputGutData.put((String) gutData.get(IndexFields.ID), gutDataAsMap);
			});
			code.setGutData(outputGutData);
		}
		return code;

	}

	@SuppressWarnings("unchecked")
	private List<SignatureItems> generateSignatureCollections(Map<String, Object> model, List<Map<String, Object>> items) {
		List<SignatureItems> signatureItems = new ArrayList<>(); 
		if (items != null && items.size() > 0) {
			items.forEach(item -> {
				SignatureItems signatureItem = new SignatureItems();
				signatureItem.setId((String) item.get(IndexFields.ID));
				signatureItem.setTitle((String) item.get(IndexFields.TITLE));
				signatureItem.setDescription((String) item.get(IndexFields.DESCRIPTION));
				signatureItem.setThumbnail((String) item.get(IndexFields.THUMBNAIL));
				signatureItem.setEfficacy((item.get(IndexFields.EFFICACY) != null) ? ((Number) item.get(IndexFields.EFFICACY)).doubleValue() : 0.5);
				signatureItem.setEngagement((item.get(IndexFields.ENGAGEMENT) != null) ? ((Number) item.get(IndexFields.ENGAGEMENT)).doubleValue() : 0.5);
				signatureItem.setRelevance((item.get(IndexFields.RELEVANCE) != null) ? ((Number) item.get(IndexFields.RELEVANCE)).doubleValue() : 0.5);
				signatureItem.setOwner(setUser((Map<String, Object>) item.get(IndexFields.OWNER)));
				signatureItem.setCreator(setUser((Map<String, Object>) item.get(IndexFields.CREATOR)));
				signatureItems.add(signatureItem);
			});
		}
		return signatureItems;
	}

	@SuppressWarnings("unchecked")
	private List<SignatureResources> generateSignatureResources(Map<String, Object> model, List<Map<String, Object>> resources) {
		List<SignatureResources> signatureResources = new ArrayList<>();
		if (resources != null && resources.size() > 0) {
			resources.forEach(resource -> {
				SignatureResources signatureResource = new SignatureResources();
				signatureResource.setId((String) resource.get(IndexFields.ID));
				signatureResource.setTitle((String) resource.get(IndexFields.TITLE));
				signatureResource.setDescription((String) resource.get(IndexFields.DESCRIPTION));
				signatureResource.setUrl((String) resource.get(IndexFields.URL));
				signatureResource.setContentSubFormat((String) resource.get(IndexFields.CONTENT_SUB_FORMAT));
				signatureResource.setThumbnail((String) resource.get(IndexFields.THUMBNAIL));
				signatureResource.setEfficacy((resource.get(IndexFields.EFFICACY) != null) ? ((Number) resource.get(IndexFields.EFFICACY)).doubleValue() : 0.5);
				signatureResource.setEngagement((resource.get(IndexFields.ENGAGEMENT) != null) ? ((Number) resource.get(IndexFields.ENGAGEMENT)).doubleValue() : 0.5);
				signatureResource.setRelevance((resource.get(IndexFields.RELEVANCE) != null) ? ((Number) resource.get(IndexFields.RELEVANCE)).doubleValue() : 0.5);
				signatureResource.setCreator(setUser((Map<String, Object>) resource.get(IndexFields.CREATOR)));
				signatureResources.add(signatureResource);
			});
		}
		return signatureResources;
	}

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.TaxonomyDeserializer;
	}

	protected UserV2 setUser(Map<String, Object> userData) {
		UserV2 user = new UserV2();
		user.setFirstname((String) userData.get(IndexFields.FIRST_NAME));
		user.setLastname((String) userData.get(IndexFields.LAST_NAME));
		user.setUsernameDisplay((String) userData.get(IndexFields.USERNAME));
		user.setId((String) userData.get(IndexFields.USER_ID));
		return user;
	}
}
