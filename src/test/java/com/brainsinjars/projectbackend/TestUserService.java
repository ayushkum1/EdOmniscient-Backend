package com.brainsinjars.projectbackend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.brainsinjars.projectbackend.constant.HasRole;
import com.brainsinjars.projectbackend.controller.UserController;
import com.brainsinjars.projectbackend.dao.UserDao;
import com.brainsinjars.projectbackend.dto.UserDetailsDTO;
import com.brainsinjars.projectbackend.dto.UserMemberDTO;
import com.brainsinjars.projectbackend.exceptions.UserNotFoundException;
import com.brainsinjars.projectbackend.pojo.MemberType;
import com.brainsinjars.projectbackend.pojo.Role;
import com.brainsinjars.projectbackend.pojo.User;

/* 
 * @author: Ayush Kumar Singh
 * @since: 08-03-2021
*/

//testing table
/*mysql> select * from users;
+-------------------+--------------------------------------------------------------+------------+------------+-----------+------------------+--------------------------------------------------------------+------------------+-----------------+-------------+
| email             | about                                                        | fb_profile | first_name | last_name | linkedin_profile | passwd_hash                                                  | profile_pic_link | role            | location_id |
+-------------------+--------------------------------------------------------------+------------+------------+-----------+------------------+--------------------------------------------------------------+------------------+-----------------+-------------+
| inst1@gmail.com   | NULL                                                         | NULL       | Institute  | Admin     | NULL             | $2a$31$ggKb1YTdbRcG/Z4dq50Gc.Dh9pDXtlx7WiL/88zrhffZkAAH/XaGm | NULL             | INSTITUTE_ADMIN |           6 |
| inst2@gmail.com   | NULL                                                         | NULL       | Institute  | Admin     | NULL             | $2a$31$/afiWs6T7YK8jumUZ5toPu9LfWMRosQmVcbkbbnGrAgUaJaql3dw2 | NULL             | INSTITUTE_ADMIN |           7 |
| inst3@gmail.com   | NULL                                                         | NULL       | Institute  | Admin     | NULL             | $2a$31$j/Hx01PRqx5aEObsgUmcTeKzYtXve73aUbr0.apO90NWvHtP7Or7y | NULL             | INSTITUTE_ADMIN |           8 |
| inst4@gmail.com   | NULL                                                         | NULL       | Institute  | Admin     | NULL             | $2a$31$HIk9SjK5lPQX9hNn.0rNWebsrItiic6sZM1C/Xm5RnrI1.Gg2p1dO | NULL             | INSTITUTE_ADMIN |           9 |
| inst5@gmail.com   | NULL                                                         | NULL       | Institute  | Admin     | NULL             | $2a$31$j2tDF7bD/UFO8ItZsF3LCeDQDnUt0CMd2ywCquoIxWCKdCRlgUNFG | NULL             | INSTITUTE_ADMIN |          10 |
| member@gmail.com  | this is about the user, which can be anything at this moment | www.fb.com | member1    | test      | www.li.com       | $2a$31$ggKb1YTdbRcG/Z4dq50Gc.Dh9pDXtlx7WiL/88zrhffZkAAH/XaGm | www.dp.jpg       | MEMBER          |           1 |
| member2@gmail.com | this is about the user, which can be anything at this moment | www.fb.com | member2    | test      | www.li.com       | $2a$31$ggKb1YTdbRcG/Z4dq50Gc.Dh9pDXtlx7WiL/88zrhffZkAAH/XaGm | www.dp.jpg       | MEMBER          |           2 |
| member3@gmail.com | this is about the user, which can be anything at this moment | www.fb.com | member3    | test      | www.li.com       | $2a$31$ggKb1YTdbRcG/Z4dq50Gc.Dh9pDXtlx7WiL/88zrhffZkAAH/XaGm | www.dp.jpg       | MEMBER          |           3 |
| member4@gmail.com | this is about the user, which can be anything at this moment | www.fb.com | member3    | test      | www.li.com       | $2a$31$ggKb1YTdbRcG/Z4dq50Gc.Dh9pDXtlx7WiL/88zrhffZkAAH/XaGm | www.dp.jpg       | MEMBER          |           4 |
| member5@gmail.com | this is about the user, which can be anything at this moment | www.fb.com | member5    | test      | www.li.com       | $2a$31$ggKb1YTdbRcG/Z4dq50Gc.Dh9pDXtlx7WiL/88zrhffZkAAH/XaGm | www.dp.jpg       | MEMBER          |          11 |
+-------------------+--------------------------------------------------------------+------------+------------+-----------+------------------+--------------------------------------------------------------+------------------+-----------------+-------------+
*/
@SpringBootTest
public class TestUserService {

	@Autowired
	private UserDao dao;
	
	@Autowired
	private UserController controller;
	
	@Test
	public void sanityTest() {
		assertNotNull(controller);
	}
	
	@Test
	public void testExistsByEmail() {
		assertEquals(true, dao.existsByEmail("member@gmail.com"));
	}
	
	@Test
	public void testFindByEmail() throws UserNotFoundException {
		User user = dao.findByEmail("member@gmail.com");
		
		assertEquals("member@gmail.com", user.getEmail());
		assertEquals("this is about the user, which can be anything at this moment", user.getAbout());
		assertEquals("www.fb.com", user.getFbProfile());
		assertEquals("member1", user.getFirstName());
		assertEquals("test", user.getLastName());
		assertEquals(Role.MEMBER, user.getRole());
		
	}
	
	@Test
	public void testFindByRole() {
		Set<User> users = dao.findByRole(Role.MEMBER);
		
		assertEquals(5, users.size());
		
		List<User> userList = new ArrayList<>(users);
		
		assertEquals("member@gmail.com", userList.get(0).getEmail());
		assertEquals("this is about the user, which can be anything at this moment", userList.get(0).getAbout());
		assertEquals("www.fb.com", userList.get(0).getFbProfile());
		assertEquals("member1", userList.get(0).getFirstName());
		assertEquals("test", userList.get(0).getLastName());
		assertEquals(Role.MEMBER, userList.get(0).getRole());
		
	}
	
	@Test
	public void testFindAllUsers() {
		List<UserDetailsDTO> userList = dao.findAllUsers();
		
		assertEquals(10, userList.size());
		//for(UserDetailsDTO user: userList) {
		//	System.out.println(user.getEmail());
		//}
		//admins list started from 6th position
		assertEquals("inst1@gmail.com", userList.get(5).getEmail());
		assertEquals(null, userList.get(5).getFbProfile());
		assertEquals("Institute", userList.get(5).getFirstName());
		assertEquals("Admin", userList.get(5).getLastName());
		assertEquals("User street address", userList.get(5).getStreetAddr());
		assertEquals("Pune", userList.get(5).getCity());
		assertEquals("411001", userList.get(5).getPinCode());
		assertEquals("Pune", userList.get(5).getRegion());
		
	}
	
	@Test
	public void testFindByMemberId() throws UserNotFoundException {
		UserMemberDTO userMember = dao.findByMemberId(1);
		
		assertEquals("member@gmail.com", userMember.getEmail());
		assertEquals("this is about the user, which can be anything at this moment", userMember.getAbout());
		assertEquals("www.fb.com", userMember.getFbProfile());
		assertEquals("member1", userMember.getFirstName());
		assertEquals("test", userMember.getLastName());
		assertEquals(1, userMember.getMemberId());
		assertEquals(MemberType.FACULTY, userMember.getMemberType());
		assertEquals("108,SVV", userMember.getStreetAddr());
		assertEquals("blore", userMember.getCity());
		assertEquals("560097", userMember.getPinCode());
		assertEquals("bangalore", userMember.getRegion());
		assertEquals("ktaka", userMember.getState());		
	}
}














