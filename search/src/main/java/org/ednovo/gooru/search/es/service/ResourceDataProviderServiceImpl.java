package org.ednovo.gooru.search.es.service;

import org.ednovo.gooru.search.es.constant.Constants;
import org.ednovo.gooru.search.model.ResourceContextData;
import org.ednovo.gooru.search.model.ResourceDataProviderCriteria;
import org.springframework.stereotype.Service;

@Service
public class ResourceDataProviderServiceImpl implements ResourceDataProviderService, Constants {

/*	@Autowired
	private ResourceRepository resourceRepository;
	
	@Autowired
	private CollectionRepository collectionRepository;
	
	@Autowired
	private ContentIndexDao contentIndexDao;*/
	// Disabled while removing api-jar dependency
	@Override
	public ResourceContextData getResourceContextData(String resourceGooruOid) {/*
		Resource resource = resourceRepository.findResourceByContent(resourceGooruOid);
		ResourceContextData resourceContext = new ResourceContextData();
		resourceContext.setResourceGooruOid(resource.getGooruOid());
		resourceContext.setResourceTitle(resource.getTitle());
		return resourceContext;
	*/return null;}
	
	@Override
	public ResourceContextData getResourceContextData(ResourceDataProviderCriteria resourceDataProviderCriteria) {/*
		Resource resource = resourceRepository.findResourceByContent(resourceDataProviderCriteria.getResourceId());
		if(resource == null){
			return null;
		}
		ResourceContextData resourceContextDo = new ResourceContextData();
		resourceContextDo.setResourceGooruOid(resource.getGooruOid());
		resourceContextDo.setResourceTitle(resource.getTitle());
		resourceContextDo.setResourceFormat((resource.getResourceFormat() != null && resource.getResourceFormat().getValue() != null) ? resource.getResourceFormat().getValue() : null);
		resourceContextDo.setResourceCategory(resource.getCategory());
		resourceContextDo.setResourceBrokenStatus(resource.getBrokenStatus());
		resourceContextDo.setResourceContentId(resource.getContentId());
		resourceContextDo.setResourceCreatorUid((resource.getCreator() != null && resource.getCreator().getPartyUid() != null) ? resource.getCreator().getPartyUid() : null);
		resourceContextDo.setResourceLicenseName((resource.getLicense() != null && resource.getLicense().getName() != null) ? resource.getLicense().getName() : null);
		resourceContextDo.setResourceIsOer(resource.getIsOer() != null ? resource.getIsOer() : 0);
		//resourceContextDo.setResourceTaxonomySet(resource.getTaxonomySet() != null ? resource.getTaxonomySet() : null);
		resourceContextDo.setResourceVocabulary(resource.getVocabulary() != null ? resource.getVocabulary() : null);
		
		List<Object[]> standardsTaxonomyMetaList = getContentIndexDao().getStandardsTaxonomyMeta(resource.getContentId(), true);
		Set<String> subjectCodeIds = new HashSet<String>();
		Set<String> courseCodeIds = new HashSet<String>();
		Set<String> grades = new HashSet<String>();
		
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
			resourceContextDo.setResourceSubjectId(new ArrayList(subjectCodeIds));
			resourceContextDo.setResourceCourseId(new ArrayList(courseCodeIds));
		}
		
		if (StringUtils.isNotBlank(resource.getGrade())) {
			for (String resourceGrade : resource.getGrade().split(COMMA)) {
				grades.add(resourceGrade);
			}
		}

		if (grades.size() > 0) {
			grades = GradeUtils.extractGrades(StringUtils.join(grades, COMMA));
			resourceContextDo.setResourceGrade(new ArrayList(grades));
		}
		
		if(resource.getTaxonomySet() != null && resource.getTaxonomySet().size() > 0){
			Set<String> displayCode = new HashSet<String>();
			Set<String> curriculumCode = new HashSet<String>();
			//disabled temporarily
			for (Code resourceCode : resource.getTaxonomySet()) {
				if (resourceCode.getRootNodeId() != null && !resourceCode.getRootNodeId().equals(20000) && !resourceCode.getCode().startsWith(TWENTY_FIRST_CENTURY_SKILLS_CODE)) {
					if (!curriculumCode.contains(resourceCode.getCode().toLowerCase())) {
						curriculumCode.add(resourceCode.getCode().toLowerCase().replace(".--", " "));
					}
					if (resourceCode.getCommonCoreDotNotation() != null && resourceCode.getCommonCoreDotNotation().trim().length() > 0
							&& !displayCode.contains(resourceCode.getCommonCoreDotNotation().toLowerCase())) {
						displayCode.add(resourceCode.getCommonCoreDotNotation().toLowerCase());
					} else if (resourceCode.getdisplayCode() != null && resourceCode.getdisplayCode().trim().length() > 0 && !displayCode.contains(resourceCode.getdisplayCode().toLowerCase())) {
						displayCode.add(resourceCode.getdisplayCode().toLowerCase());
					}
				}
			}
			if (displayCode.size() > 0) {
				curriculumCode = displayCode;
			}
			resourceContextDo.setResourceStandards(new ArrayList(curriculumCode));
		}
		if(StringUtils.trimToNull(resourceDataProviderCriteria.getParentId()) != null){
			Collection collection = collectionRepository.getCollectionByGooruOid(resourceDataProviderCriteria.getParentId(), null);
			//resourceContextDo.setCollectionTaxonomySet(collection.getTaxonomySet());
		}
		return resourceContextDo;
	*/return null;}
	
/*	private ContentIndexDao getContentIndexDao() {
		return contentIndexDao;
	}
	
	public void setContentIndexDao(ContentIndexDao contentIndexDao) {
		this.contentIndexDao = contentIndexDao;
	}*/

}
