package com.brainsinjars.projectbackend.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brainsinjars.projectbackend.constant.HasRole;
import com.brainsinjars.projectbackend.dto.MessageDto;
import com.brainsinjars.projectbackend.dto.MessageType;
import com.brainsinjars.projectbackend.exceptions.RecordAlreadyExistsException;
import com.brainsinjars.projectbackend.exceptions.RecordCreationException;
import com.brainsinjars.projectbackend.exceptions.RecordDeletionException;
import com.brainsinjars.projectbackend.exceptions.RecordNotFoundException;
import com.brainsinjars.projectbackend.pojo.AcademicCalendar;
import com.brainsinjars.projectbackend.service.IAcademicService;

/* 
 * @author: Ayush Kumar Singh
 * @since: 08-03-2021
*/

@RestController
@RequestMapping("/institutes/{instituteId}")
public class AcademicController {

	@Autowired
	IAcademicService academicService;

	public AcademicController() {
		System.out.println("in constructor of academic class");
	}

	/*
	 * @GetMapping
	 * getEventsByInstituteId : finds a list of records of an Institute
	 * parameters : Institute object id: String
	 * exceptions : RecordNotFoundException,NumberFormatException
	 * returns : Status Code: 200, a list of objects of AcademicCalendar/throws exception
	*/
	@Secured({HasRole.USER, HasRole.INSTITUTE_ADMIN, HasRole.MEMBER, HasRole.ROOT})
	@GetMapping(value = "/calendar",
				produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getEventsByInstituteId(@PathVariable("instituteId") String id) {
		try {
			List<AcademicCalendar> eventsList = academicService.getAllAcademicEventsByInstituteId(id);
			return ResponseEntity.ok(eventsList);
		} catch (RecordNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new MessageDto(e.getMessage(), MessageType.ERROR));
		} catch (NumberFormatException e) {
			return ResponseEntity.badRequest()
					.body(new MessageDto(e.getMessage(), MessageType.ERROR));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new MessageDto(e.getMessage(), MessageType.ERROR));
		}
	}

	/*
	 * @GetMapping
	 * getEventsByDate : finds a list of records on a specific date
	 * parameters : Institute object id: String, date: LocalDate
	 * exceptions : RecordNotFoundException,NumberFormatException
	 * returns : Status Code: 200, a list of objects of AcademicCalendar in Response Entity/throws exception
	*/
	@Secured({HasRole.USER, HasRole.INSTITUTE_ADMIN, HasRole.MEMBER, HasRole.ROOT})
	@GetMapping(value = "/calendar/{date}", 
				produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getEventsByDate(@PathVariable("instituteId") String id,
			@PathVariable("date") String date) {
		try {
			List<AcademicCalendar> eventsList = academicService.getAcademicEventsByDate(id, LocalDate.parse(date));
			return new ResponseEntity<>(eventsList, HttpStatus.OK);
		} catch (RecordNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new MessageDto(e.getMessage(), MessageType.ERROR));
		} catch (NumberFormatException e) {
			return ResponseEntity.badRequest()
					.body(new MessageDto(e.getMessage(), MessageType.ERROR));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new MessageDto(e.getMessage(), MessageType.ERROR));
		}
	}
	
	/*
	 * @PostMapping
	 * createAcademicEvent : creates an academic event, marshalling happens here from json to java object.
	 * @PathVariable : instituteId
	 * @RequestBody: AcademicCalendar object in json
	 * parameters : Institute object id: String, AcademicCalendar Object : AcademicCalendar
	 * exceptions : RecordNotFoundException,RecordCreationException,NumberFormatException,RecordAlreadyExistsException
	 * returns : status code : 200, persisted AcademicCalandar object/throws exception
	*/
	@Secured({HasRole.INSTITUTE_ADMIN, HasRole.ROOT})
	@PostMapping(value = "/calendar",
            	 consumes = MediaType.APPLICATION_JSON_VALUE,
            	 produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createAcademicEvent(@PathVariable("instituteId") String id,
			@RequestBody AcademicCalendar academicCalendarEvent){
		try {
			System.out.println(academicCalendarEvent);
			return ResponseEntity.ok(academicService.createAcademicEvent(id, academicCalendarEvent));
		} catch (RecordCreationException e) {
			return ResponseEntity.badRequest().body(new MessageDto(e.getMessage(), MessageType.ERROR));
		} catch (NumberFormatException e) {
			return ResponseEntity.badRequest()
					.body(new MessageDto(e.getMessage(), MessageType.ERROR));
		} catch (RecordAlreadyExistsException e) {
			return ResponseEntity.badRequest().body(new MessageDto(e.getMessage(), MessageType.ERROR));
		} catch (RecordNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageDto(e.getMessage(), MessageType.ERROR));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new MessageDto(e.getMessage(), MessageType.ERROR));
		}
	}

	/*
	 * @PutMapping
	 * updateAcademicEvent : updates an AcademicCalandar object if it exists
	 * @PathVariable : id
	 * @RequestBody: AcademicCalendar object in json
	 * parameters : AcademicCalandar object id: String, AcademicCalendar object from json with updated fields after marshalling: AcademicCalendar
	 * exceptions : RecordNotFoundException,RecordCreationException,NumberFormatException
	 * returns : status code : 200, updated AcademicCalandar object in Response Entity/throws exception.
	*/
	@Secured({HasRole.INSTITUTE_ADMIN, HasRole.ROOT})
	@PutMapping(value = "/calendar/{id}",
            	consumes = MediaType.APPLICATION_JSON_VALUE,
            	produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateAcademicEvent(@PathVariable("id") String id,
			@RequestBody AcademicCalendar academicCalendar) {
		try {
			return ResponseEntity.ok(academicService.updateAcademicCalendar(id, academicCalendar));

		} catch (RecordCreationException e) {
			return ResponseEntity.badRequest().body(new MessageDto("Error in updating", MessageType.ERROR));
		} catch (NumberFormatException e) {
			return ResponseEntity.badRequest()
					.body(new MessageDto(e.getMessage(), MessageType.ERROR));
		} catch (RecordNotFoundException e) {
			return ResponseEntity.badRequest().body(new MessageDto(e.getMessage(), MessageType.ERROR));
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new MessageDto(e.getMessage(), MessageType.ERROR));
		}

	}
	
	/*
	 * @PutMapping
	 * updateByInstituteIdAndEventId : updates an AcademicCalandar object of an specific Institute if it exists
	 * @PathVariable : instituteId
	 * @PathVariable : id
	 * @RequestBody: AcademicCalendar object in json
	 * parameters : Institute object id: String, AcademicCalendar object id: String, AcademicCalendar object from json with updated fields after marshalling: AcademicCalendar
	 * exceptions : RecordNotFoundException,RecordCreationException,NumberFormatException
	 * returns : Status code : 200, updated AcademicCalandar object in Response Entity/throws exception.
	*/
	@Secured({HasRole.INSTITUTE_ADMIN, HasRole.ROOT})
	@PutMapping(value = "/update/{id}",
            	consumes = MediaType.APPLICATION_JSON_VALUE,
            	produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateByInstituteIdAndEventId(@PathVariable("instituteId") String instituteId, @PathVariable("id") String id,
			@RequestBody AcademicCalendar academicCalendar) {
		try {
			return ResponseEntity.ok(academicService.updateByInstituteIdAndEventId(instituteId, id, academicCalendar));

		} catch (RecordCreationException e) {
			return ResponseEntity.badRequest().body(new MessageDto("Error in updating", MessageType.ERROR));
		} catch (NumberFormatException e) {
			return ResponseEntity.badRequest()
					.body(new MessageDto(e.getMessage(), MessageType.ERROR));
		} catch (RecordNotFoundException e) {
			return ResponseEntity.badRequest().body(new MessageDto(e.getMessage(), MessageType.ERROR));
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new MessageDto(e.getMessage(), MessageType.ERROR));
		}

	}
	
	/*
	 * @DeleteMapping
	 * deleteAcademicEventByEventIdAndInstituteId : deletes a single AcademicCalendar object of an specific Institute if it exists 
	 * @PathVariable: instituteId
	 * @PathVariable : id
	 * parameters : Institute object id: String, AcademicCalendar object id: String
	 * exceptions : RecordNotFoundException,NumberFormatException,RecordDeletionException
	 * returns : status code: 200, deleted AcademicCalandar object in Response Entity/throws exception.
	*/
	@Secured({HasRole.INSTITUTE_ADMIN, HasRole.ROOT})
	@DeleteMapping(value = "/delete-event/{id}")
	public ResponseEntity<?> deleteAcademicEventByEventIdAndInstituteId(@PathVariable("instituteId") String instituteId,
			@PathVariable("id") String eventId) {
		// ResponseEntity.ok is a shortcut
		try {
			academicService.deleteEventByIdAndInstituteId(instituteId, eventId);
			return ResponseEntity.ok("Deleted Successfully");//returning message, could have returned deleted object as well
		} catch (RecordDeletionException e) {
			return ResponseEntity.badRequest()
					.body(new MessageDto("Error in deleting record by date", MessageType.ERROR));
		} catch (NumberFormatException e) {
			return ResponseEntity.badRequest()
					.body(new MessageDto("institute id entered as string", MessageType.ERROR));
		}catch (RecordNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new MessageDto(e.getMessage(), MessageType.ERROR));
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new MessageDto(e.getMessage(), MessageType.ERROR));
		}
	}

	/*
	 * @DeleteMapping
	 * deleteAcademicEvent : deletes an AcademicCalandar object by its id if it exists
	 * @PathVariable: id
	 * parameters : AcademicCalandar object id: String
	 * exceptions : RecordNotFoundException,RecordDeletionException,NumberFormatException
	 * returns : status code: 200, deleted AcademicCalandar object/throws exception.
	*/
	@Secured({HasRole.INSTITUTE_ADMIN, HasRole.ROOT})
	@DeleteMapping(value = "/calendar/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteAcademicEvent(@PathVariable("id") String id){
		try {
			return ResponseEntity.ok(academicService.deleteAcademicCalendar(id));
		} catch (RecordDeletionException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new MessageDto("Error in deleting record", MessageType.ERROR));
		} catch (NumberFormatException e) {
			return ResponseEntity.badRequest()
					.body(new MessageDto(e.getMessage(), MessageType.ERROR));
		}
		catch (RecordNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new MessageDto(e.getMessage(), MessageType.ERROR));
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new MessageDto(e.getMessage(), MessageType.ERROR));
		}
	}

	/*
	 * @DeleteMapping
	 * deleteAcademicEventByDate : deletes multiple events on that date
	 * @PathVariable: instituteId
	 * @PathVariable: date
	 * parameters : Institute object id: String, Date : LocalDate
	 * exceptions : RecordNotFoundException,RecordDeletionException,NumberFormatException
	 * returns : status code: 200, message : Deleted Successfully /throws exception.
	*/
	@Secured({HasRole.INSTITUTE_ADMIN, HasRole.ROOT})
	@DeleteMapping(value = "/calendar/date/{date}")
	public ResponseEntity<?> deleteAcademicEventByDate(@PathVariable("instituteId") String id,
			@PathVariable("date") String date) {
		// ResponseEntity.ok is a shortcut
		try {
			academicService.deleteAcademicCalendarByDate(id, LocalDate.parse(date));
			return ResponseEntity.ok("Deleted Successfully");
		} catch (RecordDeletionException e) {
			return ResponseEntity.badRequest()
					.body(new MessageDto("Error in deleting record by date", MessageType.ERROR));
		} catch (NumberFormatException e) {
			return ResponseEntity.badRequest()
					.body(new MessageDto("institute id entered as string", MessageType.ERROR));
		}catch (RecordNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new MessageDto(e.getMessage(), MessageType.ERROR));
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new MessageDto(e.getMessage(), MessageType.ERROR));
		}
	}

}
