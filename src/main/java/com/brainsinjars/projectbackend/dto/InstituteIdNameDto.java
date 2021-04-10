package com.brainsinjars.projectbackend.dto;

public class InstituteIdNameDto {
    private long id;
    private String name;
    private String region;

    public InstituteIdNameDto() {
    }

    public InstituteIdNameDto(long id, String name, String region) {
        this.id = id;
        this.name = name;
        this.region = region;
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

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
