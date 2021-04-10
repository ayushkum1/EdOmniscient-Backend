/* 
 * Created by Channappa Mirgale on 09-03-2021 
*/


package com.brainsinjars.projectbackend.dao;

import java.util.List;




import com.brainsinjars.projectbackend.dto.ReviewAddUpdateDTO;
import com.brainsinjars.projectbackend.dto.ReviewDTO;
import com.brainsinjars.projectbackend.exceptions.ReviewHandlingException;
import com.brainsinjars.projectbackend.pojo.Review;


public interface IReviewDao {
	List<ReviewDTO> findAllReviews(long instituteId) throws ReviewHandlingException;
    Review findById(long reviewId) throws ReviewHandlingException;
    ReviewDTO findByUserEmail(String email) throws ReviewHandlingException;
    List<Integer> findAllRatingsByInstitute(long instituteId);
 //   List<Review> getByInstitute(long instituteId) throws ReviewHandlingException;
    boolean addReview(ReviewAddUpdateDTO review,long instituteId);
	boolean updateReview(long reviewId,ReviewAddUpdateDTO review,long instituteId) throws ReviewHandlingException;
	boolean removeReview(long reviewId,long instituteId) throws ReviewHandlingException;
}
