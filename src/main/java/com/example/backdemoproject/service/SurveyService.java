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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. ID: " + userId));

    // role에 따라 다른 결과 반환
    if (user.getRole() == UserRole.ADMIN) {
      // 관리자: 전체 설문 조회
      return getAllSurveys(userId);
    } else {
      // 일반 사용자: 내가 초대받은 설문만 조회
      return getMyInvitedSurveys(userId);
    }
  }

  /**
   * 전체 설문 목록 조회 (관리자용)
   */
  public List<SurveyResponseDto> getAllSurveys(Long userId) {
    // Repository에서 Entity 조회
    List<Survey> surveys = surveyRepository.findAllSurveys();

    // Entity → DTO 변환
    // stream()를 생성하고, 각 entity를 dto로 반환 후 list로 변환 (for문 대신)
    return surveys.stream()
            .map(survey -> {
              Long userVoteOptionId = voteRepository.findBySurveyIdAndUserId(survey.getId(), userId)
                      .map(vote -> vote.getOption().getId())
                      .orElse(null);
              return SurveyResponseDto.from(survey, userVoteOptionId);
            })
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
            .map(invitation -> {
              Survey survey = invitation.getSurvey();
              Long userVoteOptionId = voteRepository.findBySurveyIdAndUserId(survey.getId(), userId)
                      .map(vote -> vote.getOption().getId())
                      .orElse(null);
              return SurveyResponseDto.from(survey, userVoteOptionId);
            })
            .collect(Collectors.toList());
  }

  /**
   * 설문 상세 조회 (선택지 포함)
   */
  public SurveyDetailDto getSurveyDetail(Long surveyId, Long userId) {
    // 설문 조회
    Survey survey = surveyRepository.findById(surveyId)
            .orElseThrow(() -> new IllegalArgumentException("설문을 찾을 수 없습니다."));

    List<SurveyOption> options = surveyOptionRepository.findBySurveyIdOrderByOrderNumAsc(surveyId);

    // 투표 정보 확인
    Optional<Vote> userVote = voteRepository.findBySurveyIdAndUserId(surveyId, userId);
    Long userVoteOptionId = userVote.map(vote -> vote.getOption().getId()).orElse(null);

    // DTO 변환
    return SurveyDetailDto.from(survey, options, userVoteOptionId);
  }

  /**
   * 설문 생성
   */
  @Transactional
  public SurveyDetailDto createSurvey(SurveyCreateRequestDto requestDto) {
    // 사용자 조회
    User creator = userRepository.findById(requestDto.getCreatorId())
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

    // 관리자 권한 확인
    if (creator.getRole() != UserRole.ADMIN) {
      throw new IllegalStateException("설문 생성 권한이 없습니다.");
    }

    // 설문 엔티티 저장
    Survey survey = Survey.builder()
            .title(requestDto.getTitle())
            .description(requestDto.getDescription())
            .dueDate(requestDto.getDueDate())
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

    // 사용자 초대
    if (requestDto.getInvitedUserIds() != null && !requestDto.getInvitedUserIds().isEmpty()) {
      List<User> invitedUsers = userRepository.findAllById(requestDto.getInvitedUserIds());
      List<SurveyInvitation> invitations = invitedUsers.stream()
              .map(user -> SurveyInvitation.builder()
                      .survey(survey)
                      .user(user)
                      .build())
              .collect(Collectors.toList());
      invitationRepository.saveAll(invitations);
    }

    // DTO 변환
    return SurveyDetailDto.from(survey, options, null);
  }

  /**
   * 설문 투표
   */
  @Transactional
  public void voteSurvey(Long surveyId, VoteRequestDto requestDto) {
    // 사용자가 이미 이 설문에 투표했는지 확인
    if (voteRepository.existsBySurveyIdAndUserId(surveyId, requestDto.getUserId())) {
      throw new IllegalStateException("이미 설문에 투표했습니다.");
    }

    // 필요한 엔티티 조회
    Survey survey = surveyRepository.findById(surveyId)
            .orElseThrow(() -> new IllegalArgumentException("설문을 찾을 수 없습니다."));

    // 설문 마감일 확인
    if (survey.getDueDate() != null && LocalDateTime.now().isAfter(survey.getDueDate())) {
      throw new IllegalStateException("설문이 마감되었습니다.");
    }

    User user = userRepository.findById(requestDto.getUserId())
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    SurveyOption option = surveyOptionRepository.findById(requestDto.getOptionId())
            .orElseThrow(() -> new IllegalArgumentException("선택지를 찾을 수 없습니다."));

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
            .orElseThrow(() -> new IllegalArgumentException("설문을 찾을 수 없습니다"));

    // 설문에 포함된 모든 선택지 조회
    List<SurveyOption> options = surveyOptionRepository.findBySurveyIdOrderByOrderNumAsc(surveyId);

    // 설문에 모든 투표 기록(사용자, 선택지 정보) 조회
    List<Vote> votes = voteRepository.findAllBySurveyIdWithUserAndOption(surveyId);
    int totalVotes = votes.size();

    // 선택지별 투표 그룹화 (<선택지 ID, 투표한 목록> Map 생성)
    Map<Long, List<Vote>> votesByOption = votes.stream()
            .collect(Collectors.groupingBy(vote -> vote.getOption().getId()));

    // 각 선택지별 결과 DTO 변환
    List<SurveyResultDto.OptionResultDto> optionResults = options.stream()
            .map(option -> {
              // 선택지에 해당하는 투표 목록
              List<Vote> optionVotes = votesByOption.getOrDefault(option.getId(), List.of());
              // 득표율 계산
              int percentage = (totalVotes == 0) ? 0 : (optionVotes.size() / totalVotes) * 100;

              // 선택지에 투표한 사용자 목록 dto 생성
              List<SurveyResultDto.VoterDto> voters = optionVotes.stream()
                      .map(vote -> SurveyResultDto.VoterDto.builder()
                              .userId(vote.getUser().getId())
                              .username(vote.getUser().getName())
                              .votedAt(vote.getCreatedAt())
                              .build())
                      .collect(Collectors.toList());

              // 선택지별 결과 dto 생성
              return SurveyResultDto.OptionResultDto.builder()
                      .optionId(option.getId())
                      .optionText(option.getOptionText())
                      .percentage(percentage)
                      .voters(voters)
                      .build();
            })
            .collect(Collectors.toList());

    // 모든 정보에 대한 dto 반환
    return SurveyResultDto.builder()
            .surveyId(survey.getId())
            .title(survey.getTitle())
            .description(survey.getDescription())
            .status(survey.getStatus())
            .createdAt(survey.getCreatedAt())
            .dueDate(survey.getDueDate())
            .totalVotes(totalVotes)
            .options(optionResults)
            .build();
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