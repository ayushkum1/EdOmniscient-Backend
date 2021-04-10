package com.brainsinjars.projectbackend.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
public class Review extends BaseEntity {

    @Digits(integer = 1, fraction = 0)
    @Positive(message = "Rating cannot be negative")
    @Max(value = 5)
    private short rating;

    @NotBlank
    @Lob
    @Length(min = 50, max = 2000, message = "Review content should have a length between 50 and 2000 characters")
    @Column(length = 2000)
    private String content;

    /**
     * OneToOne bidirectional mapping with member
     */
    @JsonIgnoreProperties("review")
    @OneToOne
    @MapsId
    @JoinColumn(name = "member_id")
    private Member member;

    /**
     * Bidirectional mapping to institute table
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institute_id", nullable = false)
    @JsonIgnore
    private Institute institute;

    @CreatedDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdDateTime;

    @LastModifiedDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime modifiedDateTime;

    // No-args constructor
    public Review() {
    }

    // All-args constructor
    public Review(@NotNull @Digits(integer = 1, fraction = 0) @Positive(message = "Rating cannot be negative") @Max(value = 5) short rating,
                  @Length(min = 50, max = 2000, message = "Review content should have a length between 50 and 2000 characters") String content,
                  Member member, Institute institute) {
        this.rating = rating;
        this.content = content;
        this.member = member;
        this.institute = institute;
    }

    public short getRating() {
        return rating;
    }

    public void setRating(short rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Institute getInstitute() {
        return institute;
    }

    public void setInstitute(Institute institute) {
        this.institute = institute;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public LocalDateTime getModifiedDateTime() {
        return modifiedDateTime;
    }

    public void setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
    }
}
