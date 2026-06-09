package com.nhnacademy.aiflyschedule.tool;

import com.nhnacademy.aiflyschedule.callback.LoggingCallback;
import java.time.LocalDate;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DateTimeTool {
    private final LoggingCallback loggingCallback;

    @Tool(description = "상대적 날짜를 실제 날짜(YYYY-MM-DD)로 변환합니다. '내일', '모레', '3일 뒤' 등을 지원합니다.")
    public String parseDate(
            @ToolParam(description = "상대적 날짜 표현 (예: 내일, 모레, 3일 뒤)") String relativeDate) {

        // 1. Tool 호출 시작 로깅
        loggingCallback.beforeToolCall("parseDate", Map.of("relativeDate", relativeDate));

        String result = null;
        try {
            LocalDate today = LocalDate.now();

            result = switch (relativeDate) {
                case "내일" -> today.plusDays(1).toString();
                case "모레" -> today.plusDays(2).toString();
                case "글피" -> today.plusDays(3).toString();
                default -> {
                    if (relativeDate.contains("일 뒤")) {
                        try {
                            int days = Integer.parseInt(relativeDate.replaceAll("[^0-9]", ""));
                            yield today.plusDays(days).toString();
                        } catch (NumberFormatException e) {
                            yield today.toString(); // 숫자 파싱 실패 시 오늘 날짜 반환
                        }
                    }
                    yield today.toString();
                }
            };
        } finally {
            loggingCallback.afterToolCall("parseDate", result != null ? result : "null");
        }

        return result;
    }
}