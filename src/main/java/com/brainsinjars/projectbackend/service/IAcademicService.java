package com.brainsinjars.projectbackend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import com.brainsinjars.projectbackend.exceptions.RecordAlreadyExistsException;
import com.brainsinjars.projectbackend.exceptions.RecordCreationException;
import com.brainsinjars.projectbackend.exceptions.RecordDeletionException;
import com.brainsinjars.projectbackend.exceptions.RecordNotFoundException;
import com.brainsinjars.projectbackend.pojo.AcademicCalendar;

/* 
 * @author: Ayush Kumar Singh
 * @since: 08-03-2021
*/

public interface IAcademicService {

	//method to get list of all events
	List<AcademicCalendar> findAll();//not sure if i need this

	//get event by its id
	AcademicCalendar getById(String id) throws RecordNotFoundException;
	
	//method to get event by its date, it will be a list as many events can be there on one date of a specific institute
	List<AcademicCalendar> getAcademicEventsByDate(String id, LocalDate date) throws RecordNotFoundException;
	
	//get all academic events of an institute by instituteId 
	List<AcademicCalendar> getAllAcademicEventsByInstituteId(String id) throws RecordNotFoundException;
		
	//create a academic event
	//fetching institute by its id, that throws record not found exception
	AcademicCalendar createAcademicEvent(String id, AcademicCalendar academicCalendar) throws RecordCreationException, RecordNotFoundException, RecordAlreadyExistsException;

	//update an event by getting its id and new object
	AcademicCalendar updateAcademicCalendar(String id, AcademicCalendar academicCalendar) throws RecordCreationException, RecordNotFoundException;
	
	AcademicCalendar updateByInstituteIdAndEventId(String instituteId, String id, AcademicCalendar academicCalendar) throws RecordNotFoundException, RecordCreationException;
	
	//delete a event by id
	AcademicCalendar deleteAcademicCalendar(String id) throws RecordDeletionException, RecordNotFoundException;
	
	//deleting academic event by date, institute specific
	void deleteAcademicCalendarByDate(String id, LocalDate date) throws RecordDeletionException, RecordNotFoundException;
	
	//method to delete an event, institute specific
	AcademicCalendar deleteEventByIdAndInstituteId(String instituteId, String eventId) throws RecordNotFoundException, RecordDeletionException;
	
}
