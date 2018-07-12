package org.ednovo.gooru.search.responses.v3;

import java.io.Serializable;

import org.springframework.data.annotation.Transient;



public class UserV3 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7049097325707715150L;
	
	private String firstName;
	
	private String lastName;	
	
	@Transient
	private String username;	

	private String profileImageUrl;

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

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
