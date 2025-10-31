package com.example.backdemoproject.service;

import com.example.backdemoproject.dto.request.SurveyCreateRequestDto;
import com.example.backdemoproject.dto.request.VoteRequestDto;
import com.example.backdemoproject.dto.response.SurveyDetailDto;
import com.example.backdemoproject.dto.response.SurveyResponseDto;
import com.example.backdemoproject.dto.response.SurveyResultDto;
import com.example.backdemoproject.entity.*;
import com.example.backdemoproject.enums.UserRole;
import com.example.backdemoproject.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service // Service 레어어 선언
@RequiredArgsConstructor // 생성자 주입
@Transactional(readOnly = true)  // 읽기 전용 트랜잭션
public class SurveyService {

  private final SurveyRepository surveyRepository; // Repository 주입
  private final SurveyInvitationRepository invitationRepository;
  private final SurveyOptionRepository surveyOptionRepository;
  private final UserRepository userRepository;
  private final VoteRepository voteRepository;

  /**
   * userId의 role에 따라 다른 설문 목록 반환
   * - ADMIN: 전체 설문
   * - USER: 내가 초대받은 설문만
   */
  public List<SurveyResponseDto> getSurveysByUserId(Long userId) {
    // 사용자 조회
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. ID: " + userId));

    // role에 따라 다른 결과 반환
    if (user.getRole() == UserRole.ADMIN) {
      // 관리자: 전체 설문 조회
      return getAllSurveys();
    } else {
      // 일반 사용자: 내가 초대받은 설문만 조회
      return getMyInvitedSurveys(userId);
    }
  }

  /**
   * 전체 설문 목록 조회 (관리자용)
   */
  public List<SurveyResponseDto> getAllSurveys() {
    // Repository에서 Entity 조회
    List<Survey> surveys = surveyRepository.findAllSurveys();

    // Entity → DTO 변환
    // stream()를 생성하고, 각 entity를 dto로 반환 후 list로 변환 (for문 대신)
    return surveys.stream()
            .map(SurveyResponseDto::from)
            .collect(Collectors.toList()); // stream을 다시 Collection(List, Set)으로 변환
  }

  /**
   * 내가 초대받은 설문 목록 조회 (사용자용)
   */
  public List<SurveyResponseDto> getMyInvitedSurveys(Long userId) {
    // 내가 초대받은 목록 조회
    List<SurveyInvitation> invitations = invitationRepository.findByUserId(userId);

    // 초대된 설문들만 추출
    return invitations.stream()
            .map(invitation -> SurveyResponseDto.from(invitation.getSurvey()))
            .collect(Collectors.toList());
  }

  /**
   * 설문 상세 조회 (선택지 포함)
   */
  public SurveyDetailDto getSurveyDetail(Long surveyId) {
    // 설문 조회
    Survey survey = surveyRepository.findById(surveyId)
            .orElseThrow(() -> new RuntimeException("설문을 찾을 수 없습니다."));

    // 설문 선택지 조회
    List<SurveyOption> options = surveyOptionRepository.findBySurveyId(surveyId);

    // 모든 투표 내역 조회 (user, option fetch join)
    List<Vote> votes = voteRepository.findAllBySurveyIdWithUserAndOption(surveyId);

    // 선택지별 투표 그룹화
    Map<Long, List<Vote>> voteByOption = votes.stream()
            .collect(Collectors.groupingBy(vote -> vote.getOption().getId()));

    // 선택지별 결과 DTO 변환


    return SurveyResultDto.builder()
            .surveyId(survey.getId())
            .title(survey.getTitle())
            .description(survey.getDescription())
            .status(survey.getStatus())
            .options(optionResults)
            .createdAt(survey.getCreatedAt())
            .build();
  }

  /**
   * 설문 생성
   */
  @Transactional
  public SurveyDetailDto createSurvey(SurveyCreateRequestDto requestDto) {
    // 사용자 조회
    User creator = userRepository.findById(requestDto.getCreatorId())
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

    // 관리자 권한 확인
    if (creator.getRole() != UserRole.ADMIN) {
      throw new RuntimeException("설문 생성 권한이 없습니다.");
    }

    // 설문 엔티티 저장
    Survey survey = Survey.builder()
            .title(requestDto.getTitle())
            .description(requestDto.getDescription())
            .creator(creator)
            .build();
    surveyRepository.save(survey);

    // 설문 선택지 엔티티 저장
    List<SurveyOption> options = requestDto.getOptions().stream()
            .map(optionDto -> SurveyOption.builder()
                    .survey(survey)
                    .optionText(optionDto.getOptionText())
                    .orderNum(optionDto.getOrderNum())
                    .build())
            .collect(Collectors.toList());
    surveyOptionRepository.saveAll(options);

    // DTO 변환
    return SurveyDetailDto.from(survey, options);
  }

  /**
   * 설문 투표
   */
  @Transactional
  public void voteSurvey(Long surveyId, VoteRequestDto requestDto) {
    // 사용자가 이미 이 설문에 투표했는지 확인
    if (voteRepository.existsBySurveyIdAndUserId(surveyId, requestDto.getUserId())) {
      throw new RuntimeException("이미 이 설문에 투표했습니다.");
    }

    // 필요한 엔티티 조회
    Survey survey = surveyRepository.findById(surveyId)
            .orElseThrow(() -> new RuntimeException("설문을 찾을 수 없습니다."));
    User user = userRepository.findById(requestDto.getUserId())
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    SurveyOption option = surveyOptionRepository.findById(requestDto.getOptionId())
            .orElseThrow(() -> new RuntimeException("선택지를 찾을 수 없습니다."));

    // 선택지가 해당 설문에 속하는지 확인
    if (!option.getSurvey().getId().equals(surveyId)) {
      throw new RuntimeException("해당 설문에 존재하지 않는 선택지입니다.");
    }

    // Vote 엔티티 저장
    Vote vote = Vote.builder()
            .survey(survey)
            .user(user)
            .option(option)
            .build();

    voteRepository.save(vote);
  }

  /**
   * 설문 결과
   */
  public SurveyResultDto getSurveyResult(Long surveyId) {
    // 설문 조회
    Survey survey = surveyRepository.findById(surveyId)
            .orElseThrow(() -> new RuntimeException("설문을 찾을 수 없습니다"));

    // 설문의 선택지 조회
    List<SurveyOption> options = surveyOptionRepository.findBySurveyId(surveyId);

    List<Vote> votes = voteRepository.findAllBy

  }
}

/*
* Service
* - 비즈니스 로직 처리
* - Repository를 호출해서 데이터를 가져옴
* - Entity를 DTO로 변환
*
* Stream
* - List, Set 같은 컬렉션을 "가공하기 쉬운 형태"로 변환
*
* Collectors
* - Stream을 다양한 형태로 변환(수집)하는 메서드들을 모아놓은 클래스
* */