package com.nhnacademy.aiflyschedule.controller;

import com.nhnacademy.aiflyschedule.service.MonitoredChatService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/callback")
public class CallbackTestController {

    private final MonitoredChatService chatService;

    public CallbackTestController(MonitoredChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * Callback 테스트
     * GET /api/callback/test?message=내일광주에서제주로가는항공편알려줘
     */
    @GetMapping("/test")
    public String testCallback(@RequestParam String message) {
        return chatService.chatWithMonitoring(message);
    }
}