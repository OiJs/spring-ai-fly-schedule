package com.nhnacademy.aiflyschedule.agent;

import com.nhnacademy.aiflyschedule.dto.response.FlightInfoResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TimeFilterAgent {

    public Map<String, List<FlightInfoResponse>> groupByAfterTime(Map<String, List<FlightInfoResponse>> flights, String afterTime){
    //afterTime (14:00) 파싱
    //departureTime, // 출발시간(YYYYMMDDHHMI) HH에 맞춰서 필터링
        if(afterTime == null || afterTime.isBlank()) {
            return flights;
        }

        int targetTime = Integer.parseInt(afterTime.trim().replace(":",""));

        return flights.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .filter(flight -> {
                                    String depTime = flight.departureTime();

                                    if(depTime == null || depTime.length() < 12) {
                                        return false;
                                    }

                                    int flightTime = Integer.parseInt(depTime.substring(8, 12));

                                    return flightTime >= targetTime;
                                }).toList()
                ))
                .entrySet().stream()
                .filter(entry -> !entry.getValue().isEmpty())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    }

}
