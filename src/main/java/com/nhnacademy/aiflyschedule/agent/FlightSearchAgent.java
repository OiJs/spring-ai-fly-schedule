package com.nhnacademy.aiflyschedule.agent;

import com.nhnacademy.aiflyschedule.dto.response.FlightInfoResponse;
import com.nhnacademy.aiflyschedule.dto.response.FlightSearchResult;
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
    private final DateParserAgent dateParserAgent;
    private final AirportCodeAgent airportCodeAgent;
    private final GroupingAgent groupingAgent;

    /**
     * 항공사 검색, 항공사별 그룹핑
     * @param departure 출발 공항 이름
     * @param arrival 도착 공항 이름
     * @param date 날짜
     * @return 항공사별로 그룹화된 항공편 결과 DTO
     */
    public FlightSearchResult searchAndGroupByAirline(String departure,
                                                       String arrival,
                                                       String date) {

        log.info("FlightSearchAgent: 항공편 검색 시작");

        log.info("  단계 1: 날짜 파싱");
        String formattedDate = dateParserAgent.parseDate(date);
        log.info("  → 날짜: {} → {}", date, formattedDate);

        log.info("  단계 2: 공항 코드 변환");
        String depCode = airportCodeAgent.getAirportCode(departure);
        String arrCode = airportCodeAgent.getAirportCode(arrival);

        log.info("  → 출발: {} → {}", departure, depCode);
        log.info("  → 도착: {} → {}", arrival, arrCode);

        log.info("  단계 3: 항공편 API 호출");
        List<FlightInfoResponse> flights = apiClientService.getFlightSchedule(depCode, arrCode, formattedDate);
        log.info("  → {}편 조회 완료", flights.size());

        log.info("  단계 4: 항공사별 그룹핑");
        FlightSearchResult result = groupingAgent.groupByAirline(flights);
        log.info("FlightSearchAgent: 항공편 검색 완료 ({}개 항공사)", result.airlineGroups().size());

        return result;
    }
}
