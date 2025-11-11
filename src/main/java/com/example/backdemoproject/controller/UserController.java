package com.example.backdemoproject.controller;

import com.example.backdemoproject.dto.request.LoginRequestDto;
import com.example.backdemoproject.dto.request.RegisterRequestDto;
import com.example.backdemoproject.dto.response.UserResponseDto;
import com.example.backdemoproject.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Tag(name = "User", description = "사용자 API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  /**
   * 전체 사용자 목록 조회 (관리자용)
   * [GET] /api/users
   */
  @Operation(summary = "사용자 목록 조회", description = "모든 사용자 목록을 조회하는 API입니다.")
  @GetMapping
  public ResponseEntity<List<UserResponseDto>> getAllUsers() {

    // 사용자 목록 조회
    List<UserResponseDto> users = userService.getUsers();
    return ResponseEntity.ok(users);
  }

  /**
   * 사용자 로그인
   * [GET] /api/users/login/{username}
   */
  @Operation(summary = "사용자 로그인", description = "사용자명으로 사용자를 조회하고 정보를 반환합니다.")
  @GetMapping("/login/{username}")
  public ResponseEntity<UserResponseDto> loginUser(@PathVariable String username) {
    UserResponseDto userResponseDto = userService.getUserByUsername(username);
    return ResponseEntity.ok(userResponseDto);
  }

  /**
   * 관리자 로그인
   * [POST] /api/users/login
   */
  @Operation(summary = "관리자 로그인", description = "관리자 계정으로 로그인하는 API입니다.")
  @PostMapping("/login")
  public ResponseEntity<UserResponseDto> loginAdmin(@RequestBody LoginRequestDto requestDto) {
    UserResponseDto userResponseDto = userService.loginAdmin(requestDto.getUserName(), requestDto.getPassword());
    return ResponseEntity.ok(userResponseDto);
  }

  /**
   * 사용자 등록
   * [POST] /api/users/register
   */
  @Operation(summary = "사용자 등록", description = "사용자를 등록하는 API입니다.")
  @PostMapping("/register")
  public ResponseEntity<Void> registerUser(@RequestBody RegisterRequestDto requestDto) {
    userService.registerUser(requestDto.getUserName(), requestDto.getUserEmail());
    return ResponseEntity.ok().build();
  }
}
