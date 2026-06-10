package com.nhnacademy.aiflyschedule.service;

import com.nhnacademy.aiflyschedule.context.FlightSearchContext;
import com.nhnacademy.aiflyschedule.dto.request.LlmRequest;
import com.nhnacademy.aiflyschedule.dto.response.ChatApiResponse;
import com.nhnacademy.aiflyschedule.dto.response.FlightSearchResult;
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

    public ChatApiResponse askOllama(LlmRequest request) {
        if(request == null || request.message() == null || request.message().isBlank()) {
            throw new IllegalArgumentException("메시지가 없습니다.");
        }
        
        try {
            // LLM 호출 (Tool 자동 실행 포함)
            String llmMessage = ollamaChatClient.prompt()
                    .user(request.message())
                    .call()
                    .content();
            
            // Tool 실행 중 컨텍스트에 저장된 그룹핑된 데이터 조회
            FlightSearchResult searchResult = FlightSearchContext.getResult();
            
            // 하이브리드 응답 반환
            return new ChatApiResponse(llmMessage, searchResult);
        } finally {
            // ★ 중요: 현재 스레드의 요청 처리가 끝나면 반드시 메모리 해제
            FlightSearchContext.clear();
        }
    }

    public ChatApiResponse askGemini(LlmRequest request) {
        if(request == null || request.message() == null || request.message().isBlank()) {
            throw new IllegalArgumentException("메시지가 없습니다.");
        }
        
        try {
            String llmMessage = geminiChatClient.prompt()
                    .user(request.message())
                    .call()
                    .content();
                    
            FlightSearchResult searchResult = FlightSearchContext.getResult();
            
            return new ChatApiResponse(llmMessage, searchResult);
        } finally {
            FlightSearchContext.clear();
        }
    }
}
