package com.example.backdemoproject.dto.response;

import com.example.backdemoproject.entity.Survey;
import com.example.backdemoproject.entity.SurveyOption;
import com.example.backdemoproject.enums.SurveyStatus;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyDetailDto {

  private Long id;
  private String title;
  private String description;
  private Long adminId;
  private String adminUsername;
  private SurveyStatus status;
  private LocalDateTime createdAt;
  private List<SurveyOptionDto> options;  // 선택지 목록

  // Entity → DTO 변환
  public static SurveyDetailDto from(Survey survey, List<SurveyOption> options) {
    return SurveyDetailDto.builder()
            .id(survey.getId())
            .title(survey.getTitle())
            .description(survey.getDescription())
            .adminId(survey.getCreator().getId())
            .adminUsername(survey.getCreator().getUsername())
            .status(survey.getStatus())
            .createdAt(survey.getCreatedAt())
            .options(options.stream()
                    .map(SurveyOptionDto::from)
                    .collect(Collectors.toList()))
            .build();
  }
}