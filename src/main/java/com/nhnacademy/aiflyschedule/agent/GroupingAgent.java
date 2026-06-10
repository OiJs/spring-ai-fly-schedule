package com.nhnacademy.aiflyschedule.agent;

import com.nhnacademy.aiflyschedule.dto.response.FlightInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 그룹핑 A2A 에이전트
 *
 * 항공편을 다양한 기준으로 그룹핑합니다.
 */
@Slf4j
@Service
public class GroupingAgent {

    /**
     * 항공사별로 항공편을 그룹핑합니다.
     *
     * @param flights 항공편 목록
     * @return 항공사별로 그룹핑된 항공편
     */
    public Map<String, List<FlightInfoResponse>> groupByAirline(List<FlightInfoResponse> flights) {
        log.info("GroupingAgent: 항공사별 그룹핑 시작 ({}편)", flights.size());

        Map<String, List<FlightInfoResponse>> grouped = flights.stream()
                .collect(Collectors.groupingBy(FlightInfoResponse::airlineName));

        grouped.forEach((airline, airlineFlights) -> {
            log.info("  {}: {}편", airline, airlineFlights.size());
        });

        log.info("GroupingAgent: 그룹핑 완료 ({}개 항공사)", grouped.size());
        return grouped;
    }

    /**
     * 시간대별로 항공편을 그룹핑합니다.
     */
    public Map<String, List<FlightInfoResponse>> groupByTimeSlot(List<FlightInfoResponse> flights) {
        log.info("GroupingAgent: 시간대별 그룹핑 시작");

        return flights.stream()
                .collect(Collectors.groupingBy(flight -> {
                    int hour = Integer.parseInt(flight.departureTime().substring(0, 2));

                    if (hour < 12) {
                        return "오전";
                    } else if (hour < 18) {
                        return "오후";
                    } else {
                        return "저녁";
                    }
                }));
    }
}