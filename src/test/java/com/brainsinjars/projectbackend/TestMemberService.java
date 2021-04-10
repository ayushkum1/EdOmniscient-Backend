package com.brainsinjars.projectbackend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Year;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.brainsinjars.projectbackend.controller.MemberController;
import com.brainsinjars.projectbackend.dao.IMemberDao;
import com.brainsinjars.projectbackend.dao.InstituteDao;
import com.brainsinjars.projectbackend.dao.UserDao;
import com.brainsinjars.projectbackend.dto.MemberAddDTO;
import com.brainsinjars.projectbackend.dto.MemberListDTO;
import com.brainsinjars.projectbackend.dto.MemberUpdateDTO;
import com.brainsinjars.projectbackend.exceptions.RecordNotFoundException;
import com.brainsinjars.projectbackend.exceptions.UserNotFoundException;
import com.brainsinjars.projectbackend.pojo.Member;
import com.brainsinjars.projectbackend.pojo.MemberType;
import com.brainsinjars.projectbackend.pojo.MembershipStatus;
import com.brainsinjars.projectbackend.service.ICourseService;

/* 
 * @author: Ayush Kumar Singh
 * @since: 25-03-2021
*/

//testing table
/*mysql> select * from members;
+----+-------------+----------+--------------------------+--------------+----------+------+-----------+--------------+-------------------+
| id | member_type | prn      | public_email      		 | public_phone | status   | year | course_id | institute_id | user_email        |
+----+-------------+----------+--------------------------+--------------+----------+------+-----------+--------------+-------------------+
|  1 | FACULTY     | acts1234 | member@gmail.com  		 | 8217800000   | APPROVED | 2015 |         1 |            1 | member@gmail.com  |
|  2 | FACULTY     | acts1234 | member2@gmail.com 		 | 8217800001   | APPROVED | 2016 |         7 |            1 | member2@gmail.com |
|  3 | STUDENT     | acts1234 | member3@gmail.com 		 | 8217800001   | PENDING  | 2016 |         2 |            1 | member3@gmail.com |
|  4 | FACULTY     | acts1234 | member4@gmail.com 		 | 8217800004   | APPROVED | 2016 |         3 |            1 | member4@gmail.com |
| 11 | FACULTY     | acts234  | updatedmember5@gmail.com | 891230000    | PENDING  | 2017 |         3 |            1 | member5@gmail.com |
+----+-------------+----------+-------------------+--------------+----------+------+-----------+--------------+-------------------+------+
*/
@SpringBootTest
public class TestMemberService {

	@Autowired
	private MemberController controller;
	
	@Autowired
	private IMemberDao dao;
	
	@Autowired
	ICourseService courseService;
	
	@Autowired
	InstituteDao instituteDao;
	
	@Test
	public void sanityTest() {
		assertNotNull(controller);
	}
	
	@Test
	public void testFindById() throws RecordNotFoundException, UserNotFoundException {
		Member member = dao.findById(1);
		
		assertEquals(1, member.getId());
		assertEquals(MemberType.FACULTY, member.getMemberType());
		assertEquals("acts1234", member.getPrn());
		assertEquals("member@gmail.com", member.getPublicEmail());
		assertEquals("8217800000", member.getPublicPhone());
		assertEquals(MembershipStatus.APPROVED, member.getStatus());
		assertEquals("2015", member.getYear().toString());
		assertEquals(courseService.getById("1"), member.getCourse());
		
	}
	
	@Test
	public void testFindByPassOutYear() throws RecordNotFoundException {
		List<Member> members = dao.findByPassOutYear(Year.of(2016));
		
		assertEquals(3, members.size());
		
		Member member = members.get(0);
				
		assertEquals(MemberType.FACULTY, member.getMemberType());
		assertEquals("acts1234", member.getPrn());
		assertEquals("member2@gmail.com", member.getPublicEmail());
		assertEquals("8217800001", member.getPublicPhone());
		assertEquals(MembershipStatus.APPROVED, member.getStatus());
		assertEquals("2016", member.getYear().toString());
		assertEquals(courseService.getById("7"), member.getCourse());
	
	}
	
	@Test
	public void testAddMember() {
		MemberAddDTO member = new MemberAddDTO("member5@gmail.com", 3, "acts234", MemberType.FACULTY, "8912389131", "member5@gmail.com", Year.of(2017));
		
		assertEquals(true, dao.addMember(member, 1));
		
	}
	
	@Test
	public void testUpdateMember() {
		MemberUpdateDTO member = new MemberUpdateDTO("8912300000", "updatedmember5@gmail.com");
		assertEquals(true, dao.updateMember( (long) 11, member, (long) 1));
	}
	
	//failed, lazy initialization
	@Test
	public void testFindAllMembers() throws RecordNotFoundException {
		Member m1 = dao.findById(1);
		Member m2 = dao.findById(2);
		Member m3 = dao.findById(3);
		Member m4 = dao.findById(4);
		List<MemberListDTO> members = dao.findAllMembers(1);
		//add members to 
		instituteDao.addMembers(m1, (long) 1);
		instituteDao.addMembers(m2, (long) 1);
		instituteDao.addMembers(m3, (long) 1);
		instituteDao.addMembers(m4, (long) 1);
		assertEquals(4, members.size());
	}
	
	@Test
	public void testFindApprovedMembers() {
		List<MemberListDTO> members = dao.findApprovedMembers((long)1);
		assertEquals(3, members.size());
		
		MemberListDTO member = members.get(0);
		
		assertEquals(MemberType.FACULTY, member.getMemberType());
		assertEquals("member@gmail.com", member.getPublicEmail());
		assertEquals("8217800000", member.getPublicPhone());
		assertEquals(MembershipStatus.APPROVED, member.getStatus());
		assertEquals(Year.of(2015), member.getYear());
		
	}
	
}







