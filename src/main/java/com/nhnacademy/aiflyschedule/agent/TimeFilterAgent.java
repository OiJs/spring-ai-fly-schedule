package com.nhnacademy.aiflyschedule.agent;

import com.nhnacademy.aiflyschedule.dto.response.AirlineGroupResponse;
import com.nhnacademy.aiflyschedule.dto.response.FlightInfoResponse;
import com.nhnacademy.aiflyschedule.dto.response.FlightSearchResult;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TimeFilterAgent {

    public FlightSearchResult groupByAfterTime(FlightSearchResult searchResult, String afterTime) {
        if (afterTime == null || afterTime.isBlank() || searchResult == null) {
            return searchResult;
        }

        int targetTime = parseTimeToInt(afterTime);

        List<AirlineGroupResponse> filteredGroups = searchResult.airlineGroups().stream()
                .map(group -> {
                    List<FlightInfoResponse> filteredFlights = group.flights().stream()
                            .filter(flight -> {
                                String depTime = flight.departureTime();
                                if (depTime == null || depTime.length() < 12) {
                                    return false;
                                }
                                int flightTime = Integer.parseInt(depTime.substring(8, 12));
                                return flightTime >= targetTime;
                            }).toList();

                    return filteredFlights.isEmpty() ? null : new AirlineGroupResponse(group.airlineName(), filteredFlights);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new FlightSearchResult(filteredGroups);
    }

    private int parseTimeToInt(String timeStr) {
        try {
            String normalized = timeStr.replaceAll("\\s+", "");

            if (normalized.contains("오전") || normalized.contains("오후")) {
                boolean isPm = normalized.contains("오후");
                normalized = normalized.replace("오전", "").replace("오후", "");

                int hour = 0;
                int minute = 0;

                if (normalized.contains("시")) {
                    int hourIndex = normalized.indexOf("시");
                    hour = Integer.parseInt(normalized.substring(0, hourIndex));

                    if (normalized.contains("분")) {
                        int minuteIndex = normalized.indexOf("분");
                        minute = Integer.parseInt(normalized.substring(hourIndex + 1, minuteIndex));
                    }
                } else {
                    hour = Integer.parseInt(normalized);
                }

                if (isPm && hour < 12) {
                    hour += 12;
                } else if (!isPm && hour == 12) {
                    hour = 0;
                }
                return (hour * 100) + minute;
            }
            return Integer.parseInt(normalized.replace(":", ""));

        } catch (Exception e) {
            log.error("시간 파싱 실패: {}, 기본값(0) 처리", timeStr);
            return 0;
        }
    }
}
