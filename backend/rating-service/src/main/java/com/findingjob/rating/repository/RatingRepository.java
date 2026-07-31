package com.findingjob.rating.repository;

import com.findingjob.rating.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    List<Rating> findByToCompanyIdAndIsHiddenFalse(Long companyId);

    List<Rating> findByToUserIdAndIsHiddenFalse(Long userId);

    List<Rating> findByFromUserIdAndToCompanyId(Long fromUserId, Long toCompanyId);

    List<Rating> findByFromUserIdAndToUserId(Long fromUserId, Long toUserId);

    long countByToCompanyIdAndIsHiddenFalse(Long companyId);
}
