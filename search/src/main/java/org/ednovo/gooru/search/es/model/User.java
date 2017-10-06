package org.ednovo.gooru.search.es.model;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

public class User implements Serializable {

	private static final long serialVersionUID = 8293909847220631830L;

	private String firstName;
	
	private String lastName;
	
	private String username;
	
	private String gooruUId;
	
	private String emailId = "";
	
	private String partyUid;

	private String profileImageUrl;
	
	private Boolean isDeleted = false;
	
	private String organizationName;
	
	private String userRoleSetString;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUsernameDisplay() {
    String usernameDisplay = username;
    if (username == null || username.isEmpty()) {
      String firstName = "";
      if (this.getFirstName() != null) {
        firstName = this.getFirstName();
        firstName = StringUtils.remove(firstName, " ");
      }
      String lastName = "";
      if (this.getLastName() != null) {
        lastName = this.getLastName();
      }

      usernameDisplay = firstName;
      if (lastName.length() > 0) {
        usernameDisplay = usernameDisplay + lastName.substring(0, 1);
      }
      if (usernameDisplay.length() > 20) {
        usernameDisplay = usernameDisplay.substring(0, 20);
      }
    }
    return usernameDisplay;
  }
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getGooruUId() {
		return gooruUId;
	}

	public void setGooruUId(String gooruUId) {
		this.gooruUId = gooruUId;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPartyUid() {
		return partyUid;
	}

	public void setPartyUid(String partyUid) {
		this.partyUid = partyUid;
	}

  public String getProfileImageUrl() {
    return profileImageUrl;
  }

  public void setProfileImageUrl(String profileImageUrl) {
    this.profileImageUrl = profileImageUrl;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public String getOrganizationName() {
    return organizationName;
  }

  public void setOrganizationName(String organizationName) {
    this.organizationName = organizationName;
  }

  public String getUserRoleSetString() {
    return userRoleSetString;
  }

  public void setUserRoleSetString(String userRoleSetString) {
    this.userRoleSetString = userRoleSetString;
  }

}
