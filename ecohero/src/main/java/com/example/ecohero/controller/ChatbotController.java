package com.example.ecohero.controller;

import com.example.ecohero.service.ChatbotService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController // 데이터만 반환하는 컨트롤러
public class ChatbotController {

    private final ChatbotService chatbotService;

    public ChatbotController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    @GetMapping("/api/chatbot")
    public String getChatbotResponse(@RequestParam(required = false) String region,
                                     @RequestParam String material) {
        return chatbotService.getRecyclingRule(region != null && !region.isBlank() ? region : null, material);
    }

}
