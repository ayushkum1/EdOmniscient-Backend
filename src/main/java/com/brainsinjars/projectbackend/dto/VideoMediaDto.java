package com.brainsinjars.projectbackend.dto;

import java.util.Arrays;

/**
 * @author Kartik Singhal
 * @since 10-03-2021
 */

public class VideoMediaDto {
	private String[] urls;
	private String category;

	public VideoMediaDto() {

	}

	public VideoMediaDto(String[] urls, String category) {
		super();
		this.urls = urls;
		this.category = category;
	}

	public String[] getUrls() {
		return urls;
	}

	public void setUrls(String[] urls) {
		this.urls = urls;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "VideoMediaDto [urls=" + Arrays.toString(urls) + ", category=" + category + "]";
	}

}
