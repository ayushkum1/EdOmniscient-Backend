package com.brainsinjars.projectbackend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.brainsinjars.projectbackend.exceptions.RecordNotFoundException;
import com.brainsinjars.projectbackend.pojo.AcademicCalendar;

/* 
 * @author: Ayush Kumar Singh
 * @since: 08-03-2021
*/

public interface AcademicRepository extends JpaRepository<AcademicCalendar, Long>{

	//check if the record exists
	boolean existsById(Long id);
	
	@Query("select a from AcademicCalendar a where a.id=:id")
	Optional<AcademicCalendar> getById(long id);
	
	@Query("select a from AcademicCalendar a join fetch a.institute i where i.id=:id and a.id=:eventId")
	Optional<AcademicCalendar> getByInstituteId(long id, @Param("eventId") long eventId);
	
	@Query("select a from AcademicCalendar a join fetch a.institute i where i.id=:id")
	List<AcademicCalendar> allAcademicEvents(@Param("id") long id);
	
	
	@Query("select a from AcademicCalendar a join fetch a.institute i where i.id=:id and a.date =:date")
	List<AcademicCalendar> academicEventsByDate(@Param("id") long id, @Param("date") LocalDate date);
	
}
