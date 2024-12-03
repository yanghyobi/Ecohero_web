package com.example.ecohero.controller;

import com.example.ecohero.entity.Comment;
import com.example.ecohero.entity.Post;
import com.example.ecohero.entity.User;
import com.example.ecohero.service.CommentService;
import com.example.ecohero.service.PostService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;

    public CommentController(CommentService commentService, PostService postService) {
        this.commentService = commentService;
        this.postService = postService;
    }

    /**
     * 댓글 생성 요청을 처리
     *
     * @param postId  댓글이 달릴 게시글의 ID
     * @param content 댓글 내용
     * @return 게시글 상세 페이지로 리다이렉트
     */
    @PostMapping("/{postId}")
    public String addComment(
            @PathVariable Long postId,
            @RequestParam String content,
            Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 현재 인증된 사용자 가져오기
        if (authentication == null || !authentication.isAuthenticated()) {
            System.out.println("인증되지 않은 사용자입니다."); // 디버깅 로그
            return "redirect:/user/login";
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof User)) {
            System.out.println("인증 객체가 User 타입이 아닙니다."); // 디버깅 로그
            return "redirect:/user/login";  // 인증되지 않은 경우 로그인 페이지로 리다이렉트
        }

        // 사용자 정보 가져오기
        User user = (User) principal;

        // 게시글 가져오기
        Post post = postService.getPostById(postId);

        // 댓글 생성
        commentService.createComment(content, post, user);

        // 댓글 작성 후 게시글 상세 페이지로 리다이렉트
        return "redirect:/posts/" + postId;
    }


    // 댓글 좋아요 처리
    @PostMapping("/{commentId}/like")
    public String likeComment(@PathVariable Long commentId) {
        // 현재 인증된 사용자 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/user/login"; // 인증되지 않은 경우 로그인 페이지로 리다이렉트
        }

        User user = (User) authentication.getPrincipal();
        commentService.likeComment(commentId, user); // 댓글 좋아요 처리

        return "redirect:/posts/" + commentId; // 게시글 상세 페이지로 리다이렉트
    }
}
