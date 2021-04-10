package com.brainsinjars.projectbackend.dto;

import com.brainsinjars.projectbackend.pojo.Location;

import java.util.Set;

/**
 * @author Nilesh Pandit
 * @since 06-03-2021
 */

public class InstitutesDto {
    private long instituteId;
    private String name;
    private String nick;
    private int reviewCount;
    private Set<CourseIdNameDTO> courses;
    private Location location;

    public InstitutesDto(long instituteId, String name, String nick, int reviewCount, Location location) {
        this.instituteId = instituteId;
        this.name = name;
        this.nick = nick;
        this.reviewCount = reviewCount;
        this.location = location;
    }

    public long getInstituteId() {
        return instituteId;
    }

    public void setInstituteId(long instituteId) {
        this.instituteId = instituteId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Set<CourseIdNameDTO> getCourses() {
        return courses;
    }

    public void setCourses(Set<CourseIdNameDTO> courses) {
        this.courses = courses;
    }
}
