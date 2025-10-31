package com.example.backdemoproject.entity;

import com.example.backdemoproject.enums.SurveyStatus;
import jakarta.persistence.*; // JPA에서 사용하는 모든 어노테이션과 클래스를 불러옴
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity // 테이블 매핑 객체임을 표시
@Table(name = "survey") // 테이블 이름 지정
@Getter @Setter // 모든 필드에 get/set 메서드 생성
@NoArgsConstructor // 파라미터 없는 생성자(new Survey()) 자동 생성
@AllArgsConstructor // 파라미터 있는 생성자 자동 생성
@Builder // 빌더 패턴 자동 생성 - 빌더 패턴: 객체에 필요한 값만 골라서 보기 쉽게 만들 수 있게 해주는 방법
@Comment("설문")
public class Survey {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Comment("설문 ID")
  private Long id;

  @Column(nullable = false, length = 200)
  @Comment("설문 제목")
  private String title;

  @Column(length = 1000)
  @Comment("설문 설명")
  private String description;

  // N:1 관계 - 하나의 관리자는 여러 설문을 가질 수 있음
  @ManyToOne(fetch = FetchType.LAZY) // FetchType.LAZY: 지연 로딩 (필요할 때만 가져옴)
  @JoinColumn(name = "creator_id", nullable = false) // FK admin_id 설정
  @Comment("설문 생성 관리자")
  private User creator;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  @Builder.Default // lombok - 기본값 지정
  @Comment("설문 상태 (ACTIVE, CLOSED)")
  private SurveyStatus status = SurveyStatus.ACTIVE;

  @CreationTimestamp // 생성 시간 자동 저장
  @Column(name = "created_at", nullable = false, updatable = false)
  @Comment("생성 일시")
  private LocalDateTime createdAt;
}
