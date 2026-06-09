package com.nhnacademy.aiflyschedule.callback;

import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 결과 필터링
 */
@Component
public class ResultFilteringCallback {
    private static final Logger log = LoggerFactory.getLogger(ResultFilteringCallback.class);

    public Object afterToolCall(String toolName, Object result) {
        if(result instanceof Map) {
            Map<?, ?> map = (Map<?,?>) result;

            if(map.size() > 100) {
                log.warn("결과가 너무 큽니다: {}개 항목 -> 50개로 제한", map.size());
                return map.entrySet().stream()
                        .limit(50)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            }
        }
        return result;
    }
}
