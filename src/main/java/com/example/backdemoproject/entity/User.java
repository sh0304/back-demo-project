package com.example.backdemoproject.entity;

import com.example.backdemoproject.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Comment("사용자")
public class User {

  @Id // 기본키
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Comment("사용자 ID")
  private Long id;

  @Column(nullable = false, unique = true, length = 50) // 컬럼 속성 지정
  @Comment("사용자명")
  private String username;

  @Column(nullable = false, unique = true, length = 100)
  @Comment("이메일")
  private String email;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  @Comment("권한 (ADMIN, USER)")
  private UserRole role;
}
