package com.nhnacademy.aiflyschedule.mcp;

import com.nhnacademy.aiflyschedule.dto.response.AirlineInfoResponse;
import com.nhnacademy.aiflyschedule.service.ApiClientService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 항공사 정보 MCP Tool
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AirlineInfoTool {

    private final ApiClientService apiClientService;

    // 항공사명 → ID 캐시
    private Map<String, String> airlineIdCache;

    /**
     * 전체 항공사 목록을 조회합니다.
     */
    @Tool(
            description = "전체 항공사 목록을 조회합니다. " +
                    "국내 모든 항공사의 코드와 이름을 반환합니다. " +
                    "사용자가 '항공사 리스트', '어떤 항공사가 있어?' 등을 물을 때 사용합니다."
    )
    public List<AirlineInfoResponse> getAirlineList() {
        log.info("MCP Tool 호출: getAirlineList()");

        List<AirlineInfoResponse> airlines = apiClientService.getAirlineInfo();

        // 캐시 업데이트
        airlineIdCache = airlines.stream()
                .collect(Collectors.toMap(
                        AirlineInfoResponse::airlineName,
                        AirlineInfoResponse::airlineId
                ));

        return airlines;
    }

    /**
     * 항공사 이름으로 항공사 ID를 조회합니다.
     */
    @Tool(
            description = "항공사 이름으로 항공사 ID를 조회합니다. " +
                    "항공사 이름을 입력하면 해당 항공사의 IATA 코드를 반환합니다. " +
                    "지원하는 항공사: 대한항공, 아시아나항공, 제주항공, 에어부산, 에어서울, 진에어, 티웨이항공 등"
    )
    public String getAirlineId(
            @ToolParam(description = "항공사 이름 (예: 아시아나항공, 대한항공, 제주항공)") String airlineName) {

        log.info("MCP Tool 호출: getAirlineId(airlineName={})", airlineName);

        // 캐시가 없으면 먼저 로드
        if (airlineIdCache == null) {
            getAirlineList();
        }

        String id = airlineIdCache.get(airlineName);

        if (id == null) {
            log.warn("항공사 ID를 찾을 수 없음: {}", airlineName);
            return "알 수 없는 항공사입니다: " + airlineName;
        }

        log.info("항공사 ID 조회 결과: {} → {}", airlineName, id);
        return id;
    }
}