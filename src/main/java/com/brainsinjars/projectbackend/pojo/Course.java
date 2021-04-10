package com.brainsinjars.projectbackend.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.*;

@Entity
@Table(name = "courses")
public class Course {
    @Id
    private long id;

    @NotBlank
    @Column(length = 15)
    private String name;

    @NotBlank
    @Lob
    @Length(min = 50, max = 2000, message = "Description should have a length between 50 and 2000 characters")
    @Column(length = 2000)
    private String description;

    @NotBlank
    private String photoUrl;

    @Positive(message = "Fees cannot be negative")
    @Digits(integer = 7, fraction = 2)
    private double fees;

    @Positive(message = "Duration cannot be negative")
    @Digits(integer = 2, fraction = 0)
    private short duration;

    /**
     * Many courses can be offered by many institutes.
     * So, we use a many to many mapping from course to institute and vice-versa
     */
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "inst_course_join_table",
            joinColumns = @JoinColumn(name = "course_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "institute_id", nullable = false)
    )
    @JsonIgnoreProperties("courses")
    private Set<Institute> institutes;

    /**
     * One Course can have many placements in many Institutes.
     * This OneToMany mapping is established after splitting a ManyToMany Placements relationship
     * between Institute and Courses
     * ### See Placements table as it is a Join Table between Institute and Course ###
     */
    @OneToMany(mappedBy = "course")
    private List<Placement> placements;

    // No-args constructor
    public Course() {
        this.institutes = new HashSet<>();
        this.placements = new ArrayList<>();
    }

    // A constructor which instantiates all fields.
    // Here the Collections are initialized to be empty collections
    public Course(long id, @NotBlank String name,
                  @NotBlank @Length(min = 50, max = 2000, message = "Description should have a length between 50 and 2000 characters") String description,
                  @NotBlank String photoUrl, @Positive(message = "Fees cannot be negative") @Digits(integer = 7, fraction = 2) double fees,
                  @Positive(message = "Duration cannot be negative") @Digits(integer = 2, fraction = 0) short duration) {
        this();
        this.id = id;
        this.name = name;
        this.description = description;
        this.photoUrl = photoUrl;
        this.fees = fees;
        this.duration = duration;
    }

    // All-args constructor
    public Course(long id, @NotBlank String name,
                  @NotBlank @Length(min = 50, max = 2000, message = "Description should have a length between 50 and 2000 characters") String description,
                  @NotBlank String photoUrl, @Positive(message = "Fees cannot be negative") @Digits(integer = 7, fraction = 2) double fees,
                  @Positive(message = "Duration cannot be negative") @Digits(integer = 2, fraction = 0) short duration,
                  Set<Institute> institutes, List<Placement> placements) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.photoUrl = photoUrl;
        this.fees = fees;
        this.duration = duration;
        this.institutes = institutes;
        this.placements = placements;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public double getFees() {
        return fees;
    }

    public void setFees(double fees) {
        this.fees = fees;
    }

    public short getDuration() {
        return duration;
    }

    public void setDuration(short duration) {
        this.duration = duration;
    }

    public Set<Institute> getInstitutes() {
        return institutes;
    }

    public void setInstitutes(Set<Institute> institutes) {
        this.institutes = institutes;
    }

    //may be removed later as we do not require these helpers
    /**
     * Adder and remover of Institutes -
     * used when we need to add or remove only one institute from the collection
     */
    public boolean addInstitute(Institute institute) {
        return this.institutes.add(institute);
    }

    public boolean removeInstitute(Institute institute) {
        return this.institutes.remove(institute);
    }

    public List<Placement> getPlacements() {
        return placements;
    }

    public void setPlacements(List<Placement> placements) {
        this.placements = placements;
    }

    /**
     * Adder and remover
     */
    public boolean addPlacement(Placement placement) {
        return this.placements.add(placement);
    }

    public boolean removePlacement(Placement placement) {
        return this.placements.remove(placement);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;
        Course course = (Course) o;
        return id == course.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
}
