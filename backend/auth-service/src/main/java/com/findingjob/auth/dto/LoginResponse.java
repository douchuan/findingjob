package com.findingjob.auth.dto;

public class LoginResponse {

    private String token;
    private boolean roleSelected;
    private String role;
    private Long userId;
    private String name;
    private String avatar;

    public LoginResponse() {}

    public LoginResponse(String token, boolean roleSelected, String role, Long userId, String name, String avatar) {
        this.token = token;
        this.roleSelected = roleSelected;
        this.role = role;
        this.userId = userId;
        this.name = name;
        this.avatar = avatar;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public boolean isRoleSelected() { return roleSelected; }
    public void setRoleSelected(boolean roleSelected) { this.roleSelected = roleSelected; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
}
