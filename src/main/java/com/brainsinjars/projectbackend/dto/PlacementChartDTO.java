package com.brainsinjars.projectbackend.dto;

import java.time.Year;

import com.brainsinjars.projectbackend.pojo.Batch;

public class PlacementChartDTO {
	private String courseName;
	private int noPlacedStudents;
	private int totalStudents;
	private Batch batch;
	private Year year;

	public PlacementChartDTO() {
		super();
	}

	public PlacementChartDTO(String courseName, int noPlacedStudents, int totalStudents, Batch batch, Year year) {
		super();
		this.courseName = courseName;
		this.noPlacedStudents = noPlacedStudents;
		this.totalStudents = totalStudents;
		this.batch = batch;
		this.year = year;
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

}
