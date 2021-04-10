package com.brainsinjars.projectbackend.dto;

public class UserPatchDTO {
	private String profilePicLink;

	public UserPatchDTO() {
		super();
	}

	public UserPatchDTO(String profilePicLink) {
		super();
		this.profilePicLink = profilePicLink;
	}

	public String getProfilePicLink() {
		return profilePicLink;
	}

	public void setProfilePicLink(String profilePicLink) {
		this.profilePicLink = profilePicLink;
	}

}
