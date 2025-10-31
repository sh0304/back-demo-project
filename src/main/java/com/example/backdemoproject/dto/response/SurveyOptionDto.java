package com.example.backdemoproject.dto.response;

import com.example.backdemoproject.entity.SurveyOption;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyOptionDto {

  private Long id;
  private String optionText;
  private Integer orderNum;

  // Entity → DTO 변환
  public static SurveyOptionDto from(SurveyOption option) {
    return SurveyOptionDto.builder()
            .id(option.getId())
            .optionText(option.getOptionText())
            .orderNum(option.getOrderNum())
            .build();
  }
}
