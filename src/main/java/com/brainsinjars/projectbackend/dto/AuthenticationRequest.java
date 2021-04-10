package com.brainsinjars.projectbackend.dto;

/**
 * @author Nilesh Pandit
 * @since 10-03-2021
 */

public class AuthenticationRequest {
    private String username;
    private String password;

    // needed for serialization
    public AuthenticationRequest() {
    }

    public AuthenticationRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
