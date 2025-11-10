# 설문조사 백엔드 데모 프로젝트

이 프로젝트는 Spring Boot를 사용하여 구현한 간단한 설문조사(Survey) 애플리케이션의 백엔드 서버입니다.

## 주요 기능

- **사용자 권한 관리**
  - 관리자(ADMIN)와 일반 사용자(USER) 역할 분리
- **설문 참여 (사용자)**
  - 초대받은 설문 목록 조회
  - 설문 상세 보기 및 투표
- **설문 관리 (관리자)**
  - 설문 및 선택지 생성
  - 투표 종료 후, 설문 결과 확인 (총 투표 수, 선택지별 득표율, 투표자 목록)

## 기술 스택

- **Backend**: Java 17, Spring Boot 3.x
- **Database**: H2 In-memory DB, Spring Data JPA
- **API Documentation**: SpringDoc (Swagger UI)
- **Build Tool**: Gradle

## 프로젝트 구조

```
.
├── src/main/java/com/example/backdemoproject/
│   ├── config/          # 애플리케이션 설정 (Swagger, Web 등)
│   ├── controller/      # API 엔드포인트 정의
│   ├── dto/             # 데이터 전송 객체 (Request/Response)
│   ├── entity/          # 데이터베이스 엔티티
│   ├── enums/           # 공통 열거형
│   ├── exception/       # 전역 예외 처리
│   ├── repository/      # 데이터베이스 접근 계층 (JPA)
│   └── service/         # 비즈니스 로직 처리
├── src/main/resources/  # 설정 파일 (application.properties), SQL 스크립트 등
└── build.gradle         # Gradle 빌드 설정
```

## API Endpoints

### Survey API (`/api/surveys`)

- `GET /api/surveys`: 설문 목록 조회
- `GET /api/surveys/{id}`: 설문 상세 조회
- `POST /api/surveys`: 설문 생성
- `POST /api/surveys/{id}/vote`: 설문 투표
- `GET /api/surveys/{id}/result`: 설문 결과 조회

### User API (`/api/users`)

- `GET /api/users`: 모든 사용자 조회
- `GET /api/users/login/{username}`: 사용자 로그인
- `POST /api/users/login`: 관리자 로그인
- `POST /api/users/register`: 사용자 등록

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

## DB 테이블

### 1. User (사용자)
| Column   | Type | Description      |
|----------| --- |------------------|
| id       | Long (PK) | 사용자 고유 ID        |
| name | String | 사용자 이름           |
| email    | String | 이메일              |
| password | String | 사용자 비밀번호         |
| role     | Enum | 권한 (ADMIN, USER) |

### 2. Survey (설문)
| Column      | Type | Description            |
|-------------| --- |------------------------|
| id          | Long (PK) | 설문 고유 ID               |
| title       | String | 설문 제목                  |
| description | String | 설문 설명                  |
| creator     | Long (FK) | 작성한 관리자 ID (User)      |
| status      | Enum | 설문 상태 (ACTIVE, CLOSED) |
| created_at  | LocalDateTime | 생성 일시                  |
| dueeDate    | LocalDateTime | 마감일                    |

### 3. SurveyOption (설문 선택지)
| Column | Type | Description |
| --- | --- | --- |
| id | Long (PK) | 선택지 고유 ID |
| survey_id | Long (FK) | 설문 ID (Survey) |
| option_text | String | 선택지 내용 (예: "짜장면") |
| order_num | Integer | 선택지 순서 |

### 4. SurveyInvitation (설문 초대)
| Column | Type | Description |
| --- | --- | --- |
| id | Long (PK) | 초대 고유 ID |
| survey_id | Long (FK) | 설문 ID (Survey) |
| user_id | Long (FK) | 초대받은 사용자 ID (User) |

### 5. Vote (투표/답변)
| Column | Type | Description |
| --- | --- | --- |
| id | Long (PK) | 투표 고유 ID |
| survey_id | Long (FK) | 설문 ID (Survey) |
| user_id | Long (FK) | 투표한 사용자 ID (User) |
| option_id | Long (FK) | 선택한 옵션 ID (SurveyOption) |
| created_at | LocalDateTime | 투표 일시 |