package com.findingjob.common.enums;

public enum UserStatus {

    ACTIVE("active"),
    PENDING_DELETION("pending_deletion"),
    DELETED("deleted"),
    BANNED("banned");

    private final String value;

    UserStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
