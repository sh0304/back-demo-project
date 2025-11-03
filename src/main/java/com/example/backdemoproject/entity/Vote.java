package com.example.backdemoproject.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

// 투표 테이블
@Entity
@Table(
        name = "vote",
        uniqueConstraints = { // 한 사용자는 한 설문에 한 번만 투표할 수 있도록 함
                @UniqueConstraint(
                        name = "uk_vote_user",
                        columnNames = {"survey_id", "user_id"}
                )
        }
)
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"survey", "user", "option"})
public class Vote {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // N:1 관계 - 하나의 설문은 여러 개의 투표를 가질 수 있음
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "survey_id", nullable = false) // 설문 ID는 FK
  private Survey survey;

  // N:1 관계 - 한 명의 사용자는 여러 설문에 투표할 수 있음
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false) // user ID는 FK
  private User user;

  // N:1 관계 - 하나의 선택지는 여러 번 선택될 수 있음
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "option_id", nullable = false) // 설문 항목 ID는 FK
  private SurveyOption option;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;
}
