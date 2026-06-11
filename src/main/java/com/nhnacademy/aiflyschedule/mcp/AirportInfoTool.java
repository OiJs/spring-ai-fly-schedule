package com.nhnacademy.aiflyschedule.mcp;

import com.nhnacademy.aiflyschedule.agent.AirportCodeAgent;
import com.nhnacademy.aiflyschedule.dto.response.AirportInfoResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

/**
 * 공항 정보 제공 도구
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AirportInfoTool {
    private final AirportCodeAgent airportCodeAgent;

    @Tool(
            description = "전체 공항 목록을 조회합니다. " +
                    "국내 모든 공항의 코드와 이름을 반환합니다. " +
                    "사용자가 '공항 리스트', '공항 목록', '어느 공항이 있어?' 등을 물을 때 사용합니다. " +
                    "[중요 지시사항] 사용자에게 목록을 보여줄 때는 반드시 마크다운(Markdown) 글머리 기호('-')를 사용하고, 각 공항마다 줄바꿈을 하여 세로로 길게 출력하세요."
    )
    public List<AirportInfoResponse> getAirportList() {
        log.info("MCP Tool 호출: getAirportList()");
        return airportCodeAgent.getAllAirports();
    }

    @Tool(
            description = "공항 이름으로 공항 코드를 조회합니다. " +
                    "공항 이름을 입력하면 해당 공항의 IATA 코드를 반환합니다. " +
                    "지원하는 공항: 김포, 인천, 김해, 광주, 제주, 대구, 청주, 양양, 울산, 여수, 사천, 무안 등"
    )
    public String getAirportCode(@ToolParam(description = "공항 이름 (예: 광주, 김포, 제주)") String airportName) {
        log.info("MCP Tool 호출: getAirportCode(airportName={})", airportName);
        return airportCodeAgent.getAirportCode(airportName);
    }
}
