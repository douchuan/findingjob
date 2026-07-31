package com.findingjob.profile.repository;

import com.findingjob.profile.entity.UserSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSkillRepository extends JpaRepository<UserSkill, Long> {

    List<UserSkill> findByUserId(Long userId);

    Optional<UserSkill> findByUserIdAndSkillId(Long userId, Long skillId);

    @Query("SELECT us FROM UserSkill us JOIN SkillTag st ON us.skillId = st.id " +
           "WHERE us.userId = :userId ORDER BY us.verifiedCount DESC, us.level DESC")
    List<UserSkill> findByUserIdOrderByVerified(Long userId);
}
