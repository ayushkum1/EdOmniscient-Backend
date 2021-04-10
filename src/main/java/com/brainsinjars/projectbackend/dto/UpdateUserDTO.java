package com.brainsinjars.projectbackend.dto;

/**
 * @author Kanchan Harjani
 * @since 09-03-2021
 */
public class UpdateUserDTO {
	private String passwdHash;
	private String firstName;
	private String lastName;
	private String about;
	private String streetAddr;
	private String city;
	private String state;
	private String pinCode;
	private String region;
	private String fbProfile;
	private String linkedinProfile;
	private String profilePicLink;

	public UpdateUserDTO() {
		super();
	}

	public UpdateUserDTO(String passwdHash, String firstName, String lastName, String about, String streetAddr,
			String city, String state, String pinCode, String region, String fbProfile, String linkedinProfile,
			String profilePicLink) {
		super();
		this.passwdHash = passwdHash;
		this.firstName = firstName;
		this.lastName = lastName;
		this.about = about;
		this.streetAddr = streetAddr;
		this.city = city;
		this.state = state;
		this.pinCode = pinCode;
		this.region = region;
		this.fbProfile = fbProfile;
		this.linkedinProfile = linkedinProfile;
		this.profilePicLink = profilePicLink;
	}

	public String getPasswdHash() {
		return passwdHash;
	}

	public void setPasswdHash(String passwdHash) {
		this.passwdHash = passwdHash;
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

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
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

}
