package com.nhnacademy.aiflyschedule.agent;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.aiflyschedule.dto.response.AirlineGroupResponse;
import com.nhnacademy.aiflyschedule.dto.response.FlightInfoResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TimeFilterAgentTest {

    private final TimeFilterAgent timeFilterAgent = new TimeFilterAgent();

    @Test
    @DisplayName("특정 시간 이후의 항공편만 필터링해야 한다 (HH:mm 형식)")
    void groupByAfterTime_formattedTime() {
        List<FlightInfoResponse> flights = List.of(
                createFlight("F1", "202606110800"),
                createFlight("F2", "202606111200"),
                createFlight("F3", "202606111500")
        );
        List<AirlineGroupResponse> groups = List.of(new AirlineGroupResponse("항공사", flights));

        List<AirlineGroupResponse> result = timeFilterAgent.groupByAfterTime(groups, "12:00");

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().flights()).hasSize(2);
        assertThat(result.getFirst().flights()).extracting("flightId").containsExactly("F2", "F3");
    }

    @Test
    @DisplayName("오전/오후 형식이 포함된 시간을 올바르게 처리해야 한다")
    void groupByAfterTime_koreanTime() {
        List<FlightInfoResponse> flights = List.of(
                createFlight("F1", "202606111000"),
                createFlight("F2", "202606111400")
        );
        List<AirlineGroupResponse> groups = List.of(new AirlineGroupResponse("항공사", flights));

        // 오후 1시 이후 -> 13:00 이후
        List<AirlineGroupResponse> result = timeFilterAgent.groupByAfterTime(groups, "오후 1시");

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().flights()).hasSize(1);
        assertThat(result.getFirst().flights().getFirst().flightId()).isEqualTo("F2");
    }

    @Test
    @DisplayName("오전 12시(00:00) 처리를 확인한다")
    void groupByAfterTime_midnight() {
        List<FlightInfoResponse> flights = List.of(createFlight("F1", "202606110030"));
        List<AirlineGroupResponse> groups = List.of(new AirlineGroupResponse("항공사", flights));

        List<AirlineGroupResponse> result = timeFilterAgent.groupByAfterTime(groups, "오전 12시");

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().flights()).hasSize(1);
    }

    @Test
    @DisplayName("시간 조건이 없거나 빈 문자열이면 원본을 그대로 반환해야 한다")
    void groupByAfterTime_emptyCondition() {
        List<AirlineGroupResponse> groups = List.of(new AirlineGroupResponse("항공사", List.of(createFlight("F1", "202606111000"))));
        
        assertThat(timeFilterAgent.groupByAfterTime(groups, null)).isEqualTo(groups);
        assertThat(timeFilterAgent.groupByAfterTime(groups, "")).isEqualTo(groups);
    }

    @Test
    @DisplayName("잘못된 시간 형식인 경우 기본값(00:00)으로 처리하여 전체를 반환한다")
    void groupByAfterTime_invalidFormat() {
        List<FlightInfoResponse> flights = List.of(createFlight("F1", "202606111000"));
        List<AirlineGroupResponse> groups = List.of(new AirlineGroupResponse("항공사", flights));

        List<AirlineGroupResponse> result = timeFilterAgent.groupByAfterTime(groups, "잘못된시간");

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().flights()).hasSize(1);
    }

    private FlightInfoResponse createFlight(String id, String depTime) {
        return new FlightInfoResponse(id, "항공사", depTime, "202606112359", 50000, 0, "출발", "도착");
    }
}
