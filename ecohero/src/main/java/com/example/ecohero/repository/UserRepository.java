package com.example.ecohero.repository;

import com.example.ecohero.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 사용자명을 기준으로 사용자 조회
    Optional<User> findByUsername(String username);

    // 이메일을 기준으로 사용자 조회
    Optional<User> findByEmail(String email);

    boolean existsById(Long id);  // id가 존재하는지 확인하는 메서드

}
