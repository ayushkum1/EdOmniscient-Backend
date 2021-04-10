package com.brainsinjars.projectbackend.repository;

import com.brainsinjars.projectbackend.dto.CourseDTO;
import com.brainsinjars.projectbackend.dto.CourseIdNameDTO;
import com.brainsinjars.projectbackend.pojo.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/* 
 * @author: Ayush Kumar Singh
 * @since: 08-03-2021
*/

public interface CourseRepository extends JpaRepository<Course, Long>{

	boolean existsById(Long id);

	@Query("select new com.brainsinjars.projectbackend.dto.CourseDTO(c.id, c.name, c.description, c.photoUrl, c.fees, c.duration) from Course c")
	Set<CourseDTO> findAllCourses();
	
	@Query("select new com.brainsinjars.projectbackend.dto.CourseDTO(c.id, c.name, c.description, c.photoUrl, c.fees, c.duration) from Course c join c.institutes i where i.id=:id")
	List<CourseDTO> getAll(long id);
	
	//find by name ----> removed
	@Query("select c from Course c where c.name=:name")
	Optional<Course> getByName(String name);

	//get list of courses by duration----> removed
//	List<Course> findByDuration(short duration);
	
	//get course by id
	@Query("select c from Course c where c.id=:id")
	Optional<Course> getById(long id);
	
	//lists should not be optional as optional on single entity throws no such element exception while an empty list should not throw that
	//return a list of courses(id and name)
	@Query("select new com.brainsinjars.projectbackend.dto.CourseIdNameDTO(c.id, c.name) from Course c")
	List<CourseIdNameDTO> getCourses();
	
	@Query("select new com.brainsinjars.projectbackend.dto.CourseDTO(c.id, c.name, c.description, c.photoUrl, c.fees,"
			+ " c.duration) from Course c")
	List<CourseDTO> getCourseDetails();
	

//	@Query("select new com.brainsinjars.projectbackend.dto.CourseIdNameDTO(c.id, c.name)"
//			+ " from Course c where :id in c.institutes")
	
	@Query("select new com.brainsinjars.projectbackend.dto.CourseIdNameDTO(c.id, c.name) from Course c join c.institutes i where i.id=:id")
	List<CourseIdNameDTO> getAllCoursesByInstituteId(long id);
	
	//testing
	@Query("select c from Course c where c.id in :ids")
    Set<Course> findCoursesByIds(@Param("ids") Set<Long> ids);
}
