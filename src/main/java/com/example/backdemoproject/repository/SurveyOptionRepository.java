package com.example.backdemoproject.repository;

import com.example.backdemoproject.entity.SurveyOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SurveyOptionRepository extends JpaRepository<SurveyOption, Long> {

  // 특정 설문의 선택지 목록 조회 (순서대로)
  List<SurveyOption> findBySurveyIdOrderByOrderNumAsc(Long surveyId);
}
