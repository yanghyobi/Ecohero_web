package com.example.ecohero.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String showHomePage(Model model) {
        model.addAttribute("message", "Welcome to the homepage!");
        model.addAttribute("chatbotEnabled", true); // 챗봇 활성화 상태 전달
        return "home"; // home.html로 렌더링
    }
}
