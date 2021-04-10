package com.brainsinjars.projectbackend.dto;

import java.util.Set;

/**
 * @author Nilesh Pandit
 * @since 10-03-2021
 */

public class InstituteCompanyPatchDto {
    private String op;
    private Set<String> companies;

    public InstituteCompanyPatchDto(String op, Set<String> companies) {
        this.op = op;
        this.companies = companies;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public Set<String> getCompanies() {
        return companies;
    }

    public void setCompanies(Set<String> companies) {
        this.companies = companies;
    }
}
