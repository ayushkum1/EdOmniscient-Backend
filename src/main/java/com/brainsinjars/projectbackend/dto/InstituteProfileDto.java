package com.brainsinjars.projectbackend.dto;

import com.brainsinjars.projectbackend.pojo.Location;

/**
 * @author Nilesh Pandit
 * @since 10-03-2021
 */

public class InstituteProfileDto {
    private long instituteId;
    private String name;
    private String nick;
    private String about;
    private String aboutPlacements;
    private String profilePicUrl;
    private String coverPicUrl;
    private Location location;
    private String instituteAdmin;

    public InstituteProfileDto() {
    }

    public InstituteProfileDto(long instituteId, String name, String nick, String about, String aboutPlacements, String profilePicUrl, String coverPicUrl, Location location, String instituteAdmin) {
        this.instituteId = instituteId;
        this.name = name;
        this.nick = nick;
        this.about = about;
        this.aboutPlacements = aboutPlacements;
        this.profilePicUrl = profilePicUrl;
        this.coverPicUrl = coverPicUrl;
        this.location = location;
        this.instituteAdmin = instituteAdmin;
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

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getCoverPicUrl() {
        return coverPicUrl;
    }

    public void setCoverPicUrl(String coverPicUrl) {
        this.coverPicUrl = coverPicUrl;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getInstituteAdmin() {
        return instituteAdmin;
    }

    public void setInstituteAdmin(String instituteAdmin) {
        this.instituteAdmin = instituteAdmin;
    }

    public String getAboutPlacements() {
        return aboutPlacements;
    }

    public void setAboutPlacements(String aboutPlacements) {
        this.aboutPlacements = aboutPlacements;
    }
}
