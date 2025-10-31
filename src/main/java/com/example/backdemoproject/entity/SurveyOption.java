package com.example.backdemoproject.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

// 설문 항목 테이블
@Entity
@Table(name = "survey_option")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Comment("설문 선택지")
public class SurveyOption {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Comment("선택지 ID")
  private Long id;

  // N:1관계 - 하나의 설문에 여러 설문 항목들을 가질 수 있음
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "survey_id", nullable = false) // 설문 ID가 FK
  @Comment("설문 ID")
  private Survey survey;

  @Column(nullable = false, length = 200)
  @Comment("선택지 내용")
  private String optionText;

  @Column(nullable = false)
  @Comment("선택지 순서")
  private Integer orderNum;
}