package com.example.backdemoproject.repository;

import com.example.backdemoproject.entity.SurveyInvitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SurveyInvitationRepository extends JpaRepository<SurveyInvitation, Long> {

  // 특정 사용자가 초대받은 설문 목록 조회
  List<SurveyInvitation> findByUserId(Long userId);
}
