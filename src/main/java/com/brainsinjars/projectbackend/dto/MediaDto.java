package com.brainsinjars.projectbackend.dto;

import com.brainsinjars.projectbackend.pojo.Category;
import com.brainsinjars.projectbackend.pojo.MediaType;

/**
 * @author Kartik Singhal
 * @since 07-03-21
 */

public class MediaDto {

	private Long id;
	private String[] urls;
	private Category category;
	private MediaType mediaType;
	private String name;
	private Long instituteId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String[] getUrls() {
		return urls;
	}

	public void setUrls(String[] urls) {
		this.urls = urls;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public MediaType getMediaType() {
		return mediaType;
	}

	public void setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getInstituteId() {
		return instituteId;
	}

	public void setInstituteId(Long instituteId) {
		this.instituteId = instituteId;
	}

	public MediaDto() {

	}

	public MediaDto(Long id, String[] urls, Category category, MediaType mediaType, String name, Long instituteId) {
		super();
		this.id = id;
		this.urls = urls;
		this.category = category;
		this.mediaType = mediaType;
		this.name = name;
		this.instituteId = instituteId;
	}

}
