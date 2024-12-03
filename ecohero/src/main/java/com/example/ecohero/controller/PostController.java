package com.example.ecohero.controller;

import com.example.ecohero.entity.Comment;
import com.example.ecohero.entity.Post;
import com.example.ecohero.entity.User;
import com.example.ecohero.service.CommentService;
import com.example.ecohero.service.PostService;
import com.example.ecohero.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final CommentService commentService;

    public PostController(PostService postService, UserService userService, CommentService commentService) {
        this.postService = postService;
        this.userService = userService;
        this.commentService = commentService;
    }

    // 게시글 목록 페이지
    @GetMapping
    public String showPostList(Model model) {
        List<Post> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);

        User user = getCurrentUser();
        if (user != null) {
            model.addAttribute("user", user);
        }
        return "post_list";
    }

    // 게시글 작성 폼 페이지
    @GetMapping("/new")
    public String showPostForm(Model model) {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return "redirect:/user/login";
        }
        model.addAttribute("userId", userId);
        return "post_form";
    }

    // 게시글 작성 처리
    @PostMapping
    public String createPost(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam(required = false) MultipartFile image,
            Model model) {

        try {
            String imageUrl = image != null && !image.isEmpty() ? saveImage(image) : null;

            User user = getCurrentUser();
            if (user == null) {
                model.addAttribute("error", "로그인이 필요합니다.");
                return "redirect:/user/login";
            }

            Post post = new Post();
            post.setTitle(title);
            post.setContent(content);
            post.setImageUrl(imageUrl);
            post.setAuthor(user);

            postService.createPost(post);

            return "redirect:/posts";
        } catch (IOException e) {
            e.printStackTrace(); // 문제 디버깅을 위한 로그 출력
            model.addAttribute("error", "이미지 저장 중 오류 발생: " + e.getMessage());
            return "post_form";
        } catch (Exception e) {
            e.printStackTrace(); // 문제 디버깅을 위한 로그 출력
            model.addAttribute("error", "게시글 작성 중 오류 발생: " + e.getMessage());
            return "post_form";
        }
    }

    // 이미지 저장 메서드
    private String saveImage(MultipartFile image) throws IOException {
        if (image.isEmpty()) {
            return null;
        }

        System.out.println("Saving image: " + image.getOriginalFilename());

        // 업로드 디렉토리 설정 업로드 경로를 외부 디렉토리로 변경
        String uploadDir = "C:/ecohero/uploads/";
        File directory = new File(uploadDir);

        // 디렉토리 생성
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("업로드 디렉토리를 생성할 수 없습니다.");
        }

        // 파일 이름 설정
        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        File uploadFile = new File(directory, fileName);

        // 파일 저장
        image.transferTo(uploadFile);

        // 저장된 파일 URL 반환
        System.out.println("Image saved at: " + uploadFile.getAbsolutePath());
        return "/uploads/" + fileName;
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }

    // 현재 로그인한 사용자 ID 반환
    private Long getCurrentUserId() {
        User user = getCurrentUser();
        return user != null ? user.getId() : null;
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<Map<String, Object>> likePost(@PathVariable Long id) {
        User user = getCurrentUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        boolean isLiked = postService.toggleLike(id, user); // 상태 토글
        Post post = postService.getPostById(id);

        // 상태와 좋아요 개수 반환
        Map<String, Object> response = new HashMap<>();
        response.put("isLiked", isLiked);
        response.put("likesCount", post.getLikesCount());
        return ResponseEntity.ok(response);
    }


    // 좋아요 상태 확인 API
    @GetMapping("/{id}/like-status")
    public ResponseEntity<Boolean> getLikeStatus(@PathVariable Long id) {
        User user = getCurrentUser();
        if (user == null) {
            return ResponseEntity.ok(false);
        }

        boolean isLiked = postService.isLikedByUser(id, user);
        return ResponseEntity.ok(isLiked);
    }

    // 게시글 상세 페이지
    @GetMapping("/{id}")
    public String showPostDetail(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id);
        List<Comment> comments = commentService.getCommentsByPost(post);

        model.addAttribute("post", post);
        model.addAttribute("comments", comments);

        User currentUser = getCurrentUser();
        if (currentUser != null) {
            model.addAttribute("currentUser", currentUser); // 템플릿에서 사용하기 위해 currentUser로 전달
        }

        return "post_detail";
    }


    // comment
    @PostMapping("/{postId}/comments")
    public String addComment(@PathVariable Long postId, @RequestParam String content, Model model) {
        User author = getCurrentUser();
        if (author == null) {
            model.addAttribute("error", "로그인이 필요합니다.");
            return "redirect:/user/login";
        }

        try {
            Post post = postService.getPostById(postId);
            commentService.createComment(content, post, author);
            return "redirect:/posts/" + postId;
        } catch (Exception e) {
            model.addAttribute("error", "댓글 작성 중 오류가 발생했습니다: " + e.getMessage());
            return "post_detail";
        }
    }

    @PostMapping("/comments/{commentId}/like")
    public ResponseEntity<Boolean> likeComment(@PathVariable Long commentId) {
        User user = getCurrentUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }

        commentService.likeComment(commentId, user);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/{id}/edit")
    public String showEditPostForm(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id);

        User user = getCurrentUser();
        if (user == null || !post.getAuthor().equals(user)) {
            return "redirect:/user/login";
        }

        model.addAttribute("post", post);
        return "post_edit";
    }

    @PostMapping("/{id}/edit")
    public String updatePost(
            @PathVariable Long id,
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam(required = false) MultipartFile image,
            Model model) {

        try {
            Post post = postService.getPostById(id);
            User user = getCurrentUser();

            if (user == null || !post.getAuthor().equals(user)) {
                model.addAttribute("error", "권한이 없습니다.");
                return "redirect:/user/login";
            }

            String imageUrl = image != null && !image.isEmpty() ? saveImage(image) : post.getImageUrl();
            post.setTitle(title);
            post.setContent(content);
            post.setImageUrl(imageUrl);

            postService.updatePost(post);
            return "redirect:/posts/" + id;
        } catch (IOException e) {
            model.addAttribute("error", "이미지 저장 중 오류 발생: " + e.getMessage());
            return "post_edit";
        }
    }


    @PostMapping("/{id}/delete")
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        User user = getCurrentUser();
        Post post = postService.getPostById(id);

        if (user == null || !post.getAuthor().equals(user)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
        }

        postService.deletePost(id);
        return ResponseEntity.ok("게시글이 삭제되었습니다.");

    }
}

