package com.brainsinjars.projectbackend.pojo;

import com.brainsinjars.projectbackend.pojo.converter.YearAttributeConverter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.Year;

@Entity
@Table(name = "members")
public class Member extends BaseEntity {

    @NotNull
    @Enumerated(EnumType.STRING)
    private MemberType memberType;

    @JsonIgnoreProperties("institutes")
    @ManyToOne(optional = false)
    private Course course;

    @NotBlank
    @Pattern(
            regexp = "((\\+*)(0*|(0 )*|(0-)*|(91 )*)(\\d{12}+|\\d{10}+))|\\d{5}([- ]*)\\d{6}",
            message = "Invalid phone number"
    )
    private String publicPhone;

    @NotBlank
    @Email(message = "Invalid email")
    private String publicEmail;

    /**
     * The @Converter annotation specifies which converter to use
     * for converting the Java type to DB type and back.
     */
    @NotNull
    @Convert(converter = YearAttributeConverter.class)
    private Year year;

    // No cascade because review can be helpful to others
    // even if member is deleted.
    @JsonIgnoreProperties("member")
    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @PrimaryKeyJoinColumn
    private Review review;

    @NotBlank
    @Column(length = 20, nullable = false)
    private String prn;

    @NotNull
    @Enumerated(EnumType.STRING)
    private MembershipStatus status;

    @OneToOne(optional = false)
    private User user;

    /**
     * One institute and have many members,
     * hence, we map a bi-directional OneToMany relationship
     * ### Check Institute Entity ###
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institute_id", nullable = false)
    @JsonIgnoreProperties({"members", "courses", "reviews"})
    private Institute institute;

    // No-args constructor
    public Member() {
    }

    // All-args constructor
    public Member(@NotNull MemberType memberType, Course course,
                  @NotBlank @Pattern(
                          regexp = "((\\+*)(0*|(0 )*|(0-)*|(91 )*)(\\d{12}+|\\d{10}+))|\\d{5}([- ]*)\\d{6}",
                          message = "Invalid phone number") String publicPhone,
                  @NotBlank @Email(
                          regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$",
                          message = "Invalid email") String publicEmail,
                  @NotBlank Year year, Review review, @NotBlank String prn, @NotNull MembershipStatus status, User user, Institute institute) {
        this.memberType = memberType;
        this.course = course;
        this.publicPhone = publicPhone;
        this.publicEmail = publicEmail;
        this.year = year;
        this.review = review;
        this.prn = prn;
        this.status = status;
        this.user = user;
        this.institute = institute;
    }

    public Course getCourse() {
        return course;
    }

    public MemberType getMemberType() {
        return memberType;
    }

    public void setMemberType(MemberType memberType) {
        this.memberType = memberType;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getPublicPhone() {
        return publicPhone;
    }

    public void setPublicPhone(String publicPhone) {
        this.publicPhone = publicPhone;
    }

    public String getPublicEmail() {
        return publicEmail;
    }

    public void setPublicEmail(String publicEmail) {
        this.publicEmail = publicEmail;
    }

    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public String getPrn() {
        return prn;
    }

    public void setPrn(String prn) {
        this.prn = prn;
    }

    public MembershipStatus getStatus() {
        return status;
    }

    public void setStatus(MembershipStatus status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Institute getInstitute() {
        return institute;
    }

    public void setInstitute(Institute institute) {
        this.institute = institute;
    }
}
