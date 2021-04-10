package com.brainsinjars.projectbackend.pojo;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(nullable = false)
    @Email(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = "Invalid email")
    private String email;

    @Column(nullable = false)
    private String passwdHash;

    @NotNull
//    @Enumerated(EnumType.STRING)
    @Column(length = 15, nullable = false)
    private String role;

    @NotBlank
    @Column(length = 30, nullable = false)
    private String firstName;

    @NotBlank
    @Column(length = 30, nullable = false)
    private String lastName;

    @Lob
    @Column(length = 2000)
    private String about;

    private String fbProfile;

    private String linkedinProfile;

    private String profilePicLink;

    /**
     * OneToOne unidirectional mapping to locations table
     */
    @NotNull
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, optional = false)
    private Location location;

    // No-args constructor
    public User() {
    }

    // All-args constructor
    public User(@Email(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = "Invalid email") String email, String passwdHash, @NotBlank String role, @NotBlank String firstName, @NotBlank String lastName, String about, String fbProfile, String linkedinProfile, String profilePicLink, @NotNull Location location) {
        this.email = email;
        this.passwdHash = passwdHash;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.about = about;
        this.fbProfile = fbProfile;
        this.linkedinProfile = linkedinProfile;
        this.profilePicLink = profilePicLink;
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswdHash() {
        return passwdHash;
    }

    public void setPasswdHash(String passwdHash) {
        this.passwdHash = passwdHash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getFbProfile() {
        return fbProfile;
    }

    public void setFbProfile(String fbProfile) {
        this.fbProfile = fbProfile;
    }

    public String getLinkedinProfile() {
        return linkedinProfile;
    }

    public void setLinkedinProfile(String linkedinProfile) {
        this.linkedinProfile = linkedinProfile;
    }

    public String getProfilePicLink() {
        return profilePicLink;
    }

    public void setProfilePicLink(String profilePicLink) {
        this.profilePicLink = profilePicLink;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", passwdHash='" + passwdHash + '\'' +
                ", role=" + role +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", about='" + about + '\'' +
                ", fbProfile='" + fbProfile + '\'' +
                ", linkedinProfile='" + linkedinProfile + '\'' +
                ", profilePicLink='" + profilePicLink + '\'' +
                ", location=" + location +
                '}';
    }
}
