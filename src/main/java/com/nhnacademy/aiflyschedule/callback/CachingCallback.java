package com.nhnacademy.aiflyschedule.callback;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//TODO Spring @Cacheable, Redis 이용한다면
// 굳이 이거 필요한가?
// 도구 성격 여부에 따라 캐싱 여부 신중히 결정
public class CachingCallback {
    private static final Logger log = LoggerFactory.getLogger(CachingCallback.class);
    private final Map<String, Object> cache = new ConcurrentHashMap<>();

    public void afterToolCall(String toolName, Object result) {
        String cacheKey = toolName + result.hashCode();
        cache.put(cacheKey, result);
        log.info("결과 캐싱: {} (키: {})", toolName, cacheKey);
    }

     public Object getCacheResult(String toolName, String params) {
        String cacheKey = toolName + params.hashCode();
        Object cashed = cache.get(cacheKey);

        if(cashed != null) {
            log.info("캐시 히트: {}", cacheKey);
        }
        return cashed;
    }
}
