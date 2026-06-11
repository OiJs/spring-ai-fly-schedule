package com.nhnacademy.aiflyschedule.mcp;

import com.nhnacademy.aiflyschedule.agent.MultiAgentOrchestrator;
import com.nhnacademy.aiflyschedule.context.FlightSearchContext;
import com.nhnacademy.aiflyschedule.dto.request.FlightSearchRequest;
import com.nhnacademy.aiflyschedule.dto.response.AirlineGroupResponse;
import com.nhnacademy.aiflyschedule.dto.response.FlightSearchResult;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

/**
 * 항공편 검색 도구 (MCP Tool)
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FlightSearchTool {

    private final MultiAgentOrchestrator orchestrator;

    @Tool(
            description = "항공편을 검색하여 항공사별로 그룹핑하여 반환합니다. " +
                    "출발 공항, 도착 공항, 날짜를 받아 항공사별로 정리된 항공편 목록을 제공합니다. " +
                    "날짜는 '내일', '모레', '2026-03-10'형식을 지원합니다. " +
                    "빠른 응답을 위해서 항공사별 최대 3편만 반환합니다. " +
                    "[중요 지시사항] 이 도구를 사용하여 비행기를 검색한 경우, 절대로 검색된 비행기 상세 목록(시간, 가격 등)을 응답 메시지에 나열하지 마세요. " +
                    "단순히 '검색이 완료되었습니다. 결과를 확인해주세요.' 라고만 짧게 대답하세요."
    )
    public FlightSearchResult searchFlightsByAirline(
            @ToolParam(description = "출발 공항 이름 (예: 광주, 김포, 제주)") String departure,
            @ToolParam(description = "도착 공항 이름 (예: 제주, 김포, 부산)") String arrival,
            @ToolParam(description = "날짜 (예: 내일, 모레, 2026-03-10)") String date,
            @ToolParam(description = "원하는 출발 시간 기준 (예: 14:00, 09:00). 조건이 없으면 빈 문자열", required = false) String afterTime,
            @ToolParam(description = "최소 가격 (조건이 없으면 null)", required = false) Integer minPrice,
            @ToolParam(description = "최대 가격 (조건이 없으면 null)", required = false) Integer maxPrice) {

        log.info("MCP Tool 호출: searchFlights(dep={}, arr={}, date={}, time={}, min={}, max={})",
                departure, arrival, date, afterTime, minPrice, maxPrice);

        FlightSearchRequest request = new FlightSearchRequest(
                departure, arrival, date, afterTime, minPrice, maxPrice);

        FlightSearchResult result = orchestrator.coordinateSearch(request);

        // 검색 결과가 있으면 컨텍스트에 저장 자연어+DTO 지원
        FlightSearchContext.setResult(result);

        return result;
    }
}
