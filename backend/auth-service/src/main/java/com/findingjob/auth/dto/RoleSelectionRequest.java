package com.findingjob.auth.dto;

import jakarta.validation.constraints.NotBlank;

public class RoleSelectionRequest {

    @NotBlank(message = "角色不能为空")
    private String role;

    public RoleSelectionRequest() {}

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
