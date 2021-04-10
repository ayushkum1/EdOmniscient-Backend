package com.brainsinjars.projectbackend.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import com.brainsinjars.projectbackend.repository.AcademicRepository;
import com.brainsinjars.projectbackend.service.IAcademicService;
import com.brainsinjars.projectbackend.service.IMediaService;
import com.brainsinjars.projectbackend.dao.InstituteDao;
import com.brainsinjars.projectbackend.exceptions.RecordAlreadyExistsException;
import com.brainsinjars.projectbackend.exceptions.RecordCreationException;
import com.brainsinjars.projectbackend.exceptions.RecordDeletionException;
import com.brainsinjars.projectbackend.exceptions.RecordNotFoundException;
import com.brainsinjars.projectbackend.pojo.AcademicCalendar;
import com.brainsinjars.projectbackend.pojo.Institute;

/* 
 * @author: Ayush Kumar Singh
 * @since: 08-03-2021
*/

@Service
@Transactional
public class AcademicServiceImpl implements IAcademicService {

	@Autowired
	private AcademicRepository academicRepository;
	@Autowired
	private InstituteDao instituteDao;

	/*
	 * findAll() : finds all the record
	 * returns : A list of AcademicCalendar objects/ empty list if no record exists
	*/
	@Override
	public List<AcademicCalendar> findAll() {
		return academicRepository.findAll();
	}

	/*
	 * getById : finds a single record record by its id
	 * parameters : AcademicCalendars object id: String
	 * exceptions : RecordNotFoundException
	 * returns : a single object of AcademicCalendar/throws exception
	*/
	@Override
	public AcademicCalendar getById(String id) throws RecordNotFoundException {

		return academicRepository.getById(Long.parseLong(id))
				.orElseThrow(() -> new RecordNotFoundException("Academic event not found"));

	}
	
	/*
	 * getAcademicEventsByDate : finds a list of records on a specific date, as one institute can have 
	 * 							 many events on one single date
	 * parameters : Institute object id: String, date: LocalDate
	 * exceptions : RecordNotFoundException
	 * returns : a list of objects of AcademicCalendar/throws exception
	*/
	@Override
	public List<AcademicCalendar> getAcademicEventsByDate(String id, LocalDate date) throws RecordNotFoundException {
		try {
			List<AcademicCalendar> events = academicRepository.academicEventsByDate(Long.parseLong(id), date);
	
			if (events.isEmpty()) {
				throw new RecordNotFoundException("No record found for this institute");
			}
			return events;
		}
		catch (NumberFormatException e) {
			throw new NumberFormatException("id entered as string");
		}
	}

	/*
	 * getAllAcademicEventsByInstituteId : finds a list of records of an Institute
	 * parameters : Institute object id: String
	 * exceptions : RecordNotFoundException
	 * returns : a list of objects of AcademicCalendar/throws exception
	*/
	@Override
	public List<AcademicCalendar> getAllAcademicEventsByInstituteId(String id) throws RecordNotFoundException {
		try {
		List<AcademicCalendar> events = academicRepository.allAcademicEvents(Long.parseLong(id));
			if (events.isEmpty()) {
				throw new RecordNotFoundException("No record found for this institute");
			}
			return events;
		}
		catch(NumberFormatException e) {
			throw new NumberFormatException("id entered as string");
		}
	}
	
	/*
	 * createAcademicEvent : creates an academic event from the json data got after marshalling.
	 * 						 Finds the institute by its id and sets the institute of the 
	 * 						 AcademicCalandar object and then saves it
	 * parameters : Institute object id: String, AcademicCalendar Object : Object
	 * exceptions : RecordNotFoundException,RecordCreationException,RecordAlreadyExistsException
	 * returns : persisted AcademicCalandar object/throws exception
	*/
	@Override
	public AcademicCalendar createAcademicEvent(String id, AcademicCalendar academicCalendar)
			throws RecordCreationException, RecordNotFoundException, RecordAlreadyExistsException {
		// get the institute from institute dao and set temp objects institute, and
		// return the temp object to be persisted

		if (academicRepository.existsById(academicCalendar.getId())) {
			throw new RecordAlreadyExistsException("event with that id already exists");
		}
		try {
			academicCalendar.setInstitute(instituteDao.findById(Long.parseLong(id)));

			System.out.println("temp event object " + academicCalendar);
			AcademicCalendar acd = academicRepository.save(academicCalendar);
			if(acd != null) {
				return acd;
			}
			else {
				throw new RecordCreationException("couldnt create record");
			}
		}
		catch (NumberFormatException e) {
			throw new NumberFormatException("id entered as string");
		}
	}

	/*
	 * updateAcademicCalendar : updates an AcademicCalandar object if it exists
	 * 							gets the AcademicCalendar object by its id and then calls its setters to set the values
	 * parameters : AcademicCalandar object id: String, AcademicCalendar object from json with updated fields after marshalling: AcademicCalendar
	 * exceptions : RecordNotFoundException,RecordCreationException
	 * returns : updated AcademicCalandar object/throws exception.
	*/
	@Override
	public AcademicCalendar updateAcademicCalendar(String id, AcademicCalendar academicCalendar)
			throws RecordCreationException, RecordNotFoundException {

		try {
			AcademicCalendar academicCalendarTemp = academicRepository.getById(Long.parseLong(id))
					.orElseThrow(() -> new RecordNotFoundException("Academic event not found with this id"));

			academicCalendarTemp.setDate(academicCalendar.getDate());
			academicCalendarTemp.setDescription(academicCalendar.getDescription());
			academicCalendarTemp.setImageUrl(academicCalendar.getImageUrl());
			academicCalendarTemp.setName(academicCalendar.getName());
			return academicCalendarTemp;
		}
		catch (NumberFormatException e) {
			throw new NumberFormatException("id entered as string");
		}

	}

	/*
	 * updateByInstituteIdAndEventId : updates an AcademicCalandar object of an specific Institute if it exists
	 * parameters : Institute object id: String, AcademicCalendar object from json with updated fields after marshalling: AcademicCalendar
	 * exceptions : RecordNotFoundException,RecordCreationException
	 * returns : updated AcademicCalandar object/throws exception.
	*/
	@Override
	public AcademicCalendar updateByInstituteIdAndEventId(String instituteId, String id,
			AcademicCalendar academicCalendar) throws RecordNotFoundException, RecordCreationException {

		try {
			AcademicCalendar academicCalendarTemp = academicRepository.getByInstituteId(Long.parseLong(instituteId), Long.parseLong(id)).orElseThrow(
					() -> new RecordNotFoundException("Academic event not found in this institute with id " + instituteId));

			academicCalendarTemp.setDate(academicCalendar.getDate());
			academicCalendarTemp.setDescription(academicCalendar.getDescription());
			academicCalendarTemp.setImageUrl(academicCalendar.getImageUrl());
			academicCalendarTemp.setName(academicCalendar.getName());

			return academicCalendarTemp;
		} catch (NumberFormatException e) {
			throw new NumberFormatException("id entered as string");
		}
	}

	/*
	 * deleteAcademicCalendarByDate : deletes multiple events on that date, if admin wants to delete all the events on that particular date
	 * 								  finds the institute object by its id then
	 * 								  finds the list of AcademicCalander objects and then removes it from institutes object
	 * 								  orphan removal is true, so all the records will be deleted
	 * parameters : Institute object id: String, Date : LocalDate
	 * exceptions : RecordNotFoundException,RecordDeletionException
	 * returns : void/throws exception.
	*/
	@Override
	public void deleteAcademicCalendarByDate(String id, LocalDate date)
			throws RecordDeletionException, RecordNotFoundException {
		Institute institute;
		try {
			institute = instituteDao.findById(Long.parseLong(id));
		} catch (RecordNotFoundException e) {
			throw new RecordNotFoundException("Institute id provided does not exist");
		}

		List<AcademicCalendar> events = academicRepository.academicEventsByDate(Long.parseLong(id), date);

		if (events.isEmpty()) {
			throw new RecordNotFoundException("No events on this date");
		}

		events.forEach((ae) -> {
			institute.removeAcademicCalendar(ae);
			System.out.println(institute.getAcademicCalendars());
		});

	}
	
	/*
	 * deleteAcademicCalendar : deletes an AcademicCalandar object by its id if it exists
	 * parameters : AcademicCalandar object id: String
	 * exceptions : RecordNotFoundException,RecordDeletionException
	 * returns : deleted AcademicCalandar object/throws exception.
	*/
	@Override
	public AcademicCalendar deleteAcademicCalendar(String id) throws RecordDeletionException, RecordNotFoundException {

		try {
			AcademicCalendar academicCalendarEvent = academicRepository.getById(Long.parseLong(id))
					.orElseThrow(() -> new RecordNotFoundException("Academic event doesnt exist"));

			academicRepository.delete(academicCalendarEvent);

			return academicCalendarEvent;
		}
		catch (NumberFormatException e) {
			throw new NumberFormatException("id entered as string");
		}

	}


	/*
	 * deleteEventByIdAndInstituteId : deletes a single AcademicCalendar object of an specific Institute if it exists 
	 * 								   gets the Institute Object by its id and then get the AcademicCalendar Object by its id
	 * 								   remove the AcademicCalendar object from Institute object
	 * 								   orphan removal will remove the record from academic_calendar table
	 * parameters : Institute object id: String, AcademicCalendar object id: String
	 * exceptions : RecordNotFoundException
	 * returns : deleted AcademicCalandar object/throws exception.
	*/
	@Override
	public AcademicCalendar deleteEventByIdAndInstituteId(String instituteId, String eventId)
			throws RecordNotFoundException {

		Institute institute;

		try {
			institute = instituteDao.findById(Long.parseLong(instituteId));
		} catch (RecordNotFoundException e) {
			throw new RecordNotFoundException("institute with that id not found");
		}

		AcademicCalendar academicCalendar = academicRepository.getById(Long.parseLong(eventId))
				.orElseThrow(() -> new RecordNotFoundException("No event with that id exists"));
		institute.removeAcademicCalendar(academicCalendar);
		return academicCalendar;
	}

}
