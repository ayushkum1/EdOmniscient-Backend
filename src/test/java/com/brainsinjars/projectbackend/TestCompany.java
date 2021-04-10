package com.brainsinjars.projectbackend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.brainsinjars.projectbackend.controller.CompanyController;
import com.brainsinjars.projectbackend.dao.CompanyDao;
import com.brainsinjars.projectbackend.dto.CompanyDto;
import com.brainsinjars.projectbackend.exceptions.RecordNotFoundException;
import com.brainsinjars.projectbackend.pojo.Company;
import com.brainsinjars.projectbackend.repository.CompanyRepository;
import com.brainsinjars.projectbackend.service.CompanyService;

/* 
 * @author: Ayush Kumar Singh
 * @since: 08-03-2021
*/

//testing table
/*mysql> select * from companies;
+----+--------------------+----------------------+----------------------+
| id | logo_path          | name                 | website_link         |
+----+--------------------+----------------------+----------------------+
|  1 | www.companylog.jpg | microsoft            | www.microsoft.com    |
|  2 | www.companylog.jpg | google               | www.google.com       |
|  3 | www.companylog.jpg | oracle               | www.oracle.com       |
|  4 | www.companylog.jpg | sun                  | www.sun.com          |
|  5 | www.walmart.com    | updated name walmart | www.walmartphoto.jpg |
+----+--------------------+----------------------+----------------------+
*/
@SpringBootTest
public class TestCompany {

	@Autowired
	private CompanyDao dao;
	
	@Autowired
	private CompanyController controller;
	
	@Test
	public void sanityTest() {
		assertNotNull(controller);
	}
	
	@Test
	public void testFindAll() {
		Set<CompanyDto> companies = dao.findAll();
	
		assertEquals(5, companies.size());
	
		CompanyDto company = new ArrayList<>(companies).get(0);
		
		assertEquals(1, company.getId());
		assertEquals("microsoft", company.getName());
		assertEquals("www.companylog.jpg", company.getLogoUrl());
		assertEquals("www.microsoft.com", company.getWebsiteUrl());
	}
	
	@Test
	public void testFindById() throws RecordNotFoundException {
		
		Company company = dao.findById(1);
		
		assertEquals(1, company.getId());
		assertEquals("microsoft", company.getName());
		assertEquals("www.companylog.jpg", company.getLogoPath());
		assertEquals("www.microsoft.com", company.getWebsiteLink());
	}
	
	@Test
	public void testFindCompaniesByIds() {
		Set<Long> setIds = new HashSet<Long>();
		setIds.add((long) 1);
		setIds.add((long) 2);
		setIds.add((long) 3);
		
		Set<Company> companies = dao.findCompaniesByIds(setIds);
		
		Company company = new ArrayList<>(companies).get(0);
		assertEquals(3, companies.size());
		assertEquals(1, company.getId());
		assertEquals("microsoft", company.getName());
		assertEquals("www.companylog.jpg", company.getLogoPath());
		assertEquals("www.microsoft.com", company.getWebsiteLink());
	}
	
	@Test
	public void testFindAllByInstituteId() {
		Set<CompanyDto> companies = dao.findAllByInstituteId(1);
		assertEquals(0, companies.size());//no companies are in institute when test was done
	}
	
	@Test
	public void testExistsById() throws RecordNotFoundException {
		assertEquals(true, dao.existsById(1));
		assertEquals(false, dao.existsById(500));
//		RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> {dao.existsById(500);});
//		
//		String expectedMessage = "Company with id='500' not found";
//		String actualMessage = exception.getMessage();
//		assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	public void testCreateNewCompany() {
		Company company = new Company(5, "walmart", "www.walmart.com", "www.walmartphoto.jpg");
		
		assertEquals(5, dao.createNewCompany(company));
	}
	
	@Test
	public void testUpdateCompany() throws RecordNotFoundException {
		CompanyDto company = new CompanyDto(5, "updated name walmart", "www.walmart.com", "www.walmartphoto.jpg");
		
		assertEquals(true, dao.updateCompany(5, company));	
	}
	
}
















