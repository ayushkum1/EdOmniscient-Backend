package com.brainsinjars.projectbackend.controller;

import com.brainsinjars.projectbackend.constant.HasRole;
import com.brainsinjars.projectbackend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Nilesh Pandit
 * @Since: 25-03-2021
 */

@RestController
@RequestMapping("/counts")
public class CountController {

    private final UserRepository userRepository;
    private final InstituteRepository instituteRepository;
    private final CourseRepository courseRepository;
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public CountController(UserRepository userRepository,
                           InstituteRepository instituteRepository,
                           CourseRepository courseRepository,
                           ReviewRepository reviewRepository,
                           MemberRepository memberRepository) {
        this.userRepository = userRepository;
        this.instituteRepository = instituteRepository;
        this.courseRepository = courseRepository;
        this.reviewRepository = reviewRepository;
        this.memberRepository = memberRepository;
    }

    @Secured(HasRole.ROOT)
    @GetMapping("/users")
    public Long getUserCount() {
        return userRepository.count();
    }

    @Secured(HasRole.ROOT)
    @GetMapping("/institutes")
    public Long getInstituteCount() {
        return instituteRepository.count();
    }

    @Secured(HasRole.ROOT)
    @GetMapping("/courses")
    public Long getCourseCount() {
        return courseRepository.count();
    }

    @Secured(HasRole.ROOT)
    @GetMapping("/reviews")
    public Long getReviewCount() {
        return reviewRepository.count();
    }

    @Secured(HasRole.ROOT)
    @GetMapping("/members")
    public Long getMemberCount() {
        return memberRepository.count();
    }
}
