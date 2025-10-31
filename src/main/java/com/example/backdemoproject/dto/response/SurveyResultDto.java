package com.example.backdemoproject.dto.response;

import com.example.backdemoproject.entity.Survey;
import com.example.backdemoproject.entity.SurveyOption;
import com.example.backdemoproject.enums.SurveyStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyResultDto {
  // 설문 결과 전체
  private Long surveyId;
  private String title;
  private String description;
  private SurveyStatus status;
  private int totalVotes;  // 총 투표 수
  private List<OptionResultDto> options;  // 각 선택지별 결과
  private LocalDateTime createdAt;

  // 선택지별 결과
  @Getter
  @Builder
  public static class OptionResultDto {
    private Long optionId;
    private String optionText;
    private double percentage;  // 득표율 (%)
    private List<VoterDto> voters; // 선택지를 선택한 사용자들
  }

  // 투표자 정보
  @Getter
  @Builder
  public static class VoterDto {
    private Long userId;
    private String username;
    private LocalDateTime votedAt;
  }
}
