package com.example.backdemoproject.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoteRequestDto {
    private Long userId;
    private Long optionId;
}
