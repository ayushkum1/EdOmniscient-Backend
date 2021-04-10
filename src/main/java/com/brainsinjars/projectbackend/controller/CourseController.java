package com.brainsinjars.projectbackend.controller;

import java.util.List;
import java.util.Set;

import com.brainsinjars.projectbackend.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.brainsinjars.projectbackend.constant.HasRole;
import com.brainsinjars.projectbackend.dto.CourseDTO;
import com.brainsinjars.projectbackend.dto.CourseIdNameDTO;
import com.brainsinjars.projectbackend.dto.MessageDto;
import com.brainsinjars.projectbackend.dto.MessageType;
import com.brainsinjars.projectbackend.exceptions.RecordAlreadyExistsException;
import com.brainsinjars.projectbackend.exceptions.RecordCreationException;
import com.brainsinjars.projectbackend.exceptions.RecordDeletionException;
import com.brainsinjars.projectbackend.exceptions.RecordNotFoundException;
import com.brainsinjars.projectbackend.pojo.Course;
import com.brainsinjars.projectbackend.service.ICourseService;

/* 
 * @author: Ayush Kumar Singh
 * @since: 08-03-2021
*/

@RestController
public class CourseController {

	@Autowired
	ICourseService courseService;

	public CourseController() {
		System.out.println("in constructor of course class");
		// TODO Auto-generated constructor stub
	}

	/*
	 * @GetMapping
	 * getAllCourses : finds a list of all CourseDTO objects of an institute
	 * @PathVariable : instituteId
	 * parameters : Institute object id: String
	 * exceptions : RecordNotFoundException,NumberFormatException
	 * returns : status code : 200, a list of CourseDTO objects/throws exception
	*/
	@GetMapping(value = "/all-courses",
            produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAllCourses(@RequestParam("instituteId") String id) {
		try {
			return ResponseEntity.ok(courseService.getAllByInstituteId(id));
		} catch (RecordNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageDto(e.getMessage(), MessageType.ERROR));
		}catch (NumberFormatException e) {
			return ResponseEntity.badRequest()
					.body(new MessageDto(e.getMessage(), MessageType.ERROR));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new MessageDto(e.getMessage(), MessageType.ERROR));
		}
	}

	/*
	 * @GetMapping
	 * getCourseById : find a CourseDTO object of an course by its id if it exists
	 * @PathVariable : id
	 * parameters : Course object id: String
	 * exceptions : RecordNotFoundException,NumberFormatException
	 * returns : status code : 200, a CourseDTO object/throws exception
	*/
	@GetMapping(value = "/courses/{id}",
            	produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCourseById(@PathVariable("id") String id) {
		try {
			CourseDTO courseDTO = courseService.getCourseDetails(id);
			return ResponseEntity.ok(courseDTO);
		} catch (RecordNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new MessageDto("Course with that id Not Found", MessageType.ERROR));
		} catch (NumberFormatException e) {
			return ResponseEntity.badRequest().body(new MessageDto(e.getMessage(), MessageType.ERROR));
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new MessageDto(e.getMessage(), MessageType.ERROR));
		}
	}

	/*
	 * @GetMapping
	 * getAllCourses : find a list of all CourseDTO objects for the root
	 * returns : status code : 200, a list of CourseDTO objects/throws exception
	*/
	@GetMapping(value = "/courses",
				produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAllCourses() {
		try {
			return ResponseEntity.ok(courseService.findAllCourses());
		} catch (Exception e) {
			return ResponseUtils.internalServerErrorResponse("Could not find any courses");
		}
	}

	/*
	 * @GetMapping
	 * getCourseDTO : find List of CourseIdAndNameDTO object with id and name
	 * exceptions : RecordNotFoundException
	 * returns : status code : 200, a CourseIdAndNameDTO object/throws exception
	*/
	@GetMapping(value = "/courses/short",
            	produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCourseDTO() {
		try {
			return ResponseEntity.ok(courseService.getAllCourseIdAndName());
		} catch (RecordNotFoundException e) {
			// only situation where there exist no record in course
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageDto(e.getMessage(), MessageType.ERROR));
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new MessageDto(e.getMessage(), MessageType.ERROR));
		}
	}

	/*
	 * @GetMapping
	 * getCourseByName : find a CourseDTO object of an course by its name if it exists
	 * @PathVariable : name
	 * parameters : Course object name: String
	 * exceptions : RecordNotFoundException
	 * returns : status code : 200, a CourseDTO object/throws exception
	*/
	@GetMapping(value = "/courses/name/{name}",
            	produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCourseByName(@PathVariable("name") String name) throws RecordNotFoundException {
		try {
			return ResponseEntity.ok(courseService.getCourseByName(name));
		} catch (RecordNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new MessageDto("No course found with that name!", MessageType.ERROR));
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new MessageDto(e.getMessage(), MessageType.ERROR));
		}
	}

	/*
	 * @GetMapping
	 * getAllCoursesOfInstitute : find a list of CourseIdNameDTO objects of an course of an Institute if it exists
	 * @PathVariable : instituteId
	 * parameters : Institute object id: String
	 * exceptions : RecordNotFoundException,NumberFormatException
	 * returns : status code : 200, a list of CourseIdNameDTO objects/throws exception
	*/
	@GetMapping(value = "/institute",
            	produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAllCoursesOfInstitute(@RequestParam("instituteId") String id) {
		try {
			List<CourseIdNameDTO> courses = courseService.getAllCoursesOfAnInstitute(id);

			// this could be removed
			if (courses.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new MessageDto("Courses Not found", MessageType.ERROR));
			}

			return ResponseEntity.ok(courses);// could have been normal
		} catch (RecordNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.body(new MessageDto("No event of this institute found", MessageType.ERROR));
		} catch (NumberFormatException e) {
			return ResponseEntity.badRequest().body(new MessageDto(e.getMessage(), MessageType.ERROR));
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new MessageDto(e.getMessage(), MessageType.ERROR));
		}
	}

	/*
	 * @GetMapping
	 * getCoursesByIds : find a list of Course objects by one or many ids
	 * @PathVariable : ids(multiple)
	 * parameters : Course object ids: Set<Long>
	 * exceptions : RecordNotFoundException
	 * returns : status code : 200, a set of Course objects/throws exception
	*/
	@GetMapping(value = "/courses-id/{ids}",
				produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCoursesByIds(@PathVariable("ids") Set<Long> ids) {
		try {
			Set<Course> courses = courseService.getCourseByIds(ids);

			if (courses.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new MessageDto("Course Not found", MessageType.ERROR));
			}
			return ResponseEntity.ok(courses);
		} catch (RecordNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new MessageDto("Course Not found", MessageType.ERROR));
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new MessageDto(e.getMessage(), MessageType.ERROR));
		}
	}
	
	/*
	 * @PostMapping
	 * createCourse : creates an Course, marshalling happens here from json to java object.
	 * @RequestBody: Course object in json
	 * parameters : Course Object : Course
	 * exceptions : RecordCreationException,RecordAlreadyExistsException
	 * returns : status code : 200, persisted Course object/throws exception
	*/
	@Secured(HasRole.ROOT)
	@PostMapping(value = "/courses",
	            consumes = MediaType.APPLICATION_JSON_VALUE,
	            produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createCourse(@RequestBody Course course) {
		try {
			return ResponseEntity.ok(courseService.createCourse(course));
		} catch (RecordCreationException e) {
			// bad request because creation can fail due to many reasons
			return ResponseEntity.badRequest().body(new MessageDto(e.getMessage(), MessageType.ERROR));
		} catch (RecordAlreadyExistsException e) {
			return ResponseEntity.badRequest().body(new MessageDto("Record Already Exists", MessageType.ERROR));
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new MessageDto(e.getMessage(), MessageType.ERROR));
		}
	}

	/*
	 * @PutMapping
	 * updateCourse : updates an Course object by its id if it exists
	 * @PathVariable : id
	 * @RequestBody: Course object in json
	 * parameters : Course object id: String, Course object from json with updated fields after marshalling: Course
	 * exceptions : RecordNotFoundException,RecordCreationException,NumberFormatException
	 * returns : status code : 200, updated Course object in Response Entity/throws exception.
	*/
	//can be updated just by course id as only root will update the course, while institute admin can only add to their institutes
	@Secured(HasRole.ROOT)
	@PutMapping(value = "/courses/{id}",
	            consumes = MediaType.APPLICATION_JSON_VALUE,
	            produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateCourse(@PathVariable String id, @RequestBody Course course)
			throws RecordNotFoundException {
		try {
			return ResponseEntity.ok(courseService.updateCourse(id, course));
		} catch (RecordCreationException e) {
			return ResponseEntity.badRequest().body(new MessageDto("Error in updating record!", MessageType.ERROR));
		} catch (NumberFormatException e) {
			return ResponseEntity.badRequest().body(new MessageDto("id entered as string", MessageType.ERROR));
		} catch (RecordNotFoundException e) {
			return ResponseEntity.badRequest().body(new MessageDto("Course not found", MessageType.ERROR));
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new MessageDto(e.getMessage(), MessageType.ERROR));
		}
	}

	/*
	 * @DeleteMapping
	 * deleteCourse : deletes a single Course object by its id if it exists 
	 * @PathVariable: id
	 * parameters : Course object id: String
	 * exceptions : RecordNotFoundException,NumberFormatException,RecordDeletionException
	 * returns : status code: 200, deleted Course object in Response Entity/throws exception.
	*/
	@Secured(HasRole.ROOT)
	@DeleteMapping(value = "/courses/{id}",
            	   produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteCourse(@PathVariable String id) {
		try {
			courseService.deleteCourse(id);
			return ResponseEntity.ok("deleted successfully");
		} catch (RecordDeletionException e) {
			return ResponseEntity.badRequest().body(new MessageDto("Couldnt not delete Record", MessageType.ERROR));
		} catch (RecordNotFoundException e) {
			return ResponseEntity.badRequest().body(new MessageDto("course not found", MessageType.ERROR));
		} catch (NumberFormatException e) {
			return ResponseEntity.badRequest().body(new MessageDto("id entered as string", MessageType.ERROR));
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new MessageDto(e.getMessage(), MessageType.ERROR));
		}
	}
	
	/*
	 * @DeleteMapping
	 * deleteMultipleCourses : deletes multiple Course objects by its ids if it exists 
	 * @PathVariable: ids
	 * parameters : set of Course objects ids: Set<Long>
	 * exceptions : RecordNotFoundException
	 * returns : status code: 200, message: Deleted Successfully /throws exception.
	*/
	@Secured(HasRole.ROOT)
	@DeleteMapping(value = "/multi/{ids}")
	public ResponseEntity<?> deleteMultipleCourses(@PathVariable("ids") Set<Long> ids){
		try {
			courseService.deleteMultipleCourses(ids);
			return ResponseEntity.ok("Deleted Successfully");
		}
		catch (RecordNotFoundException e) {
			return ResponseEntity.badRequest().body(new MessageDto(e.getMessage(), MessageType.ERROR));
		}
	}
}
