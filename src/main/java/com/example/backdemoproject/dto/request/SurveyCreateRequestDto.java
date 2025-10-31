package com.example.backdemoproject.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SurveyCreateRequestDto {
    private String title;
    private String description;
    private Long creatorId;
    private List<SurveyOptionCreateDto> options;

    @Getter
    @Setter
    public static class SurveyOptionCreateDto {
        private String optionText;
        private Integer orderNum;
    }
}
