/* 
 * @Author Channappa Mirgale 
 * @Since 09-03-21
*/


package com.brainsinjars.projectbackend.dto;

import java.time.LocalDateTime;

public class ReviewDTO {

	private long id;
	private String name;
	private short rating;
	private String content;
	private String courseName;
	private LocalDateTime createdDateTime;
	private LocalDateTime modifiedDateTime;

	public ReviewDTO() {
		super();
	}

	public ReviewDTO(long id, String name, short rating, String content, String courseName, LocalDateTime createdDateTime,
					 LocalDateTime modifiedDateTime) {
		super();
		this.id = id;
		this.name = name;
		this.rating = rating;
		this.content = content;
		this.courseName = courseName;
		this.createdDateTime = createdDateTime;
		this.modifiedDateTime = modifiedDateTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public LocalDateTime getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(LocalDateTime createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public LocalDateTime getModifiedDateTime() {
		return modifiedDateTime;
	}

	public void setModifiedDateTime(LocalDateTime modifiedDateTime) {
		this.modifiedDateTime = modifiedDateTime;
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
}
