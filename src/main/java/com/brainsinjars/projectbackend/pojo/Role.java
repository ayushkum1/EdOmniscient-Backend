package com.brainsinjars.projectbackend.pojo;

/**
 * Different roles a user can have
 */
public class Role {
    public static final String ROOT = "ROOT";
    public static final String MEMBER = "MEMBER";
    public static final String INSTITUTE_ADMIN = "INSTITUTE_ADMIN";
    public static final String USER = "USER";

    public static String[] getAllRoles() {
        return new String[] {ROOT, MEMBER, INSTITUTE_ADMIN, USER};
    }
}
