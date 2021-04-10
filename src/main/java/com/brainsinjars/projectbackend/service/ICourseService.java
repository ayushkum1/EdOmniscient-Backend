package com.brainsinjars.projectbackend.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.brainsinjars.projectbackend.dto.CourseDTO;
import com.brainsinjars.projectbackend.dto.CourseIdNameDTO;
import com.brainsinjars.projectbackend.exceptions.RecordAlreadyExistsException;
import com.brainsinjars.projectbackend.exceptions.RecordCreationException;
import com.brainsinjars.projectbackend.exceptions.RecordDeletionException;
import com.brainsinjars.projectbackend.exceptions.RecordNotFoundException;
import com.brainsinjars.projectbackend.pojo.Course;

/* 
 * @author: Ayush Kumar Singh
 * @since: 08-03-2021
*/

public interface ICourseService {

	//method to find all the records
	Set<CourseDTO> findAllCourses();

	//method to get list of all courses
	List<CourseDTO> getAllByInstituteId(String id) throws RecordNotFoundException;
	
	//method to get course by its id
	Course getById(String id) throws RecordNotFoundException;
	
	//method to get course by name
	CourseDTO getCourseByName(String name) throws RecordNotFoundException;
	
	//method to create a dto, not working currently
	List<CourseIdNameDTO> getAllCourseIdAndName() throws RecordNotFoundException;
	
	//method to get course details dto of a course by its id
	CourseDTO getCourseDetails(String id) throws RecordNotFoundException;
	
	//method to get list of course offered by many institutes
	List<CourseIdNameDTO> getAllCoursesOfAnInstitute(String id) throws RecordNotFoundException;
	
	//method to get selected courses by ids
	Set<Course> getCourseByIds(Set<Long> ids) throws RecordNotFoundException;

	//method to create a course
	Course createCourse(Course course) throws RecordCreationException, RecordAlreadyExistsException;
	
	//method to update a course by its id and object
	Course updateCourse(String id, Course course) throws RecordCreationException, RecordNotFoundException;
	
	//method to delete a course
	Course deleteCourse(String id) throws RecordDeletionException, RecordNotFoundException;
	
	//method to delete multiple course with their ids
	void deleteMultipleCourses(Set<Long> ids) throws RecordNotFoundException;
}
