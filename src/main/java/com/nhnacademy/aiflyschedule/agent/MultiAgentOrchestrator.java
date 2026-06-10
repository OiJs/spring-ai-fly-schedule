package com.nhnacademy.aiflyschedule.agent;

import com.nhnacademy.aiflyschedule.dto.response.FlightInfoResponse;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
/**
 * 멀티 에이전트 오케스트레이터
 *
 * 여러 에이전트를 조율하여 항공편 검색 및 추천 작업을 수행합니다.
 */
public class MultiAgentOrchestrator {
    private final FlightSearchAgent flightSearchAgent;
    private final TimeFilterAgent timeFilterAgent;
    private final PriceFilterAgent priceFilterAgent;

    public Map<String, List<FlightInfoResponse>> coordinateBasicSearch(String departure,
                                                                       String arrival,
                                                                       String date) {
        int maxRetries = 3;
        int retryCount = 0;
        log.info("========================================");
        log.info("MultiAgentOrchestrator: 기본 검색 조율 시작");
        log.info("========================================");

        while(retryCount < maxRetries) {
            try {
                Map<String, List<FlightInfoResponse>> result = flightSearchAgent.searchAndGroupByAirline(departure, arrival, date);

                log.info("========================================");
                log.info("MultiAgentOrchestrator: 기본 검색 조율 완료 (재시도 횟수: {})", retryCount);
                log.info("========================================");

                return result;
            } catch (Exception e) {
                retryCount++;
                if(retryCount >= maxRetries) {
                    log.error("최대 재시도 횟수 초과: {}", maxRetries);
                    throw new RuntimeException("항공편 검색 실패", e);
                }
                log.warn("재시도 {}/{}: {}", retryCount, maxRetries, e.getMessage());

                try {
                    Thread.sleep(1000 * retryCount);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("재시도 중단", ex);
                }
            }
        }
        return Collections.emptyMap();
    }

    public Map<String, List<FlightInfoResponse>> coordinateTimeFilterSearch(String departure,
                                                                            String arrival,
                                                                            String date,
                                                                            String afterTime) {
        log.info("========================================");
        log.info("MultiAgentOrchestrator: 시간 필터 검색 조율 시작");
        log.info("시간 조건: {} 이후", afterTime);
        log.info("========================================");

        Map<String, List<FlightInfoResponse>> allFlights = flightSearchAgent.searchAndGroupByAirline(departure, arrival, date);

        log.info("시간 필터링 적용");
        Map<String, List<FlightInfoResponse>> filtered = timeFilterAgent.groupByAfterTime(allFlights, afterTime);

        log.info("========================================");
        log.info("MultiAgentOrchestrator: 시간 필터 검색 조율 완료");
        log.info("========================================");

        return filtered;
    }

    public Map<String, List<FlightInfoResponse>> coordinatePriceFilterSearch(String departure,
                                                                             String arrival,
                                                                             String date,
                                                                             Integer minPrice,
                                                                             Integer maxPrice) {
        log.info("========================================");
        log.info("MultiAgentOrchestrator: 가격 필터 검색 조율 시작");
        log.info("가격 조건: {} ~ {}원", minPrice, maxPrice);
        log.info("========================================");

        Map<String, List<FlightInfoResponse>> allFlights = flightSearchAgent.searchAndGroupByAirline(departure, arrival, date);

        log.info("가격 필터링 적용");
        Map<String, List<FlightInfoResponse>> filtered = priceFilterAgent.groupByPrice(allFlights, minPrice, maxPrice);

        log.info("========================================");
        log.info("MultiAgentOrchestrator: 가격 필터 검색 조율 완료");
        log.info("========================================");

        return filtered;
    }
}
