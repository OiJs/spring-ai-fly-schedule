package com.nhnacademy.aiflyschedule.dto.response;

import java.util.List;

/**
 * 항공사별 비행기 그룹 응답 DTO
 */
public record AirlineGroupResponse(
        String airlineName,
        List<FlightInfoResponse> flights
) {
}
