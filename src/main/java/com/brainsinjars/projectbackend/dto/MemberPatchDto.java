package com.brainsinjars.projectbackend.dto;

public class MemberPatchDto {
    private String op;

    public MemberPatchDto() {
    }

    public MemberPatchDto(String op) {
        this.op = op;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }
}
