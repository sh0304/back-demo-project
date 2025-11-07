package com.example.backdemoproject.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class SurveyCreateRequestDto {
  private Long creatorId; // 생성자 ID
  private String title;
  private String description;
  private LocalDateTime dueDate; // 마감일
  private List<SurveyOptionCreateDto> options;
  private List<Long> invitedUserIds; // 초대받은 유저들

  @Getter
  @Setter
  public static class SurveyOptionCreateDto {
      private String optionText;
      private Integer orderNum;
  }
}
