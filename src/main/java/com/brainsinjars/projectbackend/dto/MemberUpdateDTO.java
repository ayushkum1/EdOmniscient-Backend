/* 
 * @Author Channappa Mirgale 
 * @Since 09-03-21
*/

package com.brainsinjars.projectbackend.dto;

public class MemberUpdateDTO {

	private String publicPhone;
	private String publicEmail;
	
	
	public MemberUpdateDTO(String publicPhone, String publicEmail) {
		super();
		this.publicPhone = publicPhone;
		this.publicEmail = publicEmail;
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
	
	
	
	
}
