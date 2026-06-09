package com.nhnacademy.aiflyschedule.mcp;

import com.nhnacademy.aiflyschedule.dto.response.FlightInfoResponse;
import com.nhnacademy.aiflyschedule.service.ApiClientService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
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
public class FlightSearchTool {
    private final ApiClientService clientService;

    @Tool(
            description = "항공편을 검색하여 항공사별로 그룹핑하여 반환합니다." +
                    "출발 공항, 도착 공항, 날짜를 받아 항공사별로 정리된 항공편 목록을 제공합니다." +
                    "날짜는 '내일', '모레', '2026-03-10', 'n일 뒤', 'n일후' 형식을 지원합니다." +
                    "빠른 응답을 위해서 항공사별 최대 3편만 반환합니다."
    )
    public Map<String, List<FlightInfoResponse>> searchFlightsByAirline(
            @ToolParam(description = "출발 공항 이름 (예: 광주, 김포, 제주)") String departure,
            @ToolParam(description = "도착 공항 이름 (예: 제주, 김포, 부산)") String arrival,
            @ToolParam(description = "날짜 (예: 내일, 모레, 2026-03-10)") String date) {

        log.info("MCP Tool 호출: searchFlightsByAirline(departure={}, arrival={}, date={})",
                departure, arrival, date);

        String formattedDate = parseDate(date);

        String depAirportId = getAirportCode(departure);
        String arrAirportId = getAirportCode(arrival);

        List<FlightInfoResponse> allFlights = clientService.getFlightSchedule(
                depAirportId, arrAirportId, formattedDate);

        Map<String, List<FlightInfoResponse>> groupedFlights = allFlights.stream()
                .collect(Collectors.groupingBy(FlightInfoResponse::airlineName));

        Map<String, List<FlightInfoResponse>> limitedFlights = new HashMap<>();
        groupedFlights.forEach((airline, flights) -> {
            if(flights.size() > 3) {
                limitedFlights.put(airline, flights.subList(0, 3));
            } else {
                limitedFlights.put(airline, flights);
            }
        });
        log.info("MCP Tool 응답: {}개 항공사, {}편",
                limitedFlights.size(),
                limitedFlights.values().stream().mapToInt(List::size).sum());

        return limitedFlights;
    }

    private String parseDate(String date) {
        LocalDate targetDate;

        if ("내일".equals(date)) {
            targetDate = LocalDate.now().plusDays(1);
        } else if ("모레".equals(date)) {
            targetDate = LocalDate.now().plusDays(2);
        }
        else if (date.matches("(\\d+)일 (뒤|후)")) {
            String daysStr = date.replaceAll("[^0-9]", "");
            int days = Integer.parseInt(daysStr);
            targetDate = LocalDate.now().plusDays(days);
        }
        else if (date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            targetDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        else {
            targetDate = LocalDate.now();
        }

        return targetDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    private String getAirportCode(String airportName) {
        return switch (airportName) {
            case "김포" -> "NAARKSS";
            case "인천" -> "NAARKII";
            case "김해" -> "NAARKPN";
            case "광주" -> "NAARKJJ";
            case "제주" -> "NAARKPC";
            case "대구" -> "NAARKTN";
            case "청주" -> "NAARKCJ";
            default -> "NAARKJJ";  // 기본값
        };
    }
}
