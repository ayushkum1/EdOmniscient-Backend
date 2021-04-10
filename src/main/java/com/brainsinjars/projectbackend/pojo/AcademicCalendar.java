package com.brainsinjars.projectbackend.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Entity
@Table(name = "academic_calendar")
public class AcademicCalendar extends BaseEntity {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    @NotBlank
    @Column(length = 35)
    private String name;
    
    @NotBlank
    private String imageUrl;

    @NotBlank
    @Lob
    @Length(min = 50, max = 1000, message = "Description should have a length between 50 and 1000 characters")
    @Column(length = 1000)
    private String description;

    /**
     * Bidirectional mapping to institute table
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institute_id", nullable = false)
    @JsonIgnore
    private Institute institute;

    // No-args constructor
    public AcademicCalendar() {
    }

	public AcademicCalendar(LocalDate date, @NotBlank String name, @NotBlank String imageUrl,
			@NotBlank @Length(min = 50, max = 1000, message = "Description should have a length between 50 and 1000 characters") String description,
			Institute institute) {
		super();
		this.date = date;
		this.name = name;
		this.imageUrl = imageUrl;
		this.description = description;
		this.institute = institute;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Institute getInstitute() {
		return institute;
	}

	public void setInstitute(Institute institute) {
		this.institute = institute;
	}

	@Override
	public String toString() {
		return "AcademicCalendar [date=" + date + ", name=" + name + ", imageUrl=" + imageUrl + ", description="
				+ description + ", institute=" + institute + "]";
	}

}
