package com.brainsinjars.projectbackend.dto;

/**
 * @Author: Nilesh Pandit
 * @Since: 25-03-2021
 */

public class PasswordChangeDto {
    private String oldPassword;
    private String newPassword;

    public PasswordChangeDto() {
    }

    public PasswordChangeDto(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
