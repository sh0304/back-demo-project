package com.example.backdemoproject.repository;

import com.example.backdemoproject.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    boolean existsBySurveyIdAndUserId(Long surveyId, Long userId);

    @Query("SELECT v FROM Vote v JOIN FETCH v.user JOIN FETCH v.option WHERE v.survey.id = :surveyId")
    List<Vote> findAllBySurveyIdWithUserAndOption(@Param("surveyId") Long surveyId);
}
