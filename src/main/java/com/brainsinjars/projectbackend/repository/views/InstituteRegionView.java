package com.brainsinjars.projectbackend.repository.views;

import java.util.Objects;

/**
 * @author Nilesh Pandit
 * @since 06-03-2021
 */

/*
 * This is a projection
 */
public class InstituteRegionView {
    private Long id;
    private String name;
    private String region;

    public InstituteRegionView(Long id, String name, String region) {
        this.id = id;
        this.name = name;
        this.region = region;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public String toString() {
        return "InstituteRegionView{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", region='" + region + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InstituteRegionView)) return false;
        InstituteRegionView that = (InstituteRegionView) o;
        return region.equals(that.region);
    }

    @Override
    public int hashCode() {
        return Objects.hash(region);
    }
}
