package com.nhnacademy.aiflyschedule.agent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.aiflyschedule.dto.response.FlightInfoResponse;
import com.nhnacademy.aiflyschedule.service.ApiClientService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FlightSearchAgentTest {
    @Mock
    private ApiClientService apiClientService;

    @InjectMocks
    private FlightSearchAgent flightSearchAgent;

    @Test
    @DisplayName("API 호출 실패 시 재시도 로직 검증 (1회 실패 후 성공)")
    void searchFlights_retrySuccess() {
        String depCode = "NAARKSS";
        String arrCode = "NAARKPC";
        String formattedDate = "20260611";

        List<FlightInfoResponse> mockFlights = List.of(
                new FlightInfoResponse("F1", "대한항공", "202606111000", "202606111100", 50000, 0, "김포", "제주")
        );

        // 첫 번째 호출은 예외 발생, 두 번째 호출은 성공하도록 설정
        when(apiClientService.getFlightSchedule(depCode, arrCode, formattedDate))
                .thenThrow(new RuntimeException("API Temporary Error"))
                .thenReturn(mockFlights);

        List<FlightInfoResponse> result = flightSearchAgent.searchFlights(depCode, arrCode, formattedDate);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().airlineName()).isEqualTo("대한항공");

        // 총 2회 호출되었는지 검증
        verify(apiClientService, org.mockito.Mockito.times(2))
                .getFlightSchedule(depCode, arrCode, formattedDate);
    }

    @Test
    @DisplayName("API 호출 최대 재시도 횟수 초과 시 빈 리스트 반환")
    void searchFlights_retryFail() {
        String depCode = "NAARKSS";
        String arrCode = "NAARKPC";
        String formattedDate = "20260611";

        // 모든 호출에서 예외 발생
        when(apiClientService.getFlightSchedule(depCode, arrCode, formattedDate))
                .thenThrow(new RuntimeException("API Persistent Error"));

        List<FlightInfoResponse> result = flightSearchAgent.searchFlights(depCode, arrCode, formattedDate);

        assertThat(result).isEmpty();

        // 최대 재시도 횟수(2회)만큼 호출되었는지 검증
        verify(apiClientService, org.mockito.Mockito.times(2))
                .getFlightSchedule(depCode, arrCode, formattedDate);
    }
}
