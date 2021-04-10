package com.brainsinjars.projectbackend.dto;

/**
 * @author Nilesh Pandit
 * @since 10-03-2021
 */

public class CompanyDto {
    private long id;
    private String name;
    private String logoUrl;
    private String websiteUrl;

    public CompanyDto(long id, String name, String logoUrl, String websiteUrl) {
        this.id = id;
        this.name = name;
        this.logoUrl = logoUrl;
        this.websiteUrl = websiteUrl;
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

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }
}
