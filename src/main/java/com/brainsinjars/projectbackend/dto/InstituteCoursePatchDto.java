package com.brainsinjars.projectbackend.dto;

import java.util.Set;

/**
 * @author Nilesh Pandit
 * @since 10-03-2021
 */

public class InstituteCoursePatchDto {
    private String op;
    private Set<String> courses;

    public InstituteCoursePatchDto(String op, Set<String> courses) {
        this.op = op;
        this.courses = courses;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public Set<String> getCourses() {
        return courses;
    }

    public void setCourses(Set<String> courses) {
        this.courses = courses;
    }
}
