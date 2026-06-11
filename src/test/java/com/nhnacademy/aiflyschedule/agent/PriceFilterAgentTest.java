package com.nhnacademy.aiflyschedule.agent;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.aiflyschedule.dto.response.AirlineGroupResponse;
import com.nhnacademy.aiflyschedule.dto.response.FlightInfoResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PriceFilterAgentTest {

    private final PriceFilterAgent priceFilterAgent = new PriceFilterAgent();

    @Test
    @DisplayName("최소 가격과 최대 가격 사이의 항공편만 필터링해야 한다")
    void groupByPrice_validRange() {
        List<FlightInfoResponse> kalFlights = List.of(
                createFlight("K1", 50000),
                createFlight("K2", 100000),
                createFlight("K3", 150000)
        );
        List<AirlineGroupResponse> groups = List.of(new AirlineGroupResponse("대한항공", kalFlights));

        List<AirlineGroupResponse> result = priceFilterAgent.groupByPrice(groups, 60000, 120000);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().flights()).hasSize(1);
        assertThat(result.getFirst().flights().getFirst().flightId()).isEqualTo("K2");
    }

    @Test
    @DisplayName("최소 가격만 주어진 경우 해당 가격 이상의 항공편만 반환해야 한다")
    void groupByPrice_minOnly() {
        List<FlightInfoResponse> flights = List.of(
                createFlight("F1", 50000),
                createFlight("F2", 100000)
        );
        List<AirlineGroupResponse> groups = List.of(new AirlineGroupResponse("항공사", flights));

        List<AirlineGroupResponse> result = priceFilterAgent.groupByPrice(groups, 80000, null);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().flights()).hasSize(1);
        assertThat(result.getFirst().flights().getFirst().flightId()).isEqualTo("F2");
    }

    @Test
    @DisplayName("최대 가격만 주어진 경우 해당 가격 이하의 항공편만 반환해야 한다")
    void groupByPrice_maxOnly() {
        List<FlightInfoResponse> flights = List.of(
                createFlight("F1", 50000),
                createFlight("F2", 100000)
        );
        List<AirlineGroupResponse> groups = List.of(new AirlineGroupResponse("항공사", flights));

        List<AirlineGroupResponse> result = priceFilterAgent.groupByPrice(groups, null, 70000);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().flights()).hasSize(1);
        assertThat(result.getFirst().flights().getFirst().flightId()).isEqualTo("F1");
    }

    @Test
    @DisplayName("필터 조건이 모두 null이면 원본을 그대로 반환해야 한다")
    void groupByPrice_noFilter() {
        List<AirlineGroupResponse> groups = List.of(new AirlineGroupResponse("항공사", List.of(createFlight("F1", 50000))));
        List<AirlineGroupResponse> result = priceFilterAgent.groupByPrice(groups, null, null);
        assertThat(result).isEqualTo(groups);
    }

    @Test
    @DisplayName("조건에 맞는 항공편이 없으면 해당 항공사 그룹이 제거되어야 한다")
    void groupByPrice_noMatch() {
        List<AirlineGroupResponse> groups = List.of(new AirlineGroupResponse("항공사", List.of(createFlight("F1", 50000))));
        List<AirlineGroupResponse> result = priceFilterAgent.groupByPrice(groups, 100000, 200000);
        assertThat(result).isEmpty();
    }

    private FlightInfoResponse createFlight(String id, int price) {
        return new FlightInfoResponse(id, "항공사", "202606111000", "202606111100", price, 0, "출발", "도착");
    }
}
