package com.example.ecohero.repository;

import com.example.ecohero.entity.Comment;
import com.example.ecohero.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);
}
