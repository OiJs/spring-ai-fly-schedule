package com.nhnacademy.aiflyschedule.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AirlineInfoResponse(

        @JsonProperty("airlineId")
        String airlineId,
        @JsonProperty("airlineNm")
        String airlineName
) {
}
