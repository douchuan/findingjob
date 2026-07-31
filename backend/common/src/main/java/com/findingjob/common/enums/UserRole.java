package com.findingjob.common.enums;

public enum UserRole {

    JOBSEEKER("jobseeker"),
    HR("hr"),
    ADMIN("admin");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
