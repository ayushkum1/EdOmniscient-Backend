package com.brainsinjars.projectbackend.dto;

/**
 * @author Kanchan Harjani
 * @since 09-03-2021
 */
public class UserDetailsDTO {
	private String email;
	private String firstName;
	private String lastName;
	private String fbProfile;
	private String linkedinProfile;
	private String profilePicLink;
	private String streetAddr;
	private String city;
	private String state;
	private String pinCode;
	private String region;

	public UserDetailsDTO() {
		super();
	}

	public UserDetailsDTO(String email, String firstName, String lastName, String fbProfile, String linkedinProfile,
			String profilePicLink, String streetAddr, String city, String state, String pinCode, String region) {
		super();
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.fbProfile = fbProfile;
		this.linkedinProfile = linkedinProfile;
		this.profilePicLink = profilePicLink;
		this.streetAddr = streetAddr;
		this.city = city;
		this.state = state;
		this.pinCode = pinCode;
		this.region = region;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

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

	public String getFbProfile() {
		return fbProfile;
	}

	public void setFbProfile(String fbProfile) {
		this.fbProfile = fbProfile;
	}

	public String getLinkedinProfile() {
		return linkedinProfile;
	}

	public void setLinkedinProfile(String linkedinProfile) {
		this.linkedinProfile = linkedinProfile;
	}

	public String getProfilePicLink() {
		return profilePicLink;
	}

	public void setProfilePicLink(String profilePicLink) {
		this.profilePicLink = profilePicLink;
	}

	public String getStreetAddr() {
		return streetAddr;
	}

	public void setStreetAddr(String streetAddr) {
		this.streetAddr = streetAddr;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

}
