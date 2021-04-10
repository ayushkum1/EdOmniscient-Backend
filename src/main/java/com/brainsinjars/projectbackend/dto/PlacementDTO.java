package com.brainsinjars.projectbackend.dto;

import java.time.Year;

import com.brainsinjars.projectbackend.pojo.Batch;
/**
 * @author Kanchan Harjani
 * @since 09-03-2021
 */
public class PlacementDTO {
	private long id;
	private String courseName;
	private int noPlacedStudents;
	private int totalStudents;
	Batch batch;
	Year year;
	double maxLPAOffered;
	double avgLPAOffered;

	public PlacementDTO(long id, String courseName, int noPlacedStudents, int totalStudents, Batch batch, Year year,
			double maxLPAOffered, double avgLPAOffered) {
		super();
		this.id = id;
		this.courseName = courseName;
		this.noPlacedStudents = noPlacedStudents;
		this.totalStudents = totalStudents;
		this.batch = batch;
		this.year = year;
		this.maxLPAOffered = maxLPAOffered;
		this.avgLPAOffered = avgLPAOffered;
	}

	public PlacementDTO() {
		// TODO Auto-generated constructor stub
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
