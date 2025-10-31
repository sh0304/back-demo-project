package com.example.backdemoproject.service;

import com.example.backdemoproject.dto.response.UserResponseDto;
import com.example.backdemoproject.entity.User;
import com.example.backdemoproject.enums.UserRole;
import com.example.backdemoproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
  private final UserRepository userRepository;

  /**
   * 전체 사용자 목록 조회 (관리자용)
   */
  public List<UserResponseDto> getUsers() {
    List<User> users = userRepository.findAllByOrderByUsernameAsc();

    return users.stream()
            .filter(user -> user.getRole() == UserRole.USER)
            .map(UserResponseDto::from)
            .collect(Collectors.toList());
  }
}
