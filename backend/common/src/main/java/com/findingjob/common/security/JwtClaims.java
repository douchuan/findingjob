package com.findingjob.common.security;

import com.findingjob.common.enums.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class JwtClaims {

    private Long userId;
    private UserRole role;
    private String phone;
    private LocalDateTime issuedAt;
    private LocalDateTime expiresAt;
}
