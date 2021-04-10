/* 
 * @Author Channappa Mirgale 
 * @Since 09-03-21
*/

package com.brainsinjars.projectbackend.dao.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.brainsinjars.projectbackend.dao.IReviewDao;
import com.brainsinjars.projectbackend.dto.ReviewAddUpdateDTO;
import com.brainsinjars.projectbackend.dto.ReviewDTO;
import com.brainsinjars.projectbackend.exceptions.MemberHandlingException;
import com.brainsinjars.projectbackend.exceptions.ReviewHandlingException;
import com.brainsinjars.projectbackend.pojo.Institute;
import com.brainsinjars.projectbackend.pojo.Member;
import com.brainsinjars.projectbackend.pojo.Review;
import com.brainsinjars.projectbackend.pojo.User;
import com.brainsinjars.projectbackend.repository.InstituteRepository;
import com.brainsinjars.projectbackend.repository.MemberRepository;
import com.brainsinjars.projectbackend.repository.ReviewRepository;

@Component
public class ReviewDaoImplement implements IReviewDao {

	private ReviewRepository reviewRepository;
	private MemberRepository memberRepository;
	private InstituteRepository instituteRepository;
	

	@Autowired
	public ReviewDaoImplement(ReviewRepository reviewRepository, MemberRepository memberRepository,
			InstituteRepository instituteRepository) {
		this.reviewRepository = reviewRepository;
		this.memberRepository = memberRepository;
		
		this.instituteRepository = instituteRepository;
	}

	@Override
	public List<ReviewDTO> findAllReviews(long instituteId) {
		Institute institute = instituteRepository.findById(instituteId)
				.orElseThrow(() -> new MemberHandlingException("Institute Not found"));
		List<ReviewDTO> reviewDTO = new ArrayList<>();
		institute.getReviews().forEach(review -> {
			User u = review.getMember().getUser();
			ReviewDTO rd = new ReviewDTO();
			BeanUtils.copyProperties(review, rd);
			rd.setCourseName(review.getMember().getCourse().getName());
			 //this will give me the first name and last name of User
			rd.setName(u.getFirstName() + " " + u.getLastName());
			reviewDTO.add(rd);
		});
		return reviewDTO;
	}

	@Override
	public Review findById(long reviewId) throws ReviewHandlingException {
		return reviewRepository.findById(reviewId)
				.orElseThrow(() -> new ReviewHandlingException("Member Not Found!!!"));
	}

	@Override
	public ReviewDTO findByUserEmail(String email) throws ReviewHandlingException {
		return reviewRepository.findUsingUserEmail(email)
				.orElseThrow(() -> new ReviewHandlingException("Review not found!"));
	}

	@Override
	public List<Integer> findAllRatingsByInstitute(long instituteId) {
		return reviewRepository.findAllRatingsByInstitute(instituteId);
	}

//	@Override
//	public List<Review> getByInstitute(long instituteId) throws ReviewHandlingException {
//		return reviewRepository.getByInstitute(instituteId)
//				.orElseThrow(() -> new ReviewHandlingException("Details of Member not found"));
//
//	}

	@Override
	public boolean addReview(ReviewAddUpdateDTO review, long instituteId) {
		Institute institute = instituteRepository.findById(instituteId)
				.orElseThrow(() -> new ReviewHandlingException("Institute Not Found"));
		Review newReview = new Review();
		Member member = memberRepository.findById(review.getMemberId())
				.orElseThrow(() -> new MemberHandlingException("Member Not Found"));
		BeanUtils.copyProperties(review, newReview);
		newReview.setMember(member);
		newReview.setInstitute(institute);
		System.out.println(review);
		newReview.setCreatedDateTime(LocalDateTime.now());
		if (reviewRepository.save(newReview) != null)
			return true;

		return false;

	}

	@Override
	public boolean updateReview(long reviewId, ReviewAddUpdateDTO review,long instituteId) throws ReviewHandlingException {
		Review reviewOP = reviewRepository.findById(reviewId)
				.orElseThrow(() -> new ReviewHandlingException("Review not found"));
		if (reviewOP.getInstitute().getId() == instituteId) 
		{
		BeanUtils.copyProperties(review, reviewOP);
		// used so when we create new review it will automatically adds the modified current date and time
		reviewOP.setModifiedDateTime(LocalDateTime.now());
		return true;
		}
		return false;
	}

	@Override
	public boolean removeReview(long reviewId,long instituteId) throws ReviewHandlingException {
		 Review review = reviewRepository.findById(reviewId)
		 .orElseThrow(() -> new ReviewHandlingException("Review ID Not Found!!"));
			if (review.getInstitute().getId() == instituteId)
			{
				reviewRepository.deleteById(reviewId);
			return true;
		}
		return false;
	}

}

