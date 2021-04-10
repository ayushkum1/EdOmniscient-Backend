package com.brainsinjars.projectbackend.dto;

import com.brainsinjars.projectbackend.pojo.MemberType;
import com.brainsinjars.projectbackend.pojo.MembershipStatus;

import java.time.Year;

/**
 * @author Kanchan Harjani
 * @since 09-03-2021
 */
public class UserMemberDTO {
	private String email;
	private String firstName;
	private String lastName;
	private Long memberId;
	private MemberType memberType;
	private MembershipStatus status;
	private String prn;
	private String publicEmail;
	private String publicPhone;
	private Year year;
	private String streetAddr;
	private String city;
	private String state;
	private String pinCode;
	private String region;
	private String about;
	private String fbProfile;
	private String linkedinProfile;
	private String profilePicLink;
	private Long instituteId;

	public UserMemberDTO() {
		super();
	}

	public UserMemberDTO(String email, String firstName, String lastName, Long memberId, MemberType memberType,
						 MembershipStatus status, String prn, String publicEmail, String publicPhone, Year year, String streetAddr,
						 String city, String state, String pinCode, String region, String about, String fbProfile,
						 String linkedinProfile, String profilePicLink) {
		super();
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.memberId = memberId;
		this.memberType = memberType;
		this.status = status;
		this.prn = prn;
		this.publicEmail = publicEmail;
		this.publicPhone = publicPhone;
		this.year = year;
		this.streetAddr = streetAddr;
		this.city = city;
		this.state = state;
		this.pinCode = pinCode;
		this.region = region;
		this.about = about;
		this.fbProfile = fbProfile;
		this.linkedinProfile = linkedinProfile;
		this.profilePicLink = profilePicLink;
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

	public MemberType getMemberType() {
		return memberType;
	}

	public void setMemberType(MemberType memberType) {
		this.memberType = memberType;
	}

	public MembershipStatus getStatus() {
		return status;
	}

	public void setStatus(MembershipStatus status) {
		this.status = status;
	}

	public String getPrn() {
		return prn;
	}

	public void setPrn(String prn) {
		this.prn = prn;
	}

	public String getPublicEmail() {
		return publicEmail;
	}

	public void setPublicEmail(String publicEmail) {
		this.publicEmail = publicEmail;
	}

	public String getPublicPhone() {
		return publicPhone;
	}

	public void setPublicPhone(String publicPhone) {
		this.publicPhone = publicPhone;
	}

	public Year getYear() {
		return year;
	}

	public void setYear(Year year) {
		this.year = year;
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

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
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

	public Long getInstituteId() {
		return instituteId;
	}

	public void setInstituteId(Long instituteId) {
		this.instituteId = instituteId;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
}
