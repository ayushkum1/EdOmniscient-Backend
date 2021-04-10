/* 
 * @Author Channappa Mirgale 
 * @Since 09-03-21
*/
package com.brainsinjars.projectbackend.service;
import java.util.List;
import java.util.OptionalDouble;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.brainsinjars.projectbackend.dao.IReviewDao;
import com.brainsinjars.projectbackend.dto.ReviewAddUpdateDTO;
import com.brainsinjars.projectbackend.dto.ReviewDTO;
import com.brainsinjars.projectbackend.exceptions.ReviewHandlingException;
import com.brainsinjars.projectbackend.pojo.Review;

@Service
@Transactional
public class ReviewService {
	@Autowired
	private IReviewDao reviewDao;

	
//	public ReviewService(IReviewDao reviewDao) {
//		this.reviewDao = reviewDao;
//	}

	// method returns a list of all the reviews
	public List<ReviewDTO> findAllReviews(long instituteId) {
		return reviewDao.findAllReviews(instituteId);
	}

	// method to adds review //content,createdtime
	public boolean addReview(ReviewAddUpdateDTO review,long instituteId)
	{
		try {
			return reviewDao.addReview(review,instituteId);
		} catch (ReviewHandlingException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	// method updates a review details
	public boolean updateReview(long reviewId, ReviewAddUpdateDTO review,long instituteId) {
		try {
			return reviewDao.updateReview(reviewId, review,instituteId);
		} catch (ReviewHandlingException e) {
			e.printStackTrace();
		}
		return false;
	}

	// method delete a review
	public boolean removeReview(long reviewId,long instituteId)
	{
		try {
			return reviewDao.removeReview(reviewId,instituteId);
		} catch (ReviewHandlingException u) {
			u.printStackTrace();
		}
		return false;
	}

	/* this method finds the single member */
	public Review findById(long reviewId) {
		Review r = null;
		try {
			r = reviewDao.findById(reviewId);
		} catch (ReviewHandlingException u) {
			u.printStackTrace();
		}
		return r;
	}

	public ReviewDTO findByUserEmail(String email) {
		try {
			return reviewDao.findByUserEmail(email);
		} catch (ReviewHandlingException e) {
			return null;
		}
	}

	public double getAverageRating(String instituteId) {
		try {
			List<Integer> ratings = reviewDao.findAllRatingsByInstitute(Long.parseLong(instituteId));
			OptionalDouble average = ratings.stream().mapToInt(value -> value).average();
			return average.orElse(0);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

}
