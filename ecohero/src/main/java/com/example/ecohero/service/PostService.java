package com.example.ecohero.service;

import com.example.ecohero.entity.Post;
import com.example.ecohero.entity.User;
import com.example.ecohero.repository.PostRepository;
import com.example.ecohero.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // 전체 게시글 조회
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // 게시글 ID로 조회
    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
    }

    // 사용자가 작성한 게시글 조회 (사용자 ID 기준)
    public List<Post> getPostsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return postRepository.findByAuthor(user);  // User 기준으로 게시글 조회
    }


    // 게시글 생성
    @Transactional
    public void createPost(Post post) {
        // author가 userRepository에 존재하는지 확인
        User author = post.getAuthor();
        if (author != null && !userRepository.existsById(author.getId())) {
            throw new IllegalArgumentException("해당 author가 존재하지 않습니다.");
        }

        // author가 존재하면 그대로 저장
        postRepository.save(post);
    }

    @Transactional
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    @Transactional
    public void updatePost(Post post) {
        postRepository.save(post);
    }

    // 게시글 좋아요 토클 처리
    @Transactional
    public boolean toggleLike(Long postId, User user) {
        Post post = getPostById(postId);

        post.toggleLike(user); // 상태 토글
        postRepository.save(post); // 변경 사항 저장

        return post.getLikedUsers().contains(user); // 현재 상태 반환
    }

    // 좋아요 누른 사용자
    public boolean isLikedByUser(Long postId, User user) {
        Post post = getPostById(postId);
        return post.getLikedUsers().contains(user);
    }
}
