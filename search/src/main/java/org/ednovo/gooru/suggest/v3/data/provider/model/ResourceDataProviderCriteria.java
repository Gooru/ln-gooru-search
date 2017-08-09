package org.ednovo.gooru.suggest.v3.data.provider.model;


public class ResourceDataProviderCriteria extends ContextDataProviderCriteria {

  private String resourceId;
  
  private String collectionId;

  public void setResourceId(String resourceId) {
    this.resourceId = resourceId;
  }

  public String getResourceId() {
    return resourceId;
  }

  public void setCollectionId(String collectionId) {
    this.collectionId = collectionId;
  }

  public String getCollectionId() {
    return collectionId;
  }
}
