/* 
 * @Author Channappa Mirgale 
 * @Since 09-03-21
*/

package com.brainsinjars.projectbackend.dto;

import java.time.Year;

import com.brainsinjars.projectbackend.pojo.MemberType;

public class MemberAddDTO {

	private String userEmail;
	private long courseId;
	private String prn;
	private MemberType memberType;
	private String publicPhone;
	private String publicEmail;
	private Year year;
	
	public MemberAddDTO()
	{
		super();
	}
	
	public MemberAddDTO(String userEmail, long courseId, String prn, MemberType memberType, String publicPhone,
			String publicEmail, Year year) {
		super();
		this.userEmail = userEmail;
		this.courseId = courseId;
		this.prn = prn;
		this.memberType = memberType;
		this.publicPhone = publicPhone;
		this.publicEmail = publicEmail;
		this.year = year;
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
	
	
}
