package com.nhnacademy.aiflyschedule.controller;

import com.nhnacademy.aiflyschedule.agent.MultiAgentOrchestrator;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coordinator")
public class CoordinatorTestController {

    private final MultiAgentOrchestrator coordinator;

    public CoordinatorTestController(MultiAgentOrchestrator coordinator) {
        this.coordinator = coordinator;
    }

    /**
     * 기본 검색 테스트
     * GET /api/coordinator/search?departure=광주&arrival=제주&date=내일
     */
    @GetMapping("/search")
    public String search(
            @RequestParam String departure,
            @RequestParam String arrival,
            @RequestParam String date) {

        var result = coordinator.coordinateBasicSearch(departure, arrival, date);

        StringBuilder sb = new StringBuilder();
        sb.append("조율된 검색 결과:\n\n");

        result.forEach((airline, flights) -> {
            sb.append("[").append(airline).append("] - ").append(flights.size()).append("편\n");
            flights.forEach(f -> {
                sb.append("  ").append(f.flightId())
                        .append(" (").append(f.departureTime())
                        .append(" → ").append(f.arrivalTime())
                        .append(") ").append(f.economyCharge()).append("원\n");
            });
            sb.append("\n");
        });

        return sb.toString();
    }

    /**
     * 시간 필터 테스트
     * GET /api/coordinator/search/time?departure=광주&arrival=제주&date=내일&afterTime=14:00
     */
    @GetMapping("/search/time")
    public String searchWithTimeFilter(
            @RequestParam String departure,
            @RequestParam String arrival,
            @RequestParam String date,
            @RequestParam String afterTime) {

        var result = coordinator.coordinateTimeFilterSearch(departure, arrival, date, afterTime);

        StringBuilder sb = new StringBuilder();
        sb.append("시간 필터 결과 (").append(afterTime).append(" 이후): \n\n");
        sb.append(result.values().stream()
                .mapToInt(List::size)
                .sum()).append(" 편의 항공편이 있습니다.\n\n");

        result.forEach((airline, flights) -> {
            sb.append("[").append(airline).append("] - ").append(flights.size()).append("편\n");
            flights.forEach(f -> {
                sb.append("  ").append(f.flightId())
                        .append(" (").append(f.departureTime())
                        .append(" → ").append(f.arrivalTime())
                        .append(") ").append(f.economyCharge()).append("원\n");
            });
            sb.append("\n");
        });

        return sb.toString();
    }

    /**
     * 가격 필터 테스트
     * GET /api/coordinator/search/price?departure=광주&arrival=제주&date=내일&minPrice=30000&maxPrice=70000
     */
    @GetMapping("/search/price")
    public String searchWithPriceFilter(
            @RequestParam String departure,
            @RequestParam String arrival,
            @RequestParam String date,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice) {

        var result = coordinator.coordinatePriceFilterSearch(departure, arrival, date, minPrice, maxPrice);

        StringBuilder sb = new StringBuilder();

        sb.append("가격 필터 결과 (").append(minPrice).append("~").append(maxPrice).append("원):\n\n")
                .append(result.values().stream()
                        .mapToInt(List::size)
                        .sum())
                .append("편의 항공편이 있습니다.");

        result.forEach((airline, flights) -> {
            sb.append("[").append(airline).append("] - ").append(flights.size()).append("편\n");
            flights.forEach(f -> {
                sb.append("  ").append(f.flightId())
                        .append(" (").append(f.departureTime())
                        .append(" → ").append(f.arrivalTime())
                        .append(") ").append(f.economyCharge()).append("원\n");
            });
            sb.append("\n");
        });

        return sb.toString();
    }
}