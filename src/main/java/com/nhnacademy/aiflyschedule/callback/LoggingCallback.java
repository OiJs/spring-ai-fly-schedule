package com.nhnacademy.aiflyschedule.callback;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;

/**
 * Tool Calling 실행 과정을 logging 하는 Callback
 */
//TODO: Logging
// Logging을 위해 모든 Tool과 Service에서 호출해서 써야 하는지?
// AOP 적용 필요
@Component
public class LoggingCallback {
    private static final Logger log = LoggerFactory.getLogger(LoggingCallback.class);

    private final Map<String, Long> toolExecutionTimes = new ConcurrentHashMap<>();

    public void beforeToolCall(String toolName, Map<String, Object> arguments) {
        log.info("========================================");
        log.info(" Tool 호출 시작");
        log.info("Tool 이름: {}", toolName);
        log.info("파라미터: {}", arguments);
        log.info("========================================");

        toolExecutionTimes.put(toolName, System.currentTimeMillis());
    }

    public void afterToolCall(String toolName, Object result) {
        Long startTime = toolExecutionTimes.get(toolName);
        long duration = startTime != null ? System.currentTimeMillis() - startTime : 0;

        log.info("========================================");
        log.info(" Tool 호출 완료");
        log.info("Tool 이름: {}", toolName);
        log.info("실행 시간: {}ms", duration);
        log.info("결과 타입: {}", result != null ? result.getClass().getSimpleName() : "null");

        // 결과가 너무 크면 요약해서 로깅
        if (result != null) {
            String resultStr = result.toString();
            if (resultStr.length() > 500) {
                log.info("결과 (요약): {}... (총 {}글자)",
                        resultStr.substring(0, 500), resultStr.length());
            } else {
                log.info("결과: {}", resultStr);
            }
        }
        log.info("========================================");

        toolExecutionTimes.remove(toolName);
    }

    public void onResponse(String request, ChatResponse response, long duration) {
        log.info("========================================");
        log.info(" LLM 응답 생성 완료");
        log.info("요청: {}", request);
        log.info("전체 실행 시간: {}ms ({}초)", duration, duration / 1000.0);

        // Tool 호출 횟수 확인
        if (response != null && response.getMetadata() != null) {
            ChatResponseMetadata metadata = response.getMetadata();
            log.info("모델: {}", metadata.getModel());
            log.info("토큰 사용량: {}", metadata.getUsage());
        }

        // 생성된 응답
        if (response != null && !response.getResults().isEmpty()) {
            String content = response.getResult().getOutput().getText();
            log.info("응답: {}", content);
        }
        log.info("========================================");
    }
}
