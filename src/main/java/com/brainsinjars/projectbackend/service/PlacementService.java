package com.brainsinjars.projectbackend.service;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import com.brainsinjars.projectbackend.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.brainsinjars.projectbackend.dao.PlacementDao;
import com.brainsinjars.projectbackend.dto.MessageDto;
import com.brainsinjars.projectbackend.dto.MessageType;
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
@Service
@Transactional
public class PlacementService {

	private PlacementDao placementDao;

	@Autowired
	public PlacementService(PlacementDao placementDao) {
		this.placementDao = placementDao;
	}

	/*
	 * This method returns list of placement records of a particular institute by
	 * batch and year
	 */
	public ResponseEntity<?> fetchPlacementDetailsByBatchAndYearAndCourse(long instituteId, String batch, Year year,
			String courseName) {
		PlacementDTO p = placementDao.fetchPlacementDetailsByBatchAndYearAndCourse(instituteId,
				Batch.valueOf(batch.toUpperCase()), year, courseName);
		if (p == null)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new MessageDto("No Placement Record Found!", MessageType.ERROR));
		return ResponseEntity.ok(p);
	}

	/* This method adds a placement record in a particular institute */
	public ResponseEntity<?> addPlacementRecord(long instituteId, PlacementDTO addPlacementRecord) {
		try {
			Placement p = placementDao.addPlacementRecord(instituteId, addPlacementRecord);
			if (p == null)
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(new MessageDto("Placement Record addition failed!", MessageType.ERROR));

			return ResponseEntity.ok(new MessageDto(
					"Placement details has been successfully added with Id: " + p.getId(), MessageType.SUCCESS));
		} catch (RecordNotFoundException e) {
			return ResponseEntity.badRequest().body(new MessageDto(e.getMessage(), MessageType.ERROR));
		}
	}

	/* This method updates a placement record of a particular institute */
	public ResponseEntity<?> updatePlacementRecord(long instituteId, long id, PlacementDTO updatePlacementRecord) {
		try {
			placementDao.updatePlacementRecord(instituteId, id, updatePlacementRecord);
			return ResponseEntity
					.ok(new MessageDto("Placement details has been updated successfully", MessageType.SUCCESS));
		} catch (PlacementRecordNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageDto(e.getMessage(), MessageType.ERROR));
		} catch (RecordNotFoundException r) {
			return ResponseEntity.badRequest().body(new MessageDto(r.getMessage(), MessageType.ERROR));
		}
	}

	/* This method deletes a placement record from a particular institute */
	public ResponseEntity<?> deletePlacementRecord(long instituteId, long id) {
		try {
			placementDao.deletePlacementRecord(instituteId, id);
			return ResponseEntity
					.ok(new MessageDto("Placement details has been deleted successfully", MessageType.SUCCESS));
		} catch (PlacementRecordNotFoundException e) {
			return ResponseEntity.badRequest().body(new MessageDto(e.getMessage(), MessageType.ERROR));
		}

	}

	/* This method returns a list of placement records of a particular institute */
	public ResponseEntity<?> fetchPlacementDetails(long instituteId) {
		List<PlacementDTO> list = new ArrayList<>();
		list = placementDao.fetchPlacementDetails(instituteId);
		if (list.isEmpty())
			return new ResponseEntity<>("No Placement Records Found!", HttpStatus.NOT_FOUND);
		return ResponseEntity.ok(list);
	}

	/*
	 * This method returns a list of last batches(this value is taken from user)
	 * from a particular institute
	 */
	public ResponseEntity<?> fetchLastNPlacementDetails(long instituteId, int batches) {
		List<PlacementDTO> list = new ArrayList<>();
		list = placementDao.fetchLastNPlacementDetails(instituteId, batches);
		if (list.isEmpty())
			return new ResponseEntity<>("No Placement Records Found!", HttpStatus.NOT_FOUND);
		return ResponseEntity.ok(list);
	}

	public ResponseEntity<?> getCourseSortByYear(String instituteId, String courseName) {
		try {
			List<PlacementChartDTO> chart;
			chart = placementDao.getCourseSortByYear(Long.valueOf(instituteId), courseName);

			if (chart.isEmpty())
				return new ResponseEntity<>("No Placement Records Found!", HttpStatus.NOT_FOUND);
			return ResponseEntity.ok(chart);
		} catch (NumberFormatException e) {
			return ResponseUtils.badRequestResponse(ResponseUtils.INVALID_ID_MESSAGE);
		}
	}

	/*
	 * This method returns number of students placed by batch, year, courseName,
	 * instituteName
	 * 
	 * public int findNoPlacedStudents(String batch, Year year, String cname, String
	 * iname) { return
	 * placementDao.findNoPlacedStudents(Batch.valueOf(batch.toUpperCase()), year,
	 * cname, iname); }
	 * 
	 * 
	 * This method returns total students placed by batch, year, courseName,
	 * instituteName
	 * 
	 * public int findTotalStudents(String batch, Year year, String cname, String
	 * iname) { return
	 * placementDao.findTotalStudents(Batch.valueOf(batch.toUpperCase()), year,
	 * cname, iname); }
	 * 
	 * 
	 * This method returns maxSalaryOffered for a particular batch, year,
	 * courseName, instituteName
	 * 
	 * public double findMaxLPAOffered(String batch, Year year, String cname, String
	 * iname) { return
	 * placementDao.findMaxLPAOffered(Batch.valueOf(batch.toUpperCase()), year,
	 * cname, iname); }
	 * 
	 * 
	 * This method returns avgSalaryOffered for a particular batch, year,
	 * courseName, instituteName
	 * 
	 * public double findAvgLPAOffered(String batch, Year year, String cname, String
	 * iname) { return
	 * placementDao.findAvgLPAOffered(Batch.valueOf(batch.toUpperCase()), year,
	 * cname, iname); }
	 */
}
