package com.nhnacademy.aiflyschedule.agent;

import com.nhnacademy.aiflyschedule.dto.request.FlightSearchRequest;
import com.nhnacademy.aiflyschedule.dto.response.AirlineGroupResponse;
import com.nhnacademy.aiflyschedule.dto.response.FlightInfoResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 멀티 에이전트 오케스트레이터
 *
 * 여러 에이전트를 조율하여 항공편 검색, 가격/시간 필터링 작업을 통합 수행합니다.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MultiAgentOrchestrator {
    private final FlightSearchAgent flightSearchAgent;
    private final TimeFilterAgent timeFilterAgent;
    private final PriceFilterAgent priceFilterAgent;
    private final DateParserAgent dateParserAgent;
    private final AirportCodeAgent airportCodeAgent;
    private final GroupingAgent groupingAgent;

    /**
     * 통합 항공편 검색 조율
     * 파이프라인을 따라 에이전트들을 호출하고 필터링된 리스트 데이터를 반환합니다.
     */
    public List<AirlineGroupResponse> coordinateSearch(FlightSearchRequest request) {
        
        log.info("MultiAgentOrchestrator: 통합 검색 조율 시작 (출발={}, 도착={}, 날짜={})", 
                request.departure(), request.arrival(), request.date());

        // 1. 날짜 파싱
        String formattedDate = dateParserAgent.parseDate(request.date());

        // 2. 공항 코드 변환
        String depCode = airportCodeAgent.getAirportCode(request.departure());
        String arrCode = airportCodeAgent.getAirportCode(request.arrival());

        // 3. 항공편 검색 (재시도 로직은 FlightSearchAgent 내부에 구현됨)
        List<FlightInfoResponse> flights = flightSearchAgent.searchFlights(depCode, arrCode, formattedDate);

        if (flights.isEmpty()) {
            return List.of();
        }

        // 4. 항공사별 그룹핑
        List<AirlineGroupResponse> groups = groupingAgent.groupByAirline(flights);

        // 5. 가격 필터 적용
        if (request.minPrice() != null || request.maxPrice() != null) {
            groups = priceFilterAgent.groupByPrice(groups, request.minPrice(), request.maxPrice());
        }

        // 6. 시간 필터 적용
        if (request.afterTime() != null && !request.afterTime().isBlank()) {
            groups = timeFilterAgent.groupByAfterTime(groups, request.afterTime());
        }

        log.info("MultiAgentOrchestrator: 통합 검색 조율 완료");
        
        return groups;
    }

    /**
     * 기본 검색 조율 (FlightSearchTool 등에서의 개별 호출 대비)
     */
    public List<AirlineGroupResponse> coordinateBasicSearch(String departure,
                                                            String arrival,
                                                            String date) {
        String formattedDate = dateParserAgent.parseDate(date);
        String depCode = airportCodeAgent.getAirportCode(departure);
        String arrCode = airportCodeAgent.getAirportCode(arrival);
        
        List<FlightInfoResponse> flights = flightSearchAgent.searchFlights(depCode, arrCode, formattedDate);
        if (flights.isEmpty()) {
            return List.of();
        }
        
        return groupingAgent.groupByAirline(flights);
    }
}
