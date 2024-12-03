package com.example.ecohero.controller;

import com.example.ecohero.entity.Post;
import com.example.ecohero.entity.User;
import com.example.ecohero.service.UserService;
import com.example.ecohero.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final PostService postService;

    public UserController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    // 사용자 정보를 조회하는 메서드
    @GetMapping("/{id}")
    public String getUserProfile(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        if (user == null) {
            model.addAttribute("error", "사용자를 찾을 수 없습니다.");
            return "error";  // 사용자 찾을 수 없으면 에러 페이지로 이동
        }
        model.addAttribute("user", user);

        // 사용자가 작성한 게시글 목록 가져오기
        List<Post> posts = postService.getPostsByUserId(id); // 해당 사용자가 작성한 게시글 목록
        model.addAttribute("posts", posts);

        return "mypage";  // 마이페이지로 이동
    }

    // 사용자 정보 수정 페이지
    @GetMapping("/edit/{id}")
    public String editUserProfile(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        if (user == null) {
            model.addAttribute("error", "사용자를 찾을 수 없습니다.");
            return "error";
        }
        model.addAttribute("user", user);
        return "editProfile";
    }

    // 사용자 정보 수정 처리
    @PostMapping("/edit/{id}")
    public String updateUserProfile(
            @PathVariable Long id,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String address,
            @RequestParam String birthdate,
            Model model
    ) {
        try {
            userService.updateUserProfile(id, email, phone, address, birthdate);
            return "redirect:/user/" + id;
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "editProfile"; // 사용자 정보 수정 페이지
        }
    }

    // 회원가입
    @GetMapping("/signup")
    public String showSignupPage() {
        return "signup";  // 회원가입 페이지로 이동
    }

    @PostMapping("/signup")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String email,
            @RequestParam String name,
            @RequestParam String phone1,
            @RequestParam String phone2,
            @RequestParam String phone3,
            @RequestParam String address,
            @RequestParam String zipcode,
            @RequestParam String birthdate,
            @RequestParam(required = false) MultipartFile profileImage,
            Model model
    ) {
        try {
            String phone = String.join("-", phone1, phone2, phone3); // 조합하여 저장
            userService.registerUser(username, password, email, name, phone, address, zipcode, birthdate, profileImage);
            model.addAttribute("message", "회원가입이 완료되었습니다.");
            return "redirect:/user/login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "signup";
        }
    }

    @GetMapping("/user/check-username")
    @ResponseBody
    public Map<String, Boolean> checkUsernameAvailability(@RequestParam String username) {
        boolean isAvailable = userService.isUsernameAvailable(username);
        Map<String, Boolean> response = new HashMap<>();
        response.put("available", isAvailable);
        return response;
    }

    // 사용자 로그인
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";  // 로그인 페이지
    }

    @PostMapping("/user/login")
    public String loginUser(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
        try {
            User user = userService.authenticateUser(username, password);

            // 사용자 인증 성공 후 SecurityContext에 인증 정보 저장
            Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 세션에 사용자 정보를 저장할 필요 없이, SecurityContext에 의해 관리됨
            session.setAttribute("user", user);

            return "redirect:/home"; // 로그인 성공 후 리다이렉트
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "login"; // 로그인 실패 시 다시 로그인 페이지로 돌아가기
        }
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            SecurityContextHolder.clearContext();  // 세션 정보 삭제
        }
        return "redirect:/home";  // 로그아웃 후 홈으로 리다이렉트
    }

    @GetMapping("/header")
    public String getHeader(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && !(authentication.getPrincipal() instanceof String)) {
            // 인증된 사용자만 가져옴
            User user = (User) authentication.getPrincipal();
            model.addAttribute("username", user.getUsername());
        } else {
            // 인증되지 않은 경우 anonymous 처리
            model.addAttribute("username", "anonymous");
        }
        return "header";
    }
}
