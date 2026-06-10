package com.nhnacademy.aiflyschedule.dto.response;

import java.util.List;

/**
 * 전체 항공편 검색 결과 DTO
 */
public record FlightSearchResult(
        List<AirlineGroupResponse> airlineGroups
) {
}
