package com.brainsinjars.projectbackend.controller;

import com.brainsinjars.projectbackend.constant.HasRole;
import com.brainsinjars.projectbackend.dto.ReviewDTO;
import com.brainsinjars.projectbackend.repository.ReviewRepository;
import com.brainsinjars.projectbackend.util.ResponseUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Nilesh Pandit
 * @Since: 25-03-2021
 */

@RestController
@RequestMapping("/reviews")
public class AllReviewsController {

    private final ReviewRepository repository;

    public AllReviewsController(ReviewRepository repository) {
        this.repository = repository;
    }

    @Secured(HasRole.ROOT)
    @GetMapping("")
    public ResponseEntity<?> getAllReviews() {
        List<ReviewDTO> allReviews = repository.findAllReviews();
        return ResponseEntity.ok(allReviews);
    }

    @Secured(HasRole.ROOT)
    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReviewById(@PathVariable String id) {
        try {
            repository.deleteUsingId(Long.parseLong(id));
            return ResponseUtils.successResponse("Review deleted successfully");
        } catch (NumberFormatException e) {
            return ResponseUtils.badRequestResponse(ResponseUtils.INVALID_ID_MESSAGE);
        }
    }
}
