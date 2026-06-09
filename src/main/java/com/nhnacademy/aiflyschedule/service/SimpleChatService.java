package com.nhnacademy.aiflyschedule.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class SimpleChatService {

    private final ChatClient ollamaChatClient;
    private final ChatClient geminiChatClient;

    public SimpleChatService(ChatClient.Builder ollamaChatClientBuilder, ChatClient.Builder geminiChatClientBuilder) {
        this.ollamaChatClient = ollamaChatClientBuilder.build();
        this.geminiChatClient = geminiChatClientBuilder.build();
    }
    public String askOllama(String question) {
        return ollamaChatClient.prompt()        // 1. 프롬프트 빌더 시작
                .user(question)                  // 2. 사용자 질문 설정
                .call()                          // 3. LLM 호출 (동기, blocking)
                .content();                      // 4. 응답 내용 반환 (String)
    }

    public String askGemini(String question) {
        return geminiChatClient.prompt()
                .user(question)
                .call()
                .content();
    }
}
