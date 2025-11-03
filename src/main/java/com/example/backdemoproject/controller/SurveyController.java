package com.example.backdemoproject.controller;

import com.example.backdemoproject.dto.request.SurveyCreateRequestDto;
import com.example.backdemoproject.dto.request.VoteRequestDto;
import com.example.backdemoproject.dto.response.SurveyDetailDto;
import com.example.backdemoproject.dto.response.SurveyResponseDto;
import com.example.backdemoproject.dto.response.SurveyResultDto;
import com.example.backdemoproject.service.SurveyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Tag(name = "Survey", description = "설문 관리 API")
@RestController // Rest API 컨트롤러 선언
@RequestMapping("/api/surveys") // URL 설정
@RequiredArgsConstructor // 생성자 주입
public class SurveyController {

  private final SurveyService surveyService; // service 주입

  /**
   * 설문 목록 조회
   * - User의 role에 따라 자동으로 다른 결과 반환
   * - ADMIN: 전체 설문
   * - USER: 내가 초대받은 설문만
   *
   * GET /api/surveys?userId=1  (관리자)
   * GET /api/surveys?userId=2  (사용자)
   */
  @Operation(summary = "설문 목록 조회", description = "모든 설문 목록을 조회하는 API입니다.")
  @GetMapping
  public ResponseEntity<List<SurveyResponseDto>> getSurveys(
          @RequestParam Long userId) {

    // userId로 사용자 조회 후 role 확인
    List<SurveyResponseDto> surveys = surveyService.getSurveysByUserId(userId);
    return ResponseEntity.ok(surveys);
  }

  /**
   * 설문 상세 조회 (선택지 포함)
   *
   * GET /api/surveys/{id}
   */
  @Operation(summary = "설문 상세 조회", description = "특정 설문의 상세 정보를 조회하는 API입니다.")
  @GetMapping("/{id}")
  public ResponseEntity<SurveyDetailDto> getSurveyDetail(@PathVariable Long id) {
    SurveyDetailDto survey = surveyService.getSurveyDetail(id);
    return ResponseEntity.ok(survey);
  }

  /**
   * 설문 생성
   *
   * POST /api/surveys (관리자용)
   */
  @Operation(summary = "설문 생성", description = "새로운 설문을 생성하는 API입니다. (관리자 전용)")
  @PostMapping
  public ResponseEntity<SurveyDetailDto> createSurvey(@RequestBody SurveyCreateRequestDto requestDto) {
    SurveyDetailDto createdSurvey = surveyService.createSurvey(requestDto);

    // Location 헤더를 자동으로 추가하며, Location 헤더를 통해 생성된 리소스의 위치를 알려줌
    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(createdSurvey.getId())
            .toUri();

    return ResponseEntity.created(location).body(createdSurvey);
  }

  /**
   * 설문 투표
   *
   * POST /api/surveys/{id}/vote (사용자용)
   */
  @Operation(summary = "설문 투표", description = "사용자가 설문에 투표하는 API입니다.")
  @PostMapping("/{id}/vote")
  public ResponseEntity<Void> voteSurvey(@PathVariable Long id, @RequestBody VoteRequestDto requestDto) {
    surveyService.voteSurvey(id, requestDto);
    return ResponseEntity.ok().build();
  }

  /**
   * 설문 결과
   *
   * GET /api/surveys/{id}/result
   */
  @Operation(summary = "설문 결과 조회", description = "설문의 투표 결과를 조회하는 API 입니다.")
  @GetMapping("/{id}/result")
  public ResponseEntity<SurveyResultDto> getSurveyResult(@PathVariable Long id) {
    SurveyResultDto survey = surveyService.getSurveyResult(id);
    return ResponseEntity.ok(survey);
  }
}

/*
* Controller
* - HTTP 요청을 받고 응답을 반환
* - URL 매핑 및 파라미터 처리
* - Service 호출 후 결과를 JSON으로 변환
*
* @RestController (@Controller + @ResponseBody)
* - 모든 메서드의 반환값이 자동으로 JSON으로 변환됨
* - REST API를 만들 때 사용
*
* @RequiredArgsConstructor
* - Lombok이 자동으로 생성자를 만들어줌
*
* */