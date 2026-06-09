package com.nhnacademy.aiflyschedule.service;

import com.nhnacademy.aiflyschedule.tool.DateTimeTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class FunctionCallTestService {

    private final ChatClient chatClient;
    private final DateTimeTool dateTimeTool;

    public FunctionCallTestService(ChatClient.Builder chatClientBuilder, DateTimeTool dateTimeTool) {
        this.chatClient = chatClientBuilder.build();
        this.dateTimeTool = dateTimeTool;
    }

    /**
     * Function Calling 테스트
     */
    public String testFunctionCalling(String userMessage) {
        return chatClient.prompt()
                .user(userMessage)
                .tools(dateTimeTool)
                .call()
                .content();
    }
}