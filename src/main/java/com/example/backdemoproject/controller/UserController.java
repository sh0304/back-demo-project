package com.example.backdemoproject.controller;

import com.example.backdemoproject.dto.response.UserResponseDto;
import com.example.backdemoproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  /**
   * 전체 사용자 목록 조회 (관리자용)
   *
   * GET /api/users
   */
  @GetMapping
  public ResponseEntity<List<UserResponseDto>> getAllUsers() {

    // 사용자 목록 조회
    List<UserResponseDto> users = userService.getUsers();
    return ResponseEntity.ok(users);
  }
}
