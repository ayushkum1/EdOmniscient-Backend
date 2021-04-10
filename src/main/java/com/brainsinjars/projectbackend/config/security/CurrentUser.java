package com.brainsinjars.projectbackend.config.security;

import com.brainsinjars.projectbackend.pojo.User;

@FunctionalInterface
public interface CurrentUser {
    User getUser();
}
