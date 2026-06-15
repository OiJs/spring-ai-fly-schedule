package com.nhnacademy.aiflyschedule.agent;

import com.nhnacademy.aiflyschedule.dto.response.FlightInfoResponse;
import com.nhnacademy.aiflyschedule.service.ApiClientService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlightSearchAgent {
    private final ApiClientService apiClientService;

    /**
     * 항공편 API 호출
     * 외부 api 호출 실패 시 2회 재시도
     * @param depCode 출발공항 Code
     * @param arrCode 도착공항 Code
     * @param formattedDate formatting된 날짜 (yyyyMMdd)
     * @return 항공편 정보 리스트 (그룹화 X)
     */

    //TODO retry로직 수정 필요
    // agent가 retry까지 전담하는게 맞는지?
    // ApiClientService에서 재시도 로직 처리하는게 더 적합 한가
    // spring-retry 고려? 학습 필요

    public List<FlightInfoResponse> searchFlights(String depCode,
                                                   String arrCode,
                                                   String formattedDate) {
        int maxRetries = 2;
        int retryCount = 0;

        while (retryCount <= maxRetries) {
            try {
                log.info("FlightSearchAgent: 항공편 API 호출 시도 {}/{} ({} -> {} / {})", 
                         retryCount + 1, maxRetries, depCode, arrCode, formattedDate);
                return apiClientService.getFlightSchedule(depCode, arrCode, formattedDate);
            } catch (Exception e) {
                if (retryCount == maxRetries) {
                    log.error("FlightSearchAgent: 최대 재시도 횟수 초과 ({}). API 호출 실패.", maxRetries);
                    return List.of();
                }
                retryCount++;
                log.warn("FlightSearchAgent: API 호출 실패. 재시도 {}/{}: {}", retryCount, maxRetries, e.getMessage());

                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return List.of();
    }
}
