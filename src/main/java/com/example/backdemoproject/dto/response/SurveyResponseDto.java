package com.example.backdemoproject.dto.response;

import com.example.backdemoproject.entity.Survey;
import com.example.backdemoproject.enums.SurveyStatus;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyResponseDto {

  private Long id;
  private String title;
  private String description;
  private Long adminId;
  private String adminUsername;  // 관리자 이름
  private SurveyStatus status;
  private LocalDateTime createdAt;

  // Entity → DTO 변환 메서드
  public static SurveyResponseDto from(Survey survey) {
    return SurveyResponseDto.builder()
            .id(survey.getId())
            .title(survey.getTitle())
            .description(survey.getDescription())
            .adminId(survey.getCreator().getId())
            .adminUsername(survey.getCreator().getUsername())
            .status(survey.getStatus())
            .createdAt(survey.getCreatedAt())
            .build();
  }
}

/*
 * DTO
 * - 계층 간 데이터 전송
 * - Controller와 Service 사이, 또는 클라이언트와 서버 사이에서 데이터를 주고받을 때 사용
 * - Entity는 데이터베이스 테이블과 직접 매핑되어 있어, 외부에 그대로 노출하면 보안 문제나 불필요한 정보 노출이 발생되기 때문에
 *   dto에서 필요한 데이터만 선택해서 담고, 여러 entity 정보를 조합하거나 가공된 데이터를 담을 수 있다.
 * */