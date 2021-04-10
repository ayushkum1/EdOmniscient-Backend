package com.brainsinjars.projectbackend.controller;

import java.time.Year;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brainsinjars.projectbackend.constant.HasRole;
import com.brainsinjars.projectbackend.dto.PlacementDTO;
import com.brainsinjars.projectbackend.service.PlacementService;

/**
 * @author Kanchan Harjani
 * @since 09-03-2021
 *
 */
//@CrossOrigin(origins = "http://localhost:3000/")
@CrossOrigin("*")
@RestController
@RequestMapping("/institutes/{instituteId}")
public class PlacementController {

	PlacementService placementService;

	@Autowired
	public PlacementController(PlacementService placementService) {
		this.placementService = placementService;
	}

	@Secured({ HasRole.INSTITUTE_ADMIN, HasRole.MEMBER, HasRole.ROOT, HasRole.USER })
	@GetMapping("/placements")
	public ResponseEntity<?> getAllPlacementRecords(@PathVariable long instituteId) {
		return placementService.fetchPlacementDetails(instituteId);
	}

	@Secured({ HasRole.INSTITUTE_ADMIN, HasRole.MEMBER, HasRole.ROOT, HasRole.USER })
	@GetMapping("/placements/{batches}")
	public ResponseEntity<?> getLastNPlacementRecords(@PathVariable long instituteId, @PathVariable int batches) {
		return placementService.fetchLastNPlacementDetails(instituteId, batches);
	}

	@Secured({ HasRole.INSTITUTE_ADMIN, HasRole.MEMBER, HasRole.ROOT, HasRole.USER })
	@GetMapping("/placements/{batch}/{year}/{courseName}")
	public ResponseEntity<?> getPlacementRecordsByBatchAndYearAndCourse(@PathVariable long instituteId,
			@PathVariable String batch, @PathVariable Year year, @PathVariable String courseName) {
		return placementService.fetchPlacementDetailsByBatchAndYearAndCourse(instituteId, batch, year, courseName);
	}

	@Secured({ HasRole.INSTITUTE_ADMIN, HasRole.MEMBER, HasRole.ROOT, HasRole.USER })
	@GetMapping("/placements/course/{courseName}")
	public ResponseEntity<?> getCourseSortByYear(@PathVariable String instituteId, @PathVariable String courseName) {
		return placementService.getCourseSortByYear(instituteId, courseName);
	}

	@Secured({ HasRole.INSTITUTE_ADMIN })
	@PostMapping("/placements")
	public ResponseEntity<?> addPlacementRecord(@PathVariable long instituteId,
			@RequestBody PlacementDTO addPlacementRecord) {
		return placementService.addPlacementRecord(instituteId, addPlacementRecord);
	}

	@Secured({ HasRole.INSTITUTE_ADMIN })
	@PutMapping("/placements/{id}")
	public ResponseEntity<?> updatePlacementRecord(@PathVariable long instituteId, @PathVariable long id,
			@RequestBody PlacementDTO updatePlacementRecord) {
		return placementService.updatePlacementRecord(instituteId, id, updatePlacementRecord);
	}

	@Secured({ HasRole.INSTITUTE_ADMIN })
	@DeleteMapping("/placements/{id}")
	public ResponseEntity<?> deletePlacementRecord(@PathVariable long instituteId, @PathVariable long id) {
		return placementService.deletePlacementRecord(instituteId, id);
	}
}
