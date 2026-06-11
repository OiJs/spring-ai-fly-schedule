package com.nhnacademy.aiflyschedule.agent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.nhnacademy.aiflyschedule.dto.response.AirportInfoResponse;
import com.nhnacademy.aiflyschedule.service.ApiClientService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AirportCodeAgentTest {

    @Mock
    private ApiClientService apiClientService;

    @InjectMocks
    private AirportCodeAgent airportCodeAgent;

    @BeforeEach
    void setUp() {
        List<AirportInfoResponse> mockAirports = List.of(
                new AirportInfoResponse("NAARKSS", "김포"),
                new AirportInfoResponse("NAARKPC", "제주")
        );
        when(apiClientService.getAirportInfo()).thenReturn(mockAirports);

        airportCodeAgent.init();
    }

    @Test
    @DisplayName("공항 이름으로 코드를 올바르게 조회해야 한다")
    void getAirportCode_success() {
        String code = airportCodeAgent.getAirportCode("김포");
        assertThat(code).isEqualTo("NAARKSS");
    }

    @Test
    @DisplayName("이미 코드 형태인 입력값은 그대로 반환해야 한다")
    void getAirportCode_alreadyCode() {
        String code = airportCodeAgent.getAirportCode("NAARKSS");
        assertThat(code).isEqualTo("NAARKSS");
    }

    @Test
    @DisplayName("전체 공항 목록을 올바르게 반환해야 한다")
    void getAllAirports() {
        List<AirportInfoResponse> airports = airportCodeAgent.getAllAirports();
        assertThat(airports).hasSize(2);
        assertThat(airports.getFirst().airportName()).isEqualTo("김포");
    }

    @Test
    @DisplayName("존재하지 않는 공항 이름인 경우 예외를 발생시켜야 한다")
    void getAirportCode_fail() {
        assertThatThrownBy(() -> airportCodeAgent.getAirportCode("없는공항"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("알 수 없는 공항");
    }
}
