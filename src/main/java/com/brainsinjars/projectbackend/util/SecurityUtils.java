package com.brainsinjars.projectbackend.util;

import com.brainsinjars.projectbackend.config.security.CurrentUser;
import com.brainsinjars.projectbackend.pojo.Role;
import com.brainsinjars.projectbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author Nilesh Pandit
 * @since 10-03-2021
 */

@Component
public final class SecurityUtils {

    private static UserRepository userRepository;
    private static PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityUtils(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        SecurityUtils.userRepository = userRepository;
        SecurityUtils.passwordEncoder = passwordEncoder;
    }

    public static boolean isUserLoggedIn() {
        Authentication authentication = getAuthentication();
        return authentication != null && !(isUserAnonymous(authentication));
    }

    public static boolean isUserAnonymous() {
        return isUserAnonymous(getAuthentication());
    }

    public static CurrentUser getCurrentUser() {
        Object principal = getAuthentication().getPrincipal();
        if (principal instanceof User) {
            User userDetails = (User) principal;
            return () -> userRepository.findById(userDetails.getUsername()).orElse(null);
        }
        return () -> null;
    }

    public static String getUsername() {
        Object principal = getAuthentication().getPrincipal();
        if (principal instanceof User) {
            return ((User) principal).getUsername();
        }
        return null;
    }

    public static boolean hasAccess(String username) {
        com.brainsinjars.projectbackend.pojo.User user = getCurrentUser().getUser();
        return user != null && (user.getRole().equals(Role.ROOT) || (user.getEmail().equals(username)));
    }

    public static PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    private static boolean isUserAnonymous(Authentication authentication) {
        return authentication instanceof AnonymousAuthenticationToken;
    }

    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
