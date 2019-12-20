/**
 * 
 */
package org.ednovo.gooru.search.es.processor.deserializer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.search.es.constant.IndexFields;
import org.ednovo.gooru.search.es.model.Gut;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.UserV2;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.ednovo.gooru.search.es.service.SearchSettingService;
import org.ednovo.gooru.search.model.GutPrerequisites;
import org.ednovo.gooru.search.model.SignatureItems;
import org.ednovo.gooru.search.model.SignatureResources;
import org.springframework.stereotype.Component;

/**
 * @author Renuka
 * 
 */
@Component
public class GutDeserializeProcessor extends DeserializeProcessor<List<Gut>, Gut> {
	
	@SuppressWarnings("unchecked")
	@Override
	List<Gut> deserialize(Map<String, Object> model, SearchData searchData, List<Gut> output) {
		Map<String, Object> hitsMap = (Map<String, Object>) model.get(SEARCH_HITS);
		List<Map<String, Object>> hits = (List<Map<String, Object>>) (hitsMap).get(SEARCH_HITS);
		output = new ArrayList<Gut>();
		for (Map<String, Object> hit : hits) {
			Map<String, Object> fields = (Map<String, Object>) hit.get(SEARCH_SOURCE);
			output.add((Gut) collect(fields, searchData, null));
		}
		return output;
	}

	@SuppressWarnings("unchecked")
	@Override
	Gut collect(Map<String, Object> model, SearchData input, Gut code) {
		if (code == null) {
			code = new Gut();
		}
		code.setId((String) model.get(IndexFields.ID));
		code.setDisplayCode((String) model.get(IndexFields.CODE));
		code.setTitle((String) model.get(IndexFields.TITLE));
		code.setDescription((String) model.get(IndexFields.DESCRIPTION));
		code.setCodeType((String) model.get(IndexFields.CODE_TYPE));
		code.setKeywords((List<String>) model.get(IndexFields.KEYWORDS));
		code.setSubjectLabel((String) model.get(IndexFields.SUBJECT_LABEL));
		code.setCourseLabel((String) model.get(IndexFields.COURSE_LABEL));
		code.setDomainLabel((String) model.get(IndexFields.DOMAIN_LABEL));

		Map<String, Object> signatureContents = new HashMap<>();
		signatureContents.put(COLLECTIONS, generateSignatureCollections(model, (List<Map<String, Object>>) model.get(IndexFields.SIGNATURE_COLLECTIONS), input));
		signatureContents.put(ASSESSMENTS, generateSignatureCollections(model, (List<Map<String, Object>>) model.get(IndexFields.SIGNATURE_ASSESSMENTS), input));
		signatureContents.put(RESOURCES, generateSignatureResources(model, (List<Map<String, Object>>) model.get(IndexFields.SIGNATURE_RESOURCES), input));
		code.setSignatureContents(signatureContents);

		List<GutPrerequisites> gutPrerequisites = new ArrayList<>();
		List<Map<String, String>> prerequisites = (List<Map<String, String>>) model.get(IndexFields.PREREQUISITES);
		if (prerequisites != null) {
			prerequisites.forEach(p -> {
				GutPrerequisites gutPrerequisite = new GutPrerequisites();
				gutPrerequisite.setId(p.get(IndexFields.ID));
				gutPrerequisite.setCode(p.get(IndexFields.CODE));
				gutPrerequisite.setTitle(p.get(IndexFields.TITLE));
				gutPrerequisites.add(gutPrerequisite);
			});
		}
		code.setGutPrerequisites(gutPrerequisites);
		return code;
	}

	@SuppressWarnings("unchecked")
	private List<SignatureItems> generateSignatureCollections(Map<String, Object> model, List<Map<String, Object>> items, SearchData input) {
		List<SignatureItems> signatureItems = new ArrayList<>(); 
		if (items != null && items.size() > 0) {
			String[] langIds = {SearchSettingService.getByName(DEFAULT_GOORU_LANG_ID)};
			if (input.getUserLanguagePreference() != null) {
				langIds = input.getUserLanguagePreference().split(COMMA);
			}
			for (Map<String, Object> item : items) {
				Map<String, Object> lang = (Map<String, Object>) item.get(IndexFields.PRIMARY_LANGUAGE);
				if (lang != null) {
					Integer langId = (Integer) lang.get(IndexFields.ID);
					if (langIds != null && langIds.length > 0) {
						if (!(Arrays.asList(langIds)).contains(langId.toString())) continue;
					}
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
				}
			}
			if (signatureItems.size() > 0) {
			  if (input.getSIFrom() > signatureItems.size()) {
			    signatureItems = new ArrayList<>();
			  } else {
			    signatureItems = signatureItems.subList(input.getSIFrom(),
		              (Math.min(input.getSIFrom() + input.getSILimit(), signatureItems.size())));
			  }
			}
		}
		return signatureItems;
	}

	@SuppressWarnings("unchecked")
	private List<SignatureResources> generateSignatureResources(Map<String, Object> model, List<Map<String, Object>> resources, SearchData input) {
		List<SignatureResources> signatureResources = new ArrayList<>();
		if (resources != null && resources.size() > 0) {
			String[] langIds = {SearchSettingService.getByName(DEFAULT_GOORU_LANG_ID)};
			if (input.getUserLanguagePreference() != null) {
				langIds = input.getUserLanguagePreference().split(COMMA);
			}
			for (Map<String, Object> resource : resources) {
				Map<String, Object> lang = (Map<String, Object>) resource.get(IndexFields.PRIMARY_LANGUAGE);
				if (lang != null) {
					Integer langId = (Integer) lang.get(IndexFields.ID);
					if (langIds != null && langIds.length > 0) {
						if (!(Arrays.asList(langIds)).contains(langId.toString())) continue;
					}
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
				}
			}
			if (signatureResources.size() > 0) {
			  if (input.getSIFrom() > signatureResources.size()) {
			    signatureResources = new ArrayList<>();
			  } else {
			    signatureResources = signatureResources.subList(input.getSIFrom(), 
			        (Math.min(input.getSIFrom() + input.getSILimit(), signatureResources.size())));
			  }
			}
		}
		return signatureResources;
	}

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.GutDeserializer;
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
