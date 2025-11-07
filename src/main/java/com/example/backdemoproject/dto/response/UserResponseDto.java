package com.example.backdemoproject.dto.response;

import com.example.backdemoproject.entity.User;
import com.example.backdemoproject.enums.UserRole;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {

  private Long userId;
  private String userName;
  private String userEmail;
  private UserRole userRole;

  public static UserResponseDto from(User user) {
    return UserResponseDto.builder()
            .userId(user.getId())
            .userName(user.getName())
            .userEmail(user.getEmail())
            .userRole(user.getRole())
            .build();
  }
}
