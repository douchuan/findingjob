package com.findingjob.common.security;

import com.findingjob.common.enums.UserRole;

public class JwtUserPrincipal {

    private Long userId;
    private UserRole role;
    private String phone;

    public JwtUserPrincipal() {}

    public JwtUserPrincipal(Long userId, UserRole role, String phone) {
        this.userId = userId;
        this.role = role;
        this.phone = phone;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public java.util.Collection<? extends org.springframework.security.core.GrantedAuthority> getAuthorities() {
        return java.util.Collections.singletonList(
            new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + role.name())
        );
    }
}
