# 설문조사 백엔드 데모 프로젝트

이 프로젝트는 Spring Boot를 사용하여 구현한 간단한 설문조사(Survey) 애플리케이션의 백엔드 서버입니다.

## 주요 기능

- **사용자 권한 관리**
  - 관리자(ADMIN)와 일반 사용자(USER) 역할 분리
- **설문 관리 (관리자)**
  - 설문 및 선택지 생성
- **설문 참여 (사용자)**
  - 초대받은 설문 목록 조회
  - 설문 상세 보기 및 투표
- **설문 결과**
  - 투표 종료 후, 설문 결과 확인

## 기술 스택

- **Backend**: Java 17, Spring Boot 3.x
- **Database**: H2 In-memory DB, Spring Data JPA
- **API Documentation**: SpringDoc (Swagger UI)
- **Build Tool**: Gradle

## 실행 방법

1.  **프로젝트 빌드**
    ```shell
    ./gradlew build
    ```

2.  **애플리케이션 실행**
    ```shell
    java -jar build/libs/back-demo-project-0.0.1-SNAPSHOT.jar
    ```

3.  서버가 실행되면 아래의 주요 경로를 통해 기능을 확인할 수 있습니다.

## API 문서

- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **H2 Console**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
  - `JDBC URL`: `jdbc:h2:mem:surveydb`
  - `사용자명`: `sa`
  - `비밀번호`: (공란)