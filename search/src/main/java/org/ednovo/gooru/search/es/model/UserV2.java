package org.ednovo.gooru.search.es.model;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class UserV2 implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7049097325707715150L;
	
	private String firstname;
	
	private String lastname;
	
	private String usernameDisplay;
	
	private String id;
	
	private String profileImage;

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getUsernameDisplay() {
	    if (usernameDisplay == null || usernameDisplay.isEmpty()) {
	      String firstName = "";
	      if (this.getFirstname() != null) {
	        firstName = this.getFirstname();
	        firstName = StringUtils.remove(firstName, " ");
	      }
	      String lastName = "";
	      if (this.getLastname() != null) {
	        lastName = this.getLastname();
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

	public void setUsernameDisplay(String usernameDisplay) {
		this.usernameDisplay = usernameDisplay;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}


}
