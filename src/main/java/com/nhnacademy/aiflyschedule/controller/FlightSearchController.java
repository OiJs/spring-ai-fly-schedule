package com.nhnacademy.aiflyschedule.controller;

import com.nhnacademy.aiflyschedule.mcp.FlightSearchTool;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/flight")
public class FlightSearchController {
    private final FlightSearchTool flightSearchTool;

    @GetMapping("/search")
    public String searchFlights(@RequestParam String departure,
                                @RequestParam String arrival,
                                @RequestParam String date) {
        var result = flightSearchTool.searchFlightsByAirline(departure, arrival, date);

        StringBuilder sb = new StringBuilder();
        sb.append("항공편 검색 결과:\n\n");

        result.forEach((airline, flights) -> {
            sb.append("[").append(airline).append("]\n");
            flights.forEach(flight -> {
                sb.append("  - ").append(flight.flightId())
                        .append(" (").append(flight.departureTime())
                        .append(" → ").append(flight.arrivalTime())
                        .append(") ").append(flight.economyCharge()).append("원\n")
                        .append(") ").append(flight.prestigeCharge()).append("원\n");
            });
            sb.append("\n");
        });
        return sb.toString();
    }
}
