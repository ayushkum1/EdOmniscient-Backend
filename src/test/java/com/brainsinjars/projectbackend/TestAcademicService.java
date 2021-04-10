package com.brainsinjars.projectbackend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.brainsinjars.projectbackend.controller.AcademicController;
import com.brainsinjars.projectbackend.dao.InstituteDao;
import com.brainsinjars.projectbackend.exceptions.RecordAlreadyExistsException;
import com.brainsinjars.projectbackend.exceptions.RecordCreationException;
import com.brainsinjars.projectbackend.exceptions.RecordNotFoundException;
import com.brainsinjars.projectbackend.pojo.AcademicCalendar;
import com.brainsinjars.projectbackend.repository.AcademicRepository;
import com.brainsinjars.projectbackend.service.IAcademicService;

/* 
 * @author: Ayush Kumar Singh
 * @since: 24-03-2021
*/

//testing table
/*mysql> select * from academic_calendar;
+----+------------+--------------------------------------------------------------------+----------------+--------------------------+--------------+
| id | date       | description                                                        | image_url      | name                     | institute_id |
+----+------------+--------------------------------------------------------------------+----------------+--------------------------+--------------+
|  1 | 2020-12-12 | something is happening on this day in this college on this day     | wwww.event.jpg | upd hackathon            |            1 |
|  2 | 2020-12-12 | something is happening on this day in this college on this day     | wwww.event.jpg | hack                     |            1 |
|  3 | 2020-12-12 | something is happening on this day in this college on this day     | wwww.event.jpg | upda hackathon           |            1 |
|  4 | 2020-12-12 | something is happening on this day in this college on this day     | wwww.event.jpg | upda hackathon           |            2 |
|  5 | 2020-11-12 | something is happening on this day in this college on this day     | wwww.event.jpg | def                      |            3 |
|  6 | 2020-10-12 | this is updated description and something is happening on this day | www.image.jpg  | updated coding challenge |            1 |
|  7 | 2021-10-10 | something is happening on this day in this college on this day     | wwww.event.jpg | jkl                      |            5 |
|  8 | 2020-11-12 | something is happening on this day in this college on this day     | www.image.jpg  | updated name             |            1 |
|  9 | 2020-10-12 | something is happening on this day in this college on this day     | wwww.event.jpg | ghi                      |            4 |
| 11 | 2020-12-12 | something is happening on this day in this college on this day     | wwww.event.jpg | hackathon                |            2 |
+----+------------+--------------------------------------------------------------------+----------------+--------------------------+--------------+
*/
@SpringBootTest
public class TestAcademicService {

	@Autowired
	private IAcademicService service;

	@Autowired
	private AcademicController controller;

	@Autowired
	private InstituteDao instDao;

	@Autowired
	private AcademicRepository repository;
	
	@Test
	void sanityTest() {
		assertNotNull(controller);// To confirm if ProductController is autowired correctly.
	}

	@Test
	public void testFindAll() throws RecordNotFoundException {
		List<AcademicCalendar> acList = service.findAll();
		assertEquals("upd hackathon", acList.get(0).getName());
		assertEquals("something is happening on this day in this college on this day", acList.get(0).getDescription());
		assertEquals("2020-12-12", acList.get(0).getDate().toString());
		assertEquals("wwww.event.jpg", acList.get(0).getImageUrl());
		assertEquals(instDao.findById(1).getId(), acList.get(0).getInstitute().getId());
	}

	@Test
	public void testGetById() throws RecordNotFoundException {
		AcademicCalendar ac = service.getById("1");
		assertEquals("upd hackathon", ac.getName());
		assertEquals("something is happening on this day in this college on this day", ac.getDescription());
		assertEquals("2020-12-12", ac.getDate().toString());
		assertEquals("wwww.event.jpg", ac.getImageUrl());
		assertEquals(instDao.findById(1).getId(), ac.getInstitute().getId());

	}

	@Test
	public void testGetAllAcademicEventsByInstituteId() throws RecordNotFoundException {
		List<AcademicCalendar> acList = service
				.getAllAcademicEventsByInstituteId(String.valueOf(instDao.findById(1).getId()));
		assertEquals("upd hackathon", acList.get(0).getName());
		assertEquals("something is happening on this day in this college on this day", acList.get(0).getDescription());
		assertEquals("2020-12-12", acList.get(0).getDate().toString());
		assertEquals("wwww.event.jpg", acList.get(0).getImageUrl());
		assertEquals(instDao.findById(1).getId(), acList.get(0).getInstitute().getId());
	}

	@Test
	public void testGetAcademicEventsByDate() throws RecordNotFoundException {
		List<AcademicCalendar> acList = service.getAcademicEventsByDate(String.valueOf(instDao.findById(1).getId()),
				LocalDate.parse("2020-12-12"));
		assertEquals("hack", acList.get(1).getName());
		assertEquals("something is happening on this day in this college on this day", acList.get(1).getDescription());
		assertEquals("2020-12-12", acList.get(1).getDate().toString());
		assertEquals("wwww.event.jpg", acList.get(1).getImageUrl());
		assertEquals(instDao.findById(1).getId(), acList.get(1).getInstitute().getId());
	}

	@Test
	public void testCreateAcademicEvent() throws RecordNotFoundException, RecordCreationException, RecordAlreadyExistsException {
		AcademicCalendar ac = new AcademicCalendar(LocalDate.parse("2020-10-12"), "coding challenge", "www.image.jpg",
				"something is happening on this day in this college on this day", instDao.findById(1));
//		ac.setId(12); didnt work
		//creates a event with next auto generated value of id, asserting 6 as id as at that id it got persisted in database
		AcademicCalendar savedAc = service.createAcademicEvent("1", ac);
		assertEquals("6", ac.getId());
		assertEquals("coding challenge", savedAc.getName());
		assertEquals("something is happening on this day in this college on this day", savedAc.getDescription());
		assertEquals("2020-10-12", savedAc.getDate().toString());
		assertEquals("www.image.jpg", savedAc.getImageUrl());
		assertEquals(instDao.findById(1).getId(), savedAc.getInstitute().getId());
	}
	
	@Test
	public void testUpdateAcademicCalendar() throws RecordNotFoundException, RecordCreationException {
		AcademicCalendar ac = service.getById("6");
		ac.setDescription("this is updated description and something is happening on this day");
		ac.setName("updated coding challenge");
		AcademicCalendar updatedAc = service.updateAcademicCalendar(String.valueOf(ac.getId()), ac);
		assertEquals("6", String.valueOf(updatedAc.getId()));
		assertEquals("updated coding challenge", updatedAc.getName());
		assertEquals("this is updated description and something is happening on this day", updatedAc.getDescription());
	}
	
	@Test
	public void testUpdateByInstituteIdAndEventId() throws RecordNotFoundException, RecordCreationException {
		Optional<AcademicCalendar> acTemp = repository.getByInstituteId(instDao.findById(1).getId(), service.getById("8").getId());
		acTemp.get().setName("updated name");
		acTemp.get().setDate(LocalDate.parse("2020-11-12"));
		
		AcademicCalendar updatedAc = service.updateByInstituteIdAndEventId(String.valueOf(instDao.findById(1).getId()), 
									 String.valueOf(service.getById("8").getId()), acTemp.get());
		assertEquals("8", String.valueOf(updatedAc.getId()));
		assertEquals("updated name", updatedAc.getName());
		assertEquals("2020-11-12", updatedAc.getDate().toString());
	}
	
}
