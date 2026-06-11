package com.nhnacademy.aiflyschedule.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FlightSearchRequest(
        @NotNull
        @NotBlank
        String departure,
        @NotNull
        @NotBlank
        String arrival,
        @NotNull
        @NotBlank
        String date,
        String afterTime,
        @Min(0)
        Integer minPrice,
        Integer maxPrice
) {
}
