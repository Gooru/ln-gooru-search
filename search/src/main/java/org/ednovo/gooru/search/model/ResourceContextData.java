package org.ednovo.gooru.search.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.ednovo.gooru.search.es.model.Code;

public class ResourceContextData {
	
	private String resourceGooruOid;
	
	private String resourceTitle;
	
	private String resourceFormat;
	
	private String resourceCategory;
	
	private Integer resourceBrokenStatus;
	
	private Long resourceContentId;
	
	private String resourceCreatorUid;
	
	private List<String> resourceGrade;
	
	private String resourceLicenseName;
	
	private Integer resourceIsOer;
	
	private Set<Code> resourceTaxonomySet;
	
	private String resourceVocabulary;
	
	private Set<Code> collectionTaxonomySet;
	
	private List<String> resourceSubjectId;
	
	private List<String> resourceCourseId;
	
	private ArrayList<String> resourceStandards;
	
	public List<String> getResourceSubjectId() {
		return resourceSubjectId;
	}

	public void setResourceSubjectId(List<String> resourceSubjectId) {
		this.resourceSubjectId = resourceSubjectId;
	}

	public List<String> getResourceCourseId() {
		return resourceCourseId;
	}

	public void setResourceCourseId(List<String> resourceCourseId) {
		this.resourceCourseId = resourceCourseId;
	}

	public Set<Code> getCollectionTaxonomySet() {
		return collectionTaxonomySet;
	}

	public void setCollectionTaxonomySet(Set<Code> collectionTaxonomySet) {
		this.collectionTaxonomySet = collectionTaxonomySet;
	}

	public void setResourceGooruOid(String resourceGooruOid) {
		this.resourceGooruOid = resourceGooruOid;
	}

	public String getResourceGooruOid() {
		return resourceGooruOid;
	}

	public void setResourceTitle(String resourceTitle) {
		this.resourceTitle = resourceTitle;
	}

	public String getResourceTitle() {
		return resourceTitle;
	}
	
	public String getResourceFormat() {
		return resourceFormat;
	}

	public void setResourceFormat(String resourceFormat) {
		this.resourceFormat = resourceFormat;
	}

	public Integer getResourceBrokenStatus() {
		return resourceBrokenStatus;
	}

	public void setResourceBrokenStatus(Integer resourceBrokenStatus) {
		this.resourceBrokenStatus = resourceBrokenStatus;
	}

	public Long getResourceContentId() {
		return resourceContentId;
	}

	public void setResourceContentId(Long resourceContentId) {
		this.resourceContentId = resourceContentId;
	}

	public String getResourceCreatorUid() {
		return resourceCreatorUid;
	}

	public void setResourceCreatorUid(String resourceCreatorUid) {
		this.resourceCreatorUid = resourceCreatorUid;
	}

	public List<String> getResourceGrade() {
		return resourceGrade;
	}

	public void setResourceGrade(List<String> resourceGrade) {
		this.resourceGrade = resourceGrade;
	}

	public String getResourceLicenseName() {
		return resourceLicenseName;
	}

	public void setResourceLicenseName(String resourceLicenseName) {
		this.resourceLicenseName = resourceLicenseName;
	}

	public Integer getResourceIsOer() {
		return resourceIsOer;
	}

	public void setResourceIsOer(Integer resourceIsOer) {
		this.resourceIsOer = resourceIsOer;
	}

	public Set<Code> getResourceTaxonomySet() {
		return resourceTaxonomySet;
	}

	public void setResourceTaxonomySet(Set<Code> resourceTaxonomySet) {
		this.resourceTaxonomySet = resourceTaxonomySet;
	}

	public String getResourceVocabulary() {
		return resourceVocabulary;
	}

	public void setResourceVocabulary(String resourceVocabulary) {
		this.resourceVocabulary = resourceVocabulary;
	}

	public void setResourceStandards(ArrayList<String> resourceStandards) {
		this.resourceStandards = resourceStandards;
	}

	public ArrayList<String> getResourceStandards() {
		return resourceStandards;
	}

	public void setResourceCategory(String resourceCategory) {
		this.resourceCategory = resourceCategory;
	}

	public String getResourceCategory() {
		return resourceCategory;
	}

}
