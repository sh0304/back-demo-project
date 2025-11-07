package com.example.backdemoproject.service;

import com.example.backdemoproject.dto.response.UserResponseDto;
import com.example.backdemoproject.entity.User;
import com.example.backdemoproject.enums.UserRole;
import com.example.backdemoproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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
    List<User> users = userRepository.findAllByOrderByNameAsc();

    return users.stream()
            .filter(user -> user.getRole() == UserRole.USER)
            .map(UserResponseDto::from)
            .collect(Collectors.toList());
  }

  /**
   * 사용자 로그인 (사용자 존재 여부 확인 및 사용자 정보 반환)
   */
  public UserResponseDto getUserByUsername(String userName) {
    return userRepository.findByName(userName)
            .map(UserResponseDto::from)
            .orElseThrow(() -> new IllegalArgumentException("등록된 사용자가 아닙니다."));
  }

  /**
   * 관리자 로그인
   */
  @Transactional
  public UserResponseDto loginAdmin(String userName, String password) {
    User user = userRepository.findByName(userName)
            .orElseThrow(() -> new IllegalArgumentException("관리자 계정이 아닙니다."));

    if (user.getRole() != UserRole.ADMIN) {
      throw new IllegalArgumentException("관리자 권한이 없습니다.");
    }

    if (!user.getPassword().equals(password)) {
      throw new IllegalArgumentException("패스워드가 일치하지 않습니다.");
    }

    return UserResponseDto.from(user);
  }

  /**
   * 사용자 등록
   */
  @Transactional
  public void registerUser(String userName, String userEmail) {
    boolean isExistUser = userRepository.existsByName(userName);
    boolean isExistUserEmail = userRepository.existsByEmail(userEmail);
    if (isExistUser) {
      throw new IllegalArgumentException("이미 등록된 사용자명입니다. 다른 이름을 사용해주세요.");
    }
    if (isExistUserEmail) {
      throw new IllegalArgumentException("이미 등록된 이메일입니다. 다른 이메일을 사용해주세요.");
    }

    User user = User.builder()
            .name(userName)
            .email(userEmail)
            .password(null)
            .role(UserRole.USER)
            .build();

    userRepository.save(user);
  }
}
