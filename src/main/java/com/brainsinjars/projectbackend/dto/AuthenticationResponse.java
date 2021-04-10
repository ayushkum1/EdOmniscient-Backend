package com.brainsinjars.projectbackend.dto;

/**
 * @author Nilesh Pandit
 * @since 10-03-2021
 */

public class AuthenticationResponse {
    private final String jwt;

    public AuthenticationResponse(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }
}
