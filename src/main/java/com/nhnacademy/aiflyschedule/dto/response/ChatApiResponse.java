package com.nhnacademy.aiflyschedule.dto.response;

import jakarta.validation.constraints.NotNull;

public record ChatApiResponse(
        @NotNull(message = "메시지를 입력해주세요")
        String message,
        FlightSearchResult data
) {
}
