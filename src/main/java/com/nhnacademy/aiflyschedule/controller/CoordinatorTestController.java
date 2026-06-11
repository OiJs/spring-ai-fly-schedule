//package com.nhnacademy.aiflyschedule.controller;
//
//import com.nhnacademy.aiflyschedule.agent.MultiAgentOrchestrator;
//import com.nhnacademy.aiflyschedule.dto.response.FlightSearchResult;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/coordinator")
//public class CoordinatorTestController {
//
//    private final MultiAgentOrchestrator coordinator;
//
//    public CoordinatorTestController(MultiAgentOrchestrator coordinator) {
//        this.coordinator = coordinator;
//    }
//
//    /**
//     * 기본 검색 테스트
//     */
//    @GetMapping("/search")
//    public String search(
//            @RequestParam String departure,
//            @RequestParam String arrival,
//            @RequestParam String date) {
//
//        FlightSearchResult result = coordinator.coordinateBasicSearch(departure, arrival, date);
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("조율된 검색 결과:\n\n");
//
//        if (result != null && result.airlineGroups() != null) {
//            result.airlineGroups().forEach(group -> {
//                sb.append("[").append(group.airlineName()).append("] - ").append(group.flights().size()).append("편\n");
//                group.flights().forEach(f -> {
//                    sb.append("  ").append(f.flightId())
//                            .append(" (").append(f.departureTime())
//                            .append(" → ").append(f.arrivalTime())
//                            .append(") ").append(f.economyCharge()).append("원\n");
//                });
//                sb.append("\n");
//            });
//        }
//
//        return sb.toString();
//    }
//
//    /**
//     * 시간 필터 테스트
//     */
//    @GetMapping("/search/time")
//    public String searchWithTimeFilter(
//            @RequestParam String departure,
//            @RequestParam String arrival,
//            @RequestParam String date,
//            @RequestParam String afterTime) {
//
//        FlightSearchResult result = coordinator.coordinateTimeFilterSearch(departure, arrival, date, afterTime);
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("시간 필터 결과 (").append(afterTime).append(" 이후): \n\n");
//
//        if (result != null && result.airlineGroups() != null) {
//            long totalFlights = result.airlineGroups().stream()
//                    .mapToLong(g -> g.flights().size())
//                    .sum();
//
//            sb.append(totalFlights).append(" 편의 항공편이 있습니다.\n\n");
//
//            result.airlineGroups().forEach(group -> {
//                sb.append("[").append(group.airlineName()).append("] - ").append(group.flights().size()).append("편\n");
//                group.flights().forEach(f -> {
//                    sb.append("  ").append(f.flightId())
//                            .append(" (").append(f.departureTime())
//                            .append(" → ").append(f.arrivalTime())
//                            .append(") ").append(f.economyCharge()).append("원\n");
//                });
//                sb.append("\n");
//            });
//        }
//
//        return sb.toString();
//    }
//
//    /**
//     * 가격 필터 테스트
//     */
//    @GetMapping("/search/price")
//    public String searchWithPriceFilter(
//            @RequestParam String departure,
//            @RequestParam String arrival,
//            @RequestParam String date,
//            @RequestParam(required = false) Integer minPrice,
//            @RequestParam(required = false) Integer maxPrice) {
//
//        FlightSearchResult result = coordinator.coordinatePriceFilterSearch(departure, arrival, date, minPrice, maxPrice);
//
//        StringBuilder sb = new StringBuilder();
//
//        if (result != null && result.airlineGroups() != null) {
//            long totalFlights = result.airlineGroups().stream()
//                    .mapToLong(g -> g.flights().size())
//                    .sum();
//
//            sb.append("가격 필터 결과 (").append(minPrice).append("~").append(maxPrice).append("원):\n\n")
//                    .append(totalFlights)
//                    .append("편의 항공편이 있습니다.\n\n");
//
//            result.airlineGroups().forEach(group -> {
//                sb.append("[").append(group.airlineName()).append("] - ").append(group.flights().size()).append("편\n");
//                group.flights().forEach(f -> {
//                    sb.append("  ").append(f.flightId())
//                            .append(" (").append(f.departureTime())
//                            .append(" → ").append(f.arrivalTime())
//                            .append(") ").append(f.economyCharge()).append("원\n");
//                });
//                sb.append("\n");
//            });
//        }
//
//        return sb.toString();
//    }
//}
