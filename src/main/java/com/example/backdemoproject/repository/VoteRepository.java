package com.example.backdemoproject.repository;

import com.example.backdemoproject.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    boolean existsBySurveyIdAndUserId(Long surveyId, Long userId);
}
