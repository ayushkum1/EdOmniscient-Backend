package com.brainsinjars.projectbackend.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "companies")
public class Company {
    @Id
    private long id;

    @Column(length = 20)
    private String name;

    @NotBlank
    private String websiteLink;

    @NotBlank
    private String logoPath;

    /**
     * Many companies visit many institutes during placements.
     * So, we use a many to many mapping from company to institute and vice-versa
     */
    @Column(nullable = false)
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "inst_comp_join_table",
            joinColumns = @JoinColumn(name = "company_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "institute_id", nullable = false)
    )
    @JsonIgnoreProperties("companies")
    private Set<Institute> institutes;

    // No-args constructor
    public Company() {
        this.institutes = new HashSet<>();
    }

    // A constructor which instantiates all fields.
    // Here the Set<Institute> is initialized to empty set
    public Company(long id, String name, @NotBlank String websiteLink, @NotBlank String logoPath) {
        this();
        this.id = id;
        this.name = name;
        this.websiteLink = websiteLink;
        this.logoPath = logoPath;
    }

    // All-args constructor
    public Company(long id, String name, @NotBlank String websiteLink, @NotBlank String logoPath, Set<Institute> institutes) {
        this.id = id;
        this.name = name;
        this.websiteLink = websiteLink;
        this.logoPath = logoPath;
        this.institutes = institutes;
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

    public String getWebsiteLink() {
        return websiteLink;
    }

    public void setWebsiteLink(String websiteLink) {
        this.websiteLink = websiteLink;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public Set<Institute> getInstitutes() {
        return institutes;
    }

    public void setInstitutes(Set<Institute> institutes) {
        this.institutes = institutes;
    }

    /**
     * Adder and remover of Institutes -
     * used when we need to add or remove only one institute from the set
     */
    public boolean addInstitute(Institute institute) {
        return this.institutes.add(institute);
    }

    public boolean removeInstitute(Institute institute) {
        return this.institutes.remove(institute);
    }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", websiteLink='" + websiteLink + '\'' +
                ", logoPath='" + logoPath + '\'' +
                ", institutes=" + institutes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Company)) return false;
        Company company = (Company) o;
        return id == company.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
