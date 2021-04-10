package com.brainsinjars.projectbackend.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "geographies")
public class Geography extends BaseEntity {

    @NotBlank
    @Column(length = 20)
    private String city;

    @NotBlank
    @Column(length = 20)
    private String state;

    @NotBlank
    @Column(length = 6)
    private String pinCode;

    @NotBlank
    @Column(length = 20)
    private String region;

    // No-args constructor
    public Geography() {
    }

    // All-args constructor
    public Geography(String city, String state, String pinCode, String region) {
        this.city = city;
        this.state = state;
        this.pinCode = pinCode;
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public String toString() {
        return "Geography{" +
                "city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", pinCode='" + pinCode + '\'' +
                ", region='" + region + '\'' +
                "} " + super.toString();
    }
}
