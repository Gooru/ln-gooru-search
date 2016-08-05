package org.ednovo.gooru.search.es.model;

public enum PublishedStatus {
  
  PUBLISHED("published"),
  UNPUBLISHED("unpublished");
  
  private String status;
  
  PublishedStatus(String status){
    this.status = status;
  }

  public String getStatus() {
    return status;
  }
  
}
