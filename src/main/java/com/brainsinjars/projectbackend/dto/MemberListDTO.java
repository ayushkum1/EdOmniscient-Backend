/* 
 * @Author Channappa Mirgale 
 * @Since 09-03-21
*/
package com.brainsinjars.projectbackend.dto;

import java.time.Year;

import com.brainsinjars.projectbackend.pojo.MemberType;
import com.brainsinjars.projectbackend.pojo.MembershipStatus;

public class MemberListDTO {

	private long id;
	//taken userEmail,name and ProfilePic from Users Pojo
	private String name;
	private String profilePic;
	private String userEmail;
	//taken courseId from Courses Pojo
	private long courseId;
	private String about;
	private String prn;
	private MemberType memberType;
	private String publicPhone;
	private MembershipStatus status;
	private String publicEmail;
	private Year year;
	
	
	public MemberListDTO(long id, String userEmail, long courseId, String prn, MemberType memberType, String publicPhone,
						 String publicEmail, Year year, MembershipStatus status, String name, String profilePic, String about) {
		super();
		this.id = id;
		this.userEmail = userEmail;
		this.courseId = courseId;
		this.prn = prn;
		this.memberType = memberType;
		this.publicPhone = publicPhone;
		this.publicEmail = publicEmail;
		this.year = year;
		this.status=status;
		this.name=name;
		this.profilePic=profilePic;
		this.about=about;
	}

	public MemberListDTO(long id, String name, String profilePic, String userEmail, long courseId, String about, String prn, MemberType memberType, String publicPhone, MembershipStatus status, String publicEmail, Year year) {
		this.id = id;
		this.name = name;
		this.profilePic = profilePic;
		this.userEmail = userEmail;
		this.courseId = courseId;
		this.about = about;
		this.prn = prn;
		this.memberType = memberType;
		this.publicPhone = publicPhone;
		this.status = status;
		this.publicEmail = publicEmail;
		this.year = year;
	}

	public MemberListDTO() {
		super();
	}


	public String getUserEmail() {
		return userEmail;
	}



	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}



	public long getCourseId() {
		return courseId;
	}


	public void setCourseId(long courseId) {
		this.courseId = courseId;
	}


	public String getPrn() {
		return prn;
	}


	public void setPrn(String prn) {
		this.prn = prn;
	}


	public MemberType getMemberType() {
		return memberType;
	}


	public void setMemberType(MemberType memberType) {
		this.memberType = memberType;
	}


	public String getPublicPhone() {
		return publicPhone;
	}


	public void setPublicPhone(String publicPhone) {
		this.publicPhone = publicPhone;
	}


	public String getPublicEmail() {
		return publicEmail;
	}


	public void setPublicEmail(String publicEmail) {
		this.publicEmail = publicEmail;
	}


	public Year getYear() {
		return year;
	}


	public void setYear(Year year) {
		this.year = year;
	}



	public MembershipStatus getStatus() {
		return status;
	}



	public void setStatus(MembershipStatus status) {
		this.status = status;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getProfilePic() {
		return profilePic;
	}



	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}



	public String getAbout() {
		return about;
	}



	public void setAbout(String about) {
		this.about = about;
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
