package org.ednovo.gooru.search.domain.service;

import java.io.Serializable;
import java.util.Map;

import org.ednovo.gooru.search.es.model.License;
import org.ednovo.gooru.search.es.model.UserV2;
 
public class CourseSearchResult implements Serializable{
   
   /**
    * 
    */
   private static final long serialVersionUID = 6280104303486215659L;
 
   private String id;
   
   private String lastModified;
   
   private String addDate;
   
   private String lastModifiedBy;
   
   private UserV2 creator;
   
   private UserV2 owner;
   
   private UserV2 orginalCreator;
   
   private License license;
   
   private boolean visibleOnProfile;
   
   private String publishStatus;
   
   private int unitCount;
   
   private int courseRemixCount;
   
   private long viewCount;
   
   private String subjectBucket;
   
   private int sequence;
   
   private Map<String, Object> taxonomy;
   
   private int collaboratorCount;
   
   private String thumbnail;
   
   private String title;
   
   private String description;
 
   private int subjectSequence;
   
   public String getLastModified() {
       return lastModified;
   }
 
   public void setLastModified(String lastModified) {
       this.lastModified = lastModified;
   }
 
   public String getAddDate() {
       return addDate;
   }
 
   public void setAddDate(String addDate) {
       this.addDate = addDate;
   }
 
   public String getLastModifiedBy() {
       return lastModifiedBy;
   }
 
   public void setLastModifiedBy(String lastModifiedBy) {
       this.lastModifiedBy = lastModifiedBy;
   }
 
   public UserV2 getCreator() {
       return creator;
   }
 
   public void setCreator(UserV2 creator) {
       this.creator = creator;
   }
 
   public UserV2 getOwner() {
       return owner;
   }
 
   public void setOwner(UserV2 owner) {
       this.owner = owner;
   }
 
   public UserV2 getOrginalCreator() {
       return orginalCreator;
   }
 
   public void setOrginalCreator(UserV2 orginalCreator) {
       this.orginalCreator = orginalCreator;
   }
 
   public License getLicense() {
       return license;
   }
 
   public void setLicense(License license) {
       this.license = license;
   }
 
   public boolean isVisibleOnProfile() {
       return visibleOnProfile;
   }
 
   public void setVisibleOnProfile(boolean visibleOnProfile) {
       this.visibleOnProfile = visibleOnProfile;
   }
 
   public String getPublishStatus() {
       return publishStatus;
   }
 
   public void setPublishStatus(String publishStatus) {
       this.publishStatus = publishStatus;
   }
 
   public int getUnitCount() {
       return unitCount;
   }
 
   public void setUnitCount(int unitCount) {
       this.unitCount = unitCount;
   }
 
   public int getCourseRemixCount() {
       return courseRemixCount;
   }
 
   public void setCourseRemixCount(int courseRemixCount) {
       this.courseRemixCount = courseRemixCount;
   }
 
   public long getViewCount() {
       return viewCount;
   }
 
   public void setViewCount(long viewCount) {
       this.viewCount = viewCount;
   }
 
   public String getSubjectBucket() {
       return subjectBucket;
   }
 
   public void setSubjectBucket(String subjectBucket) {
       this.subjectBucket = subjectBucket;
   }
 
   public int getSequence() {
       return sequence;
   }
 
   public void setSequence(int sequence) {
       this.sequence = sequence;
   }
 
   public Map<String, Object> getTaxonomy() {
       return taxonomy;
   }
 
   public void setTaxonomy(Map<String, Object> taxonomy) {
       this.taxonomy = taxonomy;
   }
 
   public int getCollaboratorCount() {
       return collaboratorCount;
   }
 
   public void setCollaboratorCount(int collaboratorCount) {
       this.collaboratorCount = collaboratorCount;
   }
 
   public String getThumbnail() {
       return thumbnail;
   }
 
   public void setThumbnail(String thumbnail) {
       this.thumbnail = thumbnail;
   }
 
   public String getTitle() {
       return title;
   }
 
   public void setTitle(String title) {
       this.title = title;
   }
 
   public String getDescription() {
       return description;
   }
 
   public void setDescription(String description) {
       this.description = description;
   }
 
   public String getId() {
       return id;
   }
 
   public void setId(String id) {
       this.id = id;
   }

	public int getSubjectSequence() {
		return subjectSequence;
	}
	
	public void setSubjectSequence(int subjectSequence) {
		this.subjectSequence = subjectSequence;
	}
 
}

