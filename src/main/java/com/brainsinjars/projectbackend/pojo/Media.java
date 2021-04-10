package com.brainsinjars.projectbackend.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "media")
public class Media extends BaseEntity {

	@NotBlank
	private String url;

	@NotNull
	@Enumerated(EnumType.STRING)
	private MediaType mediaType;

	@NotNull
	@Enumerated(EnumType.STRING)
	private Category category;

	@NotBlank
	@Column(length = 30)
	private String name;

	/**
	 * Bidirectional mapping to institute table
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "institute_id", nullable = false)
	@JsonIgnore
	private Institute institute;

	// No-args constructor
	public Media() {
	}

	// All-args constructor
	public Media(@NotBlank String url, @NotNull MediaType mediaType, @NotNull Category category, @NotBlank String name,
			Institute institute) {
		this.url = url;
		this.mediaType = mediaType;
		this.category = category;
		this.name = name;
		this.institute = institute;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public MediaType getMediaType() {
		return mediaType;
	}

	public void setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Institute getInstitute() {
		return institute;
	}

	public void setInstitute(Institute institute) {
		this.institute = institute;
	}

	@Override
	public String toString() {
		return "Media{" + "url='" + url + '\'' + ", mediaType=" + mediaType + ", category=" + category + ", name='"
				+ name + '\'' + ", institute=" + institute + '}';
	}
}
