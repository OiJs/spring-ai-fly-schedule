package com.nhnacademy.aiflyschedule.mcp;

import com.nhnacademy.aiflyschedule.dto.response.AirportInfoResponse;
import com.nhnacademy.aiflyschedule.service.ApiClientService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CachedAirportInfoTool {
    private final ApiClientService apiClientService;

    private volatile List<AirportInfoResponse> cachedAirports;
    private volatile long cacheTime = 0;
    private static final long CACHE_DURATION = 10 * 60 * 1000;  // 10분

    @Tool(description = "전체 공항 목록 조회(캐싱)")
    public List<AirportInfoResponse> getCachedAirports() {
        long now = System.currentTimeMillis();
        if(cachedAirports != null && (now - cacheTime) < CACHE_DURATION) {
            log.info("캐시된 공항 목록 반환");
            return cachedAirports;
        }

        log.info("공항 목록 API 호출 및 캐싱");
        cachedAirports = apiClientService.getAirportInfo();
        cacheTime = now;

        return cachedAirports;
    }

    public void clearCache() {
        cachedAirports = null;
        cacheTime = 0;
        log.info("공항 목록 캐시가 초기화되었습니다");
    }
}
