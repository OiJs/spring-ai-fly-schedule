package com.nhnacademy.aiflyschedule.service;

import com.nhnacademy.aiflyschedule.callback.LoggingCallback;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;

@Service
public class MonitoredChatService {

    private final ChatClient chatClient;
    private final LoggingCallback loggingCallback;

    public MonitoredChatService(
            ChatClient.Builder chatClientBuilder,
            LoggingCallback loggingCallback) {

        this.chatClient = chatClientBuilder.build();
        this.loggingCallback = loggingCallback;
    }

    /**
     * Function Calling with Callback
     */
    public String chatWithMonitoring(String userMessage) {
        long startTime = System.currentTimeMillis();

        // LLM 호출
        ChatResponse response = chatClient.prompt()
                .user(userMessage)
                .call()
                .chatResponse();

        long duration = System.currentTimeMillis() - startTime;

        // Callback: onResponse 단계
        loggingCallback.onResponse(userMessage, response, duration);

        return response.getResult().getOutput().getText();
    }
}