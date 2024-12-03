package com.example.ecohero.repository;

import com.example.ecohero.entity.Post;
import com.example.ecohero.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    // User 기준으로 게시글 조회
    List<Post> findByAuthor(User author); // 사용자가 작성한 게시글을 조회하는 메서드
}
