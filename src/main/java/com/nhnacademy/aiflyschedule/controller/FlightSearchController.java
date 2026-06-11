package com.nhnacademy.aiflyschedule.controller;

import com.nhnacademy.aiflyschedule.agent.MultiAgentOrchestrator;
import com.nhnacademy.aiflyschedule.dto.request.FlightSearchRequest;
import com.nhnacademy.aiflyschedule.dto.response.FlightSearchResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/flight")
public class FlightSearchController {
    private final MultiAgentOrchestrator orchestrator;

    @PostMapping("/search")
    public FlightSearchResult searchFlights(@RequestBody @Valid FlightSearchRequest request) {
        return orchestrator.coordinateSearch(request);
    }
}
