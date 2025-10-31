package com.example.backdemoproject.entity;

import com.example.backdemoproject.enums.InvitationStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Table(
        name = "survey_invitation",
        uniqueConstraints = { // 초대는 한 사람당 한번만 초대할 수 있도록 함
                @UniqueConstraint(
                        name = "uk_survey_invitation_user",
                        columnNames = {"survey_id", "user_id"}
                )
        }
)
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Comment("설문 초대")
public class SurveyInvitation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Comment("초대 ID")
  private Long id;

  // N:1 관계 - 하나의 설문은 여러 명의 사용자를 초대할 수 있음
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "survey_id", nullable = false) // 설문 ID는 FK
  @Comment("설문 ID")
  private Survey survey;

  // N:1 관계 - 한 명의 사용자는 여러 설문에 초대받을 수 있음
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false) // user ID는 FK
  @Comment("초대받은 사용자 ID")
  private User user;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  @Builder.Default
  @Comment("초대 상태 (PENDING, COMPLETED)")
  private InvitationStatus status = InvitationStatus.PENDING;
}