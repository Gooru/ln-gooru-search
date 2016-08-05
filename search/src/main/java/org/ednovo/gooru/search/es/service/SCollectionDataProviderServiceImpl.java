package org.ednovo.gooru.search.es.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.ednovo.gooru.search.es.constant.Constants;
import org.ednovo.gooru.search.es.repository.CollectionRepository;
import org.ednovo.gooru.search.es.repository.ContentRepository;
import org.ednovo.gooru.search.es.repository.CourseRepository;
import org.ednovo.gooru.search.model.CollectionContextData;
import org.ednovo.gooru.search.model.CollectionDataProviderCriteria;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SCollectionDataProviderServiceImpl implements SCollectionDataProviderService, Constants{

	private final String NO_QUESTION = "noQuestion";
	private final String ONLY_QUESTION = "onlyQuestion";
	private final String SOME_QUESTION = "someQuestion";
	
	@Autowired
	private CollectionRepository collectionRepository;
	
	@Autowired
	private ContentRepository contentRepository;
	
	@Autowired
	private CourseRepository courseRepository;
	
	//To be re-enabled. Disabled while removing api-jar dependency 
/*	
	@Autowired
	private ContentIndexDao contentIndexDao;
	
	@Autowired
	private ResourceRepository resourceRepository;*/
	
	@Override
	public List<CollectionContextData> getCollectionList(CollectionDataProviderCriteria collectionDataProviderCriteria) {
		List<CollectionContextData> collectionDataList = new ArrayList<CollectionContextData>();
		CollectionContextData collectionContextDo = new CollectionContextData();
		String collectionId = collectionDataProviderCriteria.getCollectionIds().get(0);
		
		Map<String, Object> collectionList = collectionRepository.getCollectionData(collectionDataProviderCriteria.getCollectionIds().get(0));
		if (collectionList != null && collectionList.get("taxonomy") != null) {
			String taxonomy = collectionList.get("taxonomy").toString();
			List<String> standards = new ArrayList<>();
			List<String> taxSubjectIds = new ArrayList<>();
			List<String> taxCourseIds = new ArrayList<>();
			try {
				JSONObject taxonomyObject = new JSONObject(taxonomy);
				Iterator<?> taxonomyCode = taxonomyObject.keys();
				while (taxonomyCode.hasNext()) {
					String code = taxonomyCode.next().toString();
					standards.add(code.toLowerCase());
					String subjectCode = null;
					String courseCode = null;

					String[] codes = code.toString().split("-");
					if (codes.length > 0) {
						if (codes.length == 2) {
							subjectCode = code;
						} else if (codes.length >= 2) {
							subjectCode = code.substring(0, StringUtils.ordinalIndexOf(code, "-", 2));
							if (codes.length == 3) {
								courseCode = code;
							} else if (codes.length >= 4) {
								courseCode = code.substring(0, StringUtils.ordinalIndexOf(code, "-", 3));
							}
						}
						taxSubjectIds.add(subjectCode != null ? subjectCode.toLowerCase() : null);
						taxCourseIds.add(courseCode != null ? courseCode.toLowerCase() : null);

					}
				}
				collectionContextDo.setCollectionTaxonomySubjectId(taxSubjectIds);
				collectionContextDo.setCollectionTaxonomyCourseId(taxCourseIds);
				collectionContextDo.setCollectionStandards(standards);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		collectionContextDo.setCollectionGooruOid(collectionId);


		if(collectionList != null && collectionList.get("course_id") != null) {
			String courseId = collectionList.get("course_id").toString();
			collectionContextDo.setCollectionCourseId(courseId);
			Map<String, Object> courseMetaMap = courseRepository.getCourseData(courseId);
			if (courseMetaMap != null && courseMetaMap.get("title") != null) {
				collectionContextDo.setCollectionCourseTitle(courseMetaMap.get("title").toString());
			}

		}
		if(collectionList != null && collectionList.get("title") != null) collectionContextDo.setCollectionTitle(collectionList.get("title").toString());
		List<Map<String, Object>> itemList = contentRepository.getItems(collectionDataProviderCriteria.getCollectionIds().get(0));
		Set<String> itemIds = new HashSet<>();
		itemList.forEach(item -> {
			itemIds.add(item.get("id").toString());
		});
		collectionContextDo.setCollectionResourceIds(itemIds);
		collectionContextDo.setCollectionItemCount(itemList.size());
		collectionDataList.add(collectionContextDo);
		/*AssessmentQuestion assessmentQuestion = null;
		List<CollectionContextData> collectionDataList = new ArrayList<CollectionContextData>();
		List<Collection> collectionList = collectionRepository.getCollectionListByIds(collectionDataProviderCriteria.getCollectionIds());
		if(collectionList != null && collectionList.size() > 0){
			for(Collection collection : collectionList){
				CollectionContextData collectionContextDo = new CollectionContextData();
				HashSet<String> resourceGooruIds = new HashSet<String>();
				Map<String, Set<Code>> resourceTaxonomy = new HashMap<String, Set<Code>>();
				Map<String, Resource> resourceData = new HashMap<String, Resource>();
				List<String> questionResourceConcepts = new ArrayList<String>();
				List<CollectionItem> collectionItemCount = new ArrayList<CollectionItem>();
				Set<String> grades = new HashSet<String>();
				int totalNoOfQuestion = 0, totalNoOfNonQuestion = 0;
				String category = "";

				for(CollectionItem collectionItem : collection.getCollectionItems()){
					if (collectionItem.getContent() != null) {
						Resource resource = getResourceRepository().findResourceByContent(collectionItem.getContent().getGooruOid());
						if (resource != null) {
							resourceGooruIds.add(collectionItem.getContent().getGooruOid());
							resourceData.put(resource.getGooruOid(), resource);
							resourceTaxonomy.put(resource.getGooruOid(), resource.getTaxonomySet());
							if (resource.getResourceType().getName().equalsIgnoreCase(ResourceType.Type.ASSESSMENT_QUESTION.getType())) {
								assessmentQuestion = (AssessmentQuestion) resource;
								questionResourceConcepts.add(assessmentQuestion.getConcept());
							}
							collectionItemCount = this.getCollectionItems(collectionItem.getContent().getGooruOid(), new HashMap<String, String>());
							if (resource.getResourceType().getName().equalsIgnoreCase(ResourceType.Type.ASSESSMENT_QUESTION.getType())) {
								totalNoOfQuestion++;
							} else if (!resource.getResourceType().getName().equalsIgnoreCase(ResourceType.Type.ASSESSMENT_QUESTION.getType())) {
								totalNoOfNonQuestion++;
							}
						}
					}
				}
				if (totalNoOfQuestion == 0) {
					category = NO_QUESTION;
				} else if (totalNoOfQuestion != 0 && totalNoOfNonQuestion != 0) {
					category = SOME_QUESTION;
				} else if (totalNoOfQuestion != 0 && totalNoOfNonQuestion == 0) {
					category = ONLY_QUESTION;
				}
				List<Object[]> standardsTaxonomyMetaList = getContentIndexDao().getStandardsTaxonomyMeta(collection.getContentId(), true);
				Set<String> subjectCodeIds = new HashSet<String>();
				Set<String> courseCodeIds = new HashSet<String>();
				
				if (standardsTaxonomyMetaList != null && standardsTaxonomyMetaList.size() > 0) {
					for (Object[] standardsTaxonomyMeta : standardsTaxonomyMetaList) {
						if(standardsTaxonomyMeta[1] != null) {
							subjectCodeIds.add(standardsTaxonomyMeta[1].toString());
						}
						if(standardsTaxonomyMeta[3] != null) {
							courseCodeIds.add(standardsTaxonomyMeta[3].toString());
						}
						if(standardsTaxonomyMeta[4] != null) {
							for (String courseGrade : standardsTaxonomyMeta[4].toString().split(COMMA)) {
								grades.add(courseGrade);
							}
						}
					}
					collectionContextDo.setCollectionSubjectId(new ArrayList(subjectCodeIds));
					collectionContextDo.setCollectionCourseId(new ArrayList(courseCodeIds));
				}
				
				if (StringUtils.isNotBlank(collection.getGrade())) {
					for (String collectionGrade : collection.getGrade().split(COMMA)) {
						grades.add(collectionGrade);
					}
				}
				if (grades.size() > 0) {
					grades = GradeUtils.extractGrades(StringUtils.join(grades, COMMA));
					collectionContextDo.setCollectionGrade(new ArrayList(grades));
				}
				
				if (collection.getTaxonomySet() != null && collection.getTaxonomySet().size() > 0) {
					Set<String> displayCode = new HashSet<String>();
					Set<String> curriculumCode = new HashSet<String>();
					for (Code collectionCode : collection.getTaxonomySet()) {
						if (collectionCode.getRootNodeId() != null && !collectionCode.getRootNodeId().equals(20000) && !collectionCode.getCode().startsWith(TWENTY_FIRST_CENTURY_SKILLS_CODE)) {
							if (!curriculumCode.contains(collectionCode.getCode().toLowerCase())) {
								curriculumCode.add(collectionCode.getCode().toLowerCase().replace(".--", " "));
							}
							if (collectionCode.getCommonCoreDotNotation() != null && collectionCode.getCommonCoreDotNotation().trim().length() > 0
									&& !displayCode.contains(collectionCode.getCommonCoreDotNotation().toLowerCase())) {
								displayCode.add(collectionCode.getCommonCoreDotNotation().toLowerCase());
							} else if (collectionCode.getdisplayCode() != null && collectionCode.getdisplayCode().trim().length() > 0 && !displayCode.contains(collectionCode.getdisplayCode().toLowerCase())) {
								displayCode.add(collectionCode.getdisplayCode().toLowerCase());
							}
						}
					}
					if (displayCode.size() > 0) {
						curriculumCode = displayCode;
					}
					collectionContextDo.setCollectionStandards(new ArrayList(curriculumCode));
				}
				
				collectionContextDo.setCollectionTitle(collection.getTitle());
				//collectionContextDo.setCollectionTaxonomy(collection.getTaxonomySet());
				collectionContextDo.setCollectionGooruOid(collection.getGooruOid());
				collectionContextDo.setCollectionContentId(collection.getContentId());
				collectionContextDo.setCollectionItemCount(collectionItemCount.size());
				collectionContextDo.setCollectionCategory(category);
				collectionContextDo.setCollectionQuestionConcepts(questionResourceConcepts);
				collectionContextDo.setCollectionResourceIds(resourceGooruIds);
				collectionContextDo.setCollectionItemData(resourceData);
				collectionContextDo.setResourcesTaxonomy(resourceTaxonomy);
				collectionDataList.add(collectionContextDo);
			}
		}
		return collectionDataList;
	*/return collectionDataList;}
	
	/*private List<CollectionItem> getCollectionItems(String collectionId, Map<String, String> filters) {
		return this.collectionRepository.getCollectionItems(collectionId, filters);
	}*/
	
/*	private ContentIndexDao getContentIndexDao() {
		return contentIndexDao;
	}
	
	public void setContentIndexDao(ContentIndexDao contentIndexDao) {
		this.contentIndexDao = contentIndexDao;
	}
	
	public ResourceRepository getResourceRepository() {
		return resourceRepository;
	}*/

}

