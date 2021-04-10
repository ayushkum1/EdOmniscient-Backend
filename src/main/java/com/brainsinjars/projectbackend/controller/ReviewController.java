/*
 * @Author Channappa Mirgale
 * @Since 09-03-21
 */

package com.brainsinjars.projectbackend.controller;

import com.brainsinjars.projectbackend.constant.HasRole;
import com.brainsinjars.projectbackend.dto.ReviewAddUpdateDTO;
import com.brainsinjars.projectbackend.dto.ReviewDTO;
import com.brainsinjars.projectbackend.service.ReviewService;
import com.brainsinjars.projectbackend.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/institutes/{instituteId}")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    public ReviewController() {
        System.out.print("In Review COntoller");
    }

//    @Autowired
//    public ReviewController(ReviewService reviewService) {
//        this.reviewService = remberService;
//    }

    //method to display the avaliable reviews
    
    @Secured({ HasRole.INSTITUTE_ADMIN, HasRole.MEMBER, HasRole.ROOT, HasRole.USER })
    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewDTO>> getAllReviews(@PathVariable long instituteId) {
        System.out.println("in all list reviews");
        //get list of all the Members
        List<ReviewDTO> reviews = reviewService.findAllReviews(instituteId);
        //check if the list is empty or not, if empty send a response code of no content
        if (reviews.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @Secured({ HasRole.INSTITUTE_ADMIN, HasRole.MEMBER, HasRole.ROOT, HasRole.USER })
    @GetMapping("/reviews/{email}")
    public ResponseEntity<?> getReviewByUserEmail(@PathVariable String email, @PathVariable String instituteId) {
        ReviewDTO userReview = reviewService.findByUserEmail(email);
        if (userReview != null)
            return ResponseEntity.ok(userReview);
        return ResponseUtils.notFoundResponse("Review not found!");
    }

    @Secured({ HasRole.INSTITUTE_ADMIN, HasRole.MEMBER, HasRole.ROOT,HasRole.USER})
    @GetMapping("/reviews/average-rating")
	public ResponseEntity<?> getAllRatingsByInstitute(@PathVariable String instituteId) {
		double ratings = reviewService.getAverageRating(instituteId);
		return ResponseEntity.ok(ratings);
	}

    //method to create the review
    @Secured({HasRole.MEMBER})
    @PostMapping("/reviews")
    public ResponseEntity<?> addReview(@RequestBody ReviewAddUpdateDTO review, @PathVariable long instituteId) {
        if (reviewService.addReview(review, instituteId))
            return ResponseEntity.ok("Record has been successfully added");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    //method to update the review
    @Secured({HasRole.MEMBER})
    @PutMapping("/reviews/{id}")
    public ResponseEntity<?> updateReview(@PathVariable(value = "id") long reviewId, @RequestBody ReviewAddUpdateDTO review, @PathVariable long instituteId) {
        if (reviewService.updateReview(reviewId, review, instituteId))
            return ResponseEntity.ok("Record has been successfully updated");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //method to delete the review using reviewId
    @Secured({HasRole.ROOT})
    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<String> deleteReview(@PathVariable("id") long reviewId, @PathVariable long instituteId) {
        if (reviewService.removeReview(reviewId, instituteId))
            return ResponseEntity.ok("Record successfully deleted");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }
}
