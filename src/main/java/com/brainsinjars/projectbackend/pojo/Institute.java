package com.brainsinjars.projectbackend.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.*;

@Entity
@Table(name = "institutes")
public class Institute {
    @Id
    @Column(name = "institute_id")
    private long id;

    @NotBlank
    @Length(min = 3, max = 100, message = "Name should have a length between 3 and 100 characters")
    @Column(length = 100)
    private String name;

    @Column(length = 100)
    private String nick;

    @NotBlank
    @Lob
    @Length(min = 50, max = 2000, message = "Description should have a length between 50 and 2000 characters")
    @Column(length = 2000)
    private String about;

    @NotBlank
    @Lob
    @Length(min = 50, max = 2000, message = "Description should have a length between 50 and 2000 characters")
    @Column(length = 2000)
    private String aboutPlacements;

    @NotBlank
    private String profilePicUrl;

    @NotBlank
    private String coverPicUrl;

    /**
     * OneToOne unidirectional mapping to locations table
     */
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, optional = false)
    private Location location;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = false, optional = false)
    @JoinColumn(referencedColumnName = "email")
    private User instituteAdmin;

    /**
     * Many courses can be offered by many institutes.
     * So, we use a many to many mapping from institute to course and vice-versa
     * The @JoinTable annotation describes the association between the tables,
     * and it is need here because Institute is on the owning side of the relationship.
     */
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "inst_course_join_table",
            joinColumns = @JoinColumn(name = "institute_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "course_id", nullable = false)
    )
    @JsonIgnoreProperties({"institutes", "placements"})
    private Set<Course> courses;

    /**
     * One institute and have many members,
     * hence, we map a bi-directional OneToMany relationship
     */
    @OneToMany(mappedBy = "institute", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Member> members;

    /**
     * One institute and have many media,
     * hence, we map a bi-directional OneToMany relationship
     */
    @OneToMany(mappedBy = "institute", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Media> media;

    /**
     * One institute and have many academicCalendars,
     * hence, we map a bi-directional OneToMany relationship
     */
    @OneToMany(mappedBy = "institute", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AcademicCalendar> academicCalendars;

    /**
     * One institute and have many reviews,
     * hence, we map a bi-directional OneToMany relationship
     */
    @OneToMany(mappedBy = "institute", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    /**
     * One institute can have many placements in many courses.
     * This OneToMany mapping is established after splitting a ManyToMany Placements relationship
     * between Institute and Courses
     * ### See Placements table as it is a Join Table between Institute and Course ###
     */
    @OneToMany(mappedBy = "institute", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Placement> placements;

    /**
     * Many companies visit many institutes during placements.
     * So, we use a many to many mapping from institute to company and vice-versa.
     * The @JoinTable annotation describes the association between the tables,
     * and it is need here because Institute is on the owning side of the relationship.
     */
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "inst_comp_join_table",
            joinColumns = @JoinColumn(name = "institute_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "company_id", nullable = false)
    )
    @JsonIgnoreProperties("institutes")
    private Set<Company> companies;

    // No-args constructor
    // Instantiates all Collections to be empty
    public Institute() {
        this.courses = new HashSet<>();
        this.members = new ArrayList<>();
        this.media = new ArrayList<>();
        this.academicCalendars = new ArrayList<>();
        this.reviews = new ArrayList<>();
        this.placements = new ArrayList<>();
        this.companies = new HashSet<>();
    }

    // A constructor which instantiates all fields.
    // Here the Collections are initialized to be empty collections
    public Institute(long id,
                     @NotBlank @Length(min = 3, max = 100, message = "Name should have a length between 3 and 100 characters") String name, String nick,
                     @NotBlank @Length(min = 50, max = 2000, message = "Description should have a length between 50 and 2000 characters") String about,
                     @NotBlank @Length(min = 50, max = 2000, message = "Description should have a length between 50 and 2000 characters") String aboutPlacements,
                     @NotBlank String profilePicUrl, @NotBlank String coverPicUrl, Location location, User instituteAdmin) {
        this();
        this.id = id;
        this.name = name;
        this.nick = nick;
        this.about = about;
        this.aboutPlacements = aboutPlacements;
        this.profilePicUrl = profilePicUrl;
        this.coverPicUrl = coverPicUrl;
        this.location = location;
        this.instituteAdmin = instituteAdmin;
    }

    // All-args constructor
    public Institute(long id, @NotBlank @Length(min = 3, max = 100, message = "Name should have a length between 3 and 100 characters") String name, String nick, @NotBlank @Length(min = 50, max = 2000, message = "Description should have a length between 50 and 2000 characters") String about, @NotBlank @Length(min = 50, max = 2000, message = "Description should have a length between 50 and 2000 characters") String aboutPlacements, @NotBlank String profilePicUrl, @NotBlank String coverPicUrl, Location location, User instituteAdmin, Set<Course> courses, List<Member> members, List<Media> media, List<AcademicCalendar> academicCalendars, List<Review> reviews, List<Placement> placements, Set<Company> companies) {
        this.id = id;
        this.name = name;
        this.nick = nick;
        this.about = about;
        this.aboutPlacements = aboutPlacements;
        this.profilePicUrl = profilePicUrl;
        this.coverPicUrl = coverPicUrl;
        this.location = location;
        this.instituteAdmin = instituteAdmin;
        this.courses = courses;
        this.members = members;
        this.media = media;
        this.academicCalendars = academicCalendars;
        this.reviews = reviews;
        this.placements = placements;
        this.companies = companies;
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

    public String getAboutPlacements() {
        return aboutPlacements;
    }

    public void setAboutPlacements(String aboutPlacements) {
        this.aboutPlacements = aboutPlacements;
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

    public User getInstituteAdmin() {
        return instituteAdmin;
    }

    public void setInstituteAdmin(User instituteAdmin) {
        this.instituteAdmin = instituteAdmin;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    /**
     * Adder and remover
     */
    public boolean addCourse(Course course) {
        return this.courses.add(course);
    }

    public boolean removeCourse(Course course) {
        return this.courses.remove(course);
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    /**
     * Adder and remover
     */
    public boolean addMember(Member member) {
        return this.members.add(member);
    }

    public boolean removeMember(Member member) {
        return this.members.remove(member);
    }

    public List<Media> getMedia() {
        return media;
    }

    public void setMedia(List<Media> media) {
        this.media = media;
    }

    /**
     * Adder and remover
     */
    public boolean addMedia(Media media) {
        return this.media.add(media);
    }

    public boolean removeMedia(Media media) {
        return this.media.remove(media);
    }

    public List<AcademicCalendar> getAcademicCalendars() {
        return academicCalendars;
    }

    public void setAcademicCalendars(List<AcademicCalendar> academicCalendars) {
        this.academicCalendars = academicCalendars;
    }

    /**
     * Adder and remover
     */
    public boolean addAcademicCalendar(AcademicCalendar academicCalendars) {
        return this.academicCalendars.add(academicCalendars);
    }

    public boolean removeAcademicCalendar(AcademicCalendar academicCalendars) {
        return this.academicCalendars.remove(academicCalendars);
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    /**
     * Adder and remover
     */
    public boolean addReview(Review review) {
        return this.reviews.add(review);
    }

    public boolean removeReview(Review review) {
        return this.reviews.remove(review);
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

    public Set<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(Set<Company> companies) {
        this.companies = companies;
    }

    /**
     * Adder and remover
     */
    public boolean addCompany(Company company) {
        return this.companies.add(company);
    }

    public boolean removeCompany(Company company) {
        return this.companies.remove(company);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Institute)) return false;
        Institute institute = (Institute) o;
        return id == institute.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
