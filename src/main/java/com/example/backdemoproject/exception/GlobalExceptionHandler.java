package com.example.backdemoproject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalStateException(IllegalStateException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", ex.getMessage()));
    }
}

/*
* @RestControllerAdvice
* - 애플리케이션 전체에서 발생하는 예외를 한 곳에서 처리하는 클래스
* - @ControllerAdvice + @ResponseBody의 조합으로, JSON 형태로 응답을 반환
* - 모든 @RestController에서 발생하는 예외를 감지
*
*
* RuntimeException
* - 포괄적인 범위
* - 실행 중 발생할 수 있는 모든 종류의 예외를 나타냄
* - 구체적인 예외 타입이 없거나 일반적인 런타임 오류를 표현할 때
* - 사용: 메서드 호출 시 파라미터 값이 부적절할 때
*
* IllegalArgumentException
* - 구체적인 예외
* - 메서드에 부적절하거나 잘못된 인자가 전달되었을 때만 사용
* - 메서드 인자(파라미터) 관련 에러 예외 처리
* - 사용: 메서드 호출 시 파라미터 값이 부적절할 때
*
* IllegalStateException
* - 객체의 상태 관련 예외 처리
* - 사용: 객체가 메서드를 실행하기에 적절하지 않은 상태일 때
* */