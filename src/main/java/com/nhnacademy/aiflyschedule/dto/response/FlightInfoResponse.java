package com.nhnacademy.aiflyschedule.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FlightInfoResponse(
        @JsonProperty("vihicleId")
        String flightId, // 편명 (예: OZ8141)

        @JsonProperty("airlineNm")
        String airlineName, // 항공사명 (예: 아시아나항공)

        @JsonProperty("depPlandTime")
        String departureTime, // 출발시간(YYYYMMDDHHMI)

        @JsonProperty("arrPlandTime")
        String arrivalTime, // 도착시간(YYYYMMDDHH)

        @JsonProperty("economyCharge")
        Integer economyCharge, // Economy 요금

        @JsonProperty("prestigeCharge")
        Integer prestigeCharge, // Prestige 요금

        @JsonProperty("depAirportNm")
        String departureAirport, // 출발 공항명

        @JsonProperty("arrAirportNm")
        String arrivalAirport // 도착 공항명
) {
}
