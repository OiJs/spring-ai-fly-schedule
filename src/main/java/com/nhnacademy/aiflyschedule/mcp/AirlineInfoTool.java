package com.nhnacademy.aiflyschedule.mcp;

import com.nhnacademy.aiflyschedule.agent.AirlineCodeAgent;
import com.nhnacademy.aiflyschedule.dto.response.AirlineInfoResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

/**
 * 항공사 정보 제공 도구
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AirlineInfoTool {
    private final AirlineCodeAgent airlineCodeAgent;

@Tool(
        description = "전체 항공사 목록을 조회합니다. " +
                "국내 모든 항공사의 코드와 이름을 반환합니다. " +
                "사용자가 '항공사 리스트', '어떤 항공사가 있어?' 등을 물을 때 사용합니다. " +
                "[중요 지시사항] 사용자에게 목록을 보여줄 때는 반드시 마크다운(Markdown) 글머리 기호('-')를 사용하고, 각 항공사마다 줄바꿈을 하여 세로로 길게 출력하세요."
    )
    public List<AirlineInfoResponse> getAirlineList() {
        log.info("MCP Tool 호출: getAirlineList()");
        return airlineCodeAgent.getAllAirlines();
    }

    @Tool(
            description = "항공사 이름으로 항공사 ID를 조회합니다. " +
                    "항공사 이름을 입력하면 해당 항공사의 IATA 코드를 반환합니다. " +
                    "지원하는 항공사: 대한항공, 아시아나항공, 제주항공, 에어부산, 에어서울, 진에어, 티웨이항공 등"
    )
    public String getAirlineId(
            @ToolParam(description = "항공사 이름 (예: 아시아나항공, 대한항공, 제주항공)") String airlineName) {
        log.info("MCP Tool 호출: getAirlineId(airlineName={})", airlineName);
        return airlineCodeAgent.getAirlineId(airlineName);
    }
}
