package com.brainsinjars.projectbackend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import javax.annotation.security.RolesAllowed;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.brainsinjars.projectbackend.controller.ReviewController;
import com.brainsinjars.projectbackend.dao.IMemberDao;
import com.brainsinjars.projectbackend.dao.IReviewDao;
import com.brainsinjars.projectbackend.dao.InstituteDao;
import com.brainsinjars.projectbackend.dto.ReviewAddUpdateDTO;
import com.brainsinjars.projectbackend.dto.ReviewDTO;
import com.brainsinjars.projectbackend.exceptions.RecordNotFoundException;
import com.brainsinjars.projectbackend.pojo.Institute;
import com.brainsinjars.projectbackend.pojo.Member;
import com.brainsinjars.projectbackend.pojo.Review;
import com.brainsinjars.projectbackend.pojo.Role;

/* 
 * @author: Ayush Kumar Singh
 * @since: 25-03-2021
*/

//testing table
/*mysql> select * from members;
+----+-------------+----------+-------------------+--------------+----------+------+-----------+--------------+-------------------+
| id | member_type | prn      | public_email      | public_phone | status   | year | course_id | institute_id | user_email        |
+----+-------------+----------+-------------------+--------------+----------+------+-----------+--------------+-------------------+
|  1 | FACULTY     | acts1234 | member@gmail.com  | 8217800000   | APPROVED | 2015 |         1 |            1 | member@gmail.com  |
|  2 | FACULTY     | acts1234 | member2@gmail.com | 8217800001   | APPROVED | 2016 |         7 |            1 | member2@gmail.com |
|  3 | STUDENT     | acts1234 | member3@gmail.com | 8217800001   | PENDING  | 2016 |         2 |            1 | member3@gmail.com |
|  4 | FACULTY     | acts1234 | member4@gmail.com | 8217800004   | APPROVED | 2016 |         3 |            1 | member4@gmail.com |
+----+-------------+----------+-------------------+--------------+----------+------+-----------+--------------+-------------------+
*/
@SpringBootTest
public class TestReviewService {

	@Autowired
	private ReviewController controller;
	
	@Autowired
	private IReviewDao dao;
	
	@Autowired
	private InstituteDao instituteDao;
	
	@Autowired
	private IMemberDao memberDao;
	
	@Test
	public void sanityTest() {
		assertNotNull(controller);
	}
	
	@Test
	public void testFindById() {
		Review review = dao.findById(1);
		assertEquals(1, review.getId());
		assertEquals("4", String.valueOf(review.getRating()));
		assertEquals("this is the content of the review, it can be anything right now............",
					review.getContent());
		assertEquals("2021-03-17T02:38:05", review.getCreatedDateTime().toString());
		assertEquals("2021-03-17T02:38:05", review.getModifiedDateTime().toString());		
	
	}
	
	@Test
	public void testFindByUserEmail() {
		ReviewDTO review = dao.findByUserEmail("member@gmail.com");
		
		assertEquals(1, review.getId());
		assertEquals("member1 test", review.getName());
		assertEquals("4", String.valueOf(review.getRating()));
		assertEquals("this is the content of the review, it can be anything right now............",
					review.getContent());
		assertEquals("2021-03-17T02:38:05", review.getCreatedDateTime().toString());
		assertEquals("2021-03-17T02:38:05", review.getModifiedDateTime().toString());				
	}
	
	@Test
	public void testFindAllRatingsByInstitute() {
		List<Integer> ratings = dao.findAllRatingsByInstitute(1);
		assertEquals(3, ratings.size());
		assertEquals(4, ratings.get(0));	
	}
	
	@Test
	public void testUpdateReview() {
//		Review review = dao.findById(3);
		ReviewAddUpdateDTO review = new ReviewAddUpdateDTO("member4@gmail.com",(long) 4,(short) 3, "this is the rating for this institute, it can be anything right now");
		assertEquals(true, dao.updateReview(3, review, 1));
	}
	
	//failed, Caused by: org.hibernate.PersistentObjectException: detached entity passed to persist: com.brainsinjars.projectbackend.pojo.Member
	@Test
	public void testAddReview() {
		ReviewAddUpdateDTO review = new ReviewAddUpdateDTO("member4@gmail.com",(long) 4,(short) 3, "this is the rating for this institute, it can be anything right now");
		assertEquals(true, dao.addReview(review, 1));
	}
	
	//failed test, lazy initialization due to 
	@Test
	//@RolesAllowed(Role.ROOT)
	public void testFindAllReviews() throws RecordNotFoundException {
		List<ReviewDTO> reviews = dao.findAllReviews(1);
		
		Institute institute = instituteDao.findById(1);
		
		reviews.forEach(review -> institute.addReview(new Review(review.getRating(), review.getContent(), memberDao.findById(review.getId()), institute)));
		assertEquals(3, reviews.size());
		
		ReviewDTO review = reviews.get(0);
		
		assertEquals(1, review.getId());
		assertEquals("Institute Admin", review.getName());
		assertEquals("4", review.getRating());
		assertEquals("this is the content of the review, it can be anything right now............",
					review.getContent());
		assertEquals("2020-12-11 02:38:05", review.getCreatedDateTime().toString());
		assertEquals("2020-12-11 02:38:05", review.getModifiedDateTime());		
	}
	
}
