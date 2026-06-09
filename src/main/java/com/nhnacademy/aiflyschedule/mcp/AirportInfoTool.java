package com.nhnacademy.aiflyschedule.mcp;

import com.nhnacademy.aiflyschedule.dto.response.AirportInfoResponse;
import com.nhnacademy.aiflyschedule.service.ApiClientService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AirportInfoTool {
    private final ApiClientService apiClientService;
    private Map<String, String> airportCodeCache;


    @Tool(
            description = "전체 공항 목록을 조회합니다. " +
                    "국내 모든 공항의 코드와 이름을 반환합니다. " +
                    "사용자가 '공항 리스트', '공항 목록', '어느 공항이 있어?' 등을 물을 때 사용합니다."
    )
    public List<AirportInfoResponse> getAirportList() {
        log.info("MCP Tool 호출: getAirportList()");

        List<AirportInfoResponse> airports = apiClientService.getAirportInfo();

        airportCodeCache = airports.stream()
                .collect(Collectors.toMap(
                        AirportInfoResponse::airportName,
                        AirportInfoResponse::airportId
                ));
        return airports;
    }

    @Tool(
            description = "공항 이름으로 공항 코드를 조회합니다. " +
            "공항 이름을 입력하면 해당 공항의 IATA 코드를 반환합니다. " +
                    "지원하는 공항: 김포, 인천, 김해, 광주, 제주, 대구, 청주, 양양, 울산, 여수, 사천, 무안 등"

    )
    public String getAirportCode(@ToolParam(description = "공항 이름 (예: 광주, 김포, 제주") String airportName) {
        log.info("MCP Tool 호출: getAirportCode(airportName={})", airportName);

        if(airportCodeCache.isEmpty()) {
            getAirportList();
        }

        String code = airportCodeCache.get(airportName);

        if (code == null) {
            log.warn("공항 코드를 찾을 수 없음: {}", airportName);
            return "알 수 없는 공항입니다: " + airportName;
        }

        log.info("공항 코드 조회 결과: {} → {}", airportName, code);
        return code;
     }
}
