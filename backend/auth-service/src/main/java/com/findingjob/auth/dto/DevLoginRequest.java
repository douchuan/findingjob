package com.findingjob.auth.dto;

public class DevLoginRequest {

    private String name;
    private String role;  // JOBSEEKER, HR, ADMIN

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
