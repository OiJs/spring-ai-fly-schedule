package com.nhnacademy.aiflyschedule.agent;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class DateParserAgentTest {
    private final DateParserAgent dateParser = new DateParserAgent();

    @Test
    @DisplayName("내일 파싱")
    void parseTomorrow() {
        String result = dateParser.parseDate("내일");
        assertNotNull(result);
        assertTrue(result.matches("\\d{8}"));
    }

    @Test
    @DisplayName("내일 파싱")
    void parseAfterTomorrow() {
        String result = dateParser.parseDate("내일");
        assertNotNull(result);
        assertTrue(result.matches("\\d{8}"));
    }

    @Test
    @DisplayName("특정 날짜 파싱")
    void parseSpecificDate() {
        String result = dateParser.parseDate("2026-03-10");
        assertEquals("20260310", result);
    }

    @Test
    @DisplayName("잘못된 날짜 형식 예외")
    void parserInvalidDate() {
        assertThrows(IllegalArgumentException.class, () -> {
            dateParser.parseDate("2026/12/23");
        });
    }
}
