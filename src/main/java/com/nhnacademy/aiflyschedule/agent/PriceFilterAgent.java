package com.nhnacademy.aiflyschedule.agent;

import com.nhnacademy.aiflyschedule.dto.response.FlightInfoResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PriceFilterAgent {

    public Map<String, List<FlightInfoResponse>> groupByPrice(Map<String, List<FlightInfoResponse>> flights, Integer minPrice, Integer maxPrice) {
        if(minPrice == null && maxPrice == null) {
            return flights;
        }

        return flights.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .filter(flight -> {
                                    Integer price = flight.economyCharge();

                                    if(price == null || price == 0) return  false;
                                    boolean isOverMin = (minPrice == null || price >= minPrice);
                                    boolean isUnderMax = (maxPrice == null || price <= maxPrice);
                                    return isOverMin && isUnderMax;
                                }).toList()
                ))
                .entrySet().stream()
                .filter(entry -> !entry.getValue().isEmpty())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
