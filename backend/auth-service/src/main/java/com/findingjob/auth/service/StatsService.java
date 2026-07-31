package com.findingjob.auth.service;

import com.findingjob.auth.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class StatsService {

    private final UserRepository userRepository;

    public StatsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Get platform-wide stats (MVP: user counts by role and status).
     * In production, this would aggregate across all services.
     */
    public Map<String, Object> getPlatformStats() {
        long totalUsers = userRepository.count();
        long activeUsers = userRepository.findAll().stream()
                .filter(u -> u.getRole() != null && u.getStatus().name().equals("ACTIVE"))
                .count();

        long jobseekers = userRepository.findAll().stream()
                .filter(u -> u.getRole() != null && "JOBSEEKER".equals(u.getRole().name()))
                .count();

        long hrs = userRepository.findAll().stream()
                .filter(u -> u.getRole() != null && "HR".equals(u.getRole().name()))
                .count();

        long admins = userRepository.findAll().stream()
                .filter(u -> u.getRole() != null && "ADMIN".equals(u.getRole().name()))
                .count();

        long pendingDeletion = userRepository.findAll().stream()
                .filter(u -> u.getStatus().name().equals("PENDING_DELETION"))
                .count();

        return Map.of(
                "totalUsers", totalUsers,
                "activeUsers", activeUsers,
                "jobseekers", jobseekers,
                "hrCount", hrs,
                "adminCount", admins,
                "pendingDeletion", pendingDeletion
        );
    }
}
