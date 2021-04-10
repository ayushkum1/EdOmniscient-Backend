/* 
 * @Author Channappa Mirgale 
 * @Since 09-03-21
*/

package com.brainsinjars.projectbackend.dto;

public class ReviewAddUpdateDTO {
		
	private String userEmail;
	private long memberId;
	private short rating;
	private String content;
	
	public ReviewAddUpdateDTO()
	{
		super();
	}
	
	public ReviewAddUpdateDTO(String userEmail, long memberId, short rating, String content) {
		super();
		this.userEmail = userEmail;
		this.memberId = memberId;
		this.rating = rating;
		this.content = content;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public long getMemberId() {
		return memberId;
	}

	public void setMemberId(long memberId) {
		this.memberId = memberId;
	}

	public short getRating() {
		return rating;
	}

	public void setRating(short rating) {
		this.rating = rating;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	

	
	
}
