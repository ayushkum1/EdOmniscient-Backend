package com.brainsinjars.projectbackend.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brainsinjars.projectbackend.dao.InstituteDao;
import com.brainsinjars.projectbackend.dto.CourseDTO;
import com.brainsinjars.projectbackend.dto.CourseIdNameDTO;
import com.brainsinjars.projectbackend.exceptions.RecordAlreadyExistsException;
import com.brainsinjars.projectbackend.exceptions.RecordCreationException;
import com.brainsinjars.projectbackend.exceptions.RecordDeletionException;
import com.brainsinjars.projectbackend.exceptions.RecordNotFoundException;
import com.brainsinjars.projectbackend.pojo.Course;
import com.brainsinjars.projectbackend.pojo.Institute;
import com.brainsinjars.projectbackend.pojo.Placement;
import com.brainsinjars.projectbackend.repository.CourseRepository;
import com.brainsinjars.projectbackend.service.ICourseService;

/* 
 * @author: Ayush Kumar Singh
 * @since: 08-03-2021
*/

@Service
@Transactional
public class CourseServiceImpl implements ICourseService {

	// opting for list and not set data structure as data set of courses is going to
	// be very small

	@Autowired
	CourseRepository courseRepository;
	
	@Autowired
	private InstituteDao dao;

	@Override
	public Set<CourseDTO> findAllCourses() {
		return courseRepository.findAllCourses();
	}

	/*
	 * getAllByInstituteId : finds a list of CourseDTO object with id, name, description, photoUrl, fees, duration of an institute
	 * parameters : Institute object id: String
	 * exceptions : RecordNotFoundException,NumberFormatException
	 * returns : a list of objects of CourseDTO/throws exception
	*/
	@Override
	public List<CourseDTO> getAllByInstituteId(String instituteId) throws RecordNotFoundException{
		// TODO Auto-generated method stub
		
		try {
			List<CourseDTO> courses = courseRepository.getAll(Long.parseLong(instituteId));
			
			if(courses.isEmpty()) {
				throw new RecordNotFoundException("No courses found");
			}
			
			return courses;
		}catch (NumberFormatException e) {
			throw new NumberFormatException("id entered as string");
		}
	}

	/*
	 * getById : finds a course object by its id
	 * parameters : Course object id: String
	 * exceptions : RecordNotFoundException,NumberFormatException
	 * returns : a Course object/throws exception
	*/
	@Override
	public Course getById(String id) throws RecordNotFoundException {
		try {
			return courseRepository.getById(Long.parseLong(id))
					.orElseThrow(() -> new RecordNotFoundException("course not found"));
		}
		catch (NumberFormatException e) {
			throw new NumberFormatException("id entered as string");
		}

	}

	/*
	 * getCourseIdAndName : finds all courseIdNameDTO with id and name objects
	 * exceptions : RecordNotFoundException
	 * returns : a list of CourseIdNameDTO objects/throws exception
	*/
	@Override
	public List<CourseIdNameDTO> getAllCourseIdAndName() throws RecordNotFoundException {
		// create a list of course dto objects
		List<CourseIdNameDTO> courseDTOs = new ArrayList<>();

		if(courseRepository.findAll().isEmpty()) {
			throw new RecordNotFoundException("No record found, no course exist");
		}
		
		courseRepository.findAll()
				.forEach((course) -> courseDTOs.add(new CourseIdNameDTO(course.getId(), course.getName())));

		return courseDTOs;
	}

	/*
	 * getCourseDetails : gets course details by its id with  id, name, description, photoUrl, fees, duration
	 * parameters: Course objects id : String
	 * exceptions : RecordNotFoundException,NumberFormatException
	 * returns : a CourseDTO object/throws exception
	*/
	@Override
	public CourseDTO getCourseDetails(String id) throws RecordNotFoundException {

		try {
			Course course = courseRepository.getById(Long.parseLong(id))
					.orElseThrow(() -> new RecordNotFoundException("course not found"));
			// create a new course dto object with only id, name, description, photoUrl,
			// fees, duration excluding other details of institute and all
			CourseDTO courseDTO = new CourseDTO(course.getId(), course.getName(), course.getDescription(),
					course.getPhotoUrl(), course.getFees(), course.getDuration());
			return courseDTO;
		} catch (NumberFormatException e) {
			throw new NumberFormatException("id entered as string");
		}
	}

	/*
	 * getCourseByName : gets course details by its name with id, name, description, photoUrl, fees, duration
	 * parameters: Course objects name : String
	 * exceptions : RecordNotFoundException
	 * returns : a CourseDTO object/throws exception
	*/
	@Override
	public CourseDTO getCourseByName(String name) throws RecordNotFoundException {
		// TODO Auto-generated method stub
		Course course = courseRepository.getByName(name)
				.orElseThrow(() -> new RecordNotFoundException("course not found"));
		CourseDTO courseDTO = new CourseDTO(course.getId(), course.getName(), course.getDescription(),
				course.getPhotoUrl(), course.getFees(), course.getDuration());
		return courseDTO;
	}

	/*
	 * getAllCoursesOfAnInstitute : gets a list of all Course objects details by institute id with  id and name
	 * parameters: Institutes id: String
	 * exceptions : RecordNotFoundException, NumberFormatException
	 * returns : a list of CourseIdNameDTO objects/throws exception
	*/
	@Override
	public List<CourseIdNameDTO> getAllCoursesOfAnInstitute(String id) throws RecordNotFoundException {
		try {
			return courseRepository.getAllCoursesByInstituteId(Long.parseLong(id));
		} catch (NumberFormatException e) {
			throw new NumberFormatException("id entered as string");
		}
	}

	/*
	 * getCourseByIds : gets a set of Course objects details by its ids (with all the information), (multiple Course objects, depending on the number of ids)
	 * parameters: Set of ids: Long
	 * exceptions : RecordNotFoundException
	 * returns : a Set of Course objects/throws exception
	*/
	@Override
	public Set<Course> getCourseByIds(Set<Long> ids) throws RecordNotFoundException {
		return courseRepository.findCoursesByIds(ids);
	}
	
	/*
	 * createCourse : creates an Course objects and persists it to data from the json data got after marshalling.
	 * parameters : Course Object : Course
	 * exceptions : RecordCreationException,RecordAlreadyExistsException
	 * returns : persisted Course object/throws exception
	*/
	@Override
	public Course createCourse(Course course) throws RecordCreationException, RecordAlreadyExistsException {
		
		if(courseRepository.existsById(course.getId())) {
			throw new RecordAlreadyExistsException("course already exists");
		}
		Course crs = courseRepository.save(course);
		System.out.println("course after save : "+crs);
		if(crs!=null) {
			return crs;
		}
		else {
			throw new RecordCreationException("Couldnt create record!");
		}
	}
	
	/*
	 * updateCourse : updates an Course object by its id if it exists
	 * parameters : Course object id: String, Course object from json with updated fields after marshalling: Course
	 * exceptions : RecordNotFoundException,RecordCreationException,NumberFormatException
	 * returns : updated Course object/throws exception.
	*/
	@Override
	public Course updateCourse(String id, Course course) throws RecordCreationException, RecordNotFoundException {
		try {
			Course courseTemp = courseRepository.getById(Long.parseLong(id))
					.orElseThrow(() -> new RecordNotFoundException("course not updated, as no course exist with that id"));

			courseTemp.setDescription(course.getDescription());
			courseTemp.setDuration(course.getDuration());
			courseTemp.setFees(course.getFees());
			courseTemp.setName(course.getName());
			courseTemp.setPhotoUrl(Optional.ofNullable(course.getPhotoUrl()).orElse(courseTemp.getPhotoUrl()));

			return courseTemp;
		} catch (NumberFormatException e) {
			throw new NumberFormatException(e.getMessage());
		}
	}

	/*
	 * deleteCourse : deletes a course by its id if it exists
	 * parameters : Course object id: String
	 * exceptions : RecordNotFoundException,NumberFormatException
	 * returns : deleted Course object/throws exception.
	*/
	@Override
	public Course deleteCourse(String id) throws RecordDeletionException, RecordNotFoundException {
		try {
			Course course = courseRepository.getById(Long.parseLong(id))
					.orElseThrow(() -> new RecordNotFoundException("course not found"));

			System.out.println("Course details " + course.toString());
			
			courseRepository.delete(course);

			return course;
		} catch (NumberFormatException e) {
			throw new NumberFormatException(e.getMessage());
		}
	}

	/*
	 * deleteMultipleCourses : deletes multiple courses by its id if it exists
	 * parameters : set of Course objects ids: Set<Long> ids
	 * exceptions : RecordNotFoundException
	 * returns : void/throws exception.
	*/
	@Override
	public void deleteMultipleCourses(Set<Long> ids) throws RecordNotFoundException {
		if(ids != null) {
			Set<Course> courses = courseRepository.findCoursesByIds(ids);
			courseRepository.deleteAll(courses);
		}
		else {
			throw new RecordNotFoundException("no course found");
		}
	}
	
}
