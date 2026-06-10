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
public class PriceFilterAgent {

    public FlightSearchResult groupByPrice(FlightSearchResult searchResult, Integer minPrice, Integer maxPrice) {
        if ((minPrice == null && maxPrice == null) || searchResult == null) {
            return searchResult;
        }

        List<AirlineGroupResponse> filteredGroups = searchResult.airlineGroups().stream()
                .map(group -> {
                    List<FlightInfoResponse> filteredFlights = group.flights().stream()
                            .filter(flight -> {
                                Integer price = flight.economyCharge();
                                if (price == null || price == 0) return false;
                                boolean isOverMin = (minPrice == null || price >= minPrice);
                                boolean isUnderMax = (maxPrice == null || price <= maxPrice);
                                return isOverMin && isUnderMax;
                            }).toList();

                    return filteredFlights.isEmpty() ? null : new AirlineGroupResponse(group.airlineName(), filteredFlights);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new FlightSearchResult(filteredGroups);
    }
}
