package com.brainsinjars.projectbackend.dto;

/**
 * @Author: Nilesh Pandit
 * @Since: 25-03-2021
 */


public class ForgotPasswordDto {
    private String email;

    public ForgotPasswordDto() {
    }

    public ForgotPasswordDto(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
