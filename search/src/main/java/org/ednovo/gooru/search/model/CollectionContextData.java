package org.ednovo.gooru.search.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ednovo.gooru.search.es.model.Code;
import org.ednovo.gooru.search.es.model.ContentSearchResult;

public class CollectionContextData {

	private String collectionGooruOid;
	
	private Long collectionContentId;
	
	private String collectionTitle;
	
	private Set<String> collectionResourceIds;
	
	private Map<String, ContentSearchResult> collectionItemData;
	
	private Set<Code> collectionTaxonomy;
	
	private Map<String, Set<Code>> resourcesTaxonomy;
	
	private List<String> collectionQuestionConcepts;
		
	private Integer collectionItemCount;
	
	private String collectionCategory;
	
	private List<String> collectionTaxonomySubjectId;
	
	private List<String> collectionTaxonomyCourseId;
	
	private String collectionCourseId;
	
	private String collectionCourseTitle;
	
	private List<String> collectionStandards;
	
	private List<String> resourceTitles;
	
	private List<String> collectionGrade;

	public void setCollectionGooruOid(String collectionGooruOid) {
		this.collectionGooruOid = collectionGooruOid;
	}

	public String getCollectionGooruOid() {
		return collectionGooruOid;
	}

	public void setCollectionTitle(String collectionTitle) {
		this.collectionTitle = collectionTitle;
	}

	public String getCollectionTitle() {
		return collectionTitle;
	}

	public void setResourcesTaxonomy(Map<String, Set<Code>> resourcesTaxonomy) {
		this.resourcesTaxonomy = resourcesTaxonomy;
	}

	public Map<String, Set<Code>> getResourcesTaxonomy() {
		return resourcesTaxonomy;
	}

	public void setCollectionResourceIds(Set<String> collectionResourceIds) {
		this.collectionResourceIds = collectionResourceIds;
	}

	public Set<String> getCollectionResourceIds() {
		return collectionResourceIds;
	}

	public void setCollectionTaxonomy(Set<Code> collectionTaxonomy) {
		this.collectionTaxonomy = collectionTaxonomy;
	}

	public Set<Code> getCollectionTaxonomy() {
		return collectionTaxonomy;
	}

	public void setCollectionQuestionConcepts(List<String> collectionQuestionConcepts) {
		this.collectionQuestionConcepts = collectionQuestionConcepts;
	}

	public List<String> getCollectionQuestionConcepts() {
		return collectionQuestionConcepts;
	}

	public void setCollectionContentId(Long contentId) {
		this.collectionContentId = contentId;
	}

	public Long getCollectionContentId() {
		return collectionContentId;
	}

	public void setCollectionItemCount(int count) {
		this.collectionItemCount = count;
	}

	public Integer getCollectionItemCount() {
		return collectionItemCount;
	}

	public void setCollectionCategory(String collectionCategory) {
		this.collectionCategory = collectionCategory;
	}

	public String getCollectionCategory() {
		return collectionCategory;
	}

	public void setCollectionTaxonomySubjectId(List<String> collectionTaxonomySubjectId) {
		this.collectionTaxonomySubjectId = collectionTaxonomySubjectId;
	}

	public List<String> getCollectionTaxonomySubjectId() {
		return collectionTaxonomySubjectId;
	}

	public List<String> getCollectionTaxonomyCourseId() {
		return collectionTaxonomyCourseId;
	}

	public void setCollectionTaxonomyCourseId(List<String> collectionTaxonomyCourseId) {
		this.collectionTaxonomyCourseId = collectionTaxonomyCourseId;
	}

	public String getCollectionCourseId() {
		return collectionCourseId;
	}

	public void setCollectionCourseId(String collectionCourseId) {
		this.collectionCourseId = collectionCourseId;
	}

	public String getCollectionCourseTitle() {
		return collectionCourseTitle;
	}

	public void setCollectionCourseTitle(String collectionCourseTitle) {
		this.collectionCourseTitle = collectionCourseTitle;
	}

	public void setResourceTitles(List<String> resourceTitles) {
		this.resourceTitles = resourceTitles;
	}

	public List<String> getResourceTitles() {
		return resourceTitles;
	}

	public void setCollectionStandards(List<String> collectionStandards) {
		this.collectionStandards = collectionStandards;
	}

	public List<String> getCollectionStandards() {
		return collectionStandards;
	}

	public void setCollectionItemData(Map<String, ContentSearchResult> collectionItemData) {
		this.collectionItemData = collectionItemData;
	}

	public Map<String, ContentSearchResult> getCollectionItemData() {
		return collectionItemData;
	}

	public List<String> getCollectionGrade() {
		return collectionGrade;
	}

	public void setCollectionGrade(List<String> collectionGrade) {
		this.collectionGrade = collectionGrade;
	}
	
}
