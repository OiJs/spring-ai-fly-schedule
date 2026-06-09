package com.nhnacademy.aiflyschedule.controller;

import com.nhnacademy.aiflyschedule.service.SimpleChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatTestController {
    private final SimpleChatService chatService;

    @GetMapping("/ollama")
    public String askOllama(@RequestParam String question) {
        long startTime = System.currentTimeMillis();

        String response = chatService.askOllama(question);

        long endTime = System.currentTimeMillis();

        String duration = (endTime - startTime) / 1000.0 + "초";

        return "[Ollama 응답 (" + duration + ")]\n" + response;
    }

    @GetMapping("/gemini")
    public String askGemini(@RequestParam String question) {
        long startTime = System.currentTimeMillis();

        String response = chatService.askGemini(question);

        long endTime = System.currentTimeMillis();
        String duration = (endTime - startTime) / 1000.0 + "초";

        return "[Gemini 응답 (" + duration + ")]\n" + response;
    }
}
