package com.brainsinjars.projectbackend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Year;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.brainsinjars.projectbackend.controller.PlacementController;
import com.brainsinjars.projectbackend.dao.PlacementDao;
import com.brainsinjars.projectbackend.dto.PlacementChartDTO;
import com.brainsinjars.projectbackend.dto.PlacementDTO;
import com.brainsinjars.projectbackend.exceptions.PlacementRecordNotFoundException;
import com.brainsinjars.projectbackend.exceptions.RecordNotFoundException;
import com.brainsinjars.projectbackend.pojo.Batch;
import com.brainsinjars.projectbackend.pojo.Placement;

/* 
 * @author: Ayush Kumar Singh
 * @since: 25-03-2021
*/

//testing table
/*mysql> select * from placements;
+----+---------------+----------+---------------+--------------------+----------------+------+-----------+--------------+
| id | avglpaoffered | batch    | maxlpaoffered | no_placed_students | total_students | year | course_id | institute_id |
+----+---------------+----------+---------------+--------------------+----------------+------+-----------+--------------+
|  1 |           6.5 | FEBRUARY |          18.8 |                 95 |            100 | 2020 |         1 |            1 |
|  2 |           6.5 | AUGUST   |          18.8 |                195 |            200 | 2020 |         2 |            1 |
|  3 |           6.5 | FEBRUARY |          18.8 |                 95 |            200 | 2021 |         2 |            1 |
|  4 |           6.5 | AUGUST   |          18.8 |                 95 |            200 | 2021 |         1 |            1 |
| 12 |           8.9 | FEBRUARY |          17.2 |                188 |            200 | 2022 |         1 |            1 |
+----+---------------+----------+---------------+--------------------+----------------+------+-----------+--------------+
*/

@SpringBootTest
public class TestPlacementService {

	@Autowired
	private PlacementDao dao;
	
	@Autowired
	private PlacementController controller;
	
	@Test
	public void sanityTest() {
		assertNotNull(controller);
	}
	
	@Test
	public void testFetchPlacementDetails() {
		List<PlacementDTO> placements = dao.fetchPlacementDetails(1);
		
		assertEquals(5, placements.size());
		
		assertEquals(1, placements.get(0).getId());
		assertEquals(6.5, placements.get(0).getAvgLPAOffered());
		assertEquals("edac", placements.get(0).getCourseName());
		assertEquals(18.8, placements.get(0).getMaxLPAOffered());
		assertEquals(100, placements.get(0).getTotalStudents());
		assertEquals(95, placements.get(0).getNoPlacedStudents());
		assertEquals("2020", placements.get(0).getYear().toString());
		assertEquals(Batch.FEBRUARY, placements.get(0).getBatch());
	}
	
	@Test
	public void testFetchPlacementDetailsByBatchAndYearAndCourse() {
		PlacementDTO placement = dao.fetchPlacementDetailsByBatchAndYearAndCourse(1, Batch.FEBRUARY, Year.of(2020), "edac");
		
		assertEquals(1, placement.getId());
		assertEquals(6.5, placement.getAvgLPAOffered());
		assertEquals("edac", placement.getCourseName());
		assertEquals(18.8, placement.getMaxLPAOffered());
		assertEquals(100, placement.getTotalStudents());
		assertEquals(95, placement.getNoPlacedStudents());
		assertEquals("2020", placement.getYear().toString());
		assertEquals(Batch.FEBRUARY, placement.getBatch());
	}
	
	@Test
	public void testFetchLastNPlacementDetails() {
		List<PlacementDTO> placements = dao.fetchLastNPlacementDetails(1, 2);
		assertEquals(2, placements.size());
		
		PlacementDTO placement = placements.get(0);
		
		assertEquals(12, placement.getId());
		assertEquals(8.9, placement.getAvgLPAOffered());
		assertEquals("edac", placement.getCourseName());
		assertEquals(17.2, placement.getMaxLPAOffered());
		assertEquals(200, placement.getTotalStudents());
		assertEquals(188, placement.getNoPlacedStudents());
		assertEquals("2022", placement.getYear().toString());
		assertEquals(Batch.FEBRUARY, placement.getBatch());
	
		
	}
	
	@Test
	public void testGetCourseSortByYear() {
		List<PlacementChartDTO> placements = dao.getCourseSortByYear((long) 1,"edac");
		
		assertEquals(3, placements.size());
		
		assertEquals(Batch.FEBRUARY, placements.get(2).getBatch());
		assertEquals("edac", placements.get(2).getCourseName());
		assertEquals(95, placements.get(2).getNoPlacedStudents());
		assertEquals(100, placements.get(2).getTotalStudents());
		assertEquals("2020", placements.get(2).getYear().toString());
		
	}
	
	
	@Test
	public void testAddPlacementRecord() throws RecordNotFoundException {
		PlacementDTO placementDto = new PlacementDTO(5, "edac", 195, 200, Batch.FEBRUARY, Year.of(2022), 19.2, 7.9);
		
		Placement placement = dao.addPlacementRecord(1, placementDto);
		
		assertEquals(Batch.FEBRUARY, placement.getBatch());
		assertEquals(195, placement.getNoPlacedStudents());
		assertEquals(200, placement.getTotalStudents());
		assertEquals("2022", placement.getYear().toString());
		
	}
	
	@Test
	public void testUpdatePlacementRecord() throws PlacementRecordNotFoundException, RecordNotFoundException {
		PlacementDTO placementDto = new PlacementDTO(5, "edac", 188, 200, Batch.FEBRUARY, Year.of(2022), 17.2, 8.9);
		Placement placement = dao.updatePlacementRecord(1, 12, placementDto);
		
		assertEquals(Batch.FEBRUARY, placement.getBatch());
		assertEquals(188, placement.getNoPlacedStudents());
		assertEquals(200, placement.getTotalStudents());
		assertEquals("2022", placement.getYear().toString());
		assertEquals(8.9, placement.getAvgLPAOffered());
		
	}
	
	
}











