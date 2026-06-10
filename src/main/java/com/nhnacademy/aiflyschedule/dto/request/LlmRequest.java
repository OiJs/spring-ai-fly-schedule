package com.nhnacademy.aiflyschedule.dto.request;


import jakarta.validation.constraints.NotBlank;

public record LlmRequest(
        @NotBlank(message = "메시지는 필수입니다.")
        String message
) {}