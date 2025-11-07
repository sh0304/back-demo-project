package com.example.backdemoproject.repository;

import com.example.backdemoproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  List<User> findAllByOrderByNameAsc();

  Optional<User> findByName(String userName);

  boolean existsByName(String userName);

  boolean existsByEmail(String email);
}