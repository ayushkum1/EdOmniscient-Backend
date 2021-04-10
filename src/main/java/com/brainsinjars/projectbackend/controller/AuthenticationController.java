package com.brainsinjars.projectbackend.controller;

import com.brainsinjars.projectbackend.dto.*;
import com.brainsinjars.projectbackend.exceptions.InvalidCredentialsException;
import com.brainsinjars.projectbackend.exceptions.UserNotFoundException;
import com.brainsinjars.projectbackend.pojo.User;
import com.brainsinjars.projectbackend.repository.UserRepository;
import com.brainsinjars.projectbackend.service.EmailService;
import com.brainsinjars.projectbackend.service.impl.UserDetailsServiceImpl;
import com.brainsinjars.projectbackend.util.JwtUtil;
import com.brainsinjars.projectbackend.util.ResponseUtils;
import com.brainsinjars.projectbackend.util.SecurityUtils;
import io.jsonwebtoken.JwtException;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author Nilesh Pandit
 * @since 10-03-2021
 */

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final UserDetailsServiceImpl userDetailsService;
    private final EmailService emailService;

    private final JwtUtil jwtUtil;
    private final String refreshTokenCookieName;

    private final UserRepository userRepository;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager,
                                    UserDetailsServiceImpl userDetailsService, EmailService emailService, JwtUtil jwtUtil,
                                    @Value("${jwt.refresh-token-cookie-name}") String refreshTokenCookieName, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.emailService = emailService;
        this.jwtUtil = jwtUtil;
        this.refreshTokenCookieName = refreshTokenCookieName;
        this.userRepository = userRepository;
    }


    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request, HttpServletResponse response) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            final UserDetails userDetails = userDetailsService
                    .loadUserByUsername(request.getUsername());
            final String jwt = jwtUtil.generateToken(userDetails);

            Cookie refreshTokenCookie = createRefreshTokenCookie(jwtUtil.generateRefreshToken(request.getUsername()));
            response.addCookie(refreshTokenCookie);

            return ResponseEntity.ok(new AuthenticationResponse(jwt));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageDto("Invalid Username or Password!", MessageType.ERROR));
        }
    }

    @GetMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = getJwtFromCookie(request);

        if (refreshToken != null) {
            Boolean isRefreshValid;
            try {
                isRefreshValid = jwtUtil.validateRefreshToken(refreshToken);
            } catch (JwtException e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().body("Invalid refresh token");
            }

            if (isRefreshValid) {
                String username = jwtUtil.extractUsername(refreshToken, true);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                String newRefreshToken = jwtUtil.generateRefreshToken(username);
                String accessToken = jwtUtil.generateToken(userDetails);
                Cookie refreshTokenCookie = createRefreshTokenCookie(newRefreshToken);
                response.addCookie(refreshTokenCookie);

                return ResponseEntity.ok(new AuthenticationResponse(accessToken));
            }
        }
        return ResponseEntity.badRequest().body("Invalid refresh token");
    }

    @PostMapping(
            value = "/forgot_password",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordDto dto) {
        // Check if user with this email exists in out DB
        // if yes, send email.
        // else, Dont
        String email = dto.getEmail();
        if (email != null) {
            boolean exists = userRepository.existsById(email);
            if (exists) {
                String resetToken = jwtUtil.generateResetToken(email);
                try {

//                System.out.println("AuthenticationController.forgotPassword: " + generateRandomPassword());
                    String emailBody = emailService.getEmailBody(resetToken);

                    emailService.sendEmail(new String[] {email}, "EdOmniscient Password Reset", emailBody, true);


                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }/* else {
                return ResponseUtils.badRequestResponse("No user found with email '" + email + "'");
            }*/

            return ResponseUtils.successResponse("You will receive an email with password reset link if this account exists");
        }
        return ResponseUtils.badRequestResponse("Invalid request");
    }

    @Transactional
    @GetMapping(
            value = "/reset_password",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> resetPassword(@RequestParam("token") String token) {
        try {
            if (token != null) {
                String email = Optional.ofNullable(
                        jwtUtil.verifyAndGetSubjectFromResetToken(token)
                ).orElseThrow(() -> new InvalidCredentialsException("Invalid token"));

                User user = userRepository.findById(email).orElseThrow(() -> new UserNotFoundException("User with email '" + email + "' not found"));
                String randomPassword = generateRandomPassword();
                user.setPasswdHash(SecurityUtils.getPasswordEncoder().encode(randomPassword));

                // Send email with new random password
                String emailBody = emailService.getResetSuccessEmailBody(randomPassword);
                emailService.sendEmail(new String[]{email}, "EdOmniscient Password Reset Success", emailBody, true);

                return ResponseEntity.ok("Check email");
            }

            return ResponseEntity.badRequest().body("Invalid token");
        } catch (InvalidCredentialsException | UserNotFoundException e) {
            return ResponseUtils.badRequestResponse(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unknown error occurred");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie logoutCookie = createLogoutCookie();
        response.addCookie(logoutCookie);
        return ResponseEntity.ok("Logged out!");
    }

    private String getJwtFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (refreshTokenCookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private Cookie createRefreshTokenCookie(String token) {

        Cookie refreshCookie = new Cookie(refreshTokenCookieName, token);
        refreshCookie.setPath("/");
        // uncomment this when we apply ssl
//        refreshCookie.setSecure(true);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setMaxAge(432000);    // lasts 7 days

        return refreshCookie;
    }

    private Cookie createLogoutCookie() {
        Cookie logoutCookie = new Cookie(refreshTokenCookieName, "");
        logoutCookie.setPath("/");
        logoutCookie.setHttpOnly(true);
        logoutCookie.setMaxAge(0);
        return logoutCookie;
    }

    private String generateRandom(int minCodePoint, int maxCodePoint) {
        RandomStringGenerator pwdGenerator = new RandomStringGenerator.Builder().withinRange(minCodePoint, maxCodePoint).build();
        String random = pwdGenerator.generate(8);
        return random;
    }

    private String generateRandomPassword() {
        Random randomIdx = new Random();
        int i = randomIdx.nextInt(8);

        char c1 = generateRandom(33, 45).charAt(randomIdx.nextInt(8));
        char c2 = generateRandom(33, 45).charAt(randomIdx.nextInt(8));

        String upper = generateRandom(65, 71).substring(4);
        String lower = generateRandom(97, 123).substring(2);

        return upper.concat(c1 + "").concat(lower).concat(c2 + "");
    }

}
