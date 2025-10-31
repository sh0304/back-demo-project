package com.example.backdemoproject.repository;

import com.example.backdemoproject.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SurveyRepository extends JpaRepository<Survey, Long> {

  @Query("SELECT s FROM Survey s JOIN FETCH s.creator ORDER BY s.createdAt DESC")
  List<Survey> findAllSurveys();
}

/*
* Repository
* - 데이터베이스와 직접 통신하는 계층
* - JPA에서 Entity를 통해 테이블을 조회, 저장, 수정, 삭제하는 역할을 담당함
* */

/*
* findAll: 모든 Survey 엔티티를 조회하라는 의미
* By: Spring Data JPA에서 쿼리 조건을 시작하는 키워드
* OrderBy: 결과를 정렬하라는 의미
* CreatedAt: Survey 엔티티 내의 createdAt 필드를 기준으로 정렬
* Desc: 내림차순(descending)으로 정렬
* -> Spring Data JPA가 메소드 이름을 분석하여 "모든 Survey를 생성일(createdAt) 기준 내림차순으로 정렬하여 조회" 하는
*     JPQL 쿼리(SELECT s FROM Survey s ORDER BY s.createdAt DESC)를 자동으로 생성하고 실행
* */