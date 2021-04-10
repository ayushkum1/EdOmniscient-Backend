package com.brainsinjars.projectbackend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.brainsinjars.projectbackend.controller.CourseController;
import com.brainsinjars.projectbackend.dto.CourseDTO;
import com.brainsinjars.projectbackend.dto.CourseIdNameDTO;
import com.brainsinjars.projectbackend.exceptions.RecordAlreadyExistsException;
import com.brainsinjars.projectbackend.exceptions.RecordCreationException;
import com.brainsinjars.projectbackend.exceptions.RecordNotFoundException;
import com.brainsinjars.projectbackend.pojo.Course;
import com.brainsinjars.projectbackend.repository.CourseRepository;
import com.brainsinjars.projectbackend.service.ICourseService;

/* 
 * @author: Ayush Kumar Singh
 * @since: 24-03-2021
*/

//testing table
/*mysql> select * from courses;
+----+----------------------------------------------------------------------+----------+-------+--------------+---------------------+
| id | description                                                          | duration | fees  | name         | photo_url           |
+----+----------------------------------------------------------------------+----------+-------+--------------+---------------------+
|  0 | this is the description of this course, it can be anything right now |       12 | 12000 | edac6        | www.coursephoto.jpg |
|  1 | this is the description of this course, it can be anything right now |        6 | 10000 | edac         | www.coursephoto.jpg |
|  2 | this is the description of this course, it can be anything right now |       12 | 12000 | edac4        | www.coursephoto.jpg |
|  3 | this is the description of this course, it can be anything right now |        6 |  6000 | edac1        | www.coursephoto.jpg |
|  4 | this is the description of this course, it can be anything right now |        6 | 16000 | edac2        | www.coursephoto.jpg |
|  5 | this is the description of this course, it can be anything right now |       12 | 26000 | edac3        | www.coursephoto.jpg |
|  6 | this is the description of this course, it can be anything right now |       12 | 36000 | edac5        | www.coursephoto.jpg |
|  7 | this is the description of this course, it can be anything right now |       12 | 25000 | edacTest     | www.coursephoto.jpg |
|  8 | this is the description of this course, it can be anything right now |       12 | 30000 | updated name | www.coursephoto.jpg |
+----+----------------------------------------------------------------------+----------+-------+--------------+---------------------+
*/
@SpringBootTest
public class TestCourseService {

	@Autowired
	private ICourseService service;
	
	@Autowired
	private CourseController controller;
	
	@Autowired
	private CourseRepository repository;
	
	@Test
	public void sanityTest() {
		assertNotNull(controller);
	}
	
	@Test
	public void testFindAllCourses() {
		Set<CourseDTO> courses = service.findAllCourses();
		List<CourseDTO> courseDto = new ArrayList<>(courses);
		assertEquals(0, courseDto.get(0).getId());
		assertEquals("edac6", courseDto.get(0).getName());
		assertEquals("this is the description of this course, it can be anything right now", courseDto.get(0).getDescription());
		assertEquals(12, courseDto.get(0).getDuration());
		assertEquals(12000, courseDto.get(0).getFees());
		assertEquals("www.coursephoto.jpg", courseDto.get(0).getPhotoUrl());
	}
	
	@Test
	public void testGetAllByInstituteId() {
		List<CourseDTO> courseDto = repository.getAll(1);
		assertEquals(4, courseDto.size());//as i have 4 records, ref database
		CourseDTO temp = courseDto.get(0);
		assertEquals(1, temp.getId());
		assertEquals("edac", temp.getName());
		assertEquals("this is the description of this course, it can be anything right now", temp.getDescription());
		assertEquals(6, temp.getDuration());
		assertEquals(10000, temp.getFees());
		assertEquals("www.coursephoto.jpg", temp.getPhotoUrl());
	
	}
	
	@Test
	public void testGetById() throws RecordNotFoundException {
		Course course = service.getById("1");
		assertEquals(1, course.getId());
		assertEquals("edac", course.getName());
		assertEquals("this is the description of this course, it can be anything right now", course.getDescription());
		assertEquals(6, course.getDuration());
		assertEquals(10000, course.getFees());
		assertEquals("www.coursephoto.jpg", course.getPhotoUrl());
	}
	
	@Test
	public void testGetCourseByName() throws RecordNotFoundException {
		CourseDTO course = service.getCourseByName("edac");
		assertEquals(1, course.getId());
		assertEquals("edac", course.getName());
		assertEquals("this is the description of this course, it can be anything right now", course.getDescription());
		assertEquals(6, course.getDuration());
		assertEquals(10000, course.getFees());
		assertEquals("www.coursephoto.jpg", course.getPhotoUrl());
	}
	
	@Test
	public void testGetCourseIdAndName() throws RecordNotFoundException {
		List<CourseIdNameDTO> course = service.getAllCourseIdAndName();
		assertEquals(9, course.size());
		CourseIdNameDTO temp = course.get(0);
		assertEquals(0, temp.getId());
		assertEquals("edac6", temp.getName());
	}
	
	@Test
	public void testGetCourseDetails() throws RecordNotFoundException {
		CourseDTO course = service.getCourseDetails("2");
		assertEquals(2, course.getId());
		assertEquals("edac4", course.getName());
		assertEquals("this is the description of this course, it can be anything right now", course.getDescription());
		assertEquals(12, course.getDuration());
		assertEquals(12000, course.getFees());
		assertEquals("www.coursephoto.jpg", course.getPhotoUrl());		
	}
	
	@Test
	public void testGetAllCoursesOfAnInstitute() throws RecordNotFoundException {
		List<CourseIdNameDTO> courses = service.getAllCoursesOfAnInstitute("2");
		assertEquals(3, courses.size());
		CourseIdNameDTO temp = courses.get(0);
		assertEquals(2, temp.getId());
		assertEquals("edac4", temp.getName());
	}
	
	@Test
	public void testGetCourseByIds() throws RecordNotFoundException {
		Set<Long> setIds = new HashSet<Long>();
		setIds.add((long) 1);
		setIds.add((long) 2);
		setIds.add((long) 3);
		
		Set<Course> coursesSet = service.getCourseByIds(setIds);
		assertEquals(3, coursesSet.size());
		
		List<Course> courses = new ArrayList<>(coursesSet);
		Course course = courses.get(0);
		
		assertEquals(1, course.getId());
		assertEquals("edac", course.getName());
		assertEquals("this is the description of this course, it can be anything right now", course.getDescription());
		assertEquals(6, course.getDuration());
		assertEquals(10000, course.getFees());
		assertEquals("www.coursephoto.jpg", course.getPhotoUrl());		
	
	}
	
	/*
	 * this.id = id;
        this.name = name;
        this.description = description;
        this.photoUrl = photoUrl;
        this.fees = fees;
        this.duration = duration;
	*/
	@Test
	public void testCreateCourse() throws RecordCreationException, RecordAlreadyExistsException {
		Course course = new Course((long) 8,"edacTest", "this is the description of this course, it can be anything right now"
									, "www.coursephoto.jpg",(double) 25000,(short) 12);
		//will create a actual record, first check if it exists or not
		Course savedCourse = service.createCourse(course);
		assertEquals(8, savedCourse.getId());
		assertEquals("edacTest", savedCourse.getName());
		assertEquals("this is the description of this course, it can be anything right now", savedCourse.getDescription());
		assertEquals(12, savedCourse.getDuration());
		assertEquals(25000, savedCourse.getFees());
		assertEquals("www.coursephoto.jpg", savedCourse.getPhotoUrl());		
	}
	
	@Test
	public void testUpdateCourse() throws RecordNotFoundException, RecordCreationException {
		Course crs = service.getById("8");
		crs.setFees(30000);
		crs.setName("updated name");
		
		Course course = service.updateCourse("8", crs);
		
		assertEquals(8, course.getId());
		assertEquals("updated name", course.getName());
		assertEquals("this is the description of this course, it can be anything right now", course.getDescription());
		assertEquals(12, course.getDuration());
		assertEquals(30000, course.getFees());
		assertEquals("www.coursephoto.jpg", course.getPhotoUrl());		
	
	}
}
