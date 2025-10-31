-- 사용자 데이터
INSERT INTO users (id, username, email, role) VALUES
(0, 'admin', 'admin1@test.com', 'ADMIN'),
(1, 'user1', 'user1@test.com', 'USER'),
(2, 'user2', 'user2@test.com', 'USER'),
(3, 'user3', 'user3@test.com', 'USER');

-- 설문 데이터
INSERT INTO survey (title, description, creator_id, status, created_at) VALUES
('점심 메뉴 투표', '오늘 점심 뭐 먹을까요?', 0, 'ACTIVE', CURRENT_TIMESTAMP),
('회의 시간 투표', '다음 회의 시간을 정해주세요', 0, 'ACTIVE', CURRENT_TIMESTAMP),
('프로젝트 선호도', '어떤 프로젝트를 하고 싶으세요?', 0, 'CLOSED', CURRENT_TIMESTAMP);

-- 설문 선택지 데이터
INSERT INTO survey_option (survey_id, option_text, order_num) VALUES
(1, '짜장면', 1),
(1, '짬뽕', 2),
(1, '탕수육', 3),
(1, '볶음밥', 4),
(2, '오전 10시', 1),
(2, '오후 2시', 2),
(2, '오후 4시', 3),
(3, 'AI 프로젝트', 1),
(3, '웹 개발', 2),
(3, '모바일 앱', 3);

-- 설문 초대 데이터
INSERT INTO survey_invitation (survey_id, user_id, status) VALUES
(1, 2, 'PENDING'),
(1, 3, 'PENDING'),
(2, 2, 'COMPLETED'),
(2, 3, 'PENDING'),
(3, 2, 'COMPLETED');

-- 투표 데이터
INSERT INTO vote (survey_id, user_id, option_id, created_at) VALUES
(2, 2, 5, CURRENT_TIMESTAMP),
(3, 2, 8, CURRENT_TIMESTAMP);