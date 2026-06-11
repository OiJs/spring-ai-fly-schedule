package com.nhnacademy.aiflyschedule.agent;

import com.nhnacademy.aiflyschedule.dto.response.AirlineGroupResponse;
import com.nhnacademy.aiflyschedule.dto.response.FlightInfoResponse;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PriceFilterAgent {

    /**
     * 가격 조건에 맞춰 항공사 그룹 목록을 필터링
     * @param groups group처리 한 항공편 목록
     * @param minPrice 최소 가격
     * @param maxPrice 최대 가격
     * @return 가격 필터링을 거친 List<AirlineGroupResponse>
     */
    public List<AirlineGroupResponse> groupByPrice(List<AirlineGroupResponse> groups, Integer minPrice, Integer maxPrice) {

        log.info("PriceFilterAgent: 가격 필터링 적용 ({} ~ {}원)", minPrice, maxPrice);
        int safeMin = (minPrice != null) ? minPrice : 0;
        int safeMax = (maxPrice != null) ? maxPrice : Integer.MAX_VALUE;

        return groups.stream()
                .map(group -> {
                    List<FlightInfoResponse> filteredFlights = group.flights().stream()
                            .filter(flight -> {
                                Integer price = flight.economyCharge();
                                if (price == null || price == 0) return false;
                                boolean isOverMin = (price >= safeMin);
                                boolean isUnderMax = (price <= safeMax);
                                return isOverMin && isUnderMax;
                            }).toList();

                    return filteredFlights.isEmpty() ? null : new AirlineGroupResponse(group.airlineName(), filteredFlights);
                })
                .filter(Objects::nonNull)
                .toList();
    }
}
