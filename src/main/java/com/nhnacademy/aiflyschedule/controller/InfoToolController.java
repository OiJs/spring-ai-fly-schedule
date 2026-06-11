package com.nhnacademy.aiflyschedule.controller;

import com.nhnacademy.aiflyschedule.agent.AirlineCodeAgent;
import com.nhnacademy.aiflyschedule.agent.AirportCodeAgent;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//TODO Agent 호출하게 수정
// String말고 DTO 반환할까 고민중
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/info")
public class InfoToolController {
    private final AirportCodeAgent airportCodeAgent;
    private final AirlineCodeAgent airlineCodeAgent;

    @GetMapping("/airports")
    public String getAirports() {
        var airports = airportCodeAgent.getAllAirports();

        StringBuilder sb = new StringBuilder();
        sb.append("전국 공항 목록:\n\n");

        airports.forEach(airport -> {
            sb.append(String.format("- %s (%s)\n",
                    airport.airportName(),
                    airport.airportId()));
        });
        return sb.toString();
    }

    @GetMapping("/airlines")
    public String getAirlines() {
        var airlines = airlineCodeAgent.getAllAirlines();

        StringBuilder sb = new StringBuilder();
        sb.append("국내 항공사 목록:\n\n");

        airlines.forEach(airline -> {
            sb.append(String.format("- %s (%s)\n",
                    airline.airlineName(),
                    airline.airlineId()));
        });

        return sb.toString();
    }

    @GetMapping("/airport-code")
    public String getAirportCode(@RequestParam String name) {
        return "공항 코드: " + airportCodeAgent.getAirportCode(name);
    }

    @GetMapping("airline-id")
    public String getAirlineId(@RequestParam String name) {
        return "항공사 ID: " + airlineCodeAgent.getAirlineId(name);
    }

}
