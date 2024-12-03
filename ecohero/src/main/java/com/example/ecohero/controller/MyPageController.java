package com.example.ecohero.controller;

import com.example.ecohero.entity.User;
import com.example.ecohero.entity.Post;
import com.example.ecohero.service.UserService;
import com.example.ecohero.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/mypage")
public class MyPageController {

    private final UserService userService;
    private final PostService postService;

    public MyPageController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    // 마이페이지
    @GetMapping
    public String showMyPage(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof User) {
            User user = (User) principal;

            model.addAttribute("user", user);

            // 사용자가 작성한 게시글 목록 가져오기
            List<Post> posts = postService.getPostsByUserId(user.getId());
            model.addAttribute("posts", posts);

            return "mypage"; // mypage.html 템플릿 반환
        } else {
            return "redirect:/user/login"; // 로그인되지 않은 경우 로그인 페이지로 리디렉션
        }
    }

    // 내 정보 수정 페이지
    @GetMapping("/edit/{id}")
    public String showEditUserInfoPage(@PathVariable Long id, Model model) {
        // 사용자 정보 가져오기
        User user = userService.findById(id);

        if (user == null) {
            return "error/404"; // 사용자 정보가 없을 경우 404 페이지 반환
        }

        model.addAttribute("user", user);
        return "updateInfo"; // updateInfo.html 템플릿 반환
    }

    // 사용자 정보 업데이트 (POST)
    @PostMapping("/update")
    public String updateUser(@ModelAttribute User user, MultipartFile profileImage, Model model) {
        try {
            // 사용자 정보 업데이트
            userService.updateUserProfile(user.getId(), user.getEmail(), user.getPhone(), user.getAddress(),
                    user.getBirthdate());

            // 세션에 저장된 사용자 정보 갱신
            User updatedUser = userService.findById(user.getId());

            // 새로 업데이트된 사용자 정보를 SecurityContext에 갱신
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(updatedUser, null, updatedUser.getAuthorities()));

            // 변경된 사용자 정보 다시 마이페이지에 반영
            model.addAttribute("user", updatedUser);

        } catch (Exception e) {
            model.addAttribute("error", "사용자 정보를 업데이트하는 중 오류가 발생했습니다.");
            return "updateInfo"; // 오류가 발생한 경우 수정 페이지로 돌아감
        }

        return "redirect:/mypage"; // 업데이트 후 리다이렉트
    }

    @GetMapping("/data")
    public ResponseEntity<Map<String, Object>> getUserData() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof User) {
            User user = (User) principal;

            Map<String, Object> userData = new HashMap<>();
            userData.put("username", user.getUsername());
            userData.put("name", user.getName());
            userData.put("birthdate", user.getBirthdate());
            userData.put("phone", user.getPhone());
            userData.put("email", user.getEmail());
            userData.put("postalCode", user.getZipcode());
            userData.put("address1", user.getAddress());
            userData.put("address2", ""); // 비어 있음

            return ResponseEntity.ok(userData);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
