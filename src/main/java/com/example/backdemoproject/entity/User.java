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
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Comment("사용자 고유 ID")
  private Long id;

  @Column(nullable = false, unique = true, length = 50) // 컬럼 속성 지정
  @Comment("사용자명")
  private String name;

  @Column(nullable = false, unique = true, length = 100)
  @Comment("이메일")
  private String email;

  @Column(length = 255)
  @Comment("비밀번호")
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  @Comment("권한 (ADMIN, USER)")
  private UserRole role;
}
