package com.brainsinjars.projectbackend.dao.impl;

import java.time.Year;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.brainsinjars.projectbackend.dao.PlacementDao;
import com.brainsinjars.projectbackend.dto.PlacementChartDTO;
import com.brainsinjars.projectbackend.dto.PlacementDTO;
import com.brainsinjars.projectbackend.exceptions.PlacementRecordNotFoundException;
import com.brainsinjars.projectbackend.exceptions.RecordNotFoundException;
import com.brainsinjars.projectbackend.pojo.Batch;
import com.brainsinjars.projectbackend.pojo.Course;
import com.brainsinjars.projectbackend.pojo.Institute;
import com.brainsinjars.projectbackend.pojo.Placement;
import com.brainsinjars.projectbackend.repository.CourseRepository;
import com.brainsinjars.projectbackend.repository.InstituteRepository;
import com.brainsinjars.projectbackend.repository.PlacementRepository;

/**
 * @author Kanchan Harjani
 * @since 08-03-2021
 */
@Repository
@Transactional
public class PlacementDaoImpl implements PlacementDao {

	private PlacementRepository placementRepository;
	private InstituteRepository instituteRepository;
	private CourseRepository courseRepository;

	@Autowired
	public PlacementDaoImpl(PlacementRepository placementRepository, InstituteRepository instituteRepository,
			CourseRepository courseRepository) {
		this.placementRepository = placementRepository;
		this.instituteRepository = instituteRepository;
		this.courseRepository = courseRepository;
	}

	/*
	 * takes instituteId and value passed through RequestBody as parameters and
	 * persists placement object in the database in a particular institute and
	 * returns placement instance if record is added successfully in the database
	 */
	@Override
	public Placement addPlacementRecord(long instituteId, PlacementDTO addPlacementRecord)
			throws RecordNotFoundException {
		Placement placement = new Placement();

		Course course = courseRepository.getByName(addPlacementRecord.getCourseName())
				.orElseThrow(() -> new RecordNotFoundException("course with that id does not exist"));
		if (course == null)
			throw new RecordNotFoundException("Invalid Course Name!");
		placement.setCourse(course);

		Institute institute = instituteRepository.getOne(instituteId);
		placement.setInstitute(institute);

		BeanUtils.copyProperties(addPlacementRecord, placement);

		return placementRepository.save(placement);
	}

	/*
	 * takes instituteId , id (placement_id) and value passed through RequestBody as
	 * parameters and updates the placement record in the database in a particular
	 * institute and returns placement instance if record is successfully updated or
	 * throws PlacementRecordNotFoundException if placement record not found
	 */
	@Override
	public Placement updatePlacementRecord(long instituteId, long id, PlacementDTO updatePlacementRecord)
			throws PlacementRecordNotFoundException, RecordNotFoundException {
		Placement placement = placementRepository.findById(id)
				.orElseThrow(() -> new PlacementRecordNotFoundException("Placement Record Not Found! Updation Failed"));

		if (placement.getInstitute().getId() != instituteId)
			throw new PlacementRecordNotFoundException("Placement Record Not Found! Updation Failed");

		Course course = courseRepository.getByName(updatePlacementRecord.getCourseName())
				.orElseThrow(() -> new RecordNotFoundException("course with that id does not exist"));
		if (course == null)
			throw new RecordNotFoundException("Invalid Course Name");
		placement.setCourse(course);
		BeanUtils.copyProperties(updatePlacementRecord, placement, "id");

		return placementRepository.save(placement);
	}

	/*
	 * takes instituteId , id (placement_id) deletes the placement record from the
	 * database from a particular institute and throws
	 * PlacementRecordNotFoundException if placement record not found
	 */
	@Override
	public Placement deletePlacementRecord(long instituteId, long id) throws PlacementRecordNotFoundException {
		Placement p = placementRepository.findById(id)
				.orElseThrow(() -> new PlacementRecordNotFoundException("Placement Record Not Found! Deletion Failed!!"));
		if (p.getInstitute().getId() == instituteId) {
			placementRepository.deleteById(id);
			return p;
		}
		throw new PlacementRecordNotFoundException("Placement Record Deletion Failed");
	}

	/*
	 * takes instituteId as a parameter and fetches placement records of a
	 * particular institute from the database
	 */
	@Override
	public List<PlacementDTO> fetchPlacementDetails(long instituteId) {
		return placementRepository.fetchPlacementDetails(instituteId);
	}

	/*
	 * takes instituteId, batch and year as parameters and using these parameters,
	 * fetches placement records of a particular institute from the database
	 */
	@Override
	public PlacementDTO fetchPlacementDetailsByBatchAndYearAndCourse(long instituteId, Batch batch, Year year, String courseName) {
		return placementRepository.fetchPlacementDetailsByBatchAndYearAndCourse(instituteId, batch, year, courseName);
	}

	/*
	 * takes instituteId, batches as parameters and using these parameters, fetches
	 * last N(batches) placement records of a particular institute from the database
	 */
	@Override
	public List<PlacementDTO> fetchLastNPlacementDetails(long instituteId, int batches) {
		Pageable sortedByYearDesc = PageRequest.of(0, batches, Sort.by("year").descending());
		return placementRepository.fetchLastNPlacementDetails(sortedByYearDesc, instituteId);
	}
	
	@Override
	public List<PlacementChartDTO> getCourseSortByYear(Long instituteId, String courseName) {
		
		return placementRepository.getCourseSortByYear(instituteId, courseName);
	}


	/*
	 * fetches number of placed students from database for a particular batch, year,
	 * courseName, instituteName
	 * 
	 * @Override public int findNoPlacedStudents(Batch batch, Year year, String
	 * cname, String iname) { return placementRepository.findNoPlacedStudents(batch,
	 * year, cname, iname); }
	 * 
	 * 
	 * fetches total students from database for a particular batch, year,
	 * courseName, instituteName
	 * 
	 * @Override public int findTotalStudents(Batch batch, Year year, String cname,
	 * String iname) { return placementRepository.findTotalStudents(batch, year,
	 * cname, iname); }
	 * 
	 * 
	 * fetches maxLPAOffered from database for a particular batch, year, courseName,
	 * instituteName
	 * 
	 * @Override public double findMaxLPAOffered(Batch batch, Year year, String
	 * cname, String iname) { return placementRepository.findMaxLPAOffered(batch,
	 * year, cname, iname); }
	 * 
	 * 
	 * fetches avgLPAOffered from database for a particular batch, year, courseName,
	 * instituteName
	 * 
	 * @Override public double findAvgLPAOffered(Batch batch, Year year, String
	 * cname, String iname) { return placementRepository.findAvgLPAOffered(batch,
	 * year, cname, iname); }
	 */

}