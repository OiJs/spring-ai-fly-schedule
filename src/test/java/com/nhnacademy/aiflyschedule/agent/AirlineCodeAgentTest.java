package com.nhnacademy.aiflyschedule.agent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.nhnacademy.aiflyschedule.dto.response.AirlineInfoResponse;
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
class AirlineCodeAgentTest {

    @Mock
    private ApiClientService apiClientService;

    @InjectMocks
    private AirlineCodeAgent airlineCodeAgent;

    @BeforeEach
    void setUp() {
        // ID, 이름 순서 확인
        List<AirlineInfoResponse> mockAirlines = List.of(
                new AirlineInfoResponse("KAL", "대한항공"),
                new AirlineInfoResponse("AAR", "아시아나항공")
        );
        when(apiClientService.getAirlineInfo()).thenReturn(mockAirlines);
        airlineCodeAgent.init();
    }

    @Test
    @DisplayName("항공사 이름으로 ID를 올바르게 조회해야 한다")
    void getAirlineId_success() {
        String id = airlineCodeAgent.getAirlineId("대한항공");
        assertThat(id).isEqualTo("KAL");
    }

    @Test
    @DisplayName("전체 항공사 목록을 올바르게 반환해야 한다")
    void getAllAirlines() {
        List<AirlineInfoResponse> airlines = airlineCodeAgent.getAllAirlines();
        assertThat(airlines).hasSize(2);
        assertThat(airlines.get(0).airlineName()).isEqualTo("대한항공");
    }

    @Test
    @DisplayName("존재하지 않는 항공사인 경우 메시지를 포함하여 반환해야 한다")
    void getAirlineId_fail() {
        String result = airlineCodeAgent.getAirlineId("우주항공");
        assertThat(result).contains("알 수 없는 항공사");
    }
}
