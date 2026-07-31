package com.findingjob.auth.repository;

import com.findingjob.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByPhone(String phone);

    Optional<User> findByGithubId(String githubId);

    Optional<User> findByGiteeId(String giteeId);

    Optional<User> findByWechatOpenId(String wechatOpenId);

    Optional<User> findByAlipayUserId(String alipayUserId);
}
