package com.nhnacademy.aiflyschedule.callback;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//TODO ErrorHandling 왜 Callback으로 처리해야 하는지?
// ExceptionHandler로 전역 처리 가능
@Component
public class ErrorHandlingCallback {
    private static final Logger log = LoggerFactory.getLogger(ErrorHandlingCallback.class);

    public Object handlerToolError(String toolName, Exception e) {
        log.error("Tool 실행 실패: {}", toolName, e);

        return Map.of(
                "error", true,
                "message", "일시적인 오류가 발생했습니다. 다시 시도해주세요.",
                "toolName", toolName
        );
    }
}
