package com.brainsinjars.projectbackend.dao;

import java.time.Year;
import java.util.List;

import com.brainsinjars.projectbackend.dto.PlacementChartDTO;
import com.brainsinjars.projectbackend.dto.PlacementDTO;
import com.brainsinjars.projectbackend.exceptions.PlacementRecordNotFoundException;
import com.brainsinjars.projectbackend.exceptions.RecordNotFoundException;
import com.brainsinjars.projectbackend.pojo.Batch;
import com.brainsinjars.projectbackend.pojo.Placement;

/**
 * @author Kanchan Harjani
 * @since 08-03-2021
 */
public interface PlacementDao {

	Placement addPlacementRecord(long instituteId, PlacementDTO addPlacementRecord) throws RecordNotFoundException;

	Placement updatePlacementRecord(long instituteId, long id, PlacementDTO updatePlacementRecord)
			throws PlacementRecordNotFoundException, RecordNotFoundException;

	Placement deletePlacementRecord(long instituteId, long id) throws PlacementRecordNotFoundException;

	List<PlacementDTO> fetchPlacementDetails(long instituteId);

	PlacementDTO fetchPlacementDetailsByBatchAndYearAndCourse(long instituteId, Batch batch, Year year,
			String courseName);

	List<PlacementDTO> fetchLastNPlacementDetails(long instituteId, int batches);

	List<PlacementChartDTO> getCourseSortByYear(Long instituteId, String courseName);

	// earlier methods for testing
	/*
	 * int findNoPlacedStudents(Batch batch, Year year, String cname, String iname);
	 * 
	 * int findTotalStudents(Batch batch, Year year, String cname, String iname);
	 * 
	 * double findMaxLPAOffered(Batch batch, Year year, String cname, String iname);
	 * 
	 * double findAvgLPAOffered(Batch batch, Year year, String cname, String iname);
	 */
}
