package com.brainsinjars.projectbackend.pojo;

import com.brainsinjars.projectbackend.pojo.converter.YearAttributeConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.time.Year;

/**
 * This class is a Join table Entity. That means it is an intermediate table for
 * a ManyToMany mapping between Institute and Course. It describes the
 * relationship as, an institute has placements for multiple courses, one course
 * has placements in multiple institutes.
 */
@Entity
@Table(name = "placements")
public class Placement extends BaseEntity {
	/**
	 * Bidirectional mapping to institute table
	 */
	@ManyToOne
	@JoinColumn(name = "institute_id", nullable = false)
	@JsonIgnore
	private Institute institute;

	/**
	 * Bidirectional mapping to course table
	 */
	@ManyToOne
	@JoinColumn(name = "course_id", nullable = false)
	@JsonIgnore
	private Course course;

	@Column(nullable = false)
	private int noPlacedStudents;

	@Column(nullable = false)
	private int totalStudents;

	@Enumerated(EnumType.STRING)
	@Column(length = 10, nullable = false)
	private Batch batch;

	/**
	 * The @Converter annotation specifies which converter to use for converting the
	 * Java type to DB type and back.
	 */
	@Column(nullable = false)
	@Convert(converter = YearAttributeConverter.class)
	private Year year;

	// The unit of measurement is Lacs per annum
	@Digits(integer = 4, fraction = 2)
	private double maxLPAOffered;

	// The unit of measurement is Lacs per annum
	@Digits(integer = 4, fraction = 2)
	private double avgLPAOffered;

	// No-args constructor
	public Placement() {
	}

	// All-args constructor
	public Placement(Institute institute, Course course, int noPlacedStudents, int totalStudents, Batch batch,
			Year year, @Digits(integer = 4, fraction = 2) double maxLPAOffered,
			@Digits(integer = 4, fraction = 2) double avgLPAOffered) {
		this.institute = institute;
		this.course = course;
		this.noPlacedStudents = noPlacedStudents;
		this.totalStudents = totalStudents;
		this.batch = batch;
		this.year = year;
		this.maxLPAOffered = maxLPAOffered;
		this.avgLPAOffered = avgLPAOffered;
	}

	public Institute getInstitute() {
		return institute;
	}

	public void setInstitute(Institute institute) {
		this.institute = institute;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public int getNoPlacedStudents() {
		return noPlacedStudents;
	}

	public void setNoPlacedStudents(int noPlacedStudents) {
		this.noPlacedStudents = noPlacedStudents;
	}

	public int getTotalStudents() {
		return totalStudents;
	}

	public void setTotalStudents(int totalStudents) {
		this.totalStudents = totalStudents;
	}

	public Batch getBatch() {
		return batch;
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
	}

	public Year getYear() {
		return year;
	}

	public void setYear(Year year) {
		this.year = year;
	}

	public double getMaxLPAOffered() {
		return maxLPAOffered;
	}

	public void setMaxLPAOffered(double maxLPAOffered) {
		this.maxLPAOffered = maxLPAOffered;
	}

	public double getAvgLPAOffered() {
		return avgLPAOffered;
	}

	public void setAvgLPAOffered(double avgLPAOffered) {
		this.avgLPAOffered = avgLPAOffered;
	}
}
