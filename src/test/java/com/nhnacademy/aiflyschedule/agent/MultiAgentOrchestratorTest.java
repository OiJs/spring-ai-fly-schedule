package com.nhnacademy.aiflyschedule.agent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.aiflyschedule.dto.request.FlightSearchRequest;
import com.nhnacademy.aiflyschedule.dto.response.AirlineGroupResponse;
import com.nhnacademy.aiflyschedule.dto.response.FlightInfoResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MultiAgentOrchestratorTest {
    @Mock
    private FlightSearchAgent flightSearchAgent;
    @Mock
    private TimeFilterAgent timeFilterAgent;
    @Mock
    private PriceFilterAgent priceFilterAgent;
    @Mock
    private DateParserAgent dateParserAgent;
    @Mock
    private AirportCodeAgent airportCodeAgent;
    @Mock
    private GroupingAgent groupingAgent;

    @InjectMocks
    private MultiAgentOrchestrator orchestrator;

    @Test
    @DisplayName("통합 검색 조율 테스트: 파싱 -> 코드변환 -> 검색 -> 그룹핑 -> 필터링")
    void coordinateSearch() {
        // Given
        FlightSearchRequest request = new FlightSearchRequest("김포", "제주", "내일", "10:00", 50000, 100000);
        
        String formattedDate = "20260611";
        String depCode = "GMP";
        String arrCode = "CJU";
        
        List<FlightInfoResponse> mockFlights = List.of(
                new FlightInfoResponse("F1", "대한항공", "202606111000", "202606111100", 70000, 0, "김포", "제주")
        );
        List<AirlineGroupResponse> mockGroups = List.of(new AirlineGroupResponse("대한항공", mockFlights));

        when(dateParserAgent.parseDate(anyString())).thenReturn(formattedDate);
        when(airportCodeAgent.getAirportCode("김포")).thenReturn(depCode);
        when(airportCodeAgent.getAirportCode("제주")).thenReturn(arrCode);
        when(flightSearchAgent.searchFlights(depCode, arrCode, formattedDate)).thenReturn(mockFlights);
        when(groupingAgent.groupByAirline(mockFlights)).thenReturn(mockGroups);
        
        // 필터들은 그대로 반환한다고 가정
        when(priceFilterAgent.groupByPrice(any(), any(), any())).thenReturn(mockGroups);
        when(timeFilterAgent.groupByAfterTime(any(), any())).thenReturn(mockGroups);

        // When
        List<AirlineGroupResponse> result = orchestrator.coordinateSearch(request);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().airlineName()).isEqualTo("대한항공");

        verify(dateParserAgent).parseDate("내일");
        verify(airportCodeAgent).getAirportCode("김포");
        verify(airportCodeAgent).getAirportCode("제주");
        verify(flightSearchAgent).searchFlights(depCode, arrCode, formattedDate);
        verify(groupingAgent).groupByAirline(mockFlights);
        verify(priceFilterAgent).groupByPrice(any(), any(), any());
        verify(timeFilterAgent).groupByAfterTime(any(), any());
    }
}
