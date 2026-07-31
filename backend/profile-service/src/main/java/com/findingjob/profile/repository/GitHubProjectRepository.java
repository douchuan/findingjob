package com.findingjob.profile.repository;

import com.findingjob.profile.entity.GitHubProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GitHubProjectRepository extends JpaRepository<GitHubProject, Long> {

    List<GitHubProject> findByProfileIdOrderByStarCountDesc(Long profileId);
}
