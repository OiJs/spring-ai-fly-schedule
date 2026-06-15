package com.nhnacademy.aiflyschedule.dto.response;

public record ChatApiResponse(
        String message,
        FlightSearchResult data
) {
}
