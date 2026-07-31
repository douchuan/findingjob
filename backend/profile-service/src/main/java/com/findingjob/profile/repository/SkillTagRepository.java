package com.findingjob.profile.repository;

import com.findingjob.profile.entity.SkillTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkillTagRepository extends JpaRepository<SkillTag, Long> {

    Optional<SkillTag> findByName(String name);

    @Query("SELECT s FROM SkillTag s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<SkillTag> searchByName(String query, Pageable pageable);

    List<SkillTag> findByNameContainingIgnoreCase(String query);
}
