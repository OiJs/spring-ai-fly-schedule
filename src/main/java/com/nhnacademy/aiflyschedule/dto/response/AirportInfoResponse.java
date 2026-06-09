package com.nhnacademy.aiflyschedule.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AirportInfoResponse(
        @JsonProperty("airportId")
        String airportId,
        @JsonProperty("airportNm")
        String airportName
) {
}
