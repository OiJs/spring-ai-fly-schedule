package com.nhnacademy.aiflyschedule.agent;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.aiflyschedule.dto.response.AirlineGroupResponse;
import com.nhnacademy.aiflyschedule.dto.response.FlightInfoResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GroupingAgentTest {

    private final GroupingAgent groupingAgent = new GroupingAgent();

    @Test
    @DisplayName("항공편 리스트를 항공사별로 올바르게 그룹핑하여 반환해야 한다")
    void groupByAirline() {
        List<FlightInfoResponse> flights = List.of(
                new FlightInfoResponse("F1", "대한항공", "202606101000", "202606101100", 50000, 0, "김포", "제주"),
                new FlightInfoResponse("F2", "대한항공", "202606101200", "202606101300", 60000, 0, "김포", "제주"),
                new FlightInfoResponse("F3", "아시아나항공", "202606101400", "202606101500", 70000, 0, "김포", "제주")
        );

        List<AirlineGroupResponse> result = groupingAgent.groupByAirline(flights);

        assertThat(result).hasSize(2);

        var kalGroup = result.stream()
                .filter(g -> g.airlineName().equals("대한항공"))
                .findFirst().orElseThrow();
        assertThat(kalGroup.flights()).hasSize(2);

        var aarGroup = result.stream()
                .filter(g -> g.airlineName().equals("아시아나항공"))
                .findFirst().orElseThrow();
        assertThat(aarGroup.flights()).hasSize(1);
    }

    @Test
    @DisplayName("빈 리스트가 들어오면 빈 리스트를 반환해야 한다")
    void groupByAirline_empty() {
        List<AirlineGroupResponse> result = groupingAgent.groupByAirline(List.of());
        assertThat(result).isEmpty();
    }
}
