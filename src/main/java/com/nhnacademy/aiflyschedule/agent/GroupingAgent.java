package com.nhnacademy.aiflyschedule.agent;

import com.nhnacademy.aiflyschedule.dto.response.AirlineGroupResponse;
import com.nhnacademy.aiflyschedule.dto.response.FlightInfoResponse;
import com.nhnacademy.aiflyschedule.dto.response.FlightSearchResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
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
     * @return 항공사별로 그룹핑된 항공편 검색 결과
     */
    public FlightSearchResult groupByAirline(List<FlightInfoResponse> flights) {
        log.info("GroupingAgent: 항공사별 그룹핑 시작 ({}편)", flights.size());

        List<AirlineGroupResponse> groups = flights.stream()
                .collect(Collectors.groupingBy(FlightInfoResponse::airlineName))
                .entrySet().stream()
                .map(entry -> {
                    log.info("  {}: {}편", entry.getKey(), entry.getValue().size());
                    return new AirlineGroupResponse(entry.getKey(), entry.getValue());
                })
                .collect(Collectors.toList());

        log.info("GroupingAgent: 그룹핑 완료 ({}개 항공사)", groups.size());
        return new FlightSearchResult(groups);
    }
}
