package com.nhnacademy.aiflyschedule.controller;

import com.nhnacademy.aiflyschedule.dto.request.LlmRequest;
import com.nhnacademy.aiflyschedule.dto.response.ChatApiResponse;
import com.nhnacademy.aiflyschedule.service.SimpleChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class LlmChatController {
    private final SimpleChatService chatService;

    @PostMapping("/ollama")
    public ChatApiResponse askOllama(@RequestBody LlmRequest request) {
        return chatService.askOllama(request);
    }

    @PostMapping("/gemini")
    public ChatApiResponse askGemini(@RequestBody LlmRequest request) {
        return chatService.askGemini(request);
    }
}
