package com.brainsinjars.projectbackend.pojo;

import javax.persistence.*;

@Entity
@Table(name = "locations")
public class Location extends BaseEntity {

    @Column(length = 200)
    private String streetAddr;

    /**
     * A ManyToOne unidirectional mapping with geography as many locations can have same geography
     */
    @ManyToOne(optional = false)
    private Geography geography;

    // No-args constructor
    public Location() {
    }

    // All-args constructor
    public Location(String streetAddr, Geography geography) {
        this.streetAddr = streetAddr;
        this.geography = geography;
    }

    public String getStreetAddr() {
        return streetAddr;
    }

    public void setStreetAddr(String streetAddr) {
        this.streetAddr = streetAddr;
    }

    public Geography getGeography() {
        return geography;
    }

    public void setGeography(Geography geography) {
        this.geography = geography;
    }

    @Override
    public String toString() {
        return "Location{" +
                "streetAddr='" + streetAddr + '\'' +
                ", geography=" + geography +
                "} " + super.toString();
    }
}
